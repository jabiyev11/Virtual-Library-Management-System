package window;


import user.PasswordChecker;
import user.User;
import user.UserManager;
import user.UserRole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

public class SignupWindow extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField reEnterPasswordField;
    private JTextField nameField;
    private JTextField addressField;
    private JTextField phoneNumberField;
    private JTextField emailField;
    private JButton signupButton;
    private JCheckBox showPasswordCheckbox;
    private UserManager userManager;


    public SignupWindow() {
        this.userManager = new UserManager();
        setTitle("Sign Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 550); // Adjusted size to accommodate additional fields
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        JLabel createAccountLabel = new JLabel("Create Account");
        createAccountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        createAccountLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel reEnterPasswordLabel = new JLabel("Re-enter Password:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel addressLabel = new JLabel("Address:");
        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        JLabel emailLabel = new JLabel("Email:");

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        reEnterPasswordField = new JPasswordField(15);
        nameField = new JTextField(15);
        addressField = new JTextField(15);
        phoneNumberField = new JTextField(15);
        emailField = new JTextField(15);

        signupButton = new JButton("Sign Up");
        showPasswordCheckbox = new JCheckBox("Show Password");


        showPasswordCheckbox.addActionListener(e -> {
            if (showPasswordCheckbox.isSelected()) {
                passwordField.setEchoChar((char) 0);
                reEnterPasswordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('\u2022');
                reEnterPasswordField.setEchoChar('\u2022');
            }
        });


        JLabel haveAccountLabel = new JLabel("Already have an account? Log In");
        haveAccountLabel.setForeground(Color.BLUE);
        haveAccountLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        haveAccountLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dispose();
                SwingUtilities.invokeLater(() -> new LoginWindow().setVisible(true));
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(createAccountLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(reEnterPasswordLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(reEnterPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        panel.add(nameLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        panel.add(addressLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        panel.add(phoneNumberLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(phoneNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        panel.add(emailLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        panel.add(showPasswordCheckbox, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        signupButton.setPreferredSize(new Dimension(120, 35));
        buttonPanel.add(signupButton);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(haveAccountLabel, gbc);

        add(panel);
        setVisible(true);

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();
                char[] reEnteredPassword = reEnterPasswordField.getPassword();
                String name = nameField.getText();
                String address = addressField.getText();
                String phoneNumber = phoneNumberField.getText();
                String email = emailField.getText();

                if (username.isEmpty() || password.length == 0 || reEnteredPassword.length == 0 ||
                        name.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(SignupWindow.this, "Please fill all the fields");
                    return;
                }

                if (!Arrays.equals(password, reEnteredPassword)) {
                    JOptionPane.showMessageDialog(SignupWindow.this, "Passwords do not match");
                    passwordField.setText("");
                    reEnterPasswordField.setText("");
                    return;
                }

                List<String> passwordErrors = PasswordChecker.validateStrongPassword(username, new String (password));
                if(!passwordErrors.isEmpty()){
                    StringBuilder errorMessage = new StringBuilder();
                    for(String error : passwordErrors){
                        errorMessage.append(error).append("\n");
                    }
                    JOptionPane.showMessageDialog(SignupWindow.this, errorMessage.toString());
                    clearFields();
                    return;
                }

                if (userManager.isUsernameTaken(username)) {
                    JOptionPane.showMessageDialog(SignupWindow.this, "Username is already taken");
                } else {
                    User newUser = new User(username, new String(password), name, address, phoneNumber, email, UserRole.USER);
                    userManager.addUser(newUser);
                    JOptionPane.showMessageDialog(SignupWindow.this, "User registered successfully");
                    clearFields();
                    setVisible(false);
                    SwingUtilities.invokeLater(() -> new LoginWindow().setVisible(true));

                }
            }
        });
    }

    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        reEnterPasswordField.setText("");
        nameField.setText("");
        addressField.setText("");
        phoneNumberField.setText("");
        emailField.setText("");
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignupWindow::new);
    }
}
