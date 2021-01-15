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
    public void removeOrderFromBook(Order order, OrderBook orderBook, List<Order> value, BigInteger matchedTradeId) {
        List<Order> orders;
        BigDecimal price;
        if (order.getType().equals("market")) {
            price = new BigDecimal(0);
        }
        else{
            price = order.getPrice();
        }

        printBook(orderBook.orderBook);
        orders = orderBook.getOrderBook().get(order.getSymbol()).get(price);


        if (orders.size() != 0)
            orders.remove(0);

        if (orderBook.getOrderBook().get(order.getSymbol()).get(price).size() == 0||orderBook.getOrderBook().get(order.getSymbol()).get(new BigDecimal(0)).size()==0) {
            orderBook.getOrderBook().get(order.getSymbol()).remove(price);
        }
        if(orderBook.getOrderBook().get(order.getSymbol()).size() ==0){
            orderBook.getOrderBook().remove(order.getSymbol());
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
        orderBook.get(order.getSymbol()).putIfAbsent(price, new ArrayList<Order>());
        orderBook.get(order.getSymbol()).get(price).add(order);
    }
}
