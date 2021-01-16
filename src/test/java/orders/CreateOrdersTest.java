package orders;

import matching.Exchange;
import matching.Order;
import matching.OrderBook;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

public class CreateOrdersTest {
    @Before
    public void beforeEachTestMethod() {


    }


    @Test
    public void createMarketOrder_Test() {
        List<Order> orders = new ArrayList<>();
        Exchange exchange = new Exchange();

        orders.add(new Order("AAPL", "buy", "market", null, (long) 1608917403));
        exchange.processTrades(orders, new HashSet<>());
        OrderBook buyOrderBook = exchange.getOrderBook("buy");
        SortedMap<BigDecimal, List<Order>> map = buyOrderBook.getOrderBook().get(orders.get(0).getSymbol());
        BigDecimal key = null;
        List<Order> value = null;
        for (Map.Entry<BigDecimal, List<Order>> entries : map.entrySet()) {
            key = entries.getKey();
            value = entries.getValue();
        }
        Assert.assertEquals(new BigDecimal(0), key);
        Assert.assertEquals(1, value.size());
    }
    @Test
    public void createLimitOrder_Test() {
        List<Order> orders = new ArrayList<>();
        Exchange exchange = new Exchange();
        orders.add(new Order("AAPL", "sell", "limit", new BigDecimal(130.44), (long) 1608917403));
        exchange.processTrades(orders, new HashSet<>());
        OrderBook sellOrderBook = exchange.getOrderBook("sell");
        SortedMap<BigDecimal, List<Order>> map = sellOrderBook.getOrderBook().get(orders.get(0).getSymbol());
        BigDecimal key = null;
        List<Order> value = null;
        for (Map.Entry<BigDecimal, List<Order>> entries : map.entrySet()) {
            key = entries.getKey();
            value = entries.getValue();
        }
        Assert.assertEquals(new BigDecimal(130.44), key);
        Assert.assertEquals(1, value.size());
    }
    @Test
    public void createOrders_DoNotCross_Test() {
        List<Order> orders = new ArrayList<>();
        Exchange exchange = new Exchange();
        orders.add(new Order("AAPL", "buy", "market", null, (long) 1608917403));
        orders.add(new Order("AAPL", "sell", "market", null, (long) 1608917403));
        orders.add(new Order("AAPL", "buy", "market", null, (long) 1608917403));
        orders.add(new Order("AAPL", "sell", "market", null, (long) 1608917403));

        exchange.processTrades(orders, new HashSet<>());

        OrderBook buyOrderBook = exchange.getOrderBook("buy");
        SortedMap<BigDecimal, List<Order>> map = buyOrderBook.getOrderBook().get(orders.get(0).getSymbol());
        BigDecimal key = null;
        List<Order> value = null;
        for (Map.Entry<BigDecimal, List<Order>> entries : map.entrySet()) {
            key = entries.getKey();
            value = entries.getValue();
        }
        Assert.assertEquals(new BigDecimal(0), key);
        Assert.assertEquals(1, value.size());
    }
    @Test
    public void createBuySellOrders_DoNotCross_Test() {
        List<Order> orders = new ArrayList<>();
        Exchange exchange = new Exchange();
        orders.add(new Order("AAPL", "buy", "limit", new BigDecimal(1), (long) 1608917403));
        orders.add(new Order("AAPL", "buy", "limit", new BigDecimal(2), (long) 1608917403));
        orders.add(new Order("AAPL", "sell", "limit", new BigDecimal(3), (long) 1608917403));
        orders.add(new Order("AAPL", "sell", "limit", new BigDecimal(4), (long) 1608917403));

        exchange.processTrades(orders, new HashSet<>());
        OrderBook buyOrderBook = exchange.getOrderBook("buy");
        SortedMap<BigDecimal, List<Order>> map = buyOrderBook.getOrderBook().get(orders.get(0).getSymbol());
        BigDecimal key = null;
        List<Order> value = null;
        for (Map.Entry<BigDecimal, List<Order>> entries : map.entrySet()) {
            key = entries.getKey();
            value = entries.getValue();
        }
        Assert.assertEquals(new BigDecimal(1), key);
        Assert.assertEquals(1, value.size());
    }

}
