package matching;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Util {
    //first id will be 2, to account for column header
    private static BigInteger next = BigInteger.ONE;


    static BigInteger next() {
        next = next.add(BigInteger.ONE);
        return next;
    }

    public BigDecimal getPriceAsBigDecimal(Order ord) {
        BigDecimal price;
        if(ord.getPrice() == null){
            price = new BigDecimal(0);
        }
        else{
            price = ord.getPrice();
        }
        return price;
    }
}
