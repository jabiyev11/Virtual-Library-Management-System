package library;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class Book {

    //Populated 74 books initially by just writing to csv that is why did not start from 1 but 75
    //Could be an inefficient way, not sure

    private static AtomicLong counter = new AtomicLong(75L);
    private Long bookID;
    private String title;
    private String author;
    private BookGenre bookGenre;
    private LocalDate publicationDate;
    private boolean isAvailable;


    static {
        // Initialize the counter based on existing books in the CSV file
        try (BufferedReader br = new BufferedReader(new FileReader("data/GeneralLibraryData.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                Long id = Long.parseLong(fields[0].trim());
                if (id > counter.get()) {
                    counter.set(id);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Book(String title, String author, BookGenre bookGenre, LocalDate publicationDate, boolean isAvailable) {
        this.bookID = counter.incrementAndGet();
        this.title = title;
        this.author = author;
        this.bookGenre = bookGenre;
        this.publicationDate = publicationDate;
        this.isAvailable = isAvailable;
    }


    //To load books from general csv, we will need to pass ID
    public Book(Long bookID, String title, String author, BookGenre bookGenre, LocalDate publicationDate, boolean isAvailable) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.bookGenre = bookGenre;
        this.publicationDate = publicationDate;
        this.isAvailable = isAvailable;
    }

    //To load books from personal books
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book book)) return false;
        return isAvailable == book.isAvailable && Objects.equals(bookID, book.bookID) && Objects.equals(title, book.title) && Objects.equals(author, book.author) && bookGenre == book.bookGenre && Objects.equals(publicationDate, book.publicationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookID, title, author, bookGenre, publicationDate, isAvailable);
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookID=" + bookID +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", bookGenre=" + bookGenre +
                ", publicationDate=" + publicationDate +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
