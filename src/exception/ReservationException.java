/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exception;

/**
 * Exception thrown when a user tries to reserve a book that is already reserved by someone else
 * or when trying to perform an invalid reservation operation.
 * @author marcoang
 */
public class ReservationException extends Exception {
    public ReservationException(String message) {
        super(message);
    }
}
