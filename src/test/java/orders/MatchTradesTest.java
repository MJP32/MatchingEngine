package orders;

import matching.Exchange;
import matching.Order;
import matching.OrderBook;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

public class MatchTradesTest {


    @Before
    public void beforeEachTestMethod() {


    }



    @Test
    public void BasicCross_Test() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("AAPL", "sell", "limit", new BigDecimal("130.99"), (long) 1608917403));
        orders.add(new Order("AAPL", "buy", "limit", new BigDecimal("130.99"), (long) 1608917404));
        orders.add(new Order("AAPL", "sell", "limit", new BigDecimal("130.98"), (long) 1608917402));
        orders.add(new Order("AAPL", "buy", "limit", new BigDecimal("130.98"), (long) 1608917405));
        Exchange exchange = new Exchange();
        exchange.processTrades(orders, new HashSet<>());
        OrderBook buyOrderBook = exchange.getOrderBook("buy");
//        SortedMap<BigDecimal, List<Order>> map = buyOrderBook.getOrderBook().get(orders.get(0).getSymbol());


        Map<Order, Order> crossedOrders = Exchange.crossedOrders;
        Assert.assertEquals(2, crossedOrders.size());
//        Assert.assertEquals(0, value.size());

    }
    @Test
    public void CrossWithTradesOnBuyBook_Test() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("AAPL", "sell", "limit", new BigDecimal(130.99), (long) 1608917403));
        orders.add(new Order("AAPL", "buy", "limit", new BigDecimal(130.99), (long) 1608917404));
        orders.add(new Order("AAPL", "sell", "limit", new BigDecimal(130.98), (long) 1608917402));
        orders.add(new Order("AAPL", "buy", "limit", new BigDecimal(130.98), (long) 1608917405));
        Exchange exchange = new Exchange();
        exchange.processTrades(orders, new HashSet<>());
        OrderBook buyOrderBook = exchange.getOrderBook("buy");
//        SortedMap<BigDecimal, List<Order>> map = buyOrderBook.getOrderBook().get(orders.get(0).getSymbol());


        Map<Order, Order> crossedOrders = Exchange.crossedOrders;
        Assert.assertEquals(2, crossedOrders.size());
//        Assert.assertEquals(0, value.size());

    }

}
