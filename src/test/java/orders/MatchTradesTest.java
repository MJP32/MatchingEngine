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
    public void createSellBook() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("AAPL", "sell", "limit", new BigDecimal(130.99), (long) 1608917403));
        orders.add(new Order("AAPL", "sell", "limit", new BigDecimal(130.98), (long) 1608917402));
        orders.add(new Order("AAPL", "buy", "limit", new BigDecimal(130.98), (long) 1608917404));
        orders.add(new Order("AAPL", "buy", "limit", new BigDecimal(130.99), (long) 1608917405));
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

        Collection values = ((HashMap) OrderBook.sellOrderBook).values();

        String expectedMessage = "[{130.979999999999989768184605054557323455810546875=[Order{symbol='AAPL', side='sell', type='limit', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917402}, Order{symbol='AAPL', side='sell', type='limit', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917404}, Order{symbol='AAPL', side='sell', type='limit', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917406}, Order{symbol='AAPL', side='sell', type='limit', price=130.979999999999989768184605054557323455810546875, timeStamp=1608917402}], 130.990000000000009094947017729282379150390625=[Order{symbol='AAPL', side='sell', type='limit', price=130.990000000000009094947017729282379150390625, timeStamp=1608917403}, Order{symbol='AAPL', side='sell', type='limit', price=130.990000000000009094947017729282379150390625, timeStamp=1608917405}]}]";
        Assert.assertEquals(expectedMessage, ((HashMap) OrderBook.sellOrderBook).values().toString());
        Assert.assertEquals(true, OrderBook.sellOrderBook.keySet().contains("AAPL"));

    }

}
