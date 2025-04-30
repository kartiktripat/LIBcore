package server.models;

import java.io.*;
import java.util.*;

public class LoanHistory implements Serializable {
    private List<LoanTransaction> transactionLog;

    public LoanHistory() {
        transactionLog = new ArrayList<>();
    }

    public void addTransaction(LoanTransaction transaction) {
        transactionLog.add(transaction);
    }

    public List<LoanTransaction> viewHistoryByMember(String memberID) {
        List<LoanTransaction> result = new ArrayList<>();
        for (LoanTransaction t : transactionLog) {
            if (t.getMemberID().equals(memberID)) {
                result.add(t);
            }
        }
        return result;
    }

    public void exportToTxt(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (LoanTransaction t : transactionLog) {
                writer.write(t.getTransactionID() + "," + t.getMemberID() + "," + t.getResourceID() + "," + t.getStatus());
                writer.newLine();
            }
        }
    }

    public List<LoanTransaction> getAllTransactions() {
        return transactionLog;
    }
}
