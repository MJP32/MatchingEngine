package Test2;

import matching.Exchange;
import matching.Order;
import matching.Util;
import org.junit.Assert;
import org.junit.Before;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.*;

public class TradeOrderTest {
    Exchange exchange = new Exchange();
    List<Order> orders;
    @Before
    public void beforeEachTestMethod() {
        orders = new ArrayList<>();
        orders.clear();
        exchange.matchedOrders.clear();
        exchange.getTradingBook("buy").getOrderBook().clear();
        exchange.getTradingBook("sell").getOrderBook().clear();

    }
    @Test
    public void sellOrderCrossesWithHighestBuyPrice() {
        beforeEachTestMethod();
        orders = new ArrayList<>();
        orders.add(new Order("AAPL", "buy", "limit", new BigDecimal(130.97), (long) 1608917403));
        orders.add(new Order("AAPL", "buy", "limit", new BigDecimal(130.98), (long) 1608917405));
        orders.add(new Order("AAPL", "sell", "limit", new BigDecimal(130.97), (long) 1608917405));
        exchange.processTrades(orders, new HashSet<>());
        Map<Order, Order> crossedOrders = Exchange.matchedOrders;
        Assert.assertEquals(1, crossedOrders.size());
        Order matchedBuyOrder = null;
        for (Map.Entry<Order, Order> entries : crossedOrders.entrySet()) {
            Order key = entries.getKey();
            if (key.getSide().equals("sell")) {
                matchedBuyOrder = entries.getValue();
            }
            else {
                matchedBuyOrder= key;
            }
        }
        Order buyOrderFromBook = null;

        if(exchange.getOrderBook("buy").getOrderBook().containsKey("AAPL")){
            SortedMap<BigDecimal, List<Order>> bigDecimalListSortedMap = exchange.getOrderBook("buy").getOrderBook().get("AAPL");
            for(Map.Entry<BigDecimal, List<Order>> entry :bigDecimalListSortedMap.entrySet()){
                for(Order order :entry.getValue()){
                    buyOrderFromBook = order;
                }
            }
        }
        Assert.assertNotNull(matchedBuyOrder);
        Assert.assertEquals(Util.getPriceAsDouble(matchedBuyOrder), 130.98, .0001);
        Assert.assertNotNull(buyOrderFromBook);
        Assert.assertEquals(Util.getPriceAsDouble(buyOrderFromBook), 130.97, .0001);

    }

    //@Test(dependsOnMethods = { "sellOrderCrossesWithHighestBuyPrice" })
    @Test
    public void method2() {
        beforeEachTestMethod();

        orders = new ArrayList<>();
        orders.add(new Order("AAPL", "buy", "limit", new BigDecimal(130.98), (long) 1608917403));
        orders.add(new Order("AAPL", "buy", "limit", new BigDecimal(130.96), (long) 1608917403));
        orders.add(new Order("AAPL", "buy", "limit", new BigDecimal(130.95), (long) 1608917403));
        orders.add(new Order("AAPL", "sell", "limit", new BigDecimal(130.95), (long) 1608917403));

        exchange.processTrades(orders, new HashSet<>());
        Map<Order, Order> crossedOrders = Exchange.matchedOrders;
        Assert.assertEquals(1, crossedOrders.size());
        Order matchedBuyOrder = null;
        List<Double> buyPricesOnBook = new ArrayList<>();
        for (Map.Entry<Order, Order> entries : crossedOrders.entrySet()) {
            Order key = entries.getKey();
            if (key.getSide().equals("sell")) {
                matchedBuyOrder = entries.getValue();
            }
            else {
                matchedBuyOrder= key;
            }
        }
        Order buyOrderFromBook = null;
        if(exchange.getOrderBook("buy").getOrderBook().containsKey("AAPL")){
            SortedMap<BigDecimal, List<Order>> bigDecimalListSortedMap = exchange.getOrderBook("buy").getOrderBook().get("AAPL");
            for(Map.Entry<BigDecimal, List<Order>> entry :bigDecimalListSortedMap.entrySet()){
                for(Order order :entry.getValue()){
                    buyOrderFromBook = order;
                    buyPricesOnBook.add(Util.getPriceAsDouble(order));
                }
            }
        }
        Assert.assertNotNull(matchedBuyOrder);
        Assert.assertEquals(Util.getPriceAsDouble(matchedBuyOrder), 130.98, .0001);
        Assert.assertNotNull(buyOrderFromBook);
        Assert.assertEquals(buyPricesOnBook.toArray(new Double[buyPricesOnBook.size()]), new Double[]{130.96,130.95});
    }
    //@Test(dependsOnMethods = { "method2" })
    @Test
    public void buyOrderCrossesWithLowestSellPrice() {
        beforeEachTestMethod();;

        orders = new ArrayList<>();
        orders.add(new Order("AAPL", "sell", "limit", new BigDecimal(130.98), (long) 1608917403));
        orders.add(new Order("AAPL", "sell", "limit", new BigDecimal(130.96), (long) 1608917403));
        orders.add(new Order("AAPL", "sell", "limit", new BigDecimal(130.95), (long) 1608917403));
        orders.add(new Order("AAPL", "buy", "limit", new BigDecimal(130.95), (long) 1608917403));

        exchange.processTrades(orders, new HashSet<>());
        Map<Order, Order> crossedOrders = Exchange.matchedOrders;
        Assert.assertEquals(1, crossedOrders.size());
        Order matchedSellOrder = null;
        List<Double> sellPricesOnBook = new ArrayList<>();
        for (Map.Entry<Order, Order> entries : crossedOrders.entrySet()) {
            Order key = entries.getKey();
            if (key.getSide().equals("buy")) {
                matchedSellOrder = entries.getValue();
            }
            else {
                matchedSellOrder= key;
            }
        }
        Order buyOrderFromBook = null;
        if(exchange.getOrderBook("sell").getOrderBook().containsKey("AAPL")){
            SortedMap<BigDecimal, List<Order>> bigDecimalListSortedMap = exchange.getOrderBook("sell").getOrderBook().get("AAPL");
            for(Map.Entry<BigDecimal, List<Order>> entry :bigDecimalListSortedMap.entrySet()){
                for(Order order :entry.getValue()){
                    buyOrderFromBook = order;
                    sellPricesOnBook.add(Util.getPriceAsDouble(order));
                }
            }
        }
        Assert.assertNotNull(matchedSellOrder);
        Assert.assertEquals(Util.getPriceAsDouble(matchedSellOrder), 130.95, .0001);
        Assert.assertNotNull(buyOrderFromBook);
        Assert.assertEquals(sellPricesOnBook.toArray(new Double[sellPricesOnBook.size()]), new Double[]{130.96,130.98});
    }

}
