package matching;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class Exchange {
    /*
    <AAPL,<130.98, <1608917401, 1608917401 >>>
          <130.99, <1608917401, 1608917401 >>>
    */

    public static final Map<Order, Order> crossedOrders = new HashMap<>();
    private final OrderBook buyOrderBook = OrderBook.createOrderBook("buy");
    private final OrderBook sellOrderBook = OrderBook.createOrderBook("sell");

    //<AAPL,<1608917401, 3>
    public final Map<String, Map<Long, Integer>> symbolTimeCount = new HashMap<>();
    public List<Order> rejectedOrders = new ArrayList<>();


    public void processTrades(List<Order> orders, Set<String> haltedSymbols) {
        for (Order order : orders) {
            //System.out.println("Entered " + order.toString());
            if (canTrade(order, haltedSymbols)) {
                if (!cross(order)) {
                    getOrderBook(order.getSide()).addToOrderBook(order);
                }
            }
        }
    }

    private boolean cross(Order order) {
        OrderBook orderBook = getTradingBook(order.getSide());
        if (orderBook == null || orderBook.getSize() == 0) {
            return false;
        }
        if(!orderBook.getOrderBook().containsKey(order.getSymbol())){
            return false;
        }
        SortedMap<BigDecimal, List<Order>> map = orderBook.getOrderBook().get(order.getSymbol());

        for (Map.Entry<BigDecimal, List<Order>> entries : map.entrySet()) {
            BigDecimal key = entries.getKey();
            List<Order> value = entries.getValue();
            //System.out.println("key " + key);
            if(getOrderToMatch(order, value, key)){
                return true;
            }

            /*if (matchedTradeId != (BigInteger.valueOf(-1))) {
                int timeCount = getNumTimesTradedThisSecond(order.getSymbol(), order.getTimeStamp());
                if (timeCount < 3) {
                    updateTimesTradedThisSecond(order);
                    System.out.println("matchedTradeId "+matchedTradeId);
         
                    //orderBook.removeOrderFromBook(order, orderBook,value, matchedTradeId);
                    return true;
                } else {
                    System.out.println(order.getSymbol() + " already traded 3 times this second " + order.getTimeStamp());
                }
            }*/
        }
        return false;
    }

    private boolean getOrderToMatch(Order orderToMatch, List<Order> value, BigDecimal key) {
        String orderToMatchSide = orderToMatch.getSide();
        String orderToMatchType = orderToMatch.getType();
        for (Order orderFromBook : Collections.unmodifiableList(value)) {
            if (orderToMatchType.equals("limit")&& orderFromBook.getType().equals("limit") ) {
                BigDecimal orderToMatchPrice = orderToMatch.getPrice();
                BigDecimal orderFromBookPrice = getPriceAsBigDecimal(orderFromBook);
                if (orderToMatchSide.equals("buy")) {
                    if (orderToMatchPrice.compareTo(orderFromBookPrice) >= 0 ) {
                        System.out.println("1. "+orderToMatch.getId() +" matches with  " +orderFromBook.getId());
                        System.out.println("1. "+orderToMatchPrice +" ->  " +orderFromBookPrice);

                        if(canBeTradedThisSecond(orderToMatch)){
                            crossedOrders.put(orderToMatch, orderFromBook);
                            value.remove(orderFromBook);
                            return true;
                        }
                    }
                } else {
                    if (orderToMatchPrice.compareTo(orderFromBookPrice) <= 0 && orderFromBook.getType().equals("limit")) {
                        System.out.println("2. "+orderToMatch.getId() +" matches with  " +orderFromBook.getId());
                        System.out.println("2. "+orderToMatchPrice +" ->  " +orderFromBookPrice);
                        if(canBeTradedThisSecond(orderToMatch)){
                            crossedOrders.put(orderToMatch, orderFromBook);
                            value.remove(orderFromBook);
                            return true;
                        }
                    }
                }
            } else {
                if (orderToMatchSide.equals("buy")) {
                    System.out.println("3. "+orderToMatch.getId() +" matches with  " +orderFromBook.getId());
                    if(canBeTradedThisSecond(orderToMatch)) {
                        crossedOrders.put(orderToMatch, orderFromBook);
                        value.remove(orderFromBook);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean canBeTradedThisSecond(Order order) {

        int timeCount = getNumTimesTradedThisSecond(order.getSymbol(), order.getTimeStamp());
        if (timeCount < 3) {
            updateTimesTradedThisSecond(order);
            return true;
        }
        return false;
    }

    public BigDecimal getPriceAsBigDecimal(Order ord) {
        BigDecimal price;
        if(ord.getPrice() == null){
             price = new BigDecimal(0);
        }
        else{
             price = ord.getPrice();
        }
        return price;
    }

    private void updateTimesTradedThisSecond(Order order) {
        symbolTimeCount.putIfAbsent(order.getSymbol(), new HashMap<Long, Integer>());
        symbolTimeCount.get(order.getSymbol()).putIfAbsent(order.getTimeStamp(), 0);
        Integer prevCount = symbolTimeCount.get(order.getSymbol()).get(order.getTimeStamp());
        symbolTimeCount.get(order.getSymbol()).put(order.getTimeStamp(), prevCount + 1);
    }

    private int getNumTimesTradedThisSecond(String symbol, Long timeStamp) {
        int timeCount = 0;
        if (symbolTimeCount.containsKey(symbol)) {
            Map<Long, Integer> timeCountMap = symbolTimeCount.get(symbol);
            if (timeCountMap.containsKey(timeStamp)) {
                timeCount = timeCountMap.get(timeStamp);
            }
        }
        return timeCount;
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
        if (!(order.getType().equals("limit") || order.getType().equals("market"))) {
            rejectedOrders.add(order);
            String err = "Only buy and limit orders supported";
            System.err.println(err);
            return false;
        }
        if (order.getSymbol().equals("") ) {
            rejectedOrders.add(order);
            String err = "Symbol required";
            System.err.println(err);
            return false;
        }

        return true;
    }

    public OrderBook getOrderBook(String side) {
        if (side.equals("buy"))
            return buyOrderBook;
        else if (side.equals("sell"))
            return sellOrderBook;
        else
            return null;
    }

    public OrderBook getTradingBook(String side) {
        if (side.equals("buy"))
            return sellOrderBook;
        else if (side.equals("sell"))
            return buyOrderBook;
        else
            return null;
    }

}
