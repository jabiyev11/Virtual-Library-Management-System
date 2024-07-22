package window;



import library.Book;
import library.GeneralLibrary;
import library.PersonalLibrary;
import transaction.Transaction;
import transaction.TransactionType;
import user.User;
import user.UserRole;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import dialog.*;

public class GeneralLibraryWindow extends JFrame implements WindowCompatible, TableCompatible {

    private static final String CSV_GENERAL_LIBRARY_FILE_PATH = "data/GeneralLibraryData.csv";
    private static final String CSV_PERSONAL_LIBRARY_FILE_PATH = "data/PersonalLibraryData.csv";
    private static final String CSV_BORROWED_BOOKS_FILE_PATH = "data/BorrowedBooksData.csv";
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

            JButton borrowBook = new JButton("Borrow Book");
            borrowBook.setFont(new Font("SansSerif", Font.PLAIN, 13));
            borrowBook.setPreferredSize(new Dimension(150, 30));
            borrowBook.addActionListener(e -> borrowBook());


            buttonPanel.add(addBookToPersonalLibrary);
            buttonPanel.add(borrowBook);

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

    private void borrowBook(){

        int selectedRow = table.getSelectedRow();

        if(selectedRow >= 0){
            Book selectedBook = books.get(selectedRow);
            if(isBookAvailable(selectedBook)){
                selectedBook.setAvailable(false);
                updateBookAvailabilityInCSV(selectedBook);
                recordBorrowTransaction(selectedBook);
                updateTable();
                BorrowedBooksWindow borrowedBooksWindow = new BorrowedBooksWindow(currentUser);
                borrowedBooksWindow.setVisible(true);
                JOptionPane.showMessageDialog(this, "Book borrowed successfully");
            }else{
                JOptionPane.showMessageDialog(this, "Book is not available for borrowing");
            }
        }else{
            JOptionPane.showMessageDialog(this, "Please first select book to borrow");
        }
    }

    private void updateBookAvailabilityInCSV(Book book) {

        Function<Book, String> bookToCSV = b -> b.getBookID() + "," + b.getTitle() + "," + b.getAuthor() + "," + b.getBookGenre() + "," +
                b.getPublicationDate() + "," + b.isAvailable();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_GENERAL_LIBRARY_FILE_PATH))) {
            for (Book b : books) {
                writer.write(bookToCSV.apply(b));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void recordBorrowTransaction(Book book){
        Supplier<String> transactionIDSupplier = this::generateTransactionID;

        String transactionID = transactionIDSupplier.get();

        LocalDateTime borrowTime = LocalDateTime.now();
        LocalDateTime returnTime = LocalDateTime.of(9999, 12, 31, 23, 59, 59);

        Transaction transaction = new Transaction(transactionID, currentUser.getUserID(), book.getBookID(),
                borrowTime, returnTime, TransactionType.BORROW);

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_BORROWED_BOOKS_FILE_PATH, true))){

            writer.write(transaction.getTransactionID() + "," + transaction.getUserID() + "," +
                    transaction.getBookID() + "," + book.getTitle() + "," + book.getAuthor() + "," + transaction.getBorrowDate() + "," + transaction.getReturnDate() +
                    "," + transaction.getTransactionType().toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Problem in writing details of borrowed books");
        }
    }

    private String generateTransactionID(){
        Supplier<String> idSupplied = () -> UUID.randomUUID().toString();
        return idSupplied.get();
    }



    private void openAddingBookDialog(){
        AddBookDialog dialog = new AddBookDialog(this);
        dialog.setVisible(true);

        Book newBook = dialog.getBook();
        if(newBook != null){
            books.add(newBook);
            updateTable();
            saveBookToGeneralCSV();
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
                saveBookToGeneralCSV();
                PersonalLibrary.updatePersonalLibraries(bookToUpdate, updatedBook);
                JOptionPane.showMessageDialog(this, "Book updated successfully");
            }
        }else{
            JOptionPane.showMessageDialog(this, "Please select a Book first");
        }
    }

    private void removeBook() {
        int selectedRow = table.getSelectedRow();

        if(selectedRow >= 0){
            Book bookToRemove = books.get(selectedRow);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to remove this book?",
                    "Confirm Removal", JOptionPane.YES_NO_OPTION);

            if(confirm == JOptionPane.YES_OPTION){
                books.remove(selectedRow);
                updateTable();
                saveBookToGeneralCSV();
                PersonalLibrary.removeBookFromPersonalLibraries(bookToRemove);
                JOptionPane.showMessageDialog(this, "Book removed successfully");
            }
        }else{
            JOptionPane.showMessageDialog(this, "Please first select book");
        }

    }

    private void saveBookToGeneralCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_GENERAL_LIBRARY_FILE_PATH))) {
            Consumer<Book> writeBookToCSV = book -> {
                try {
                    writer.write(book.getBookID() + "," + book.getTitle() + "," + book.getAuthor() + "," +
                            book.getBookGenre() + "," + book.getPublicationDate() + "," + book.isAvailable() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };

            books.forEach(writeBookToCSV);
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

    private boolean isBookAvailable(Book book){
        Predicate<Book> isAvailable = b -> b.isAvailable();
        return isAvailable.test(book);
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
