/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author marcoang
 */
public class PublicMember extends User {
    private String membershipType; // Regular or Premium
    private String address;

    // Public Members have a borrow limit of 3 books (Regular) or 5 (Premium)
    public PublicMember(String id, String name, String email, double fineBalance, String membershipType, String address) {
        super(id, name, email, fineBalance, membershipType.equalsIgnoreCase("Premium") ? 5 : 3); 
        this.membershipType = membershipType;
        this.address = address;
    }

    public String getMembershipType() { return membershipType; }
    public void setMembershipType(String membershipType) { this.membershipType = membershipType; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toCSVString() {
        // Tag "PublicMember" for FileHandler
        return "PublicMember," + getId() + "," + getName() + "," + getEmail() + "," + getFineBalance() + "," + membershipType + "," + address;
    }

    @Override
    public String toString() {
        return "[Public Member] " + super.toString() + " | Membership: " + membershipType + " | Address: " + address;
    }
}
