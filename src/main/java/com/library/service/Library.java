package com.library.service;

import com.library.exception.BookNotFoundException;
import com.library.exception.UserNotFoundException;
import com.library.model.Book;
import com.library.model.Loan;
import com.library.model.User;
import com.library.repository.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Library {
    private final Repository<Book> bookRepo = new Repository<>();
    private final Repository<User> userRepo = new Repository<>();
    private final List<Loan> loans = new ArrayList<>();

    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        bookRepo.add(book.getIsbn(), book);
        assert repOk() : "Library invariant violated after adding book";
    }

    public Book getBook(String isbn) throws BookNotFoundException {
        return bookRepo.get(isbn)
                .orElseThrow(() -> new BookNotFoundException("Kitap bulunamadı: " + isbn));
    }

    public List<Book> getAllBooks() {
        return bookRepo.getAll();
    }

    // ----- User Operations -----
    public void addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        userRepo.add(user.getUserId(), user);
        assert repOk() : "Library invariant violated after adding user";
    }

    public User getUser(String userId) throws UserNotFoundException {
        return userRepo.get(userId)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı: " + userId));
    }

    public void borrowBook(String isbn, String userId, Date borrowDate, Date dueDate)
            throws BookNotFoundException, UserNotFoundException {

        Book book = getBook(isbn);
        User user = getUser(userId);

        boolean isBorrowed = loans.stream()
                .anyMatch(loan -> loan.getBook().equals(book));

        if (isBorrowed) {
            throw new IllegalStateException("Kitap zaten ödünç verilmiş: " + isbn);
        }

        Loan loan = new Loan(book, user, borrowDate, dueDate);
        loans.add(loan);
        assert repOk() : "Library invariant violated after borrowing book";
    }

    public List<Loan> getLoansForUser(String userId) throws UserNotFoundException {
        getUser(userId); 

        List<Loan> result = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan.getUser().getUserId().equals(userId)) {
                result.add(loan);
            }
        }
        return result; 
    }

    public void removeExpiredLoans(Date currentDate) {
        if (currentDate == null) {
            throw new IllegalArgumentException("Current date cannot be null");
        }

        Iterator<Loan> iterator = loans.iterator();
        while (iterator.hasNext()) {
            Loan loan = iterator.next();
            if (loan.getDueDate().before(currentDate)) {
                iterator.remove(); 
            }
        }
        assert repOk() : "Library invariant violated after removing expired loans";
    }

    public int getLoanCount() {
        return loans.size();
    }

    private boolean repOk() {
        return bookRepo != null && bookRepo.repOk() 
                && userRepo != null && userRepo.repOk() 
                && loans != null;
    }
}