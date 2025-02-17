package window;


import user.PasswordChecker;
import user.User;
import user.UserManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

public class LoginWindow extends JFrame implements WindowCompatible{

    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserManager userManager;


    public LoginWindow() {
        userManager = new UserManager();
        setupUI();
    }

    @Override
    public void setupUI() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Log In");
        JCheckBox showPasswordCheckbox = new JCheckBox("Show Password");

        showPasswordCheckbox.addActionListener(e -> {
            if (showPasswordCheckbox.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('\u2022');
            }
        });

        JLabel signUpLabel = new JLabel("Want to create new account? Sign Up");
        signUpLabel.setForeground(Color.BLUE);
        signUpLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signUpLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                dispose();
                SwingUtilities.invokeLater(() -> new SignupWindow().setVisible(true));
            }
        });


        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(showPasswordCheckbox, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        loginButton.setPreferredSize(new Dimension(120, 35));
        loginButton.addActionListener(e -> loginButtonLogic());
        buttonPanel.add(loginButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(signUpLabel, gbc);

        add(panel);
        setVisible(true);
    }

    private void loginButtonLogic() {
        String username = usernameField.getText();
        char[] password = passwordField.getPassword();


        if (username.isEmpty() || password.length == 0) {
            JOptionPane.showMessageDialog(LoginWindow.this, "Please enter both username and password");
            return;
        }


        if (userManager.validateUser(username, password)) {

            char[] expectedLibrarianPassword = "Lib@1234".toCharArray();

            if(username.equals("librarian") && Arrays.equals(password, expectedLibrarianPassword)){
                JOptionPane.showMessageDialog(LoginWindow.this, "Librarian logged in successfully");
            }

            else{
                JOptionPane.showMessageDialog(LoginWindow.this, "Successful log in. Welcome " + username);

            }

            setVisible(false);
            User currentUser = UserManager.getUserByUsername(username);
            SwingUtilities.invokeLater(() -> new LibraryWindow(currentUser).setVisible(true));

        } else {
            JOptionPane.showMessageDialog(LoginWindow.this, "Invalid Credentials");
            usernameField.setText("");
            passwordField.setText("");

        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginWindow::new);
    }
}