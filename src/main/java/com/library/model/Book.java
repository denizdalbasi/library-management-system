package com.library.model;

import java.util.Objects;
import java.time.Year;

public final class Book {

    private final String isbn;
    private final String title;
    private final String author;
    private final int year;

    public Book(String isbn, String title, String author, int year) {
        if (isbn == null || isbn.trim().isEmpty())
            throw new IllegalArgumentException("ISBN boş olamaz");
        if (title == null || title.trim().isEmpty())
            throw new IllegalArgumentException("Başlık boş olamaz");
        if (author == null || author.trim().isEmpty())
            throw new IllegalArgumentException("Yazar boş olamaz");
        if (year < 0 || year > Year.now().getValue())
            throw new IllegalArgumentException("Geçersiz yıl");

        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        return String.format("Book{isbn='%s', title='%s'}", isbn, title);
    }
}