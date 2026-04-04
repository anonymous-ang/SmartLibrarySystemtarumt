/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author marcoang
 */
public class Student extends User {
    private String major;

    // Students have a strict borrow limit of 5 books
    public Student(String id, String name, String email, double fineBalance, String major) {
        super(id, name, email, fineBalance, 5); 
        this.major = major;
    }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    @Override
    public String toCSVString() {
        // Tag "Student" helps FileHandler parse it correctly
        return "Student," + getId() + "," + getName() + "," + getEmail() + "," + getFineBalance() + "," + major;
    }

    @Override
    public String toString() {
        return "[Student] " + super.toString() + " | Major: " + major;
    }
}