package orders;
import matching.Exchange;
import matching.OrderBook;
import org.junit.Assert;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MethodOrderTest {


    @Test
    @Order(1)
    void test2() {
        List<matching.Order> orders = new ArrayList<>();
        orders.add(new matching.Order("AAPL", "sell", "limit", new BigDecimal(130.99), (long) 1608917403));
        orders.add(new matching.Order("AAPL", "buy", "limit", new BigDecimal(130.99), (long) 1608917404));
        orders.add(new matching.Order("AAPL", "sell", "limit", new BigDecimal(130.98), (long) 1608917402));
        orders.add(new matching.Order("AAPL", "buy", "limit", new BigDecimal(130.98), (long) 1608917405));
        orders.add(new matching.Order("AAPL", "sell", "market", null, (long) 1608917403));
        orders.add(new matching.Order("AAPL", "buy", "market", null, (long) 1608917404));
        orders.add(new matching.Order("AAPL", "sell", "market", null, (long) 1608917402));
        orders.add(new matching.Order("AAPL", "buy", "market", null, (long) 1608917405));
        Exchange exchange = new Exchange();
        exchange.processTrades(orders, new HashSet<>());
        Map<matching.Order, matching.Order> crossedOrders = Exchange.MatchedOrders;
        Assert.assertEquals(4, crossedOrders.size());
    }


    @Test
    @Order(2)
    void test3() {
        List<matching.Order> orders = new ArrayList<>();
        orders.add(new matching.Order("AAPL", "buy", "market", null, (long) 1608917405));
        orders.add(new matching.Order("AAPL", "buy", "market", null, (long) 1608917405));
        Exchange exchange = new Exchange();
        exchange.processTrades(orders, new HashSet<>());

        OrderBook buyOrderBook = exchange.getOrderBook("buy");
        SortedMap<BigDecimal, List<matching.Order>> map = buyOrderBook.getOrderBook().get(orders.get(0).getSymbol());
        List<matching.Order> value = null;
        BigDecimal key = null;
        for (Map.Entry<BigDecimal, List<matching.Order>> entries : map.entrySet()) {
            key = entries.getKey();
            value = entries.getValue();
        }

        Assert.assertEquals(new BigDecimal(0), key);
        Assert.assertEquals(2, value.size());
    }

    @Test
    @Order(3)
    void test1() {
        assertEquals(2, 1 + 1);
    }

    @Test
    void test4() {
        assertEquals(2, 1 + 1);
    }
}