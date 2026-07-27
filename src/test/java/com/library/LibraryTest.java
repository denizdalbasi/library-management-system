package com.library;

import com.library.model.Book;
import com.library.model.User;
import com.library.service.Library;
import com.library.exception.BookNotFoundException;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Date;

public class LibraryTest {

    @Test
    public void testAddAndGetBook() {
        Library library = new Library();
        Book book = new Book("123-456", "Clean Code", "Robert C. Martin", 2008);
        
        library.addBook(book);
        Book retrieved = library.getBook("123-456");
        
        assertEquals("Clean Code", retrieved.getTitle());
    }

    @Test(expected = BookNotFoundException.class)
    public void testGetNonExistentBookThrowsException() {
        Library library = new Library();
        library.getBook("invalid-isbn");
    }
}