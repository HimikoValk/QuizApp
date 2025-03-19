package com.himiko.gui.screen.screens;


import com.himiko.Main;
import com.himiko.game.Game;
import com.himiko.gui.GUI;
import com.himiko.gui.screen.Screen;
import com.himiko.gui.screen.ScreenHandler;
import com.himiko.network.protocol.request.Request;
import com.himiko.network.protocol.request.RequestType;

import javax.swing.*;
import java.awt.*;

/**
 * @author Valk on 16.03.2025
 * @project quizClient
 */
public class GameScreen extends Screen {
    private Game game;
    private JLabel gameInfoLabel;
    private JButton leaveButton;

    public GameScreen(Game game) {
        super("Game: " + game.getGameID());
        this.game = game;

        // Label mit Spielinformationen
        this.gameInfoLabel = new JLabel("Spiel: " + game.getGameID() + " | Spieler: " + game.getCurrentPlayers() + "/" + game.getMaxUserSize());
        this.gameInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        this.gameInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Leave-Button zum Verlassen des Spiels
        this.leaveButton = GUI.uiManager.createStyledButton("Leave");
        this.leaveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.leaveButton.addActionListener(e -> {
            Main.NETWORK.getPackageHandler().sendRequest(new Request<>(this.game.getGameID(), RequestType.GAME_LEAVE));
            ScreenHandler.INSTANCE.changeScreen(ScreenHandler.GAME_SELECTION_SCREEN);
        });

        // Layout setzen
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(gameInfoLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(leaveButton);

        super.setComponents(mainPanel);
    }

    @Override
    public void render(Graphics g) {

    }
}
