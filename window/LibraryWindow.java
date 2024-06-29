package window;

import javax.swing.*;
import java.awt.*;

public class LibraryWindow extends JFrame {

    public LibraryWindow() {
        setTitle("Library System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to Library", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));

        add(welcomeLabel, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibraryWindow::new);
    }
}
