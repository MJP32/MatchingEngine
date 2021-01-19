package matching;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Util {
    //first id will be 2, to account for column header
    private static int next =1;// BigInteger.ONE;


    static int next() {
        next++;//;= next.add(BigInteger.ONE);
        return next;
    }

    public static double getPriceAsDouble(Order ord) {
        double price;
        if(ord.getPrice() == null){
            price = 0.0;
        }
        else{
            price = Double.valueOf(String.valueOf(ord.getPrice()));
        }
        return price;
    }

    public static BigDecimal getPriceAsBigDecimal(Order ord) {
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
