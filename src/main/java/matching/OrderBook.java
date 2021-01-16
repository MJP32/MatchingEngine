package matching;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class OrderBook {
    String side;
    private final Map<String, SortedMap<BigDecimal, List<Order>>> orderBook = new HashMap<>();
    public Map<String, SortedMap<BigDecimal, List<Order>>> getOrderBook() {
        return orderBook;
    }
    public OrderBook(String side) {
        this.side = side;
    }
    public int getSize(){
        return orderBook.size();
    }

    public static OrderBook createOrderBook(String side){
        return new OrderBook(side);
    }

    public void addToOrderBook(Order order) {
        BigDecimal price = order.getPrice();
        if (Objects.equals(price, null)) {
            price = BigDecimal.valueOf(0);
        }
        if(order.getSide().equals("buy")) {
            orderBook.putIfAbsent(order.getSymbol(), new TreeMap<>(Comparator.reverseOrder()));
        }
        else{
            orderBook.putIfAbsent(order.getSymbol(), new TreeMap<>());
        }
        orderBook.get(order.getSymbol()).putIfAbsent(price, new ArrayList<>());
        orderBook.get(order.getSymbol()).get(price).add(order);
    }

    public static void printBook(Map<String, SortedMap<BigDecimal, List<Order>>> orderBook) {
        for (Map.Entry<String, SortedMap<BigDecimal, List<Order>>> entry : orderBook.entrySet()) {
            String key = entry.getKey();
            SortedMap<BigDecimal, List<Order>> values = entry.getValue();
            for (Map.Entry<BigDecimal, List<Order>> value : values.entrySet()) {
                BigDecimal price = value.getKey();
                List<Order> timeStamps = value.getValue();
                timeStamps.sort(Comparator.comparing(Order::getTimeStamp));
                StringBuilder timeStampsString = new StringBuilder();
                for (Order ord : timeStamps) {
                    StringBuilder append = timeStampsString.append(ord.getTimeStamp()).append(" ");
                }
                System.out.println(key + " " + price + " " + timeStampsString.toString());
            }
        }
    }
}
