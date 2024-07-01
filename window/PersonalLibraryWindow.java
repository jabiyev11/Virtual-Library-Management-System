package window;

import library.Book;
import library.BookGenre;
import user.User;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PersonalLibraryWindow extends JFrame {

    private User currentUser;
    private JTable personalLibraryTable;

    private static final String CSV_PERSONAL_LIBRARY_FILE_PATH = "data/PersonalLibraryData.csv";

    public PersonalLibraryWindow(User currentUser){
        this.currentUser = currentUser;
        setTitle("Personal Library");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        List<Book> personalBooks = loadPersonalBooks();

        String[] columnNames = {"Title", "Author", "Genre", "Publication Date"};
        String[][] data = new String[personalBooks.size()][4];

        for(int i = 0; i < personalBooks.size(); i++){
            Book book = personalBooks.get(i);
            data[i][0] = book.getTitle();
            data[i][1] = book.getAuthor();
            data[i][2] = book.getBookGenre().toString();
            data[i][3] = book.getPublicationDate().toString();
        }

        personalLibraryTable = new JTable(data, columnNames);
        add(new JScrollPane(personalLibraryTable), BorderLayout.CENTER);


    }


    private List<Book> loadPersonalBooks(){

        List<Book> personalBooks = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(CSV_PERSONAL_LIBRARY_FILE_PATH))){
            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split(",");
                if(parts[0].equals(currentUser.getUserID())){
//                    long bookID = Long.parseLong(parts[1]);
                    String author = parts[1];
                    String title = parts[2];
                    BookGenre genre = BookGenre.valueOf(parts[3]);
                    LocalDate publicationDate = LocalDate.parse(parts[4]);
                    personalBooks.add(new Book(author, title, genre, publicationDate));
                }
            }
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Error during loading data from Personal csv");  //Helpful for debugging
        }
        return personalBooks;
    }



}
