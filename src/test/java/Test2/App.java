package Test2;

import matching.Exchange;
import matching.Order;
import matching.OrderBook;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.*;

public class App {
    Exchange exchange = new Exchange();
    List<Order> orders;
    //This test will be failed.
    @Test
    public void method1() {
        orders = new ArrayList<>();
        orders.add(new Order("AAPL", "sell", "limit", new BigDecimal(130.99), (long) 1608917403));
        orders.add(new Order("AAPL", "buy", "limit", new BigDecimal(130.99), (long) 1608917404));
        orders.add(new Order("AAPL", "sell", "limit", new BigDecimal(130.98), (long) 1608917402));
        orders.add(new Order("AAPL", "buy", "limit", new BigDecimal(130.98), (long) 1608917405));
        orders.add(new Order("AAPL", "sell", "market", null, (long) 1608917403));
        orders.add(new Order("AAPL", "buy", "market", null, (long) 1608917404));
        orders.add(new Order("AAPL", "sell", "market", null, (long) 1608917402));
        orders.add(new Order("AAPL", "buy", "market", null, (long) 1608917405));
        orders.add(new Order("AAPL", "sell", "market", null, (long) 1608917405));
        orders.add(new Order("AAPL", "buy", "market", null, (long) 1608917405));
        exchange.processTrades(orders, new HashSet<>());
        Map<Order, Order> crossedOrders = Exchange.MatchedOrders;
        Assert.assertEquals(5, crossedOrders.size());


    }

    @Test(dependsOnMethods = { "method1" })
    public void method2() {
        orders.add(new Order("AMZN", "buy", "market", null, (long) 1608917405));
        orders.add(new Order("AMZN", "buy", "market", null, (long) 1608917405));
        exchange.processTrades(orders, new HashSet<>());
        int numOrdersOnBuyBook;
        if(exchange.getOrderBook("buy").getOrderBook().containsKey("AMZN")) {
            numOrdersOnBuyBook = exchange.getOrderBook("buy").getOrderBook().get("AMZN").values().iterator().next().size();
        }
        else{
            numOrdersOnBuyBook =0;
        }
        Assert.assertEquals(numOrdersOnBuyBook, 2);
    }
    @Test(dependsOnMethods = { "method2" })
    public void method3() {
        orders.add(new Order("AMZN", "sell", "market", null, (long) 1608917405));
        orders.add(new Order("AMZN", "sell", "market", null, (long) 1608917405));
        exchange.processTrades(orders, new HashSet<>());

        int numOrdersOnBuyBook;
        if(exchange.getOrderBook("sell").getOrderBook().containsKey("AMZN")){
            numOrdersOnBuyBook = exchange.getOrderBook("sell").getOrderBook().get("AMZN").values().iterator().next().size();
        }
        else{
            numOrdersOnBuyBook = 0;
        }
        Assert.assertEquals(numOrdersOnBuyBook, 0);
    }

}
