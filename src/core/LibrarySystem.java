/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core;
import model.*;
import exception.*;
import java.time.LocalDate;
import java.util.ArrayList;
/**
 *
 * @author marcoang
 */


public class LibrarySystem {
    
    // AGGREGATION: LibrarySystem "has-a" collection of Users, Items, and Transactions.
    // They can conceptually exist independently (e.g., loaded from a file).
    private ArrayList<User> users;
    private ArrayList<LibraryItem> items;
    private ArrayList<Transaction> transactions;

    // COMPOSITION: LibrarySystem completely owns and controls the lifecycle of StudyRooms.
    // If the LibrarySystem is destroyed, the StudyRooms are conceptually destroyed.
    private ArrayList<StudyRoom> studyRooms;
    
    // NEW: Reservation management for books on loan
    private ArrayList<Reservation> reservations;
    
    // Fine rate per day (RM)
    private static final double FINE_RATE_PER_DAY = 1.50;
    
    // Loan duration in days
    private static final int LOAN_DURATION_DAYS = 14;

    public LibrarySystem() {
        this.users = new ArrayList<>();
        this.items = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.reservations = new ArrayList<>();
        
        // Implementing Composition: The System creates its own parts internally.
        this.studyRooms = new ArrayList<>();
        this.studyRooms.add(new StudyRoom("SR-101", 4));
        this.studyRooms.add(new StudyRoom("SR-102", 6));
        this.studyRooms.add(new StudyRoom("SR-103", 2));
    }

    // --- AGGREGATION LIST GETTERS/SETTERS (Used by FileHandler later) ---
    public ArrayList<User> getUsers() { return users; }
    public void setUsers(ArrayList<User> users) { this.users = users; }

    public ArrayList<LibraryItem> getItems() { return items; }
    public void setItems(ArrayList<LibraryItem> items) { this.items = items; }

    public ArrayList<Transaction> getTransactions() { return transactions; }
    public void setTransactions(ArrayList<Transaction> transactions) { this.transactions = transactions; }

    public ArrayList<StudyRoom> getStudyRooms() { return studyRooms; }
    
    public ArrayList<Reservation> getReservations() { return reservations; }
    public void setReservations(ArrayList<Reservation> reservations) { this.reservations = reservations; }
    
    public double getFineRatePerDay() { return FINE_RATE_PER_DAY; }
    public int getLoanDurationDays() { return LOAN_DURATION_DAYS; }

    // --- CRUD: CREATE ---
    public void registerUser(User user) {
        users.add(user);
    }

    public void addLibraryItem(LibraryItem item) {
        items.add(item);
    }

    // --- CRUD: READ (With Custom Exceptions) ---
    public User findUserById(String userId) throws EntityNotFoundException {
        for (User u : users) {
            if (u.getId().equalsIgnoreCase(userId)) {
                return u;
            }
        }
        throw new EntityNotFoundException("Error: User with ID '" + userId + "' was not found.");
    }

    public LibraryItem findItemById(String itemId) throws EntityNotFoundException {
        for (LibraryItem item : items) {
            if (item.getId().equalsIgnoreCase(itemId)) {
                return item;
            }
        }
        throw new EntityNotFoundException("Error: Item with ID '" + itemId + "' was not found in the catalog.");
    }

    // --- BUSINESS LOGIC: BORROW ITEM ---
    // Notice the 'throws' keyword. We force the caller (Main UI) to handle the errors!
     public void borrowItem(String userId, String itemId) throws EntityNotFoundException, ItemNotAvailableException, LimitExceededException {
        User user = findUserById(userId);
        LibraryItem item = findItemById(itemId);

        if (!item.isAvailable()) {
            throw new ItemNotAvailableException("Sorry, the item '" + item.getTitle() + "' is currently on loan.");
        }

        // --- NEW LOGIC: ENFORCE BORROW LIMIT ---
        int activeLoans = 0;
        for (Transaction txn : transactions) {
            if (txn.getUser().getId().equalsIgnoreCase(userId) && !txn.isReturned()) {
                activeLoans++;
            }
        }
        
        if (activeLoans >= user.getMaxBorrowLimit()) {
            throw new LimitExceededException("User " + user.getName() + " has reached their maximum limit of " + user.getMaxBorrowLimit() + " items.");
        }
        // ----------------------------------------

        // Logic: Update item status
        item.setAvailable(false);

        // Logic: Create Transaction (Proper Object Association)
        String txnId = "TXN-" + System.currentTimeMillis(); 
        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(LOAN_DURATION_DAYS); 

        Transaction newTxn = new Transaction(txnId, user, item, issueDate, dueDate, false);
        transactions.add(newTxn);
        
        // Check if there's a reservation for this item and fulfill it
        fulfillReservationIfAny(item, user);
    }
    
    // Helper method to fulfill reservation when a book becomes available
    private void fulfillReservationIfAny(LibraryItem item, User currentUser) {
        if (!(item instanceof Book)) return;
        Book book = (Book) item;
        
        // Find first pending reservation for this book
        for (Reservation res : reservations) {
            if (res.getBook().equals(book) && !res.isFulfilled() && !res.isCancelled()) {
                // Notify the reserved user (in real system, send email/SMS)
                System.out.println("NOTIFICATION: Book '" + book.getTitle() + "' is now available for " + res.getUser().getName());
                break;
            }
        }
    }

    // --- BUSINESS LOGIC: RETURN ITEM ---
    public void returnItem(String txnId) throws EntityNotFoundException {
        for (Transaction txn : transactions) {
            if (txn.getTransactionId().equalsIgnoreCase(txnId) && !txn.isReturned()) {
                // Update transaction status
                txn.setReturned(true);
                
                // Update item availability (Accessing actual object via Association)
                txn.getItem().setAvailable(true);
                
                // Fine calculation logic using constant FINE_RATE_PER_DAY
                LocalDate today = LocalDate.now();
                if (today.isAfter(txn.getDueDate())) {
                    long daysLate = java.time.temporal.ChronoUnit.DAYS.between(txn.getDueDate(), today);
                    double fine = daysLate * FINE_RATE_PER_DAY;
                    User user = txn.getUser();
                    user.setFineBalance(user.getFineBalance() + fine);
                    System.out.println("Item returned late by " + daysLate + " days. Fine: RM" + String.format("%.2f", fine));
                } else {
                    System.out.println("Item returned on time. No fine applied.");
                }
                
                // Check and notify any reservations for this item
                notifyReservationIfAny(txn.getItem());
                
                return; // Exit successfully
            }
        }
        throw new EntityNotFoundException("Error: Active Transaction ID '" + txnId + "' not found.");
    }
    
    // Helper method to notify users with reservations when a book is returned
    private void notifyReservationIfAny(LibraryItem item) {
        if (!(item instanceof Book)) return;
        Book book = (Book) item;
        
        // Find first pending reservation for this book
        for (Reservation res : reservations) {
            if (res.getBook().equals(book) && !res.isFulfilled() && !res.isCancelled()) {
                // Notify the reserved user (in real system, send email/SMS)
                System.out.println("NOTIFICATION: Book '" + book.getTitle() + "' is now available for " + res.getUser().getName());
                break;
            }
        }
    }
    // --- HELPER METHOD: FIND ROOM ---
    public StudyRoom findRoomByNumber(String roomNumber) throws EntityNotFoundException {
        for (StudyRoom room : studyRooms) {
            if (room.getRoomNumber().equalsIgnoreCase(roomNumber)) {
                return room;
            }
        }
        throw new EntityNotFoundException("Error: Study Room '" + roomNumber + "' does not exist.");
    }

    // --- BUSINESS LOGIC: BOOK A ROOM ---
    public void bookStudyRoom(String userId, String roomNumber) throws EntityNotFoundException, RoomUnavailableException {
        User user = findUserById(userId);
        StudyRoom room = findRoomByNumber(roomNumber);

        if (!room.isAvailable()) {
            throw new RoomUnavailableException("Sorry, room " + roomNumber + " is already booked by another user.");
        }

        // Logic: Associate the user with the room
        room.setBookedBy(user);
    }
    
    // --- BUSINESS LOGIC: CANCEL ROOM BOOKING ---
    public void cancelStudyRoomBooking(String roomNumber) throws EntityNotFoundException, InvalidOperationException {
        // 1. Find the room
        StudyRoom room = findRoomByNumber(roomNumber);

        // 2. Check if it is actually booked
        if (room.isAvailable()) {
            throw new InvalidOperationException("Cannot cancel: Room " + roomNumber + " is already available.");
        }

        // 3. Revert to normal status by removing the User association (setting it to null)
        room.setBookedBy(null); 
    }
    
    // ==================== NEW FEATURES ====================
    
    // --- BUSINESS LOGIC: RESERVE A BOOK ---
    public void reserveBook(String userId, String bookId) throws EntityNotFoundException, ItemNotAvailableException, ReservationException {
        User user = findUserById(userId);
        LibraryItem item = findItemById(bookId);
        
        // Only books can be reserved
        if (!(item instanceof Book)) {
            throw new ReservationException("Only books can be reserved. Digital resources cannot be reserved.");
        }
        Book book = (Book) item;
        
        // Check if the book is already available - no need to reserve
        if (book.isAvailable()) {
            throw new ReservationException("Book '" + book.getTitle() + "' is currently available. You can borrow it directly without reservation.");
        }
        
        // Check if user already has an active reservation for this book
        for (Reservation res : reservations) {
            if (res.getUser().equals(user) && res.getBook().equals(book) && !res.isFulfilled() && !res.isCancelled()) {
                throw new ReservationException("You already have a pending reservation for this book.");
            }
        }
        
        // Create new reservation
        String reservationId = "RES-" + System.currentTimeMillis();
        Reservation newReservation = new Reservation(reservationId, user, book, LocalDate.now());
        reservations.add(newReservation);
        
        System.out.println("Reservation successful! Your reservation ID is: " + reservationId);
        System.out.println("You will be notified when the book becomes available.");
    }
    
    // --- BUSINESS LOGIC: VIEW USER'S RESERVATIONS ---
    public ArrayList<Reservation> getUserReservations(String userId) throws EntityNotFoundException {
        User user = findUserById(userId);
        ArrayList<Reservation> userReservations = new ArrayList<>();
        
        for (Reservation res : reservations) {
            if (res.getUser().equals(user) && !res.isFulfilled() && !res.isCancelled()) {
                userReservations.add(res);
            }
        }
        
        return userReservations;
    }
    
    // --- BUSINESS LOGIC: CANCEL RESERVATION ---
    public void cancelReservation(String reservationId) throws EntityNotFoundException, InvalidOperationException {
        Reservation foundReservation = null;
        
        for (Reservation res : reservations) {
            if (res.getReservationId().equalsIgnoreCase(reservationId)) {
                foundReservation = res;
                break;
            }
        }
        
        if (foundReservation == null) {
            throw new EntityNotFoundException("Reservation with ID '" + reservationId + "' not found.");
        }
        
        if (foundReservation.isFulfilled()) {
            throw new InvalidOperationException("Cannot cancel: This reservation has already been fulfilled.");
        }
        
        if (foundReservation.isCancelled()) {
            throw new InvalidOperationException("Cannot cancel: This reservation is already cancelled.");
        }
        
        foundReservation.setCancelled(true);
        System.out.println("Reservation " + reservationId + " has been cancelled successfully.");
    }
    
    // --- BUSINESS LOGIC: PAY FINE ---
    public void payFine(String userId, double amount) throws EntityNotFoundException, InvalidOperationException {
        User user = findUserById(userId);
        double currentBalance = user.getFineBalance();
        
        if (currentBalance <= 0) {
            throw new InvalidOperationException("User has no outstanding fines.");
        }
        
        if (amount <= 0) {
            throw new InvalidOperationException("Payment amount must be positive.");
        }
        
        if (amount > currentBalance) {
            throw new InvalidOperationException("Payment amount exceeds fine balance. Current balance: RM" + String.format("%.2f", currentBalance));
        }
        
        user.setFineBalance(currentBalance - amount);
        System.out.println("Payment of RM" + String.format("%.2f", amount) + " received. Remaining balance: RM" + String.format("%.2f", user.getFineBalance()));
    }
    
    // --- BUSINESS LOGIC: SEARCH CATALOG BY GENRE ---
    public ArrayList<Book> searchBooksByGenre(String genre) {
        ArrayList<Book> matchingBooks = new ArrayList<>();
        
        for (LibraryItem item : items) {
            if (item instanceof Book) {
                Book book = (Book) item;
                if (book.getGenre().equalsIgnoreCase(genre)) {
                    matchingBooks.add(book);
                }
            }
        }
        
        return matchingBooks;
    }
    
    // --- BUSINESS LOGIC: SEARCH CATALOG BY AUTHOR ---
    public ArrayList<Book> searchBooksByAuthor(String author) {
        ArrayList<Book> matchingBooks = new ArrayList<>();
        
        for (LibraryItem item : items) {
            if (item instanceof Book) {
                Book book = (Book) item;
                if (book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                    matchingBooks.add(book);
                }
            }
        }
        
        return matchingBooks;
    }
    
    // --- BUSINESS LOGIC: VIEW E-BOOKS (DIGITAL RESOURCES) ---
    public ArrayList<DigitalResource> viewEBooks() {
        ArrayList<DigitalResource> eResources = new ArrayList<>();
        
        for (LibraryItem item : items) {
            if (item instanceof DigitalResource) {
                eResources.add((DigitalResource) item);
            }
        }
        
        return eResources;
    }
    
    // --- BUSINESS LOGIC: REMOVE DAMAGED BOOK (LIBRARIAN FUNCTION) ---
    public void removeDamagedItem(String itemId) throws EntityNotFoundException, InvalidOperationException {
        LibraryItem itemToRemove = null;
        
        for (LibraryItem item : items) {
            if (item.getId().equalsIgnoreCase(itemId)) {
                itemToRemove = item;
                break;
            }
        }
        
        if (itemToRemove == null) {
            throw new EntityNotFoundException("Item with ID '" + itemId + "' not found.");
        }
        
        // Check if item is currently on loan
        if (!itemToRemove.isAvailable()) {
            throw new InvalidOperationException("Cannot remove item: It is currently on loan. Please wait for return.");
        }
        
        items.remove(itemToRemove);
        System.out.println("Item '" + itemToRemove.getTitle() + "' has been removed from the catalog.");
    }
}