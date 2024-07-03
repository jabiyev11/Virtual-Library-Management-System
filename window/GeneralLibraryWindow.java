package window;


import dialog.UpdateBookDialog;
import library.Book;
import library.BookGenre;
import library.GeneralLibrary;
import user.User;
import user.UserManager;
import user.UserRole;
import window.AddBookDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class GeneralLibraryWindow extends JFrame implements WindowCompatible, TableCompatible {

    private static final String CSV_GENERAL_LIBRARY_FILE_PATH = "data/GeneralLibraryData.csv";
    private static final String CSV_PERSONAL_LIBRARY_FILE_PATH = "data/PersonalLibraryData.csv";
    private List<Book> books;
    private JTable table;
    private User currentUser;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField searchField;
    private JPanel mainPanel;

    public GeneralLibraryWindow(User currentUser) {

        this.currentUser = currentUser;

        books = loadBooks();
        setupUI();
        setupTable(mainPanel);
        setupSearchBar(mainPanel);

    }

    @Override
    public void setupUI() {
        setTitle("General Library");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        if (currentUser.getRole() == UserRole.USER) {
            JButton addBookToPersonalLibrary = new JButton("Add to Favourite List");
            addBookToPersonalLibrary.setFont(new Font("SansSerif", Font.PLAIN, 13));
            addBookToPersonalLibrary.setPreferredSize(new Dimension(150, 30));
            addBookToPersonalLibrary.addActionListener(e -> addToPersonalLibrary());
            buttonPanel.add(addBookToPersonalLibrary);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        } else if (currentUser.getRole() == UserRole.LIBRARIAN) {      //Can use else, but else if is better if in the future we will have additional role
            JButton addNewBook = new JButton("Add Book");
            addNewBook.setFont(new Font("SansSerif", Font.PLAIN, 13));
            addNewBook.setPreferredSize(new Dimension(150, 30));
            addNewBook.addActionListener(e -> openAddingBookDialog());

            JButton updateBook = new JButton("Update Book");
            updateBook.setFont(new Font("SansSerif", Font.PLAIN, 13));
            updateBook.setPreferredSize(new Dimension(150, 30));
            updateBook.addActionListener(e -> openUpdateBookDialog());

            JButton removeBook = new JButton("Remove Book");
            removeBook.setFont(new Font("SansSerif", Font.PLAIN, 13));
            removeBook.setPreferredSize(new Dimension(150, 30));
            removeBook.addActionListener(e -> removeBook());

            buttonPanel.add(addNewBook);
            buttonPanel.add(updateBook);
            buttonPanel.add(removeBook);

            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        }

        add(mainPanel);
        setVisible(true);

    }




    private void openAddingBookDialog(){
        AddBookDialog dialog = new AddBookDialog(this);
        dialog.setVisible(true);

        Book newBook = dialog.getBook();
        if(newBook != null){
            books.add(newBook);
            updateTable();
            saveBookToCSV();
            JOptionPane.showMessageDialog(this, "Book added successfully");
        }
    }

    private void openUpdateBookDialog(){
        int selectedRow = table.getSelectedRow();

        if(selectedRow >= 0){
            Book bookToUpdate = books.get(selectedRow);

            UpdateBookDialog dialog = new UpdateBookDialog(this, bookToUpdate);
            dialog.setVisible(true);


            Book updatedBook = dialog.getUpdatedBook();

            if(updatedBook != null){
                books.set(selectedRow, updatedBook);
                updateTable();
                saveBookToCSV();
                JOptionPane.showMessageDialog(this, "Book updated successfully");
            }
        }else{
            JOptionPane.showMessageDialog(this, "Please select a Book first");
        }
    }

    private void removeBook() {
        int selectedRow = table.getSelectedRow();

        if(selectedRow >= 0){
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to remove this book?",
                    "Confirm Removal", JOptionPane.YES_NO_OPTION);

            if(confirm == JOptionPane.YES_OPTION){
                books.remove(selectedRow);
                updateTable();
                saveBookToCSV();
                JOptionPane.showMessageDialog(this, "Book removed successfully");
            }
        }else{
            JOptionPane.showMessageDialog(this, "Please first select book");
        }

    }

    private void saveBookToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_GENERAL_LIBRARY_FILE_PATH))) {
            for (Book book : books) {
                writer.write(book.getBookID() + "," + book.getTitle() + "," + book.getAuthor() + "," +
                        book.getBookGenre() + "," + book.getPublicationDate() + "," + book.isAvailable() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to save books to CSV");
            System.out.println("Error during writing books to general csv file"); //For debugging
        }
    }

    private void updateTable() {
        model.setRowCount(0); // Clear the table
        for (Book book : books) {
            Object[] row = {
                    book.getTitle(),
                    book.getAuthor(),
                    book.getBookGenre(),
                    book.getPublicationDate(),
                    book.isAvailable() ? "Available" : "Not Available"
            };
            model.addRow(row);
        }
    }

    private List<Book> loadBooks() {
        return GeneralLibrary.readBooksFromCSV(CSV_GENERAL_LIBRARY_FILE_PATH);
    }

    @Override
    public void setupTable(JPanel mainPanel) {

        String[] columnNames = {"Title", "Author", "Genre", "Publication Date", "Availability"};
        Object[][] data = new Object[books.size()][5];

        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            data[i] = new Object[]{
                    book.getTitle(),
                    book.getAuthor(),
                    book.getBookGenre(),
                    book.getPublicationDate(),
                    book.isAvailable() ? "Available" : "Not Available"
            };

        }

        model = new CustomTableModel(data, columnNames, currentUser);
        table = new JTable(model);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(table);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }


    private void addToPersonalLibrary() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow >= 0) {
            String title = table.getValueAt(selectedRow, 0).toString();
            String author = table.getValueAt(selectedRow, 1).toString();
            String genre = table.getValueAt(selectedRow, 2).toString();
            String publicationDate = table.getValueAt(selectedRow, 3).toString();

            try (FileWriter writer = new FileWriter(CSV_PERSONAL_LIBRARY_FILE_PATH, true)) {
                writer.write(currentUser.getUserID() + "," + title + "," + author + "," + genre +
                        "," + publicationDate + "\n");
                JOptionPane.showMessageDialog(null, "Book added to Personal Library");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to add book to Personal Library");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please first select book to add to Personal Library");
        }
    }


    private void setupSearchBar(JPanel mainPanel) {

        JPanel searchPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(20);

        searchField.addKeyListener(new KeyAdapter() {
            @Override                                           //Cannot use Lambda because of
            public void keyReleased(KeyEvent e) {               //too many abstract methods in KeyAdapter class
                performSearch();
            }
        });

        searchPanel.add(new JLabel("Search"));
        searchPanel.add(searchField);
        mainPanel.add(searchPanel, BorderLayout.NORTH);
    }

    private void performSearch() {
        String query = searchField.getText().toLowerCase();

        RowFilter<DefaultTableModel, Integer> rowFilter = new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                for (int i = 0; i < entry.getModel().getColumnCount(); i++) {
                    String cellValue = entry.getStringValue(i).toLowerCase();
                    if (cellValue.contains(query)) {
                        return true;
                    }
                }
                return false;
            }
        };

        sorter.setRowFilter(rowFilter);
    }

}
