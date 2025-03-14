package com.himiko.gui.screen.screens;


import com.himiko.Main;
import com.himiko.game.utils.UserData;
import com.himiko.gui.GUI;
import com.himiko.gui.screen.Screen;
import com.himiko.gui.screen.ScreenHandler;
import com.himiko.logger.Logger;
import com.himiko.network.protocol.PackageCategory;
import com.himiko.network.protocol.request.Request;
import com.himiko.network.protocol.request.RequestType;

import javax.swing.*;
import java.awt.*;

/**
 * @author Valk on 02.03.2025
 * @project quizClient
 */
public class ConnectionScreen extends Screen {
    private Logger logger;

    private JTextField addressField;
    private JTextField portField;
    private JTextField usernameField;
    private JButton connectButton;
    private JButton enterButton;
    private JLabel titleLabel;
    private JLabel serverAddressLabel;
    private JLabel portLabel;

    public ConnectionScreen() {
        super("Connection Screen");

        this.logger = Main.logger;

        UIManager.put("Button.font", new Font("Arial", Font.BOLD, 14));
        UIManager.put("TextField.font", new Font("Arial", Font.PLAIN, 14));
        UIManager.put("Label.font", new Font("Arial", Font.PLAIN, 14));

        this.titleLabel = GUI.uiManager.createStyledLabel("Connection Screen");
        this.titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        this.titleLabel.setSize(200, 100);

        this.serverAddressLabel = GUI.uiManager.createStyledLabel("Server-IP:");

        this.portLabel = GUI.uiManager.createStyledLabel("Port:");

        this.addressField = GUI.uiManager.createStyledTextField("127.0.0.1");
        this.addressField.setSize(100, 30);

        this.portField = GUI.uiManager.createStyledTextField("8080");
        this.portField.setSize(100, 30);

        this.usernameField = GUI.uiManager.createStyledTextField("Username");
        this.usernameField.setSize(100, 30);
        this.usernameField.setEnabled(false);

        this.connectButton = GUI.uiManager.createStyledButton("Connect");
        this.connectButton.setSize(150, 30);

        this.enterButton = GUI.uiManager.createStyledButton("Enter");
        this.enterButton.setSize(150, 30);
        this.enterButton.setEnabled(false);

        this.connectButton.addActionListener(e -> {
            try {
                Main.NETWORK.connect(this.addressField.getText(), Integer.parseInt(this.portField.getText()));
                if (Main.NETWORK.getConnection().isConnected()) {
                    this.usernameField.setEnabled(true);
                    this.enterButton.setEnabled(true);

                    Main.NETWORK.start();
                    JOptionPane.showMessageDialog(null, "Connected to server!", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid port number!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        this.enterButton.addActionListener(e -> {
            if (Main.NETWORK.getConnection().isConnected()) {
                //Request login/access (ID will be generated on server side)
                Main.NETWORK.getPackageHandler().sendRequest(new Request<UserData>(new UserData(this.usernameField.getText(), 0L), RequestType.USER_LOGIN));
                try {
                    Thread.sleep(200);
                    if(Main.NETWORK.hasAccess()) {
                        //TODO:IMPLEMENT GAME SCREEN USW.
                        this.logger.debug("Has access..");
                        ScreenHandler.INSTANCE.changeScreen(ScreenHandler.ACTION_SELECTION_SCREEN);
                    }
                }catch (Exception ex) {
                    this.logger.error("Something went wrong...");
                }
            }
        });


        super.setComponents(this.titleLabel,this.serverAddressLabel, this.portLabel,this.usernameField, this.addressField,this.portField,this.enterButton, this.connectButton);
    }


    @Override
    public void render(Graphics g) {
        this.connectButton.repaint();
        this.enterButton.repaint();
    }

    @Override
    public void onEnter() {
        WIDTH = Main.GUI.getWidth();
        HEIGHT = Main.GUI.getHeight();

        this.titleLabel.setBounds(WIDTH / 2 - (this.titleLabel.getWidth() / 2), 0, this.titleLabel.getWidth(), this.titleLabel.getHeight());

        this.addressField.setBounds(WIDTH / 2 - (this.addressField.getWidth() / 2),
                HEIGHT / 2, this.addressField.getWidth(), this.addressField.getHeight());
        this.serverAddressLabel.setBounds(this.addressField.getX() - this.serverAddressLabel.getWidth(), this.addressField.getY() - (this.serverAddressLabel.getHeight() / 2 - 10), this.serverAddressLabel.getWidth(), this.serverAddressLabel.getHeight());

        this.usernameField.setBounds(WIDTH / 2 - (this.usernameField.getWidth() / 2),
                this.addressField.getY() - this.usernameField.getHeight(), this.usernameField.getWidth(), this.usernameField.getHeight());

        this.portField.setBounds(WIDTH / 2 - (this.portField.getWidth() / 2),
                this.addressField.getY() + this.portField.getHeight(), this.portField.getWidth(), this.portField.getHeight());
        this.portLabel.setBounds(this.portField.getX() - this.portLabel.getWidth(), this.portField.getY(), this.portLabel.getWidth(), this.portLabel.getHeight());

        this.connectButton.setBounds(WIDTH / 2 - (this.connectButton.getWidth() / 2),
                this.portField.getY() + this.connectButton.getHeight() + 10, this.connectButton.getWidth(), this.connectButton.getHeight());

        this.enterButton.setBounds(WIDTH / 2 - (this.enterButton.getWidth() / 2),
                this.connectButton.getY() + this.enterButton.getHeight(), this.enterButton.getWidth(), this.enterButton.getHeight());

        super.onEnter();
    }
}
