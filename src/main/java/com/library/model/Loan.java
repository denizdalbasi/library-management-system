package com.library.model;

import java.util.Date;
import java.util.Objects;

public final class Loan {

    private final Book book;
    private final User user;
    private final Date borrowDate;
    private final Date dueDate;

    public Loan(Book book, User user, Date borrowDate, Date dueDate) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (borrowDate == null) {
            throw new IllegalArgumentException("Borrow date cannot be null");
        }
        if (dueDate == null) {
            throw new IllegalArgumentException("Due date cannot be null");
        }

        if (dueDate.before(borrowDate)) {
            throw new IllegalArgumentException("Due date cannot be before borrow date");
        }

        this.book = book;
        this.user = user;
        
        this.borrowDate = new Date(borrowDate.getTime());
        this.dueDate = new Date(dueDate.getTime());

        assert repOk() : "Representation invariant violated in Loan creation";
    }

    public Book getBook() {
        return book;
    }

    public User getUser() {
        return user;
    }

    public Date getBorrowDate() {
        return new Date(borrowDate.getTime());
    }

    public Date getDueDate() {
        return new Date(dueDate.getTime());
    }

    public boolean isOverdue() {
        return new Date().after(this.dueDate);
    }

    public boolean repOk() {
        if (book == null || user == null || borrowDate == null || dueDate == null) {
            return false;
        }
        if (dueDate.before(borrowDate)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Loan)) return false;
        Loan loan = (Loan) o;
        return Objects.equals(book, loan.book) &&
               Objects.equals(user, loan.user) &&
               Objects.equals(borrowDate, loan.borrowDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(book, user, borrowDate);
    }

    @Override
    public String toString() {
        return String.format("Loan{book=%s, user=%s, due=%s}", book, user, dueDate);
    }
}