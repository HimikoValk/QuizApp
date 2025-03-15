package com.himiko.gui.screen.screens;


import com.himiko.Main;
import com.himiko.game.Game;
import com.himiko.game.manager.GameManager;
import com.himiko.gui.GUI;
import com.himiko.gui.manager.UIManager;
import com.himiko.gui.screen.Screen;
import com.himiko.gui.screen.ScreenHandler;
import com.himiko.logger.Logger;
import com.himiko.network.protocol.data.GameJoinData;
import com.himiko.network.protocol.request.Request;
import com.himiko.network.protocol.request.RequestType;

import javax.swing.*;
import java.awt.*;

/**
 * @author Valk on 14.03.2025
 * @project quizClient
 */
public class GameSelectionScreen extends Screen {
    private JPanel gameRoomPanel;
    private JLabel titelLabel;
    private JButton backButton;
    private Logger logger;

    public GameSelectionScreen() {
        super("Game Selection Screen");

        this.logger = Main.logger;
        this.titelLabel = GUI.uiManager.createStyledLabel(this.getName());
        this.titelLabel.setFont(new Font("Arial", Font.BOLD, 15));

        this.backButton = GUI.uiManager.createStyledButton("Back");
        this.backButton.addActionListener(a -> {
            ScreenHandler.INSTANCE.changeScreen(ScreenHandler.ACTION_SELECTION_SCREEN);
        });

        this.gameRoomPanel = new JPanel();
        this.gameRoomPanel.setLayout(new BoxLayout(this.gameRoomPanel, BoxLayout.Y_AXIS));
        this.gameRoomPanel.setBounds(50, 50, 200, 300);
        this.gameRoomPanel.setBackground(GUI.uiManager.getCurrentTheme().backgroundColor.brighter());

        super.setComponents(this.backButton,this.gameRoomPanel, this.titelLabel);
    }


    @Override
    public void onEnter() {
        this.titelLabel.setBounds(WIDTH / 2, HEIGHT, this.titelLabel.getWidth() + (2* this.titelLabel.getText().length()), this.titelLabel.getHeight());
        this.backButton.setBounds(WIDTH - 100, 400, this.backButton.getWidth() + 100, this.backButton.getHeight() + 50);
        this.updateGameButtons();
        super.onEnter();
    }

    @Override
    public void render(Graphics g) {

    }

    private void updateGameButtons()
    {
        this.gameRoomPanel.removeAll();

        int x = this.gameRoomPanel.getX() - 50;
        int y = this.gameRoomPanel.getY();

        for(Game game :GameManager.getGames())
        {
            this.logger.debug("{}",game.getGameID());
            JButton gameJoinButton = GUI.uiManager.createStyledButton("Game:" + game.getCurrentPlayers() + "/" + game.getMaxUserSize());
            gameJoinButton.setBounds(x, y, this.gameRoomPanel.getWidth(), 50);
            gameJoinButton.addActionListener(a -> {
                GameJoinData gameJoinData = new GameJoinData(game.getGameID(), 0);
                Main.NETWORK.getPackageHandler().sendRequest(new Request<>(gameJoinData, RequestType.GAME_JOIN));
            });

            this.gameRoomPanel.add(gameJoinButton);
            y += gameJoinButton.getHeight();
        }

        this.gameRoomPanel.revalidate();
        this.gameRoomPanel.repaint();
    }
}
