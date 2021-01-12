package matching;

import java.math.BigInteger;

public class Util {
    private static BigInteger next = BigInteger.ZERO;


    static BigInteger next() {
        next = next.add(BigInteger.ONE);
        return next;
    }
}
