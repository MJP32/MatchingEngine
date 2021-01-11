import matching.Exchange;
import matching.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

public class ExchangeTest {
    Exchange exchange = new Exchange();

    @Before
    public void beforeEachTestMethod() {


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

        /*{
            AAPL={
                    130.990000000000009094947017729282379150390625=
                    [
                            Order{symbol='AAPL', side='buy', type='limit', price=130.990000000000009094947017729282379150390625, timeStamp=1608917403},
                            Order{symbol='AAPL', side='buy', type='limit', price=130.990000000000009094947017729282379150390625, timeStamp=1608917405}
                    ],
                    130.979999999999989768184605054557323455810546875=
                    [
                            Order{symbol='AAPL', side='buy', type='limit', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917402},
                            Order{symbol='AAPL', side='buy', type='limit', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917404},
                            Order{symbol='AAPL', side='buy', type='limit', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917406},
                            Order{symbol='AAPL', side='buy', type='limit', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917402}
                    ]
            }
        }*/

        Iterator<Map.Entry<String, SortedMap<BigDecimal, List<Order>>>> ordersIter = exchange.buyOrderBook.entrySet().iterator();

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

            for( List<Order> ord : value.values()){
                prices2.add(ord.get(0).getPrice().doubleValue());
            }



        }

        String expectedMessage = "[{130.990000000000009094947017729282379150390625=[Order{symbol='AAPL', side='buy', type='limit', price=130.990000000000009094947017729282379150390625, timeStamp=1608917403}, Order{symbol='AAPL', side='buy', type='limit', price=130.990000000000009094947017729282379150390625, timeStamp=1608917405}], 130.979999999999989768184605054557323455810546875=[Order{symbol='AAPL', side='buy', type='limit', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917402}, Order{symbol='AAPL', side='buy', type='limit', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917404}, Order{symbol='AAPL', side='buy', type='limit', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917406}, Order{symbol='AAPL', side='buy', type='limit', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917402}]}]";
        Assert.assertEquals(expectedMessage, ((HashMap) exchange.buyOrderBook).values().toString());
        Assert.assertEquals(true, exchange.buyOrderBook.keySet().contains("AAPL"));
        Assert.assertEquals(2, prices2.size());

    }

    @Test
    public void createSellBook() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("AAPL", "sell", "limit", new BigDecimal(130.99), (long) 1608917403));
        orders.add(new Order("AAPL", "sell", "limit", new BigDecimal(130.98), (long) 1608917402));
        orders.add(new Order("AAPL", "sell", "limit", new BigDecimal(130.98), (long) 1608917404));
        orders.add(new Order("AAPL", "sell", "limit", new BigDecimal(130.99), (long) 1608917405));
        orders.add(new Order("AAPL", "sell", "limit", new BigDecimal(130.98), (long) 1608917406));
        orders.add(new Order("AAPL", "sell", "limit", new BigDecimal(130.98), (long) 1608917402));
        exchange.processTrades(orders, new HashSet<>());

        /*{
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
                            Order{symbol='AAPL', side='sell', type='limit', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917402}
                    ]
            }
        }*/

        Collection values = ((HashMap) exchange.sellOrderBook).values();

        String expectedMessage = "[{130.979999999999989768184605054557323455810546875=[Order{symbol='AAPL', side='sell', type='limit', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917402}, Order{symbol='AAPL', side='sell', type='limit', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917404}, Order{symbol='AAPL', side='sell', type='limit', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917406}, Order{symbol='AAPL', side='sell', type='limit', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917402}], 130.990000000000009094947017729282379150390625=[Order{symbol='AAPL', side='sell', type='limit', price=130.990000000000009094947017729282379150390625, timeStamp=1608917403}, Order{symbol='AAPL', side='sell', type='limit', price=130.990000000000009094947017729282379150390625, timeStamp=1608917405}]}]";
        Assert.assertEquals(expectedMessage, ((HashMap) exchange.sellOrderBook).values().toString());
        Assert.assertEquals(true, exchange.sellOrderBook.keySet().contains("AAPL"));

    }

}
