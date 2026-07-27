package com.library.app;

import com.library.exception.BookNotFoundException;
import com.library.exception.UserNotFoundException;
import com.library.model.Book;
import com.library.model.Loan;
import com.library.model.User;
import com.library.repository.Repository;
import com.library.service.Library;

import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("       LIBRARY MANAGEMENT SYSTEM TEST RUN          ");
        System.out.println("==================================================\n");

        testBookValidationAndCreation();
        testUserValidationAndCreation();
        testLoanValidationAndDefensiveCopy();
        testRepositoryOperations();
        testLibraryServiceAndBorrowing();

        System.out.println("\n==================================================");
        System.out.println("      ALL SYSTEM TESTS COMPLETED SUCCESSFULLY!    ");
        System.out.println("==================================================");
    }

    private static void testBookValidationAndCreation() {
        System.out.println("--- 1. TESTING BOOK CREATION & VALIDATION ---");
        
        Book book = new Book("978-0132350884", "Clean Code", "Robert C. Martin", 2008);
        System.out.println("[SUCCESS] Created book: " + book.getTitle() + " (ISBN: " + book.getIsbn() + ")");

        try {
            System.out.println("[TEST] Attempting to create book with empty title...");
            new Book("123", "", "Author", 2020);
        } catch (IllegalArgumentException e) {
            System.out.println("[REJECTED] Unacceptable book details: " + e.getMessage());
        }

        try {
            System.out.println("[TEST] Attempting to create book with invalid publication year (-5)...");
            new Book("123", "Title", "Author", -5);
        } catch (IllegalArgumentException e) {
            System.out.println("[REJECTED] Unacceptable date/year: " + e.getMessage());
        }
        System.out.println();
    }

    private static void testUserValidationAndCreation() {
        System.out.println("--- 2. TESTING USER CREATION & VALIDATION ---");

        User user = new User("U001", "Alice", "alice@example.com");
        System.out.println("[SUCCESS] Registered user: " + user.getName() + " (ID: " + user.getUserId() + ")");

        try {
            System.out.println("[TEST] Attempting to register user with null ID...");
            new User(null, "Bob", "bob@example.com");
        } catch (IllegalArgumentException e) {
            System.out.println("[REJECTED] Unacceptable user details: " + e.getMessage());
        }
        System.out.println();
    }

    private static void testLoanValidationAndDefensiveCopy() {
        System.out.println("--- 3. TESTING LOAN DATES & DEFENSIVE COPIES ---");

        Book book = new Book("978-0132350884", "Clean Code", "Robert C. Martin", 2008);
        User user = new User("U001", "Alice", "alice@example.com");

        long now = System.currentTimeMillis();
        Date borrowDate = new Date(now - 86400000L); // 1 day ago
        Date dueDate = new Date(now + 86400000L);    // 1 day in future

        Loan loan = new Loan(book, user, borrowDate, dueDate);
        System.out.println("[SUCCESS] Loan created for '" + loan.getBook().getTitle() + "' to user '" + loan.getUser().getName() + "'");
        System.out.println("          Borrow Date: " + loan.getBorrowDate());
        System.out.println("          Due Date:    " + loan.getDueDate());
        System.out.println("          Is Overdue?  " + loan.isOverdue());

        try {
            System.out.println("[TEST] Attempting loan with DUE date BEFORE BORROW date...");
            new Loan(book, user, dueDate, borrowDate);
        } catch (IllegalArgumentException e) {
            System.out.println("[REJECTED] Unacceptable date range: " + e.getMessage());
        }
        System.out.println();
    }

    private static void testRepositoryOperations() {
        System.out.println("--- 4. TESTING REPOSITORY STORAGE ---");

        Repository<Book> bookRepo = new Repository<>();
        Book book = new Book("111", "Refactoring", "Martin Fowler", 1999);

        bookRepo.add("111", book);
        System.out.println("[SUCCESS] Added 'Refactoring' to repository. Total items: " + bookRepo.getAll().size());

        bookRepo.remove("111");
        System.out.println("[SUCCESS] Removed item '111' from repository. Total items: " + bookRepo.getAll().size());
        System.out.println();
    }

    private static void testLibraryServiceAndBorrowing() {
        System.out.println("--- 5. TESTING LIBRARY SERVICE & BORROWING WORKFLOW ---");

        Library library = new Library();
        Book book1 = new Book("978-0132350884", "Clean Code", "Robert C. Martin", 2008);
        Book book2 = new Book("978-0201633610", "Design Patterns", "Erich Gamma", 1994);
        User user1 = new User("U001", "Alice", "alice@example.com");

        library.addBook(book1);
        library.addBook(book2);
        library.addUser(user1);
        System.out.println("[SUCCESS] Library initialized with 2 books and 1 user.");

        // Successful borrow
        long now = System.currentTimeMillis();
        Date borrowDate = new Date(now - (86400000L * 2)); 
        Date futureDueDate = new Date(now + (86400000L * 5));
        
        library.borrowBook("978-0132350884", "U001", borrowDate, futureDueDate);
        System.out.println("[BORROWED] User 'Alice' borrowed 'Clean Code'. Active loans count: " + library.getLoanCount());

        // Attempting double borrow
        try {
            System.out.println("[TEST] Attempting to borrow 'Clean Code' again while it is already checked out...");
            library.borrowBook("978-0132350884", "U001", borrowDate, futureDueDate);
        } catch (IllegalStateException e) {
            System.out.println("[REJECTED] Double borrowing blocked: " + e.getMessage());
        }

        Date pastDueDate = new Date(now - 100000); 
        library.borrowBook("978-0201633610", "U001", borrowDate, pastDueDate);
        System.out.println("[BORROWED] User 'Alice' borrowed 'Design Patterns' (Notice: Due date was in the past).");
        System.out.println("           Total active loans before cleanup: " + library.getLoanCount());

        try {
            System.out.println("[TEST] Attempting to borrow non-existent ISBN '999-999'...");
            library.borrowBook("999-999", "U001", borrowDate, futureDueDate);
        } catch (BookNotFoundException e) {
            System.out.println("[NOT FOUND] System threw exception: " + e.getMessage());
        }

        System.out.println("[CLEANUP] Removing expired/late loans based on current time...");
        library.removeExpiredLoans(new Date());
        System.out.println("[SUCCESS] Active loans count after removing expired loans: " + library.getLoanCount());
    }
}