/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author marcoang
 */
public class Book extends LibraryItem {
    // Additional attributes specific to Book
    private String author;
    private String isbn;
    private String genre;

    // Constructor using super() to call the parent class constructor
    public Book(String id, String title, boolean isAvailable, String author, String isbn, String genre) {
        super(id, title, isAvailable);
        this.author = author;
        this.isbn = isbn;
        this.genre = genre;
    }

    // Getters and Setters
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    // Implementing the abstract method
    // Notice how we prepend "Book" to help the FileHandler identify the object type later!
    @Override
    public String toCSVString() {
        return "Book," + getId() + "," + getTitle() + "," + isAvailable() + "," + author + "," + isbn + "," + genre;
    }

    //  Overriding toString() to include Book-specific details
    @Override
    public String toString() {
        return "[Book] " + super.toString() + " | Author: " + author + " | ISBN: " + isbn + " | Genre: " + genre;
    }
}
