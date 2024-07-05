package window;


import transaction.BorrowedBookTransaction;
import transaction.TransactionType;
import user.User;
import user.UserManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionHistoryWindow extends JFrame {

    private static final String CSV_BORROWED_BOOKS_FILE_PATH = "data/BorrowedBooksData.csv";
    private List<BorrowedBookTransaction> transactions;
    private JTable table;

    public TransactionHistoryWindow() {
        setTitle("Transaction History");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        transactions = readTransactionsFromCSV();
        setupUI();
    }

    private void setupUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columnsNames = {"Transaction ID", "User", "Title", "Author", "Borrow Date", "Return Date", "Transaction Type"};
        DefaultTableModel model = new DefaultTableModel(columnsNames, 0);

        for (BorrowedBookTransaction transaction : transactions) {

            String userID = transaction.getUserID();

            String username = UserManager.getUsernameByID(userID);
            Object[] row = {
                    transaction.getTransactionID(),
                    username,
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

        add(mainPanel);
        setVisible(true);
    }

    private List<BorrowedBookTransaction> readTransactionsFromCSV() {
        List<BorrowedBookTransaction> transactions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_BORROWED_BOOKS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    BorrowedBookTransaction transaction = getBorrowedBookTransaction(parts);
                    transactions.add(transaction);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to read transaction history from CSV");
        }

        return transactions;
    }

    private BorrowedBookTransaction getBorrowedBookTransaction(String[] parts) {
        String transactionID = parts[0];
        String userID = parts[1];
        Long bookID = Long.parseLong(parts[2]);
        String title = parts[3];
        String author = parts[4];
        LocalDateTime borrowDate = LocalDateTime.parse(parts[5]);
        LocalDateTime returnDate = parts[6].isEmpty() ? null : LocalDateTime.parse(parts[6]);
        TransactionType transactionType = TransactionType.valueOf(parts[7]);

        return new BorrowedBookTransaction(transactionID, userID, bookID, title, author, borrowDate, returnDate, transactionType);
    }
}

