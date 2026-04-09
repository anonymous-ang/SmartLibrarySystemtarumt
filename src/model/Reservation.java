/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;

/**
 * Represents a reservation request for a book that is currently on loan.
 * @author marcoang
 */
public class Reservation {
    private String reservationId;
    private User user;
    private Book book;
    private LocalDate requestDate;
    private boolean isFulfilled;
    private boolean isCancelled;

    public Reservation(String reservationId, User user, Book book, LocalDate requestDate) {
        this.reservationId = reservationId;
        this.user = user;
        this.book = book;
        this.requestDate = requestDate;
        this.isFulfilled = false;
        this.isCancelled = false;
    }

    // Getters and Setters
    public String getReservationId() { return reservationId; }
    public void setReservationId(String reservationId) { this.reservationId = reservationId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    public LocalDate getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDate requestDate) { this.requestDate = requestDate; }

    public boolean isFulfilled() { return isFulfilled; }
    public void setFulfilled(boolean fulfilled) { isFulfilled = fulfilled; }

    public boolean isCancelled() { return isCancelled; }
    public void setCancelled(boolean cancelled) { isCancelled = cancelled; }

    public String toCSVString() {
        return reservationId + "," + user.getId() + "," + book.getId() + "," + requestDate.toString() + "," + isFulfilled + "," + isCancelled;
    }

    @Override
    public String toString() {
        String status = isFulfilled ? "Fulfilled" : (isCancelled ? "Cancelled" : "Pending");
        return "Reservation ID: " + reservationId + 
               " | User: " + user.getName() + 
               " | Book: " + book.getTitle() + 
               " | Date: " + requestDate + 
               " | Status: " + status;
    }
}
