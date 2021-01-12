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
    Exchange exchange = new Exchange();

    @Before
    public void beforeEachTestMethod() {


    }



    @Test
    public void equalBuySell() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("AAPL", "sell", "limit", new BigDecimal(130.99), (long) 1608917403));
        orders.add(new Order("AAPL", "sell", "limit", new BigDecimal(130.98), (long) 1608917402));
        orders.add(new Order("AAPL", "buy", "limit", new BigDecimal(130.99), (long) 1608917404));
        orders.add(new Order("AAPL", "buy", "limit", new BigDecimal(130.98), (long) 1608917405));
        exchange.processTrades(orders, new HashSet<>());



        Collection values = ((HashMap) OrderBook.sellOrderBook).values();

        String expectedMessage = "[{}]";
        Assert.assertEquals(expectedMessage, ((HashMap) OrderBook.sellOrderBook).values().toString());
        Assert.assertEquals(true, OrderBook.sellOrderBook.keySet().contains("AAPL"));

    }

}
