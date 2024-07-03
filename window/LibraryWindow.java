package window;

import library.Book;
import library.GeneralLibrary;
import user.User;
import user.UserRole;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LibraryWindow extends JFrame implements WindowCompatible{

    private static final String CSV_GENERAL_LIBRARY_FILE_PATH = "data/GeneralLibraryData.csv";
    private static User currentUser;

    public LibraryWindow(User currentUser) {
        this.currentUser = currentUser;
        setupUI();
    }

    @Override
    public void setupUI() {
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

        if(currentUser.getRole() == UserRole.USER) {
            JButton personalLibraryButton = new JButton("Access Personal Library");

            personalLibraryButton.setFont(new Font("SansSerif", Font.PLAIN, 18));
            personalLibraryButton.addActionListener(e -> openPersonalLibraryWindow());

            buttonPanel.add(personalLibraryButton);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        }
        add(mainPanel);
        setVisible(true);
    }

    private void openGeneralLibraryWindow(){
        GeneralLibraryWindow generalLibraryWindow = new GeneralLibraryWindow(currentUser);
        generalLibraryWindow.setVisible(true);
    }

    private void openPersonalLibraryWindow(){
        PersonalLibraryWindow personalLibraryWindow = new PersonalLibraryWindow(currentUser);
        personalLibraryWindow.setVisible(true);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibraryWindow(currentUser));
    }
}
