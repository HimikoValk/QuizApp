package com.himiko.gui.screen.screens;


import com.google.gson.Gson;
import com.himiko.Main;
import com.himiko.game.Game;
import com.himiko.game.GameState;
import com.himiko.game.elemtents.Question;
import com.himiko.game.utils.UserData;
import com.himiko.gui.GUI;
import com.himiko.gui.screen.Screen;
import com.himiko.gui.screen.ScreenHandler;
import com.himiko.logger.Logger;
import com.himiko.network.protocol.data.GameInfo;
import com.himiko.network.protocol.data.QuestionInfo;
import com.himiko.network.protocol.data.ScoreData;
import com.himiko.network.protocol.request.Request;
import com.himiko.network.protocol.request.RequestType;
import com.himiko.network.protocol.response.Response;
import com.himiko.network.protocol.response.ResponseType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author Valk on 16.03.2025
 * @project quizClient
 */
public class GameScreen extends Screen {
    private Logger logger;
    private Game game;

    private JLabel gameInfoLabel;
    private JLabel gameStateLabel;
    private JLabel questionLabel;
    private JLabel pointsLabel;
    private JButton leaveButton;
    private JButton startButton;
    private JList<String> userList;
    private DefaultListModel<String> userListModel;
    private JPanel mainPanel;
    private JPanel questionPanel;
    private JPanel finishPanel;
    private Timer questionTimer;
    private int timeRemaining = 20;
    private QuestionInfo lastQuestionInfo;

    public GameScreen(Game game) {
        super("Game: " + game.getGameID());
        this.logger = Main.logger;

        this.game = game;

        this.mainPanel = GUI.uiManager.createStyledPanel(null);
        this.questionPanel = GUI.uiManager.createStyledPanel(null);
        this.finishPanel = GUI.uiManager.createStyledPanel(null);

        this.gameStateLabel = GUI.uiManager.createStyledLabel("" + game.getGameState());
        this.gameStateLabel.setFont(new Font("Arial", Font.BOLD, 18));

        this.gameInfoLabel = GUI.uiManager.createStyledLabel("Game: " + game.getGameID() + " | Players: " + game.getCurrentPlayers() + "/" + game.getMaxUserSize());
        this.gameInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));

        this.questionLabel = GUI.uiManager.createStyledLabel("Question:");
        this.questionLabel.setFont(new Font("Arial", Font.BOLD, 15));

        this.pointsLabel = GUI.uiManager.createStyledLabel("Points:");

        this.startButton = GUI.uiManager.createStyledButton("Start");
        this.startButton.addActionListener(a -> {
            Response<?> response = Main.NETWORK.getPackageHandler().sendRequestWithCallBack(new Request<>(this.game.getGameID(), RequestType.GAME_START));

            if (response.getResponseType() != ResponseType.SUCCESS) {
                JOptionPane.showMessageDialog(null, response.getData() != null ? "Something went wrong...\nMessage:" + response.getData().toString() : "Something went wrong...");
            } else {
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

        this.questionTimer = new Timer(1000, e->{
           this.timeRemaining--;
           this.questionLabel.setText("<html>" + (this.lastQuestionInfo != null ? this.lastQuestionInfo.getQuestion() : "Waiting...")
                   + "<br>(Time: " + this.timeRemaining + "s) </html>");
            if(this.timeRemaining <= 0) {
                this.questionTimer.stop();
                this.timeRemaining = 20;
            }
        });

        this.mainPanel.add(gameInfoLabel);
        this.mainPanel.add(gameStateLabel);
        this.mainPanel.add(startButton);
        this.mainPanel.add(leaveButton);
        this.mainPanel.add(userScrollPane);

        this.questionPanel.add(questionLabel);
        this.questionPanel.add(pointsLabel);
        super.setComponents(
                mainPanel,
                questionPanel,
                finishPanel);
    }

    @Override
    public void onEnter() {
        this.WIDTH = Main.GUI.getWidth();
        this.HEIGHT = Main.GUI.getHeight();

        this.mainPanel.setBounds(0, 0, WIDTH, HEIGHT);
        this.questionPanel.setBounds(0, 0, WIDTH, HEIGHT);
        this.finishPanel.setBounds(0, 0, WIDTH, HEIGHT);
        this.gameInfoLabel.setBounds(WIDTH / 2 - 100, 40, 300, 30);
        this.gameStateLabel.setBounds(WIDTH / 2 - 100, 20, 200, 40);
        this.startButton.setBounds(WIDTH / 2 - 50, HEIGHT / 2 - 40, 100, 40);
        this.leaveButton.setBounds(WIDTH / 2 - 50, HEIGHT / 2, 100, 40);
        //userList.getParent().setBounds(WIDTH - 100, this.gameInfoLabel.getY() + 50, 100, 50);
        //
        super.onEnter();
    }

    @Override
    public void render(Graphics g) {
        //Request game info (State,Users,usw...)
        Response<?> response = Main.NETWORK.getPackageHandler().sendRequestWithCallBack(new Request<>(game.getGameID(), RequestType.GET_GAME_INFO));
        GameInfo gameInfo = null;
        try {
            gameInfo = Main.NETWORK.getPackageHandler().parseDataToClass(response.getData().toString(), GameInfo.class);
        } catch(Exception ex) {
            this.logger.error("Error parsing GameInfo: " + ex.getMessage());
            return;
        }

        this.logger.debug("Game info:{}", gameInfo.getGameState());
        //Update Components
        this.updateComponents(gameInfo);

        if (this.game.getGameState() == GameState.RUNNING)
        {
            Response<?> questionResponse = Main.NETWORK.getPackageHandler().sendRequestWithCallBack(new Request<>(null, RequestType.GET_QUESTION_INFO));
            if(questionResponse.getData() == null) return;
            QuestionInfo questionInfo = null;
            try {
              //   questionInfo = Main.NETWORK.getPackageHandler().parseDataToClass(questionResponse.getData(), QuestionInfo.class);
                questionInfo = Main.NETWORK.getPackageHandler().parseDataToClass(questionResponse, QuestionInfo.class);
            } catch(Exception ex) {
                logger.error("Error parsing QuestionInfo: " + ex.getMessage());
                return;
            }
            //Muss dann nicht noch mal alles rendern ...
            if(this.lastQuestionInfo != null && this.lastQuestionInfo.getQuestion().equals(questionInfo.getQuestion())) return;

            this.logger.info("Received info:\nQuestion:{} Options Size:{}", questionInfo.getQuestion(), questionInfo.getOptions().length);
            this.updateQuizComponents(questionInfo);
            this.lastQuestionInfo = questionInfo;
        }else if(this.game.getGameState() == GameState.FINISHED)
        {
            this.questionTimer.stop();
            this.questionPanel.setVisible(false);
            this.mainPanel.setVisible(false);
            this.finishPanel.setVisible(true);
        }
        else
        {
            this.questionTimer.stop();
            this.questionPanel.setVisible(false);
            this.mainPanel.setVisible(true);
        }
    }

    private void updateComponents(GameInfo gameInfo) {
        this.game.setGameState(gameInfo.getGameState());
        this.game.setCurrentPlayers(gameInfo.getCurrentPlayers());

        this.gameInfoLabel.setText("Game: " + this.game.getGameID() + " | Players: "
                + this.game.getCurrentPlayers() + "/" + this.game.getMaxUserSize());
        this.gameStateLabel.setText("Game State:" + this.game.getGameState());
    }

    private void updateQuizComponents(QuestionInfo questionInfo) {
        this.questionPanel.setVisible(true);
        this.mainPanel.setVisible(false);

        this.questionPanel.removeAll();
        if (questionInfo != null)
        {
            this.questionLabel.setText("Question: " + questionInfo.getQuestion() + " (Time: 20s)");
            this.logger.debug("Question:{} (Time: 20s)", questionInfo.getQuestion());
            this.questionLabel.setBounds(20, 20, this.WIDTH + 100, 60);

            this.timeRemaining = 20;
            this.questionTimer.start();


            if (questionInfo.getQuestion() != null)
            {
                this.questionPanel.removeAll();
                this.questionPanel.add(this.questionLabel);
                //Buttons
                int btnWidth = (WIDTH - 80) / 2, btnHeight = 40;
                int i = 0;
                for (String option : questionInfo.getOptions()) {
                    JButton answerButton = GUI.uiManager.createStyledButton(option);
                    int x = 20 + (i % 2) * (btnWidth + 20);
                    int y = 100 + (i / 2) * (btnHeight + 20);
                    answerButton.setBounds(x, y, btnWidth, btnHeight);
                    answerButton.addActionListener(a -> {
                        Main.NETWORK.getPackageHandler().sendRequest(new Request<>(answerButton.getText(), RequestType.ANSWER));
                    });
                    this.questionPanel.add(answerButton);
                    i++;
                }
            }
            this.questionPanel.add(this.leaveButton);
        }
    }

    private void updateFinishComponents()
    {
        Response<?> scoreResponse = Main.NETWORK.getPackageHandler().sendRequestWithCallBack(new Request<>(null, RequestType.SCORE_INFO));
        if(scoreResponse.getData() == null)return;
        ScoreData scoreData = Main.NETWORK.getPackageHandler().parseDataToClass(scoreResponse, ScoreData.class);
        Map<UserData, Integer> scores = scoreData.getUserScoreMap();
        ArrayList<Map.Entry<UserData, Integer>> sortedScores = new ArrayList<>(scores.entrySet());
        sortedScores.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        StringBuilder scoreboardText = new StringBuilder("Final Scores:\n");
        int rank = 1;
        int playerRank = -1;
        String currentUser = Main.NETWORK.userData.getName();

        for (Map.Entry<UserData, Integer> entry : sortedScores) {
            scoreboardText.append(rank).append(". ").append(entry.getKey().getName()).append(" - ").append(entry.getValue()).append(" points\n");
            if (entry.getKey().getName().equals(currentUser)) {
                playerRank = rank;
            }
            rank++;
        }

        JLabel scoreboardLabel = GUI.uiManager.createStyledLabel("<html>" + scoreboardText.toString().replace("\n", "<br>") + "</html>");
        JLabel rankLabel = GUI.uiManager.createStyledLabel("Your Rank: " + playerRank);

        finishPanel.removeAll();
        finishPanel.add(scoreboardLabel);
        finishPanel.add(rankLabel);
        finishPanel.setVisible(true);
    }

}
