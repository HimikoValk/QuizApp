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
import com.himiko.network.protocol.response.Response;
import com.himiko.network.protocol.response.ResponseType;

import javax.swing.*;
import java.awt.*;

/**
 * @author Valk on 14.03.2025
 * @project quizClient
 */
public class GameSelectionScreen extends Screen {
    private JPanel gameRoomPanel;
    private JScrollPane scrollPane;
    private JLabel titelLabel;
    private JButton backButton;
    private Logger logger;

    public GameSelectionScreen() {
        super("Game Selection Screen");
        this.logger = Main.logger;

        this.titelLabel = GUI.uiManager.createStyledLabel("Game Selection Screen");
        this.titelLabel.setFont(new Font("Arial", Font.BOLD, 15));

        this.backButton = GUI.uiManager.createStyledButton("Back");
        this.backButton.addActionListener(a -> ScreenHandler.INSTANCE.changeScreen(ScreenHandler.ACTION_SELECTION_SCREEN));

        this.gameRoomPanel = new JPanel();
        this.gameRoomPanel.setLayout(new BoxLayout(this.gameRoomPanel, BoxLayout.Y_AXIS));
        this.gameRoomPanel.setBackground(GUI.uiManager.getCurrentTheme().backgroundColor.brighter());

        this.scrollPane = new JScrollPane(this.gameRoomPanel);
        this.scrollPane.setBackground(GUI.uiManager.getCurrentTheme().backgroundColor.brighter());
        this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        super.setComponents(this.titelLabel, this.scrollPane, this.backButton);
    }

    @Override
    public void onEnter() {
        this.WIDTH = Main.GUI.getWidth();
        this.HEIGHT = Main.GUI.getHeight();

        this.scrollPane.setBounds(WIDTH / 2 - 100, 80, 200, 300);
        this.gameRoomPanel.setBounds(WIDTH / 2 - 100, 80, 200, 300);
        this.titelLabel.setBounds(WIDTH / 2 - 100, 20, 200, 40);
        this.backButton.setBounds(WIDTH / 2 - 50, HEIGHT - 100, 100, 40);

        this.updateGameButtons();

        super.onEnter();
    }

    @Override
    public void render(Graphics g) {

    }

    private void updateGameButtons()
    {
        //TODO:FIX!!!!!!!!!!!!
        this.gameRoomPanel.removeAll();

        for(Game game : GameManager.getGames())
        {
            this.logger.debug("{}",game.getGameID());
            JButton gameJoinButton = GUI.uiManager.createStyledButton("Game:" + game.getCurrentPlayers() + "/" + game.getMaxUserSize());

            gameJoinButton.addActionListener(a ->
            {
                GameJoinData gameJoinData = new GameJoinData(game.getGameID(), null);
                Response<?> response = Main.NETWORK.getPackageHandler().sendRequestWithCallBack(new Request<>(gameJoinData, RequestType.GAME_JOIN));

                if(response.getResponseType() == ResponseType.SUCCESS) {
                    ScreenHandler.INSTANCE.changeScreen(new GameScreen(game));
                }else {
                    JOptionPane.showMessageDialog(null, "Failed to enter! Room might be full...", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            this.gameRoomPanel.add(gameJoinButton);
        }

        this.gameRoomPanel.revalidate();
        this.gameRoomPanel.repaint();
    }
}
