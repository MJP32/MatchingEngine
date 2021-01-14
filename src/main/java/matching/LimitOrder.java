package matching;

import java.math.BigDecimal;

public class LimitOrder extends Order{
    public LimitOrder(String symbol, String side, String type, BigDecimal price, Long timeStamp) {
        super(symbol, side, type, price, timeStamp);
    }
}
