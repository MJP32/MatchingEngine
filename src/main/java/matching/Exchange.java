package matching;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class Exchange {
    /*
    <AAPL,<130.98, <1608917401, 1608917401 >>>
          <130.99, <1608917401, 1608917401 >>>
    */
    public final Map<String, List<Long>> tradeTime = new HashMap<>();
    public final Map<Order, Order> crossedOrders = new HashMap<>();

    //<AAPL,<1608917401, 3>
    public final Map<String, Map<Long, Integer>> symbolTimeCount = new HashMap<>();
    private final Comparator<Order> orderComparator = Comparator.comparing(Order::getTimeStamp);
    public List<Order> rejectedOrders = new ArrayList<>();

    public void processTrades(List<Order> orders, Set<String> haltedSymbols) {
        for (Order order : orders) {
            System.out.println("Entered " + order.toString());
            if (canTrade(order, haltedSymbols)) {
                if (!cross(order)) {
                    OrderBook.addToOrderBook(order);
                }
            }
        }
    }

    private boolean cross(Order order) {
        String orderSide = order.getSide();
        String orderType = order.getType();
        BigDecimal orderPrice = order.getPrice();
        Map<String, SortedMap<BigDecimal, List<Order>>> orderBook;
        if (orderSide.equals("buy")) {
            orderBook = OrderBook.sellOrderBook;
        } else {
            orderBook = OrderBook.buyOrderBook;
        }

        if (orderBook == null || orderBook.size() == 0 || orderBook.values().size() == 0) {
            return false;
        }

        SortedMap<BigDecimal, List<Order>> map = orderBook.get(order.getSymbol());
        Set<BigDecimal> prices = map.keySet();
        for (Map.Entry<BigDecimal, List<Order>> entries : map.entrySet()) {
            BigDecimal key = entries.getKey();
            List<Order> value = entries.getValue();


            BigInteger matchedTradeId = getOrderToMatch(order, value, key);

            if (matchedTradeId == (BigInteger.valueOf(-1))) {
                int timeCount = getTimesTradedThisSecond(order.getSymbol(), order.getTimeStamp());
                if (timeCount < 3) {
                    updateTimesTradedThisSecond(order);
                    //crossedOrders.put(order, o)
                    OrderBook.removeOrderFromBook(order, orderBook, matchedTradeId);
                    return true;
                } else {
                    System.out.println(order.getSymbol() + " already traded 3 times this second " + order.getTimeStamp());
                    System.out.println(" Trying to find another match ");
                    //return false;
                }
            }
        }


        return false;
    }

    private BigInteger getOrderToMatch(Order order, List<Order> value, BigDecimal key) {
        String orderSide = order.getSide();
        String orderType = order.getType();
        for (Order ord : value) {
            if (orderType.equals("limit")) {
                BigDecimal orderPrice = order.getPrice();
                //order.getPrice()
                if (orderSide.equals("buy")) {
                    if (orderPrice.compareTo(key) >= 0) {
                        return ord.getId();
                    }
                } else {
                    if (orderPrice.compareTo(key) <= 0) {
                        return ord.getId();
                    }
                }
            } else {
                if (orderSide.equals("buy")) {

                    return ord.getId();

                } else {

                    return ord.getId();

                }
            }
        }
        return BigInteger.valueOf(-1);
    }

    private void updateTimesTradedThisSecond(Order order) {
        symbolTimeCount.putIfAbsent(order.getSymbol(), new HashMap<Long, Integer>());
        symbolTimeCount.get(order.getSymbol()).putIfAbsent(order.getTimeStamp(), 0);
        Integer prevCount = symbolTimeCount.get(order.getSymbol()).get(order.getTimeStamp());
        symbolTimeCount.get(order.getSymbol()).put(order.getTimeStamp(), prevCount + 1);
    }

    private int getTimesTradedThisSecond(String symbol, Long timeStamp) {
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
        return true;
    }

}
