package window;

import library.Book;
import library.GeneralLibrary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GeneralLibraryWindow extends JFrame {

    private static final String CSV_GENERAL_LIBRARY_FILE_PATH = "data/GeneralLibraryData.csv";
    private List<Book> books;

    public GeneralLibraryWindow() {

        setTitle("General Library");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        books = loadBooks();
        setupTable();


    }

    private List<Book> loadBooks(){
        return GeneralLibrary.readBooksFromCSV(CSV_GENERAL_LIBRARY_FILE_PATH);
    }

    private void setupTable(){

        String[] columnNames = {"Title", "Author", "Genre", "Publication Date", "Availability"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for(Book book : books){
            Object[] row = {
                    book.getTitle(),
                    book.getAuthor(),
                    book.getBookGenre(),
                    book.getPublicationDate(),
                    book.isAvailable() ? "Available" : "Not Available"
            };
            model.addRow(row);

        }
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }
}
