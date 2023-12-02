package Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Registration extends JFrame implements ActionListener {
    private static final String CREDENTIALS_FILE = "database.csv";
    private JTextField userTextField;
    private JPasswordField passwordField;
    private JTextField phoneNumberTextField;
    private JButton registerButton;
    private JButton loginButton;
    private JLabel messageLabel;

    public static final int WIDTH = 800, HEIGHT = 790;

    public Registration() {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setBounds(300, 90, 900, 600);

        JLabel userLabel = new JLabel("Username:", SwingConstants.CENTER);
        userLabel.setForeground(Color.decode("#F0ECE5"));
        userTextField = new JTextField(20);
        JPanel userPanel = new JPanel(new GridBagLayout());
        userPanel.add(userLabel);
        userPanel.add(userTextField);
        userPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, userPanel.getPreferredSize().height+70));
        add(userPanel);

        JLabel passwordLabel = new JLabel("Password:", SwingConstants.CENTER);
        passwordLabel.setForeground(Color.decode("#F0ECE5"));
        passwordField = new JPasswordField(20);
        JPanel passwordPanel = new JPanel(new GridBagLayout());
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        passwordPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, passwordPanel.getPreferredSize().height+70));
        add(passwordPanel);

        JLabel phoneNumberLabel = new JLabel("Phone Number:", SwingConstants.CENTER);
        phoneNumberLabel.setForeground(Color.decode("#F0ECE5"));
        phoneNumberTextField = new JTextField(20);
        JPanel phoneNumberPanel = new JPanel(new GridBagLayout());
        phoneNumberPanel.add(phoneNumberLabel);
        phoneNumberPanel.add(phoneNumberTextField);
        phoneNumberPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, phoneNumberPanel.getPreferredSize().height+70));
        add(phoneNumberPanel);

        registerButton = new JButton("Register");
        registerButton.addActionListener(this);
        JPanel registerPanel = new JPanel(new GridBagLayout());
        registerPanel.add(registerButton);
        registerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, registerPanel.getPreferredSize().height+70));
        add(registerPanel);

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.add(loginButton);
        loginPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, loginPanel.getPreferredSize().height+70));
        add(loginPanel);

        messageLabel = new JLabel();
        messageLabel.setForeground(Color.RED);
        JPanel messagePanel = new JPanel(new GridBagLayout());
        messagePanel.add(messageLabel);
        messagePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, messagePanel.getPreferredSize().height+70));
        add(messagePanel);

        Color backgroundColor = Color.decode("#161A30"); // Set the background color to light blue
        registerPanel.setBackground(backgroundColor);
        loginPanel.setBackground(backgroundColor);
        messagePanel.setBackground(backgroundColor);
        userPanel.setBackground(backgroundColor);
        passwordPanel.setBackground(backgroundColor);
        phoneNumberPanel.setBackground(backgroundColor);
        getContentPane().setBackground(Color.decode("#161A30"));

        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            String username = userTextField.getText();
            String password = new String(passwordField.getPassword());
            String phoneNumber = phoneNumberTextField.getText();
    
            if (username.isEmpty() || password.isEmpty() || phoneNumber.isEmpty()) {
                messageLabel.setText("All fields must be filled");
                return;
            }
    
            if (usernameExists(username)) {
                messageLabel.setText("Username already exists");
            } else {
                registerUser(username, password, phoneNumber);
                messageLabel.setText("Registration successful");
            }
        } else if (e.getSource() == loginButton) {
            new LoginPage();
            this.dispose();
        }
    }

    private boolean usernameExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIALS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void registerUser(String username, String password, String phoneNumber) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CREDENTIALS_FILE, true))) {
            writer.write(username + "," + password + "," + phoneNumber + "," + 0);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        new Registration();
    }
}