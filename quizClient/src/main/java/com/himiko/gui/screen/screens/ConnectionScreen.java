package com.himiko.gui.screen.screens;


import com.himiko.Main;
import com.himiko.gui.screen.Screen;

import javax.swing.*;
import java.awt.*;

/**
 * @author Valk on 02.03.2025
 * @project quizClient
 */
public class ConnectionScreen extends Screen {
    private int WIDTH;
    private int HEIGHT;

    private JTextField addressField;
    private JTextField portField;
    private JTextField usernameField;
    private JButton connectButton;
    private JButton enterButton;
    private JLabel titleLabel;

    public ConnectionScreen() {
        super("Connection Screen");

        UIManager.put("Button.font", new Font("Arial", Font.BOLD, 14));
        UIManager.put("TextField.font", new Font("Arial", Font.PLAIN, 14));
        UIManager.put("Label.font", new Font("Arial", Font.PLAIN, 14));

        this.titleLabel = new JLabel("Connect to Server");
        this.titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        this.titleLabel.setSize(200, 100);

        this.addressField = createStyledTextField("127.0.0.1");
        this.addressField.setSize(100, 30);

        this.portField = createStyledTextField("8080");
        this.portField.setSize(100, 30);

        this.usernameField = new JTextField("Username", 15);
        //this.usernameField.setSize(100, 20);
        this.usernameField.setEnabled(false);

        this.connectButton = createStyledButton("Connect", new Color(46, 204, 113));
        //this.connectButton.setSize(100, 20);

        this.enterButton = new JButton("Enter to QuizApp");
        this.enterButton.setSize(150, 30);
        this.enterButton.setEnabled(false);

        this.connectButton.addActionListener(e -> {
            try {
                Main.NETWORK.connect(this.addressField.getText(), Integer.parseInt(this.portField.getText()));
                if (Main.NETWORK.getConnection().isConnected()) {
                    this.usernameField.setEnabled(true);
                    this.enterButton.setEnabled(true);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid port number!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        this.enterButton.addActionListener(e -> {
            if (Main.NETWORK.getConnection().isConnected()) {

            }
        });

        /*
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(500, 800));
        panel.setLayout(new GridLayout(6, 1, 10, 10)); // 6 Reihen, 1 Spalte, 10px Abstand
        panel.add(titleLabel);
        panel.add(this.addressField);
        panel.add(this.portField);
        panel.add(this.usernameField);
        panel.add(this.connectButton);
        panel.add(this.enterButton);
*/
        super.setComponents(titleLabel,this.usernameField, this.addressField,this.portField,this.enterButton, this.connectButton);
        //super.setComponents(panel);
    }


    @Override
    public void render(Graphics g) {

    }

    @Override
    public void onEnter() {
        WIDTH = Main.GUI.getWidth();
        HEIGHT = Main.GUI.getHeight();
        this.titleLabel.setBounds(WIDTH / 2 - (this.titleLabel.getWidth() / 2), 0, this.titleLabel.getWidth(), this.titleLabel.getHeight());
        this.addressField.setBounds(WIDTH / 2 - (this.addressField.getWidth() / 2),
                HEIGHT / 2, this.addressField.getWidth(), this.addressField.getHeight());

        this.usernameField.setBounds(WIDTH / 2 - (this.usernameField.getWidth() / 2),
                this.addressField.getY() - this.usernameField.getHeight(), this.usernameField.getWidth(), this.usernameField.getHeight());

        this.portField.setBounds(WIDTH / 2 - (this.portField.getWidth() / 2),
                this.addressField.getY() + this.portField.getHeight(), this.portField.getWidth(), this.portField.getHeight());

        this.connectButton.setBounds(WIDTH / 2 - (this.connectButton.getWidth() / 2),
                this.portField.getY() + this.connectButton.getHeight(), this.connectButton.getWidth(), this.connectButton.getHeight());

        this.enterButton.setBounds(WIDTH / 2 - (this.enterButton.getWidth() / 2),
                this.connectButton.getY() + this.enterButton.getHeight(), this.enterButton.getWidth(), this.enterButton.getHeight());

       super.onEnter();
    }

    private JTextField createStyledTextField(String text) {
        JTextField field = new JTextField(text, 20);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setBackground(Color.WHITE);
        field.setPreferredSize(new Dimension(250, 40));
        return field;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover-Effekt
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }
}
