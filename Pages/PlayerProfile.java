package Pages;

import javax.swing.*;

import Pages.Game.WireBuzz;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class PlayerProfile extends JFrame implements ActionListener {
    private static final String CREDENTIALS_FILE = "database.csv";
    private JLabel usernameLabel;
    private JLabel phoneNumberLabel;
    private JLabel highScoreLabel;
    private JButton startGameButton;
    private JButton settingsButton;
    private int level=1;
    private JLabel levelLabel;
    private String username;
    public static final int WIDTH = 800, HEIGHT = 790;

    public PlayerProfile(String username) {
        this.username = username;
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setBounds(300, 90, 900, 600);

        String[] userDetails = fetchUserDetails(username);

        JLabel welcomeLabel = new JLabel("Logged In Successfully!");
        welcomeLabel.setForeground(Color.decode("#F0ECE5"));
        JPanel welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.add(welcomeLabel);
        welcomePanel.setMaximumSize(new Dimension(welcomePanel.getMaximumSize().width, welcomeLabel.getPreferredSize().height+70));
        add(welcomePanel);

        usernameLabel = new JLabel("Username: " + userDetails[0], SwingConstants.CENTER);
        usernameLabel.setForeground(Color.decode("#F0ECE5"));
        JPanel usernamePanel = new JPanel(new GridBagLayout());
        usernamePanel.add(usernameLabel);
        usernamePanel.setMaximumSize(new Dimension(usernamePanel.getMaximumSize().width, usernameLabel.getPreferredSize().height+70));
        add(usernamePanel);

        phoneNumberLabel = new JLabel("Phone Number: " + userDetails[2], SwingConstants.CENTER);
        phoneNumberLabel.setForeground(Color.decode("#F0ECE5"));
        JPanel phoneNumberPanel = new JPanel(new GridBagLayout());
        phoneNumberPanel.add(phoneNumberLabel);
        phoneNumberPanel.setMaximumSize(new Dimension(phoneNumberPanel.getMaximumSize().width, phoneNumberLabel.getPreferredSize().height+70));
        add(phoneNumberPanel);

        highScoreLabel = new JLabel("High Score: " + userDetails[3], SwingConstants.CENTER);
        highScoreLabel.setForeground(Color.decode("#F0ECE5"));
        JPanel highScorePanel = new JPanel(new GridBagLayout());
        highScorePanel.add(highScoreLabel);
        highScorePanel.setMaximumSize(new Dimension(highScorePanel.getMaximumSize().width, highScoreLabel.getPreferredSize().height+70));
        add(highScorePanel);

        levelLabel = new JLabel("Level: " + level, SwingConstants.CENTER);
        levelLabel.setForeground(Color.decode("#F0ECE5"));
        JPanel levelPanel = new JPanel(new GridBagLayout());
        levelPanel.add(levelLabel);
        levelPanel.setMaximumSize(new Dimension(levelPanel.getMaximumSize().width, levelLabel.getPreferredSize().height+70));
        add(levelPanel);

        startGameButton = new JButton("Start Game");
        startGameButton.addActionListener(this);
        JPanel startGamePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcStart = new GridBagConstraints();
        gbcStart.insets = new Insets(10, 0, 10, 0); // top padding 10, no left padding, bottom padding 10, no right padding
        startGamePanel.add(startGameButton, gbcStart);
        startGamePanel.setMaximumSize(new Dimension(startGamePanel.getMaximumSize().width, startGameButton.getPreferredSize().height+70));
        add(startGamePanel);

        settingsButton = new JButton("Settings");
        settingsButton.addActionListener(this);
        JPanel settingsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcSettings = new GridBagConstraints();
        gbcSettings.insets = new Insets(10, 0, 10, 0); // top padding 10, no left padding, bottom padding 10, no right padding
        settingsPanel.add(settingsButton, gbcSettings);
        settingsPanel.setMaximumSize(new Dimension(settingsPanel.getMaximumSize().width, settingsButton.getPreferredSize().height+70));
        add(settingsPanel);

        Color backgroundColor = Color.decode("#161A30"); // Set the background color to light blue

        getContentPane().setBackground(backgroundColor);

        welcomePanel.setBackground(backgroundColor);
        usernamePanel.setBackground(backgroundColor);
        phoneNumberPanel.setBackground(backgroundColor);
        highScorePanel.setBackground(backgroundColor);
        levelPanel.setBackground(backgroundColor);
        startGamePanel.setBackground(backgroundColor);
        settingsPanel.setBackground(backgroundColor);

        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startGameButton) {
            // handle start game
            new WireBuzz(true, level, username);
            this.dispose();
        } else if (e.getSource() == settingsButton) {
            // handle settings
            // open new window to change level
            String[] options = {"Level 3", "Level 2", "Level 1"};
            String[] extra = {"Level 1", "Level 2", "Level 3"};
            int response = JOptionPane.showOptionDialog(null, "Choose a level", "Settings", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, extra[level-1]);
            if (response == 0) {
                level = 3;
                // make button blue
            } else if (response == 1) {
                level = 2;
            } else if (response == 2) {
                level = 1;
            }
            levelLabel.setText("Level: " + level);
        }
    }

    private String[] fetchUserDetails(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIALS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    return parts;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static void main(String[] args) {
        new PlayerProfile("root");
    }
}