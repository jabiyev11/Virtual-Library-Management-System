package window;

import library.Book;
import library.GeneralLibrary;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LibraryWindow extends JFrame {

    private static final String CSV_GENERAL_LIBRARY_FILE_PATH = "data/GeneralLibraryData.csv";

    public LibraryWindow() {
        setTitle("Library System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcomeLabel = new JLabel("Welcome to the Library", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 36));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton generalLibraryButton = new JButton("Explore General Library");
        generalLibraryButton.setFont(new Font("SansSerif", Font.PLAIN, 18));

        generalLibraryButton.addActionListener(e -> openGeneralLibraryWindow());

        buttonPanel.add(generalLibraryButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);


        add(mainPanel);
        setVisible(true);
    }

    private void openGeneralLibraryWindow(){
        GeneralLibraryWindow generalLibraryWindow = new GeneralLibraryWindow();
        generalLibraryWindow.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibraryWindow::new);
    }
}
