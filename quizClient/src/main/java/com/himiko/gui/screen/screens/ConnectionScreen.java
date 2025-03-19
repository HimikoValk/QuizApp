package com.himiko.gui.screen.screens;


import com.himiko.Main;
import com.himiko.game.utils.UserData;
import com.himiko.gui.GUI;
import com.himiko.gui.screen.Screen;
import com.himiko.gui.screen.ScreenHandler;
import com.himiko.logger.Logger;
import com.himiko.network.protocol.request.Request;
import com.himiko.network.protocol.request.RequestType;
import com.himiko.network.protocol.response.Response;
import com.himiko.network.protocol.response.ResponseType;

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

        this.serverAddressLabel = GUI.uiManager.createStyledLabel("Server-IP:");

        this.portLabel = GUI.uiManager.createStyledLabel("Port:");

        this.addressField = GUI.uiManager.createStyledTextField("127.0.0.1");

        this.portField = GUI.uiManager.createStyledTextField("8080");

        this.usernameField = GUI.uiManager.createStyledTextField("Username");
        this.usernameField.setEnabled(false);

        this.connectButton = GUI.uiManager.createStyledButton("Connect");

        this.enterButton = GUI.uiManager.createStyledButton("Enter");
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
                Response<?> response = Main.NETWORK.getPackageHandler().sendRequestWithCallBack(new Request<UserData>(new UserData(this.usernameField.getText(), 0L), RequestType.USER_LOGIN));
                if(response.getResponseType() == ResponseType.SUCCESS)
                {
                    this.logger.info("Successfully logged in");
                    ScreenHandler.INSTANCE.changeScreen(ScreenHandler.ACTION_SELECTION_SCREEN);
                }else {
                    JOptionPane.showMessageDialog(null, "Failed to enter!", "Error", JOptionPane.ERROR_MESSAGE);
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
        this.WIDTH = Main.GUI.getWidth();
        this.HEIGHT = Main.GUI.getHeight();

        int titleWidth = 300, titleHeight = 50;
        int fieldWidth = 200, fieldHeight = 30;
        int labelWidth = 100, labelHeight = 30;
        int buttonWidth = 150, buttonHeight = 30;
        int verticalGap = 10;

        this.titleLabel.setBounds((this.WIDTH - titleWidth) / 2, 20, titleWidth, titleHeight);
        this.usernameField.setBounds((this.WIDTH - fieldWidth) / 2, this.titleLabel.getY() + titleHeight + verticalGap, fieldWidth, fieldHeight);
        this.addressField.setBounds((this.WIDTH - fieldWidth) / 2, this.usernameField.getY() + fieldHeight + verticalGap, fieldWidth, fieldHeight);
        this.serverAddressLabel.setBounds(this.addressField.getX() - labelWidth - 10, this.addressField.getY(), labelWidth, labelHeight);
        this.portField.setBounds((this.WIDTH - fieldWidth) / 2, this.addressField.getY() + fieldHeight + verticalGap, fieldWidth, fieldHeight);
        this.portLabel.setBounds(this.portField.getX() - labelWidth - 10, this.portField.getY(), labelWidth, labelHeight);
        this.connectButton.setBounds((WIDTH - buttonWidth) / 2, portField.getY() + fieldHeight + verticalGap, buttonWidth, buttonHeight);
        this.enterButton.setBounds((WIDTH - buttonWidth) / 2, connectButton.getY() + buttonHeight + verticalGap, buttonWidth, buttonHeight);

        super.onEnter();
    }
}
