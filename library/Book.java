package library;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Book {

    private static Long counter = 1L;
    private Long bookID;
    private String title;
    private String author;
    private BookGenre bookGenre;
    private LocalDate publicationDate;
    private boolean isAvailable;

    public Book(String title, String author, BookGenre bookGenre, LocalDate publicationDate, boolean isAvailable) {
        this.bookID = counter++;
        this.title = title;
        this.author = author;
        this.bookGenre = bookGenre;
        this.publicationDate = publicationDate;
        this.isAvailable = isAvailable;
    }


    //To load books from csv, we will need to pass ID
    public Book(Long bookID, String title, String author, BookGenre bookGenre, LocalDate publicationDate, boolean isAvailable) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.bookGenre = bookGenre;
        this.publicationDate = publicationDate;
        this.isAvailable = isAvailable;
    }

    public Book( String title, String author, BookGenre bookGenre, LocalDate publicationDate) {
        this.title = title;
        this.author = author;
        this.bookGenre = bookGenre;
        this.publicationDate = publicationDate;
    }

    public Long getBookID() {
        return bookID;
    }

    public void setBookID(Long bookID) {
        this.bookID = bookID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BookGenre getBookGenre() {
        return bookGenre;
    }

    public void setBookGenre(BookGenre bookGenre) {
        this.bookGenre = bookGenre;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
