package dialog;

import library.Book;
import library.BookGenre;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddBookDialog extends JDialog {

    private JTextField titleField;
    private JTextField authorField;
    private JComboBox<BookGenre> genreComboBox;
    private JTextField publicationDateField;
    private Book newBook;

    public AddBookDialog(Frame parent) {
        super(parent, "Add New Book", true); // Modal dialog with title "Add New Book"
        initializeUI();
    }

    private void initializeUI() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Title:"));
        titleField = new JTextField();
        panel.add(titleField);

        panel.add(new JLabel("Author:"));
        authorField = new JTextField();
        panel.add(authorField);

        panel.add(new JLabel("Genre:"));
        genreComboBox = new JComboBox<>(BookGenre.values());
        panel.add(genreComboBox);

        panel.add(new JLabel("Publication Date (YYYY-MM-DD):"));
        publicationDateField = new JTextField();
        panel.add(publicationDateField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateFields()) {
                    createBook();
                    dispose(); // Close the dialog
                } else {
                    JOptionPane.showMessageDialog(AddBookDialog.this,
                            "Please fill in all fields correctly.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newBook = null; // Cancel button sets newBook to null
                dispose(); // Close the dialog
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null); // Center the dialog on screen
    }

    private boolean validateFields() {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String publicationDateStr = publicationDateField.getText().trim();

        if (title.isEmpty() || author.isEmpty() || publicationDateStr.isEmpty()) {
            return false;
        }

        // Validate publication date format
        try {
            LocalDate.parse(publicationDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private void createBook() {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        BookGenre genre = (BookGenre) genreComboBox.getSelectedItem();
        LocalDate publicationDate = LocalDate.parse(publicationDateField.getText().trim(), DateTimeFormatter.ISO_LOCAL_DATE);

        newBook = new Book(title, author, genre, publicationDate, true); // Assuming book is available by default
    }

    public Book getBook() {
        return newBook;
    }
}
