package orders;

import matching.Exchange;
import matching.Order;
import matching.OrderBook;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.*;

public class MatchTradesTest {
    Exchange exchange = new Exchange();
    List<Order> orders;

    @Before
    public void beforeEachTestMethod() {

    }
    @Test
    public void BasicCross_Test() {
        orders = new ArrayList<>();
        orders.add(new Order("AAPL", "sell", "limit", new BigDecimal(130.99), (long) 1608917403));
        orders.add(new Order("AAPL", "buy", "limit", new BigDecimal(130.99), (long) 1608917404));
        orders.add(new Order("AAPL", "sell", "limit", new BigDecimal(130.98), (long) 1608917402));
        orders.add(new Order("AAPL", "buy", "limit", new BigDecimal(130.98), (long) 1608917405));
        orders.add(new Order("AAPL", "sell", "market", null, (long) 1608917403));
        orders.add(new Order("AAPL", "buy", "market", null, (long) 1608917404));
        orders.add(new Order("AAPL", "sell", "market", null, (long) 1608917402));
        orders.add(new Order("AAPL", "buy", "market", null, (long) 1608917405));
        orders.add(new Order("AAPL", "buy", "market", null, (long) 1608917405));
        orders.add(new Order("AAPL", "buy", "market", null, (long) 1608917405));
        exchange.processTrades(orders, new HashSet<>());
        Map<Order, Order> crossedOrders = Exchange.matchedOrders;
        Assert.assertEquals(8, crossedOrders.size());

        OrderBook buyOrderBook = exchange.getOrderBook("buy");
        SortedMap<BigDecimal, List<Order>> map = buyOrderBook.getOrderBook().get(orders.get(0).getSymbol());
        List<Order> value = null;
        BigDecimal key = null;
        for (Map.Entry<BigDecimal, List<Order>> entries : map.entrySet()) {
            key = entries.getKey();
            value = entries.getValue();
        }

        Assert.assertEquals(new BigDecimal(0), key);
        Assert.assertEquals(8, value.size());
    }

    @Test
    public void CrossWithTradesOnBuyBook_Test() {

        OrderBook buyOrderBook = exchange.getOrderBook("buy");
        SortedMap<BigDecimal, List<Order>> map = buyOrderBook.getOrderBook().get(orders.get(0).getSymbol());
        List<Order> value = null;
        BigDecimal key = null;
        for (Map.Entry<BigDecimal, List<Order>> entries : map.entrySet()) {
            key = entries.getKey();
            value = entries.getValue();
        }
        Assert.assertEquals(new BigDecimal(0), key);
        Assert.assertEquals(2, value.size());
    }
}
