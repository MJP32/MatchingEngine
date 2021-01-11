package matching;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Exchange {
    /*@Override
    public String toString() {
        return "MyEngine{" +
                "buyOrderBook=" + buyOrderBook +
                ", sellOrderBook=" + sellOrderBook +
                '}';
    }*/

    /*public final OrderBook buyOrderBook = OrderBook.buildBuyOrderBook();

    public final OrderBook sellOrderBook = OrderBook.buildSellOrderBook();

    <AAPL,<130.98, <1608917401, 1608917401 >>>
          <130.99, <1608917401, 1608917401 >>>
*/
    public final Map<String, SortedMap<BigDecimal, List<Order>>> buyOrderBook = new HashMap<>();
    public final Map<String, SortedMap<BigDecimal, List<Order>>> sellOrderBook = new HashMap<>();

    public final Map<String, List<Long>> tradeTime = new HashMap<>();
    public final Map<Order, Order> crossedOrders = new HashMap<>();

    //<AAPL,<1608917401, 3>
    public final Map<String, Map<Long, Integer>> symbolTimeCount = new HashMap<>();
    private final Comparator<BigDecimal> priceComparator = Comparator.reverseOrder();
    private final Comparator<Order> orderComparator = Comparator.comparing(Order::getTimeStamp);

    public List<Order> rejectedOrders = new ArrayList<>();

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*public List<Trade> enterOrder(final Order newOrder) {
        List<Trade> trades = new ArrayList<>();
        tradeMatching(newOrder, trades);
        return trades;
    }

    public List<String> getUnmatchedTrades() {
        List<String> trades = buyOrderBook.getTrades();
        trades.forEach(k-> System.out.println(k));
        return trades;
    }*/

    public void processTrades(List<Order> orders, Set<String> haltedSymbols) {
        for (Order order : orders) {
            System.out.println("Entered " + order.toString());
            if (canTrade(order, haltedSymbols)) {
                boolean crossed = false;

                if (order.getSide().equals("buy")) {
                    crossed = cross(order, sellOrderBook);
                } else {
                    crossed = cross(order, buyOrderBook);
                }
                if (!crossed) {
                    if (order.getSide().equals("buy")) {
                        addToBuyOrderBook(order);
                    } else {
                        addToSellOrderBook(order);
                    }
                }
            }
            else {

            }
        }
    }

    private boolean cross(Order order, Map<String, SortedMap<BigDecimal, List<Order>>> orderBook) {

        String orderSide = order.getSide();
        String orderType = order.getType();
        BigDecimal orderPrice = order.getPrice();

        if(orderSide.equals("buy")){
            orderBook = sellOrderBook;
        }
        else{
            orderBook = buyOrderBook;
        }

        if (orderBook == null || orderBook.size() == 0 || orderBook.values().size() == 0) {
            return false;
        }

        SortedMap<BigDecimal, List<Order>> map = orderBook.get(order.getSymbol());
        Set<BigDecimal> prices = map.keySet();
        for( Map.Entry<BigDecimal, List<Order>> entries : map.entrySet()){
            BigDecimal key = entries.getKey();
            List<Order> value = entries.getValue();


            if(orderType.equals("limit")){
                if(orderSide.equals("buy")){
                    if(orderPrice.compareTo(key) >= 0){

                    }
                }
                else{
                    if(orderPrice.compareTo(key) <= 0){

                    }
                }
            }
            else{

            }

        }

        for (BigDecimal price : prices) {
            if (order.getPrice().compareTo(price) <= 0) {

                Integer timeCount = 0;
                if (symbolTimeCount.containsKey(order.getSymbol())) {
                    Map<Long, Integer> timeCountMap = symbolTimeCount.get(order.getSymbol());
                    if (timeCountMap.containsKey(order.getTimeStamp())) {
                        timeCount = timeCountMap.get(order.getTimeStamp());
                    }
                }

                if (timeCount < 3) {
                    symbolTimeCount.putIfAbsent(order.getSymbol(), new HashMap<Long, Integer>());
                    symbolTimeCount.get(order.getSymbol()).putIfAbsent(order.getTimeStamp(), 0);
                    Integer prevCount = symbolTimeCount.get(order.getSymbol()).get(order.getTimeStamp());
                    symbolTimeCount.get(order.getSymbol()).put(order.getTimeStamp(), prevCount + 1);


                    //crossedOrders.put(order, o)
                    removeOrderFromBook(order, orderBook);
                    return true;

                } else {
                    System.out.println(order.getSymbol() + " already traded 3 times this second " + order.getTimeStamp());
                    return false;

                }
            }
        }
        return false;
    }

    private void removeOrderFromBook(Order order, Map<String, SortedMap<BigDecimal, List<Order>>> orderBook) {
        if (order.getSide().equals("buy")) {
            orderBook = sellOrderBook;
        } else {
            orderBook = buyOrderBook;
        }
        Set<String> symbols = orderBook.keySet();
        List<Order> orders = orderBook.get(order.getSymbol()).get(order.getPrice());
        if (orders.size() != 0)
            orders.remove(0);

        if (orderBook.get(order.getSymbol()).get(order.getPrice()).size() == 0) {
            orderBook.get(order.getSymbol()).remove(order.getPrice());
        }
    }

    public void printBook(Map<String, SortedMap<BigDecimal, List<Order>>> orderBook) {
        for (Map.Entry<String, SortedMap<BigDecimal, List<Order>>> entry : orderBook.entrySet()) {
            String key = entry.getKey();
            SortedMap<BigDecimal, List<Order>> values = entry.getValue();
            for (Map.Entry<BigDecimal, List<Order>> value : values.entrySet()) {
                BigDecimal price = value.getKey();
                List<Order> timeStamps = value.getValue();
                Collections.sort(timeStamps, Comparator.comparing(order -> order.getTimeStamp()));
                StringBuilder timeStampsString = new StringBuilder();
                for (Order ord : timeStamps) {
                    timeStampsString.append(ord.getTimeStamp() + " ");
                }
                System.out.println(key + " " + price + " " + timeStampsString.toString());
            }
        }
    }

    private void addToSellOrderBook(Order order) {
        sellOrderBook.putIfAbsent(order.getSymbol(), new TreeMap<>());
        sellOrderBook.get(order.getSymbol()).putIfAbsent(order.getPrice(), new ArrayList<Order>());
        sellOrderBook.get(order.getSymbol()).get(order.getPrice()).add(order);
    }

    private void addToBuyOrderBook(Order order) {
        buyOrderBook.putIfAbsent(order.getSymbol(), new TreeMap<>(priceComparator));
        buyOrderBook.get(order.getSymbol()).putIfAbsent(order.getPrice(), new ArrayList<Order>());
        buyOrderBook.get(order.getSymbol()).get(order.getPrice()).add(order);
    }

    private boolean canTrade(Order order, Set<String> haltedSymbols) {
        if (haltedSymbols.contains(order.getSymbol())) {
            rejectedOrders.add(order);
            String err = "Restricted symbol";
            System.err.println(err);
            return false;
        }
        if (order.getType().equals("limit") && order.getPrice() == null) {
            rejectedOrders.add(order);
            String err = "Limit Orders must have price";
            System.err.println(err);
            return false;
        }
        return true;
    }
/*
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void tradeMatching(final Order newOrder, final List<Trade> trades) {
        Order matchingOrder = getOppositeOrderBook(newOrder).cross(newOrder);
        if (matchingOrder != null) {
            Trade trade = trade(newOrder, matchingOrder);
            trades.add(trade);

            if (isFilled(matchingOrder, trade)) {
                getOrderBook(matchingOrder).remove(matchingOrder);
            } else {
                matchingOrder.decrease(trade.getQuantity());
            }

            if (!isFilled(newOrder, trade)) {
                newOrder.decrease(trade.getQuantity());
                tradeMatching(newOrder, trades);
            }

        } else {
            getOrderBook(newOrder).add(newOrder);
        }
    }

    private OrderBook getOrderBook(final Order newOrder) {
        return (newOrder.getSide() == OrderSide.BUY) ? buyOrderBook : sellOrderBook;
    }

    private OrderBook getOppositeOrderBook(final Order newOrder) {
        return (newOrder.getSide() == OrderSide.BUY) ? sellOrderBook : buyOrderBook;
    }

    private boolean isFilled(final Order order, final Trade trade) {
        return order.getQuantity().compareTo(trade.getQuantity()) == 0;
    }

    private Trade trade(final Order newOrder, final Order matchingOrder) {
        return new Trade(
                newOrder.getSide() == OrderSide.SELL ? newOrder : matchingOrder,
                newOrder.getSide() == OrderSide.BUY ? newOrder : matchingOrder,
                newOrder.getQuantity().min( matchingOrder.getQuantity() ),
                matchingOrder.getPrice()); // "limit order" always trades at the order book price
    }*/
}
