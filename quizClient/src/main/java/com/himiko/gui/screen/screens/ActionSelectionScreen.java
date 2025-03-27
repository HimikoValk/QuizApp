package com.himiko.gui.screen.screens;

import com.himiko.Main;
import com.himiko.game.manager.GameManager;
import com.himiko.gui.GUI;
import com.himiko.gui.screen.Screen;
import com.himiko.gui.screen.ScreenHandler;
import com.himiko.logger.Logger;
import com.himiko.network.protocol.request.Request;
import com.himiko.network.protocol.request.RequestType;

import javax.swing.*;
import java.awt.*;

public class ActionSelectionScreen extends Screen {
    private Logger logger;

    private JButton joinPublicGameButton;
    private JButton searchGameButton;
    private JButton createGameButton;
    private JButton profileButton;
    private JButton refreshButton;
    private JButton logoutButton;
    private JLabel titleLabel;
    private JLabel onlinePlayersLabel;

    public ActionSelectionScreen() {
        super("Action Selection Screen");

        this.logger = Main.logger;

        this.titleLabel = GUI.uiManager.createStyledLabel("Action Selection");
        this.titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        this.titleLabel.setSize(200, 100);

        this.onlinePlayersLabel = GUI.uiManager.createStyledLabel("Online Players: " + GameManager.currentPlayerCount);

        this.joinPublicGameButton = GUI.uiManager.createStyledButton("Join Public Game");
        this.searchGameButton = GUI.uiManager.createStyledButton("Find Game");
        this.createGameButton = GUI.uiManager.createStyledButton("Create Game");
        this.profileButton = GUI.uiManager.createStyledButton("Profile");
        this.refreshButton = GUI.uiManager.createStyledButton("Refresh");
        this.logoutButton = GUI.uiManager.createStyledButton("Logout");

        this.joinPublicGameButton.addActionListener(e -> {
            this.logger.debug("Joining public game...");
            Main.NETWORK.getPackageHandler().sendRequest(new Request<>(null, RequestType.GET_GAMES));
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            ScreenHandler.INSTANCE.changeScreen(ScreenHandler.GAME_SELECTION_SCREEN);
        });

        this.searchGameButton.addActionListener(e -> {
            this.logger.debug("Searching for a game...");
        });

        this.createGameButton.addActionListener(e -> {
            this.logger.debug("Creating a new game...");
            ScreenHandler.INSTANCE.changeScreen(new CreateGameScreen());
        });

        this.profileButton.addActionListener(e -> {
            this.logger.debug("Opening profile...");
            // TODO: Implement profile screen logic
        });

        this.refreshButton.addActionListener(e ->{
            this.updatePlayerCount();
        });

        this.logoutButton.addActionListener(e ->{
            Main.NETWORK.getPackageHandler().sendRequest(new Request<>(RequestType.USER_LOGOUT));
            ScreenHandler.INSTANCE.changeScreen(ScreenHandler.CONNECTION_SCREEN);
        });

        super.setComponents(this.titleLabel, this.onlinePlayersLabel, this.joinPublicGameButton,this.searchGameButton, this.createGameButton, this.profileButton, this.refreshButton,this.logoutButton);
    }

    @Override
    public void render(Graphics g) {
        this.updatePlayerCount();
        this.searchGameButton.repaint();
        this.createGameButton.repaint();
        this.profileButton.repaint();
    }

    @Override
    public void onEnter() {
        this.WIDTH = Main.GUI.getWidth();
        this.HEIGHT = Main.GUI.getHeight();


        this.titleLabel.setBounds(WIDTH / 2 - 70, 20, 200, 40);
        this.onlinePlayersLabel.setBounds(WIDTH / 2 - 75, 70, 150, 30);

        this.joinPublicGameButton.setBounds(WIDTH / 2 - 100, 100, 200, 40);
        this.searchGameButton.setBounds(WIDTH / 2 - 100, 150, 200, 40);
        this.createGameButton.setBounds(WIDTH / 2 - 100, 200, 200, 40);
        this.profileButton.setBounds(WIDTH / 2 - 100, 250, 200, 40);
        this.refreshButton.setBounds(0,400 , 200, 40);
        this.logoutButton.setBounds(WIDTH - 200,400 , 200, 40);


        super.onEnter();
    }

    private void updatePlayerCount() {
        try {
            Main.NETWORK.getPackageHandler().sendRequest(new Request<>(RequestType.SERVER_INFORMATION));
            this.onlinePlayersLabel.setText("Online Players: " + GameManager.currentPlayerCount);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
