package matching;



public class HaltedTrades {


    private String symbol;
    private String isHalted;
    public String getSymbol() {
        return symbol;
    }
    public String getIsHalted() {
        return isHalted;
    }

    public HaltedTrades(String symbol, String isHalted) {
        this.symbol = symbol;
        this.isHalted = isHalted;
    }

    @Override
    public String toString() {
        return "HaltedTrades{" +
                "symbol='" + symbol + '\'' +
                ", isHalted='" + isHalted + '\'' +
                '}';
    }
}
