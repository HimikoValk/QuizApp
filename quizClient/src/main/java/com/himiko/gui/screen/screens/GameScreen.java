package com.himiko.gui.screen.screens;


import com.himiko.Main;
import com.himiko.game.Game;
import com.himiko.game.GameState;
import com.himiko.gui.GUI;
import com.himiko.gui.screen.Screen;
import com.himiko.gui.screen.ScreenHandler;
import com.himiko.logger.Logger;
import com.himiko.network.protocol.data.GameInfo;
import com.himiko.network.protocol.request.Request;
import com.himiko.network.protocol.request.RequestType;
import com.himiko.network.protocol.response.Response;
import com.himiko.network.protocol.response.ResponseType;

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
    private JButton startButton;
    private JList<String> userList;
    private DefaultListModel<String> userListModel;
    private JPanel mainPanel;
    private JPanel questionPanel;
    private JPanel finishPanel;

    public GameScreen(Game game) {
        super("Game: " + game.getGameID());
        this.logger = Main.logger;

        this.game = game;

        this.mainPanel = GUI.uiManager.createStyledPanel(null);
        this.questionPanel = GUI.uiManager.createStyledPanel(null);

        this.gameStateLabel = GUI.uiManager.createStyledLabel("" + game.getGameState());
        this.gameStateLabel.setFont(new Font("Arial", Font.BOLD, 18));

        this.gameInfoLabel = GUI.uiManager.createStyledLabel("Game: " + game.getGameID() + " | Players: " + game.getCurrentPlayers() + "/" + game.getMaxUserSize());
        this.gameInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));

        this.startButton = GUI.uiManager.createStyledButton("Start");
        this.startButton.addActionListener(a -> {
            Response<?> response = Main.NETWORK.getPackageHandler().sendRequestWithCallBack(new Request<>(this.game.getGameID(), RequestType.GAME_START));

            if(response.getResponseType() != ResponseType.SUCCESS) {
                JOptionPane.showMessageDialog(null, response.getData() != null ? "Something went wrong...\nMessage:" + response.getData().toString() : "Something went wrong...");
            }else{
                this.logger.info("Successfully started game!");
            }
        });

        this.leaveButton = GUI.uiManager.createStyledButton("Leave");
        this.leaveButton.addActionListener(e -> {
            Main.NETWORK.getPackageHandler().sendRequest(new Request<>(this.game.getGameID(), RequestType.GAME_LEAVE));
            ScreenHandler.INSTANCE.changeScreen(ScreenHandler.GAME_SELECTION_SCREEN);
        });


        this.userListModel = new DefaultListModel<>();
        this.userList = new JList<>(userListModel);
        this.userList.setFont(new Font("Arial", Font.PLAIN, 12));
        this.userList.setVisibleRowCount(5);
        JScrollPane userScrollPane = new JScrollPane(userList);

        this.mainPanel.add(gameInfoLabel);
        this.mainPanel.add(gameStateLabel);
        this.mainPanel.add(startButton);
        this.mainPanel.add(leaveButton);
        this.mainPanel.add(userScrollPane);

        //TODO:Implement function to Visualize the Questions, User list, User creator, Points,
        //TODO:Implement dynamic gui
        super.setComponents(
                mainPanel,
                questionPanel);
    }

    @Override
    public void onEnter() {
        this.WIDTH = Main.GUI.getWidth();
        this.HEIGHT = Main.GUI.getHeight();

        this.mainPanel.setBounds(0, 0, WIDTH, HEIGHT);
        this.questionPanel.setBounds(0, 0, WIDTH, HEIGHT);
        this.gameInfoLabel.setBounds(WIDTH / 2 - 100, 40,150, 30);
        this.gameStateLabel.setBounds(WIDTH / 2 - 100, 20,200, 40);
        this.startButton.setBounds(WIDTH / 2 - 50, HEIGHT / 2 - 40,100, 40);
        this.leaveButton.setBounds(WIDTH / 2 - 50, HEIGHT / 2,100, 40);
        userList.getParent().setBounds(WIDTH - 100, this.gameInfoLabel.getY() + 50, 100, 50);
        super.onEnter();
    }

    @Override
    public void render(Graphics g) {
        //Request game info (State,Users,usw...)
        Response<?> response = Main.NETWORK.getPackageHandler().sendRequestWithCallBack(new Request<>(game.getGameID(), RequestType.GET_GAME_INFO));
        GameInfo gameInfo = Main.NETWORK.getPackageHandler().parseDataToClass(response.getData().toString(), GameInfo.class);

        this.logger.debug("Game info:{}", gameInfo.getGameState());
        //Update Components
        this.updateComponents(gameInfo);
    }

    private void updateComponents(GameInfo gameInfo)
    {
        if(gameInfo.getGameState() == GameState.RUNNING)
        {
            this.questionPanel.setVisible(true);
            this.mainPanel.setVisible(false);
        }

        this.game.setGameState(gameInfo.getGameState());
        this.game.setCurrentPlayers(gameInfo.getCurrentPlayers());

        this.gameInfoLabel.setText("Game: " + this.game.getGameID() + " | Players: "
                + this.game.getCurrentPlayers() + "/" + this.game.getMaxUserSize());
        this.gameStateLabel.setText("Game State:" + this.game.getGameState());
    }

    private void createQuizComponents()
    {

    }

}
