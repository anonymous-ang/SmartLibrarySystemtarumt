/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author marcoang
 */
public class StudyRoom {
    private String roomNumber;
    private int capacity;
    private User bookedBy; // Association: Links the room to the user who booked it

    public StudyRoom(String roomNumber, int capacity) {
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.bookedBy = null; // null means it is available
    }

    public String getRoomNumber() { return roomNumber; }
    public int getCapacity() { return capacity; }
    public User getBookedBy() { return bookedBy; }

    public void setBookedBy(User user) { 
        this.bookedBy = user; 
    }

    public boolean isAvailable() {
        return bookedBy == null;
    }

    @Override
    public String toString() {
        String status = isAvailable() ? "Available" : "Booked by: " + bookedBy.getName() + " (" + bookedBy.getId() + ")";
        return "Room: " + roomNumber + " | Capacity: " + capacity + " pax | Status: " + status;
    }
}