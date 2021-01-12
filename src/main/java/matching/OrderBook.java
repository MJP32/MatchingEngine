package matching;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class OrderBook {
    public static final Map<String, SortedMap<BigDecimal, List<Order>>> buyOrderBook;

    static {
        buyOrderBook = new HashMap<>();
    }

    public static final Map<String, SortedMap<BigDecimal, List<Order>>> sellOrderBook;

    static {
        sellOrderBook = new HashMap<>();
    }

    private static final Comparator<BigDecimal> priceComparator = Comparator.reverseOrder();

    public static void removeOrderFromBook(Order order, Map<String, SortedMap<BigDecimal, List<Order>>> orderBook, BigInteger matchedTradeId) {
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

    public static void printBook(Map<String, SortedMap<BigDecimal, List<Order>>> orderBook) {
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



    public static void addToOrderBook(Order order) {


        BigDecimal price = order.getPrice();
        if (Objects.equals(price, null)) {
            price = BigDecimal.valueOf(0);
        }
        if(order.getSide().equals("buy")) {
            buyOrderBook.putIfAbsent(order.getSymbol(), new TreeMap<>(priceComparator));
            buyOrderBook.get(order.getSymbol()).putIfAbsent(price, new ArrayList<Order>());
            buyOrderBook.get(order.getSymbol()).get(price).add(order);
        }
        else{
            sellOrderBook.putIfAbsent(order.getSymbol(), new TreeMap<>());
            sellOrderBook.get(order.getSymbol()).putIfAbsent(price, new ArrayList<Order>());
            sellOrderBook.get(order.getSymbol()).get(price).add(order);
        }
    }
}
