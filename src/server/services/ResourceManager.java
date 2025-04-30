package server.services;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ResourceManager {
    private static final String RESOURCE_FILE = "data/resources.txt";

    public static void addResource(String id, String title, String author, String category) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RESOURCE_FILE, true))) {
            writer.write(id + "," + title + "," + author + "," + category + ",true");
            writer.newLine();
        }
    }

    public static List<String> getAllResources() throws IOException {
        List<String> resources = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(RESOURCE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                resources.add(line);
            }
        }
        return resources;
    }

    public static void updateAvailability(String resourceId, boolean available) throws IOException {
        List<String> resources = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(RESOURCE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5 && parts[0].equals(resourceId)) {
                    parts[4] = String.valueOf(available);
                    resources.add(String.join(",", parts));
                } else {
                    resources.add(line);
                }
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RESOURCE_FILE))) {
            for (String res : resources) {
                writer.write(res);
                writer.newLine();
            }
        }
    }
}
