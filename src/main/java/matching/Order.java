package matching;


import java.math.BigDecimal;
import java.math.BigInteger;

public class Order {
    private int id;
    private String symbol;
    private String side;
    private String type;
    private BigDecimal price;
    private Long timeStamp;

    public Order() {
    }

    public Order(String symbol, String side, String type, BigDecimal price, Long timeStamp) {
        this.id = Util.next();
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

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                ", side='" + side + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
