package com.himiko.gui.screen.screens;


import com.himiko.Main;
import com.himiko.game.Game;
import com.himiko.gui.GUI;
import com.himiko.gui.screen.Screen;
import com.himiko.gui.screen.ScreenHandler;
import com.himiko.logger.Logger;
import com.himiko.network.protocol.data.GameInfo;
import com.himiko.network.protocol.request.Request;
import com.himiko.network.protocol.request.RequestType;
import com.himiko.network.protocol.response.Response;

import javax.swing.*;
import java.awt.*;

/**
 * @author Valk on 16.03.2025
 * @project quizClient
 */
public class GameScreen extends Screen {
    private Logger logger;
    private Game game;
    private JLabel gameInfoLabel;
    private JLabel gameStateLabel;
    private JButton leaveButton;
    private JPanel mainPanel;

    public GameScreen(Game game) {
        super("Game: " + game.getGameID());
        this.logger = Main.logger;

        this.game = game;

        this.gameInfoLabel = GUI.uiManager.createStyledLabel("Game: " + game.getGameID() + " | Players: " + game.getCurrentPlayers() + "/" + game.getMaxUserSize());
        this.gameInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));

        this.gameStateLabel = GUI.uiManager.createStyledLabel("" + game.getGameState());
        this.gameStateLabel.setFont(new Font("Arial", Font.BOLD, 15));

        this.leaveButton = GUI.uiManager.createStyledButton("Leave");
        this.leaveButton.addActionListener(e -> {
            Main.NETWORK.getPackageHandler().sendRequest(new Request<>(this.game.getGameID(), RequestType.GAME_LEAVE));
            ScreenHandler.INSTANCE.changeScreen(ScreenHandler.GAME_SELECTION_SCREEN);
        });

        this.mainPanel = new JPanel();
        this.mainPanel.setBackground(GUI.uiManager.getCurrentTheme().backgroundColor);
        this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));
        this.mainPanel.add(this.gameInfoLabel);
        this.mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        this.mainPanel.add(this.leaveButton);

        super.setComponents(mainPanel);
    }

    @Override
    public void onEnter() {
        this.WIDTH = Main.GUI.getWidth();
        this.HEIGHT = Main.GUI.getHeight();

        this.mainPanel.setBounds(0, 0, this.WIDTH, this.HEIGHT);
        this.leaveButton.setVisible(true);
        super.onEnter();
    }

    @Override
    public void render(Graphics g) {
        this.gameInfoLabel.setText("Game: " + this.game.getGameID() + " | Players: "
                + this.game.getCurrentPlayers() + "/" + this.game.getMaxUserSize());

        //
        Response<?> response = Main.NETWORK.getPackageHandler().sendRequestWithCallBack(new Request<>(game.getGameID(), RequestType.GET_GAME_INFO));
        GameInfo gameInfo = Main.NETWORK.getPackageHandler().parseDataToClass(response.getData().toString(), GameInfo.class);
        this.logger.debug("Game info:{}", gameInfo.getGameState());
        this.game.setGameState(gameInfo.getGameState());
        this.game.setCurrentPlayers(gameInfo.getCurrentPlayers());
    }
}
