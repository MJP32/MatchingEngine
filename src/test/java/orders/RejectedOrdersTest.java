package orders;

import matching.Exchange;
import matching.Order;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

public class RejectedOrdersTest {
    Exchange exchange = new Exchange();

    @Test
    public void HaltedSymbolTest() {
        List<Order> orders = new ArrayList<>();
        Exchange exchange = new Exchange();
        orders.add(new Order("FB", "buy", "market", null, (long) 1608917403));
        orders.add(new Order("GOOG", "sell", "market", null, (long) 1608917403));
        orders.add(new Order("FB", "buy", "market", null, (long) 1608917403));
        orders.add(new Order("GOOG", "sell", "market", null, (long) 1608917403));
        orders.add(new Order("AMZN", "sell", "market", null, (long) 1608917403));

        exchange.processTrades(orders, new HashSet<>(Arrays.asList("FB", "GOOG")));

        List<Order> rejectedOrders = exchange.rejectedOrders;
        Assert.assertEquals(4, rejectedOrders.size());
        Assert.assertEquals("FB", rejectedOrders.get(0).getSymbol());
        Assert.assertEquals("GOOG", rejectedOrders.get(1).getSymbol());


    }
    @Test
    public void ThreeSecondTest() {
        List<Order> orders = new ArrayList<>();
        Exchange exchange = new Exchange();

        orders.add(new Order("AMZN", "sell", "market", null, (long) 1608917403));
        orders.add(new Order("AMZN", "sell", "market", null, (long) 1608917403));
        orders.add(new Order("AMZN", "sell", "market", null, (long) 1608917403));
        orders.add(new Order("AMZN", "sell", "market", null, (long) 1608917403));
        orders.add(new Order("AMZN", "sell", "market", null, (long) 1608917403));
        orders.add(new Order("AMZN", "buy", "market", null, (long) 1608917403));
        orders.add(new Order("AMZN", "buy", "market", null, (long) 1608917403));
        orders.add(new Order("AMZN", "buy", "market", null, (long) 1608917403));
        orders.add(new Order("AMZN", "buy", "market", null, (long) 1608917403));
        orders.add(new Order("AMZN", "buy", "market", null, (long) 1608917403));

        exchange.processTrades(orders, new HashSet<>(Arrays.asList("FB", "GOOG")));
        Map<Order, Order> crossedOrders =  Exchange.MatchedOrders;

        Map<String, SortedMap<BigDecimal, List<Order>>> buy = exchange.getOrderBook("buy").getOrderBook();
        SortedMap<BigDecimal, List<Order>> buyOrders = buy.values().iterator().next();

        Map<String, SortedMap<BigDecimal, List<Order>>> sell = exchange.getOrderBook("sell").getOrderBook();
        SortedMap<BigDecimal, List<Order>> sellOrders = sell.values().iterator().next();
        //should be 3 orders traded
        Assert.assertEquals(3, crossedOrders.size());

        //two orders remain on the buy book
        Assert.assertEquals(2,buyOrders.values().iterator().next().size());

        //two orders remain on the sell book
        Assert.assertEquals(2,sellOrders.values().iterator().next().size());
    }


}
