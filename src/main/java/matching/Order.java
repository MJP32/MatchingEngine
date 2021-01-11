package matching;
import com.opencsv.bean.CsvBindByName;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Order {

    private BigInteger id;

    @CsvBindByName
    private String symbol;

    @CsvBindByName
    private String side;

    @CsvBindByName
    private String type;

    @CsvBindByName
    private BigDecimal price;

    @CsvBindByName
    private Long timeStamp;

    public Order() {
    }

    public Order(String symbol, String side, String type, BigDecimal price, Long timeStamp) {
        this.id = Util.getNextOrderId() ;
        this.symbol = symbol;
        this.side = side;
        this.type = type;
        this.price = price;
        this.timeStamp = timeStamp;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getSide() {
        return side;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        return "Order{" +
                "symbol='" + symbol + '\'' +
                ", side='" + side + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
