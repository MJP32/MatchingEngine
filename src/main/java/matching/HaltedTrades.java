package matching;

import com.opencsv.bean.CsvBindByName;

public class HaltedTrades {
    @CsvBindByName
    private String symbol;

    @CsvBindByName(column = "is_halted")
    private String isHalted;


    public String getSymbol() {
        return symbol;
    }

    public String getIsHalted() {
        return isHalted;
    }

    @Override
    public String toString() {
        return "HaltedTrades{" +
                "symbol='" + symbol + '\'' +
                ", isHalted='" + isHalted + '\'' +
                '}';
    }
}
