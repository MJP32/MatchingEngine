package matching;

public enum OrderSide {

    BUY, SELL;
    private OrderSide opposite;

    static {
        BUY.opposite = SELL;
        SELL.opposite = BUY;
    }

    public OrderSide getOppositeSide() {
        return opposite;
    }
}
