package window;

import library.Book;
import transaction.BorrowedBookTransaction;
import transaction.TransactionType;
import user.User;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BorrowedBooksWindow extends JFrame implements TableCompatible, WindowCompatible{


    private static final String CSV_BORROWED_BOOKS_FILE_PATH = "data/BorrowedBooksData.csv";
    private static final String CSV_GENERAL_LIBRARY_FILE_PATH = "data/GeneralLibraryData.csv";

    private List<BorrowedBookTransaction> borrowedBooks;
    private JTable table;
    private JPanel mainPanel;
    private User currentUser;


    public BorrowedBooksWindow(User currentUser){

        this.currentUser= currentUser;

        mainPanel = new JPanel(new BorderLayout());


        borrowedBooks = readBorrowedBooksFromCSV();
        setupUI();





    }

    @Override
    public void setupUI() {

        setTitle("Borrowed Books");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setupTable(mainPanel);

        JPanel buttonPanel = new JPanel();

        JButton returnButton = new JButton("Return Book");
        returnButton.setFont(new Font("SansSerif", Font.PLAIN, 13));
        returnButton.setPreferredSize(new Dimension(150, 30));
      returnButton.addActionListener(e -> returnBook());

        buttonPanel.add(returnButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);



        add(mainPanel);
        setVisible(true);

    }

    @Override
    public void setupTable(JPanel mainPanel){


        String[] columnsNames = {"Title", "Author", "Borrow Date", "Return Date", "Transaction Type"};
        DefaultTableModel model = new DefaultTableModel(columnsNames, 0);

        for (BorrowedBookTransaction transaction : borrowedBooks) {
            Object[] row = {
                    transaction.getTitle(),
                    transaction.getAuthor(),
                    transaction.getBorrowTime(),
                    transaction.getReturnTime(),
                    transaction.getTransactionType()
            };
            model.addRow(row);
        }

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

    }


    private List<BorrowedBookTransaction> readBorrowedBooksFromCSV() {
        List<BorrowedBookTransaction> transactions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_BORROWED_BOOKS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8 && parts[1].equals(currentUser.getUserID())) {
                    BorrowedBookTransaction transaction = getBorrowedBookTransaction(parts);
                    transactions.add(transaction);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to read borrowed books data from CSV");
        }

        return transactions;
    }

    private static BorrowedBookTransaction getBorrowedBookTransaction(String[] parts) {
        String transactionID = parts[0];
        String userID = parts[1];
        Long bookID = Long.parseLong(parts[2]);
        String title = parts[3];
        String author = parts[4];
        LocalDateTime borrowDate = LocalDateTime.parse(parts[5]);
        LocalDateTime returnDate = LocalDateTime.parse(parts[6]);
        String transactionType = parts[7];

        TransactionType transactionTypeParsed = BorrowedBookTransaction.parseTransactionType(transactionType);


        BorrowedBookTransaction transaction = new BorrowedBookTransaction(transactionID, userID, bookID,
                title, author, borrowDate, returnDate, transactionTypeParsed);
        return transaction;
    }

    private void returnBook(){

        int selectedRow = table.getSelectedRow();

        if(selectedRow == -1){
            JOptionPane.showMessageDialog(this, "Please select a book to return");
            return;
        }else{
            BorrowedBookTransaction selectedTransaction = borrowedBooks.get(selectedRow);
            selectedTransaction.setReturnTime(LocalDateTime.now());
            selectedTransaction.setTransactionType(TransactionType.RETURN);

            updateBorrowedBookTransactionInCSV(selectedTransaction);
            updateBookAvailabilityInCSV(selectedTransaction.getBookID(), true);

            borrowedBooks = readBorrowedBooksFromCSV();
            updateTable();
            JOptionPane.showMessageDialog(this, "Book returned successfully");
        }



    }
    private void updateBorrowedBookTransactionInCSV(BorrowedBookTransaction transaction) {
        List<BorrowedBookTransaction> transactions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_BORROWED_BOOKS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                BorrowedBookTransaction trans = getBorrowedBookTransaction(parts);
                if (trans.getTransactionID().equals(transaction.getTransactionID())) {
                    trans.setReturnTime(transaction.getReturnTime());
                    trans.setTransactionType(transaction.getTransactionType());
                }
                transactions.add(trans);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to update borrowed books data in CSV");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_BORROWED_BOOKS_FILE_PATH, false))) {
            for (BorrowedBookTransaction trans : transactions) {
                writer.write(trans.getTransactionID() + "," + trans.getUserID() + "," +
                        trans.getBookID() + "," + trans.getTitle() + "," + trans.getAuthor() + "," +
                        trans.getBorrowTime() + "," + (trans.getReturnTime() != null ? trans.getReturnTime() : "") + "," +
                        trans.getTransactionType().toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to write updated borrowed books data to CSV");
        }
    }


    private void updateBookAvailabilityInCSV(Long bookID, boolean isAvailable) {
        List<String[]> books = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_GENERAL_LIBRARY_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (Long.parseLong(parts[0]) == bookID) {
                    parts[5] = Boolean.toString(isAvailable);
                }
                books.add(parts);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to update book availability in CSV");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_GENERAL_LIBRARY_FILE_PATH, false))) {
            for (String[] book : books) {
                writer.write(String.join(",", book));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to write updated book availability data to CSV");
        }
    }

    private void updateTable() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        for (BorrowedBookTransaction transaction : borrowedBooks) {
            Object[] row = {
                    transaction.getTitle(),
                    transaction.getAuthor(),
                    transaction.getBorrowTime(),
                    transaction.getReturnTime() != null ? transaction.getReturnTime() : "Not Returned",
                    transaction.getTransactionType()
            };
            model.addRow(row);
        }
    }


}
