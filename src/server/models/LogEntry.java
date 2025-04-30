package server.models;

import java.io.Serializable;
import java.util.Date;

public class LogEntry implements Serializable {
    private Date timestamp;
    private String userID;
    private String action;
    private String message;

    public LogEntry(String userID, String action, String message) {
        this.timestamp = new Date();
        this.userID = userID;
        this.action = action;
        this.message = message;
    }

    public Date getTimestamp() { return timestamp; }
    public String getUserID() { return userID; }
    public String getAction() { return action; }
    public String getMessage() { return message; }

    @Override
    public String toString() {
        return timestamp + " - [" + userID + "] " + action + ": " + message;
    }
}
