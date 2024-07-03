package dialog;

import library.Book;
import library.BookGenre;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UpdateBookDialog extends JDialog {

    private JTextField titleField;
    private JTextField authorField;
    private JComboBox<BookGenre> genreComboBox;
    private JTextField publicationDateField;

    private Book updatedBook;

    public UpdateBookDialog(Frame parent, Book bookToUpdate) {
        super(parent, "Update Book", true); // Modal dialog with title "Update Book"
        initializeUI(bookToUpdate);
    }

    private void initializeUI(Book bookToUpdate) {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Title:"));
        titleField = new JTextField(bookToUpdate.getTitle());
        panel.add(titleField);

        panel.add(new JLabel("Author:"));
        authorField = new JTextField(bookToUpdate.getAuthor());
        panel.add(authorField);

        panel.add(new JLabel("Genre:"));
        genreComboBox = new JComboBox<>(BookGenre.values());
        genreComboBox.setSelectedItem(bookToUpdate.getBookGenre());
        panel.add(genreComboBox);

        panel.add(new JLabel("Publication Date (YYYY-MM-DD):"));
        publicationDateField = new JTextField(bookToUpdate.getPublicationDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        panel.add(publicationDateField);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateFields()) {
                    updateBook();
                    dispose(); // Close the dialog
                } else {
                    JOptionPane.showMessageDialog(UpdateBookDialog.this,
                            "Please fill in all fields correctly.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatedBook = null; // Cancel button sets updatedBook to null
                dispose(); // Close the dialog
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(updateButton);
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

    private void updateBook() {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        BookGenre genre = (BookGenre) genreComboBox.getSelectedItem();
        LocalDate publicationDate = LocalDate.parse(publicationDateField.getText().trim(), DateTimeFormatter.ISO_LOCAL_DATE);

        updatedBook = new Book(title, author, genre, publicationDate, true); // Assuming book is available by default
    }

    public Book getUpdatedBook() {
        return updatedBook;
    }
}

