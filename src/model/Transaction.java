/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.time.LocalDate;

/**
 *
 * @author marcoang
 */
public class Transaction {
    private String transactionId;
    
    // STRICT OOP DESIGN: Holding Object References, not just String IDs
    private User user;
    private LibraryItem item;
    
    private LocalDate issueDate;
    private LocalDate dueDate;
    private boolean isReturned;

    public Transaction(String transactionId, User user, LibraryItem item, LocalDate issueDate, LocalDate dueDate, boolean isReturned) {
        this.transactionId = transactionId;
        this.user = user;
        this.item = item;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.isReturned = isReturned;
    }

    // Encapsulation (Getters & Setters)
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LibraryItem getItem() { return item; }
    public void setItem(LibraryItem item) { this.item = item; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public boolean isReturned() { return isReturned; }
    public void setReturned(boolean returned) { this.isReturned = returned; }

    // When saving to CSV, we extract the IDs from the objects.
    // The FileHandler will reconstruct the actual Object references when reading.
    public String toCSVString() {
        return transactionId + "," + user.getId() + "," + item.getId() + "," + issueDate.toString() + "," + dueDate.toString() + "," + isReturned;
    }

    @Override
    public String toString() {
        return "Txn ID: " + transactionId + 
               "\n   User: " + user.getName() + " (" + user.getId() + ")" +
               "\n   Item: " + item.getTitle() + " (" + item.getId() + ")" +
               "\n   Issue Date: " + issueDate + " | Due Date: " + dueDate + 
               "\n   Status: " + (isReturned ? "Returned" : "On Loan") + "\n";
    }
}
