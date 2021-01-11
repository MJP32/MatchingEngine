package matching;

import java.math.BigInteger;

public class Util {
    private static BigInteger nextOrderID = BigInteger.ZERO;

    static BigInteger getNextOrderId() {
        nextOrderID = nextOrderID.add(BigInteger.ONE);
        return nextOrderID;
    }
}
