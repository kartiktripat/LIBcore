package server.services;

import server.models.User;
import java.io.*;
import java.util.*;

public class AuthenticationService {
    private Map<String, User> users;

    public AuthenticationService() {
        users = new HashMap<>();
        loadUsersFromFile("data/users.txt");
    }

    public boolean validateCredentials(String userID, String password) {
        User user = users.get(userID);
        return user != null && user.validatePassword(password);
    }

    public User getUser(String userID) {
        return users.get(userID);
    }

    private void loadUsersFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    User user = new User(
                        parts[0], parts[1], parts[2], parts[3], parts[4],
                        Enum.valueOf(server.models.enums.UserRole.class, parts[5])
                    );
                    users.put(user.getUserID(), user);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }
}
