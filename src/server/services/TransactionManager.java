package server.services;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionManager {
    private static final String TRANSACTION_FILE = "data/transactions.txt";

    public static List<String> getAllTransactions() throws IOException {
        List<String> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTION_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                transactions.add(line);
            }
        }
        return transactions;
    }
}
