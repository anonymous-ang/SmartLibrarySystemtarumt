/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author marcoang
 */
public class DigitalResource extends LibraryItem {
    // Additional attributes specific to Digital Resources
    private String fileFormat; // e.g., PDF, EPUB, MP4
    private double fileSizeMB;

    public DigitalResource(String id, String title, boolean isAvailable, String fileFormat, double fileSizeMB) {
        super(id, title, isAvailable);
        this.fileFormat = fileFormat;
        this.fileSizeMB = fileSizeMB;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public double getFileSizeMB() {
        return fileSizeMB;
    }

    public void setFileSizeMB(double fileSizeMB) {
        this.fileSizeMB = fileSizeMB;
    }

    // Polymorphism: Implementing the abstract method for File I/O
    @Override
    public String toCSVString() {
        return "DigitalResource," + getId() + "," + getTitle() + "," + isAvailable() + "," + fileFormat + "," + fileSizeMB;
    }

    @Override
    public String toString() {
        return "[Digital Resource] " + super.toString() + " | Format: " + fileFormat + " | Size: " + fileSizeMB + "MB";
    }
}
