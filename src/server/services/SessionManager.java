package server.services;

import java.util.*;

public class SessionManager {
    private final Map<String, Long> activeSessions;
    private final long sessionExpiryTime = 30 * 60 * 1000; // 30 minutes

    public SessionManager() {
        activeSessions = new HashMap<>();
    }

    public void createSession(String userID) {
        activeSessions.put(userID, System.currentTimeMillis());
    }

    public boolean isSessionActive(String userID) {
        if (!activeSessions.containsKey(userID)) return false;
        long lastAccess = activeSessions.get(userID);
        if (System.currentTimeMillis() - lastAccess > sessionExpiryTime) {
            activeSessions.remove(userID);
            return false;
        }
        return true;
    }

    public void refreshSession(String userID) {
        activeSessions.put(userID, System.currentTimeMillis());
    }

    public void endSession(String userID) {
        activeSessions.remove(userID);
    }
}
