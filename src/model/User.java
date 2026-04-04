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
public abstract class User {
    private String id;
    private String name;
    private String email;
    private double fineBalance;
    private int maxBorrowLimit; // Differentiates User types

    public User(String id, String name, String email, double fineBalance, int maxBorrowLimit) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.fineBalance = fineBalance;
        this.maxBorrowLimit = maxBorrowLimit;
    }

    // Getters and Setters (Encapsulation)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public double getFineBalance() { return fineBalance; }
    public void setFineBalance(double fineBalance) { this.fineBalance = fineBalance; }

    public int getMaxBorrowLimit() { return maxBorrowLimit; }
    public void setMaxBorrowLimit(int maxBorrowLimit) { this.maxBorrowLimit = maxBorrowLimit; }

    // Abstraction: For File I/O
    public abstract String toCSVString();

    // Polymorphism: Ensure Users are compared by their unique ID
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User otherUser = (User) obj;
        return Objects.equals(this.id, otherUser.id);
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Fines: RM" + String.format("%.2f", fineBalance);
    }
}