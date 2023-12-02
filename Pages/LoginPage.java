package Pages;
// LoginPage.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginPage extends JFrame implements ActionListener {
    private Container container;
    private JLabel userLabel;
    private JTextField userTextField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel messageLabel;

    public static final int WIDTH = 800, HEIGHT = 790;

    private static final String CREDENTIALS_FILE = "database.csv";

    public LoginPage() {
        setTitle("Login Form");
        // setSize(WIDTH, HEIGHT); // Set the width to 900 and the height to 600
        setBounds(300, 90, 900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        container = getContentPane();
        container.setLayout(null);
        container.setBackground(Color.decode("#161A30"));

        userLabel = new JLabel("Username");
        userLabel.setForeground(Color.decode("#F0ECE5"));
        userLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        userLabel.setSize(100, 20);
        userLabel.setLocation(WIDTH/2-userLabel.getWidth()-20, 350);
        container.add(userLabel);

        userTextField = new JTextField();
        userTextField.setFont(new Font("Arial", Font.PLAIN, 15));
        userTextField.setSize(190, 20);
        userTextField.setLocation(WIDTH/2-userLabel.getWidth()+80,350);
        container.add(userTextField);

        passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(Color.decode("#F0ECE5"));
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        passwordLabel.setSize(100, 20);
        passwordLabel.setLocation(WIDTH/2 - passwordLabel.getWidth()-20,400);
        container.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 15));
        passwordField.setSize(190, 20);
        passwordField.setLocation(WIDTH/2 - passwordLabel.getWidth()+80, 400);
        container.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 15));
        loginButton.setSize(100, 20);
        loginButton.setLocation(WIDTH/2 - loginButton.getWidth()-20, 450);
        loginButton.addActionListener(this);
        container.add(loginButton);

        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.PLAIN, 15));
        registerButton.setSize(100, 20);
        registerButton.setLocation(WIDTH/2 + 80, 450);
        registerButton.addActionListener(this);
        container.add(registerButton);

        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.red);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        messageLabel.setSize(500, 25);
        messageLabel.setLocation(WIDTH/2-110, 500);
        container.add(messageLabel);

        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = userTextField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("All fields must be filled");
                return;
            }

            if (checkCredentials(username, password)) {
                // *****************************************************
                messageLabel.setText("Login successful");
                new PlayerProfile(username);
                dispose();
                // *****************************************************
            } else {
                messageLabel.setText("Invalid username or password");
            }
        } else if (e.getSource() == registerButton) {
            // *****************************************************
            new Registration();
            dispose();
            // *****************************************************
        }
    }
    private boolean checkCredentials(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIALS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}