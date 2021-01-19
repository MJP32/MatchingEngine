package matching;

import java.math.BigDecimal;
import java.util.*;

public class Exchange {

    /*
        sell book
        <AAPL,<130.98, <1608917401, 1608917401 >>>
              <130.99, <1608917401, 1608917401 >>>
    */
    private final OrderBook sellOrderBook = OrderBook.createOrderBook("sell");

    /*
        buy book
        <AAPL,<130.99, <1608917401, 1608917401 >>>
              <130.98, <1608917401, 1608917401 >>>
    */
    private final OrderBook buyOrderBook = OrderBook.createOrderBook("buy");

    //<AAPL,<1608917401, 3>
    public final Map<String, Map<Long, Integer>> symbolTimeCount = new HashMap<>();

    public static final Map<Order, Order> matchedOrders = new HashMap<>();
    public List<Order> rejectedOrders = new LinkedList<>();

    public void processTrades(List<Order> orders, Set<String> haltedSymbols) {
        //process orders one at a time
        for (Order order : orders) {
            System.out.println("Entered " + order.toString());
            if (canTrade(order, haltedSymbols)) {
                if (!cross(order)) {
                    //if order does not cross, add limit order to book, market orders should be rejected
                    if (order.getType().equals("limit")) {
                        getOrderBook(order.getSide()).addToOrderBook(order);
                    } else {
                        rejectedOrders.add(order);
                    }
                }
            }
        }
    }

    private boolean cross(Order order) {
        OrderBook orderBook = getTradingBook(order.getSide());
        if (orderBook == null || orderBook.getSize() == 0) {
            return false;
        }
        if (!orderBook.getOrderBook().containsKey(order.getSymbol())) {
            return false;
        }
        SortedMap<BigDecimal, List<Order>> map = orderBook.getOrderBook().get(order.getSymbol());
        for (Map.Entry<BigDecimal, List<Order>> entries : map.entrySet()) {
            BigDecimal key = entries.getKey();
            List<Order> value = entries.getValue();
            if (findOrderToMatchAndRemoveFromBook(order, value, key)) {
                return true;
            }
        }
        return false;
    }

    private boolean findOrderToMatchAndRemoveFromBook(Order orderToTrade, List<Order> value, BigDecimal key) {
        String orderToTradeSide = orderToTrade.getSide();
        String orderToTradeType = orderToTrade.getType();
        for (Order orderFromBook : value) {
            String orderFromBookType = orderFromBook.getType();
            switch (orderFromBookType) {
                case "limit":
                    BigDecimal orderToMatchPrice = Util.getPriceAsBigDecimal(orderToTrade);
                    BigDecimal orderFromBookPrice = Util.getPriceAsBigDecimal(orderFromBook);
                    if (orderToTradeSide.equals("buy")) {
                        if (orderToMatchPrice.compareTo(orderFromBookPrice) >= 0 || orderToTradeType.equals("market")) {
                            if (canBeTradedThisSecond(orderToTrade)) {
                                matchedOrders.put(orderToTrade, orderFromBook);
                                //remove matched order from book
                                value.remove(orderFromBook);
                                return true;
                            }
                        }
                    } else {
                        if (orderToMatchPrice.compareTo(orderFromBookPrice) <= 0 || orderToTradeType.equals("market")) {
                            if (canBeTradedThisSecond(orderToTrade)) {
                                matchedOrders.put(orderToTrade, orderFromBook);
                                //remove matched order from book
                                value.remove(orderFromBook);
                                return true;
                            }
                        }
                    }
                    //}
                    break;
                case "market":
                    if (canBeTradedThisSecond(orderToTrade)) {
                        matchedOrders.put(orderToTrade, orderFromBook);
                        //remove matched order from book
                        value.remove(orderFromBook);
                        return true;
                    }
                    break;
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

    private void updateTimesTradedThisSecond(Order order) {
        symbolTimeCount.putIfAbsent(order.getSymbol(), new HashMap<>());
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
            String err = "Limit Orders must have a price";
            System.err.println(err);
            return false;
        }
        if (!(order.getType().equals("limit") || order.getType().equals("market"))) {
            rejectedOrders.add(order);
            String err = "Only limit and market orders supported";
            System.err.println(err);
            return false;
        }
        if (!(order.getSide().equals("buy") || order.getSide().equals("sell"))) {
            rejectedOrders.add(order);
            String err = "Only buy and sell orders supported";
            System.err.println(err);
            return false;
        }
        if (order.getSymbol().equals("")) {
            rejectedOrders.add(order);
            String err = "Symbol is a required field";
            System.err.println(err);
            return false;
        }
        if (order.getTimeStamp().equals("")) {
            rejectedOrders.add(order);
            String err = "TimeStamp required field";
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
