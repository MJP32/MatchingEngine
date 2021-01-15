package matching;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class SimpleMatchingEngine {
    private static final String INPUT_ORDERS = "orders.csv";
    //        private static final String INPUT_ORDERS = "orders_no_halted.csv";
    //    private static final String INPUT_ORDERS = "orders_tsla.csv";
//        private static final String INPUT_ORDERS = "orders_50.csv";
//    private static final String INPUT_ORDERS = "orders_large.csv";
//    private static final String INPUT_ORDERS = "orders_basic_cross.csv";
    //    private static final String INPUT_ORDERS = "orders_apple2.csv";
//    private static final String INPUT_ORDERS = "orders_apple.csv";
    private static final String INPUT_SYMBOLS = "symbols.csv";
    private static final String OUTPUT_TRADES = "trades.txt";
    private static final String OUTPUT_REJECTED = "rejected.txt";
    private static final String OUTPUT_ORDERBOOK = "orderbook.txt";

    private static final String OUTPUT_DIR = "output/1/";
    private static final String INPUT_DIR = "input/";
    private static final Exchange matchingEngine = new Exchange();

    public static void main(String[] args) throws IOException {
        System.out.println(" Matching Engine");

        createOutputFiles();
        try (BufferedWriter bufferedWriterTrades = new BufferedWriter(new FileWriter(OUTPUT_DIR + OUTPUT_TRADES));
             BufferedWriter bufferedWriterRejections = new BufferedWriter(new FileWriter(OUTPUT_DIR + OUTPUT_REJECTED));
             BufferedWriter bufferedWriterOrderBook = new BufferedWriter(new FileWriter(OUTPUT_DIR + OUTPUT_ORDERBOOK))) {

            Set<String> haltedSymbols = getHaltedSymbolsFromFile(INPUT_DIR + INPUT_SYMBOLS);
            List<Order> orders = getOrderDataFromFile(INPUT_DIR + INPUT_ORDERS);

            matchingEngine.processTrades(orders, haltedSymbols);

            //matched trades
            int crossedTrades = writeCrossedOrdersToFile(bufferedWriterTrades);

            //whats left on the books
            OrderBook buyBook = matchingEngine.getOrderBook("buy");
            OrderBook sellBook = matchingEngine.getOrderBook("sell");
            int buyBookOrders = writeOrderBookToFile(bufferedWriterOrderBook, buyBook);
            int sellBookOrders = writeOrderBookToFile(bufferedWriterOrderBook, sellBook);

            //write rejected orders
            int rejectedOrders = writeRejectedOrders(bufferedWriterRejections);

            printSummary(crossedTrades, buyBookOrders, sellBookOrders, rejectedOrders);

        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    private static void printSummary(int crossedTrades, int buyBookOrders, int sellBookOrders, int rejectedOrders) {
        System.out.println("********************************************");
        System.out.println("\t" + crossedTrades + " Orders crossed");
        System.out.println("\t" + buyBookOrders + " Orders were left on the Buy Book");
        System.out.println("\t" + sellBookOrders + " Orders were left on the Sell Book");
        System.out.println("\t" + rejectedOrders + " Orders were rejected");
        int total = crossedTrades * 2 + buyBookOrders + sellBookOrders + rejectedOrders;
        System.out.println("\t" + total + " Total number of orders processed: ");
        System.out.println("********************************************");
    }

    private static int writeRejectedOrders(BufferedWriter bufferedWriterRejections) throws IOException {
        int count = 0;
        List<Order> rejectedOrders = matchingEngine.rejectedOrders;
        for (Order ord : rejectedOrders) {
            count++;
            System.out.println(ord.toString());
            bufferedWriterRejections.write(ord.toString() + "\n");
        }
        return count;
    }

    private static int writeCrossedOrdersToFile(BufferedWriter bufferedWriterTrades) throws IOException {
        int count = 0;
        String msg = "Matched Trades";
        //System.out.println(msg);
        bufferedWriterTrades.write(msg + "\n");
        Map<Order, Order> crossedOrders = Exchange.MatchedOrders;
        for (Map.Entry<Order, Order> entry : crossedOrders.entrySet()) {
            count++;
            //System.out.println(entry.getKey() + " " + entry.getValue());
            bufferedWriterTrades.write(entry.getKey() + "\n");
            bufferedWriterTrades.write(entry.getValue() + "\n");
            bufferedWriterTrades.write("\n");
        }
        return count;
    }

    private static int writeOrderBookToFile(BufferedWriter bufferedWriterOrderBook, OrderBook tradeBook) throws IOException {
        int count = 0;
        //bufferedWriterOrderBook.write("Unmatched Orders\n");
        for (Map.Entry<String, SortedMap<BigDecimal, List<Order>>> entry : tradeBook.getOrderBook().entrySet()) {
            //System.out.println(entry.getKey());
            //bufferedWriterOrderBook.write(entry.getKey() + "\n");
            SortedMap<BigDecimal, List<Order>> value = entry.getValue();

            Iterator<List<Order>> orderIter = value.values().iterator();
            while (orderIter.hasNext()) {
                List<Order> next = orderIter.next();
                for (Order ord : next) {
                    count++;
                    //System.out.println(ord.toString());
                    bufferedWriterOrderBook.write(ord.toString() + "\n");
                }
            }

        }
        return count;
    }

    private static void createOutputFiles() throws IOException {
        File file = new File(OUTPUT_DIR + OUTPUT_TRADES);
        file.getParentFile().mkdirs();
        file.createNewFile();

        File file2 = new File(OUTPUT_DIR + OUTPUT_REJECTED);
        file2.getParentFile().mkdirs();
        file2.createNewFile();

        File file3 = new File(OUTPUT_DIR + OUTPUT_ORDERBOOK);
        file3.getParentFile().mkdirs();
        file3.createNewFile();
    }

    private static List<Order> getOrderDataFromFile(String fileName) throws FileNotFoundException {
        List<Order> orders = new ArrayList<>();
        BufferedReader fileReader = null;
        final String DELIMITER = ",";
        try {
            String line = "";
            fileReader = new BufferedReader(new FileReader(fileName));

            //skip first line
            line = fileReader.readLine();
            while ((line = fileReader.readLine()) != null) {
                String[] tokens = line.split(DELIMITER);
                if (Objects.equals(tokens[3], "")) {
                    orders.add(new Order(tokens[0], tokens[1], tokens[2], null, Long.valueOf(tokens[4])));
                } else {
                    orders.add(new Order(tokens[0], tokens[1], tokens[2], new BigDecimal(tokens[3]), Long.valueOf(tokens[4])));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert fileReader != null;
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return orders;
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
        for (HaltedTrades ht : result) {
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


