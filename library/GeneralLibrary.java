package library;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GeneralLibrary {

//    private static final String CSV_GENERAL_LIBRARY_FILE_PATH = "data/GeneralLibraryData.csv";

    public static List<Book> readBooksFromCSV(String filename){
        List<Book> books = new ArrayList<>();
        String line;

        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            reader.readLine();

            while((line = reader.readLine()) != null){
                String[] values = line.split(",");

                Long bookID = Long.parseLong(values[0]);
                String title = values[1];
                String author = values[2];
                BookGenre genre = BookGenre.valueOf(values[3]);
                LocalDate publicationDate = LocalDate.parse(values[4]);
                boolean isAvailable = Boolean.parseBoolean(values[5]);

                Book book = new Book(bookID, title, author, genre, publicationDate, isAvailable);
                books.add(book);
            }
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Error during loading of books from csv"); //Helpful for debugging
        }

        return books;
    }

//    public static void
}
