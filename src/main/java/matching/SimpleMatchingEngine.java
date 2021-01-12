package matching;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SimpleMatchingEngine {
//    private static final String INPUT_ORDERS = "orders_basic_cross.csv";
    private static final String INPUT_ORDERS = "orders_apple2.csv";
//    private static final String INPUT_ORDERS = "orders_apple.csv";
    private static final String INPUT_SYMBOLS = "symbols.csv";
    private static final String OUTPUT_TRADES = "trades.txt";
    private static final String OUTPUT_REJECTED = "rejected.txt";
    private static final String OUTPUT_ORDERBOOK = "orderbook.txt";

    private static final String OUTPUT_DIR = "output/";
    private static final String INPUT_DIR = "input/";

    public static void main(String[] args) {
        System.out.println(" Matching Engine");
        Exchange matchingEngine = new Exchange();

        try (BufferedWriter bufferedWriterTrades = new BufferedWriter(new FileWriter(OUTPUT_DIR + OUTPUT_TRADES));
             BufferedWriter bufferedWriterRejections = new BufferedWriter(new FileWriter(OUTPUT_DIR + OUTPUT_REJECTED));
             BufferedWriter bufferedWriterOrderBook = new BufferedWriter(new FileWriter(OUTPUT_DIR + OUTPUT_ORDERBOOK))) {
            Set<String> haltedSymbols = getHaltedSymbolsFromFile(INPUT_DIR + INPUT_SYMBOLS);
            haltedSymbols.forEach(System.out::println);

            List<Order> orders = getOrderDataFromFile(INPUT_DIR + INPUT_ORDERS);
            matchingEngine.processTrades(orders, haltedSymbols);


//            Exchange me = new matching.Exchange();
            //matchingEngine.rejectedOrders.forEach(k-> System.out.println(k));

            System.out.println("Buy Book");
            OrderBook.printBook(OrderBook.buyOrderBook);
            System.out.println("Sell Book");
            OrderBook.printBook(OrderBook.sellOrderBook);

/*

            List<String> unmatched = ((MyEngine) matchingEngine).buyOrderBook.getTrades();
            bufferedWriterOrderBook.write("Unmatched Orders\n");
            for (String u : unmatched) {
                System.out.println("-->unmatched -->: " + u);
                bufferedWriterOrderBook.write(u + "\n");
            }*/


        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    private static List<Order> getOrderDataFromFile(String fileName) throws FileNotFoundException {
        List beans = new CsvToBeanBuilder(new FileReader(fileName))
                .withType(Order.class)
                .build()
                .parse();

//        beans.forEach(k-> System.out.println(k.toString()));

        return beans;
    }

    private static Set<String> getHaltedSymbolsFromFile(String fileName) throws IOException {
        Set<String> haltedSymbols = new HashSet<>();
        List<HaltedTrades> beans = new CsvToBeanBuilder(new FileReader(fileName))
                .withType(HaltedTrades.class)
                .build()
                .parse();
        //beans.forEach(k-> System.out.println(k.toString()));
        List<HaltedTrades> result = beans.stream()                // convert list to stream
                .filter(line -> "TRUE".equals(line.getIsHalted()))     // we dont like mkyong
                .collect(Collectors.toList());
        for(HaltedTrades ht : result){
            haltedSymbols.add(ht.getSymbol());
        }
        return haltedSymbols;
    }

    private static void processTrades() throws IOException {
        /*for (String line; (line = bufferedReaderOrders.readLine()) != null; ) {
            try {
                matching.Exchange me = new matching.Exchange();
                Order order = new Order(line);
                System.out.println("--Entered order : " + order);
                List<Trade> trades = matchingEngine.enterOrder(order);
                for (Trade trade : trades) {
                    System.out.println("----Trade : " + trade);
                    bufferedWriterTrades.write(trade.toStringShort() + "\n");
                }
            } catch (IllegalArgumentException e) {
                bufferedWriterRejections.write(line.toString() + "\n");
                bufferedWriterRejections.write(e.toString() + "\n");
                System.err.println(e.toString());
                System.err.println("Skipping line : " + line);
            }
        }*/
    }
}


