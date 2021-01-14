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
    /*Exchange exchange = new Exchange();
    Exchange exchange2 = new Exchange();

    Iterator<Map.Entry<String, SortedMap<BigDecimal, List<Order>>>> ordersIter;
    Set<String> symbols;
    Set<Set<BigDecimal>> prices;
    Set<Double> prices2;
    OrderBook book = new OrderBook();
        List<Order> orders;
        List<Order> orders2;
    @Before
    public void beforeEachTestMethod() {
        orders = new ArrayList<>();
        orders2 = new ArrayList<>();
//        orders.clear();

        symbols = new HashSet<>();
        prices = new HashSet<>();
        prices2 = new HashSet<>();

    }

    @Test
    public void createMarketOrder() {

        orders.add(new Order("AAPL", "buy", "market", null, (long) 1608917403));
        exchange.processTrades(orders, new HashSet<>());

        Map<String, SortedMap<BigDecimal, List<Order>>> buyOrderBook = exchange.getOrderBook().getBuyOrderBook();

        ordersIter = buyOrderBook.entrySet().iterator();
        while (ordersIter.hasNext()) {
            Map.Entry<String, SortedMap<BigDecimal, List<Order>>> next = ordersIter.next();
            System.out.println(next.getKey() + " = " + next.getValue());
            symbols.add(next.getKey());
            SortedMap<BigDecimal, List<Order>> value = next.getValue();
            prices.add(value.keySet());

            for (List<Order> ord : value.values()) {
                BigDecimal price = ord.get(0).getPrice();
                if (Objects.equals(price, null)) {
                    price = BigDecimal.valueOf(0);
                }
                prices2.add(price.doubleValue());
            }
        }

        Map<Order, Order> crossedOrders = Exchange.crossedOrders;
        for(Map.Entry<Order,Order> entry : crossedOrders.entrySet()){
            System.out.println("->"+entry.getKey() +" " + entry.getValue());
        }
        String expectedMessage = "[{0=[Order{id=1, symbol='AAPL', side='buy', type='market', price=null, timeStamp=1608917403}]}]";
        Assert.assertEquals(expectedMessage, exchange.getOrderBook().getBuyOrderBook().values().toString());
        Assert.assertEquals(true, exchange.getOrderBook().getBuyOrderBook().keySet().contains("AAPL"));
        Assert.assertEquals(1, prices2.size());
        //Assert.assertEquals(true, prices2.contains(0.0));
    }
    @Test
    public void createMarketOrders() {

        orders2.add(new Order("AAPL", "buy", "market", null, (long) 1608917403));
        orders2.add(new Order("AAPL", "buy", "market", null, (long) 1608917403));
        orders2.add(new Order("AAPL", "buy", "market", null, (long) 1608917403));
        exchange2.processTrades(orders2, new HashSet<>());

        Map<String, SortedMap<BigDecimal, List<Order>>> buyOrderBook = exchange2.getOrderBook().getBuyOrderBook();

        ordersIter = buyOrderBook.entrySet().iterator();
        while (ordersIter.hasNext()) {
            Map.Entry<String, SortedMap<BigDecimal, List<Order>>> next = ordersIter.next();
            System.out.println(next.getKey() + " = " + next.getValue());
            symbols.add(next.getKey());
            SortedMap<BigDecimal, List<Order>> value = next.getValue();
            prices.add(value.keySet());

            for (List<Order> ord : value.values()) {
                BigDecimal price = ord.get(0).getPrice();
                if (Objects.equals(price, null)) {
                    price = BigDecimal.valueOf(0);
                }
                prices2.add(price.doubleValue());
            }
        }

        Map<Order, Order> crossedOrders = Exchange.crossedOrders;
        for(Map.Entry<Order,Order> entry : crossedOrders.entrySet()){
            System.out.println("->"+entry.getKey() +" " + entry.getValue());
        }
        String expectedMessage = "[{0=[Order{id=1, symbol='AAPL', side='buy', type='market', price=null, timeStamp=1608917403}, Order{id=2, symbol='AAPL', side='buy', type='market', price=null, timeStamp=1608917403}, Order{id=3, symbol='AAPL', side='buy', type='market', price=null, timeStamp=1608917403}]}]";
        Assert.assertEquals(expectedMessage, exchange2.getOrderBook().getBuyOrderBook().values().toString());
        Assert.assertEquals(true, exchange2.getOrderBook().getBuyOrderBook().keySet().contains("AAPL"));
        Assert.assertEquals(1, prices2.size());
        //Assert.assertEquals(true, prices2.contains(0.0));
    }
    *//*@Test
    public void createLimitOrder() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("AAPL", "sell", "limit", new BigDecimal(130.99), (long) 1608917403));
        exchange.processTrades(orders, new HashSet<>());
        Iterator<Map.Entry<String, SortedMap<BigDecimal, List<Order>>>> ordersIter = new OrderBook().getSellOrderBook().entrySet().iterator();

        Set<String> symbols = new HashSet<>();
        Set<Set<BigDecimal>> prices = new HashSet<>();
        Set<Double> prices2 = new HashSet<>();
        while (ordersIter.hasNext()) {
            Map.Entry<String, SortedMap<BigDecimal, List<Order>>> next = ordersIter.next();
            System.out.println(next.getKey() + " = " + next.getValue());
            symbols.add(next.getKey());
            SortedMap<BigDecimal, List<Order>> value = next.getValue();
            prices.add(value.keySet());

            for (List<Order> ord : value.values()) {
                BigDecimal price = ord.get(0).getPrice();
                if (Objects.equals(price, null)) {
                    price = BigDecimal.valueOf(0);
                }
                prices2.add(price.doubleValue());
            }
        }

        Collection values = ((HashMap) new OrderBook().getSellOrderBook()).values();

        String expectedMessage = "[{130.990000000000009094947017729282379150390625=[Order{id=1, symbol='AAPL', side='sell', type='limit', price=130.990000000000009094947017729282379150390625, timeStamp=1608917403}]}]";
        Assert.assertEquals(expectedMessage, ((HashMap) new OrderBook().getSellOrderBook()).values().toString());
        Assert.assertEquals(true, new OrderBook().getSellOrderBook().keySet().contains("AAPL"));
        Assert.assertEquals(1, prices2.size());
        Assert.assertEquals(true, prices2.contains(130.99));
    }


    @Test
    public void createBuyBook() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("AAPL", "buy", "limit", new BigDecimal(130.99), (long) 1608917403));
        orders.add(new Order("AAPL", "buy", "limit", new BigDecimal(130.98), (long) 1608917402));
        orders.add(new Order("AAPL", "buy", "limit", new BigDecimal(130.98), (long) 1608917404));
        orders.add(new Order("AAPL", "buy", "limit", new BigDecimal(130.99), (long) 1608917405));
        orders.add(new Order("AAPL", "buy", "limit", new BigDecimal(130.98), (long) 1608917406));
        orders.add(new Order("AAPL", "buy", "limit", new BigDecimal(130.98), (long) 1608917402));
        exchange.processTrades(orders, new HashSet<>());

        *//**//*{
            AAPL={
                    130.990000000000009094947017729282379150390625=
                    [
                            Order{symbol='AAPL', side='buy', type='market', price=130.990000000000009094947017729282379150390625, timeStamp=1608917403},
                            Order{symbol='AAPL', side='buy', type='market', price=130.990000000000009094947017729282379150390625, timeStamp=1608917405}
                    ],
                    130.979999999999989768184605054557323455810546875=
                    [
                            Order{symbol='AAPL', side='buy', type='market', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917402},
                            Order{symbol='AAPL', side='buy', type='market', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917404},
                            Order{symbol='AAPL', side='buy', type='market', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917406},
                            Order{symbol='AAPL', side='buy', type='market', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917402}
                    ]
            }
        }*//**//*

        Iterator<Map.Entry<String, SortedMap<BigDecimal, List<Order>>>> ordersIter = new OrderBook().getBuyOrderBook().entrySet().iterator();

        //Iterator it = mp.entrySet().iterator();
        Set<String> symbols = new HashSet<>();
        Set<Set<BigDecimal>> prices = new HashSet<>();
        Set<Double> prices2 = new HashSet<>();

        while (ordersIter.hasNext()) {
            Map.Entry<String, SortedMap<BigDecimal, List<Order>>> next = ordersIter.next();
            System.out.println(next.getKey() + " = " + next.getValue());
            symbols.add(next.getKey());
            SortedMap<BigDecimal, List<Order>> value = next.getValue();
            prices.add(value.keySet());

            for (List<Order> ord : value.values()) {
                prices2.add(ord.get(0).getPrice().doubleValue());
            }


        }

        String expectedMessage = "[{130.990000000000009094947017729282379150390625=[Order{symbol='AAPL', side='buy', type='limit', price=130.990000000000009094947017729282379150390625, timeStamp=1608917403}, Order{symbol='AAPL', side='buy', type='limit', price=130.990000000000009094947017729282379150390625, timeStamp=1608917405}], 130.979999999999989768184605054557323455810546875=[Order{symbol='AAPL', side='buy', type='limit', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917402}, Order{symbol='AAPL', side='buy', type='limit', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917404}, Order{symbol='AAPL', side='buy', type='limit', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917406}, Order{symbol='AAPL', side='buy', type='limit', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917402}]}]";
        Assert.assertEquals(expectedMessage, ((HashMap) new OrderBook().getBuyOrderBook()).values().toString());
        Assert.assertEquals(true, new OrderBook().getBuyOrderBook().keySet().contains("AAPL"));
        Assert.assertEquals(2, prices2.size());
        Assert.assertEquals(true, prices2.contains(130.99));
        Assert.assertEquals(true, prices2.contains(130.98));

    }

    @Test
    public void createSellBook() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("AAPL", "sell", "market", new BigDecimal(130.99), (long) 1608917403));
        orders.add(new Order("AAPL", "sell", "market", new BigDecimal(130.98), (long) 1608917402));
        orders.add(new Order("AAPL", "sell", "market", new BigDecimal(130.98), (long) 1608917404));
        orders.add(new Order("AAPL", "sell", "market", new BigDecimal(130.99), (long) 1608917405));
        orders.add(new Order("AAPL", "sell", "market", new BigDecimal(130.98), (long) 1608917406));
        orders.add(new Order("AAPL", "sell", "market", new BigDecimal(130.98), (long) 1608917402));
        exchange.processTrades(orders, new HashSet<>());

        *//**//*{
            AAPL={
                    130.990000000000009094947017729282379150390625=
                    [
                            Order{symbol='AAPL', side='sell', type='limit', price=130.990000000000009094947017729282379150390625, timeStamp=1608917403},
                            Order{symbol='AAPL', side='sell', type='limit', price=130.990000000000009094947017729282379150390625, timeStamp=1608917405}
                    ],
                    130.979999999999989768184605054557323455810546875=
                    [
                            Order{symbol='AAPL', side='sell', type='limit', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917402},
                            Order{symbol='AAPL', side='sell', type='limit', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917404},
                            Order{symbol='AAPL', side='sell', type='limit', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917406},
                            Order{symbol='AAPL', side='sell', type='market', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917402}
                    ]
            }
        }*//**//*

        Collection values = ((HashMap) new OrderBook().getSellOrderBook()).values();

        String expectedMessage = "[{130.979999999999989768184605054557323455810546875=[Order{symbol='AAPL', side='sell', type='market', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917402}, Order{symbol='AAPL', side='sell', type='market', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917404}, Order{symbol='AAPL', side='sell', type='market', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917406}, Order{symbol='AAPL', side='sell', type='market', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917402}], 130.990000000000009094947017729282379150390625=[Order{symbol='AAPL', side='sell', type='market', price=130.990000000000009094947017729282379150390625, timeStamp=1608917403}, Order{symbol='AAPL', side='sell', type='market', price=130.990000000000009094947017729282379150390625, timeStamp=1608917405}]}]";
        Assert.assertEquals(expectedMessage, ((HashMap) new OrderBook().getSellOrderBook()).values().toString());
        Assert.assertEquals(true, new OrderBook().getSellOrderBook().keySet().contains("AAPL"));

    }
*/
}
