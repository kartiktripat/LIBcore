package server.models;

import java.io.Serializable;

public class Resource implements Serializable {
    private String resourceID;
    private String title;
    private String author;
    private String category;
    private boolean isAvailable;

    public Resource(String resourceID, String title, String author, String category) {
        this.resourceID = resourceID;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isAvailable = true;
    }

    public boolean requestResource() {
        if (isAvailable) {
            isAvailable = false;
            return true;
        }
        return false;
    }

    public void returnResource() {
        isAvailable = true;
    }

    // Getters and Setters
    public String getResourceID() { return resourceID; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public boolean isAvailable() { return isAvailable; }

    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setCategory(String category) { this.category = category; }
}