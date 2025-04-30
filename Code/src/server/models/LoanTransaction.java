package server.models;

import server.models.enums.LoanStatus;
import java.io.Serializable;
import java.util.Date;

public class LoanTransaction implements Serializable {
    private String transactionID;
    private String memberID;
    private String resourceID;
    private Date issueDate;
    private Date dueDate;
    private Date returnDate;
    private LoanStatus status;

    public LoanTransaction(String transactionID, String memberID, String resourceID, Date issueDate, Date dueDate) {
        this.transactionID = transactionID;
        this.memberID = memberID;
        this.resourceID = resourceID;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.status = LoanStatus.CHECKED_OUT;
    }

    public boolean isOverdue() {
        return new Date().after(dueDate) && status == LoanStatus.CHECKED_OUT;
    }

    public void markReturned() {
        this.returnDate = new Date();
        this.status = LoanStatus.RETURNED;
    }

    // Getters and Setters
    public String getTransactionID() { return transactionID; }
    public String getMemberID() { return memberID; }
    public String getResourceID() { return resourceID; }
    public Date getIssueDate() { return issueDate; }
    public Date getDueDate() { return dueDate; }
    public Date getReturnDate() { return returnDate; }
    public LoanStatus getStatus() { return status; }
}