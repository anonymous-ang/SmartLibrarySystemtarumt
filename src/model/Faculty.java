/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author marcoang
 */
public class Faculty extends User {
    private String department;

    // Faculty members have a higher borrow limit of 10 books
    public Faculty(String id, String name, String email, double fineBalance, String department) {
        super(id, name, email, fineBalance, 10);
        this.department = department;
    }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    @Override
    public String toCSVString() {
        // Tag "Faculty" for FileHandler
        return "Faculty," + getId() + "," + getName() + "," + getEmail() + "," + getFineBalance() + "," + department;
    }

    @Override
    public String toString() {
        return "[Faculty] " + super.toString() + " | Dept: " + department;
    }
}
