/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Objects;

/**
 *
 * @author marcoang
 */
public abstract class LibraryItem {
    private String id;
    private String title;
    private boolean isAvailable;
    
    // Constructor
    public LibraryItem(String id, String title, boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.isAvailable = isAvailable;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    // Forces subclasses to implement their own CSV formatting
    public abstract String toCSVString();

    // Overriding the default equals() method from Object class
    // This ensures items are compared by their unique ID, not memory address.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        LibraryItem otherItem = (LibraryItem) obj;
        return Objects.equals(this.id, otherItem.id);
    }

    //Overriding toString() for a clean console display
    @Override
    public String toString() {
        return "ID: " + id + " | Title: " + title + " | Available: " + (isAvailable ? "Yes" : "No");
    }
    
}
