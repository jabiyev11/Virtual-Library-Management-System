package window;

import library.Book;
import library.GeneralLibrary;
import user.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeneralLibraryWindow extends JFrame {

    private static final String CSV_GENERAL_LIBRARY_FILE_PATH = "data/GeneralLibraryData.csv";
    private static final String CSV_PERSONAL_LIBRARY_FILE_PATH = "data/PersonalLibraryData.csv";
    private List<Book> books;
    private JTable table;
    private User currentUser;

    public GeneralLibraryWindow(User currentUser) {

        this.currentUser = currentUser;

        setTitle("General Library");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel);

        books = loadBooks();
        setupTable(mainPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton addBookToPersonalLibrary = new JButton("Add to Favourite List");
        addBookToPersonalLibrary.setFont(new Font("SansSerif", Font.PLAIN, 13));
        addBookToPersonalLibrary.setPreferredSize(new Dimension(150, 30));
        addBookToPersonalLibrary.addActionListener(e -> addToPersonalLibrary());


        buttonPanel.add(addBookToPersonalLibrary);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);


    }

    private List<Book> loadBooks(){
        return GeneralLibrary.readBooksFromCSV(CSV_GENERAL_LIBRARY_FILE_PATH);
    }

    private void setupTable(JPanel mainPanel){

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
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }


    private void addToPersonalLibrary(){
        int selectedRow = table.getSelectedRow();

        if(selectedRow >= 0){
            String title = table.getValueAt(selectedRow, 0).toString();
            String author = table.getValueAt(selectedRow, 1).toString();
            String genre = table.getValueAt(selectedRow, 2).toString();
            String publicationDate = table.getValueAt(selectedRow, 3).toString();

            try(FileWriter writer = new FileWriter(CSV_PERSONAL_LIBRARY_FILE_PATH, true)){
                writer.write(currentUser.getUserID() + "," + title + "," + author + "," + genre +
                        "," + publicationDate + "\n");
                JOptionPane.showMessageDialog(null, "Book added to Personal Library");
            }catch (IOException ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to add book to Personal Library");
            }
        }else{
            JOptionPane.showMessageDialog(null, "Please first select book to add to Personal Library");
        }
    }

}
