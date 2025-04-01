package com.himiko.gui.screen.screens;


import com.himiko.Main;
import com.himiko.game.Game;
import com.himiko.game.elemtents.Question;
import com.himiko.game.elemtents.QuestionCategory;
import com.himiko.game.manager.GameManager;
import com.himiko.gui.GUI;
import com.himiko.gui.screen.Screen;
import com.himiko.gui.screen.ScreenHandler;
import com.himiko.logger.Logger;
import com.himiko.network.protocol.data.GameCreateData;
import com.himiko.network.protocol.data.GameInfo;
import com.himiko.network.protocol.request.Request;
import com.himiko.network.protocol.request.RequestType;
import com.himiko.network.protocol.response.Response;
import com.himiko.network.protocol.response.ResponseType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author Valk on 21.03.2025
 * @project quizClient
 */
public class CreateGameScreen extends Screen {
    private Logger logger;
    private ArrayList<Question> questionList;
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel maxUserSizeLabel;
    private JButton createButton;
    private JButton addQuestionButton;
    private JButton removeQuestionButton;
    private JButton backButton;
    private JCheckBox privateGameCheckBox;
    private JCheckBox autoStartCheckBox;
    private JComboBox<Integer> maxUserSizeBox;
    private JTextField questionTextField;
    private JTextField answerTextField;
    private JTextField optionTextField1;
    private JTextField optionTextField2;
    private JTextField optionTextField3;
    private JList<String> questionOverviewList;
    private DefaultListModel<String> questionListModel;
    private JScrollPane questionListScrollPane;

    public CreateGameScreen() {
        super("Create Game");
        this.logger = Main.logger;
        this.questionList = new ArrayList<>();

        this.mainPanel = GUI.uiManager.createStyledPanel(null);

        this.titleLabel = GUI.uiManager.createStyledLabel("" + this.getName());
        this.titleLabel.setFont(new Font("Arial", Font.BOLD,18));

        this.createButton = GUI.uiManager.createStyledButton("Create Game");
        this.createButton.addActionListener(a ->{
            GameCreateData createData = new GameCreateData((int)this.maxUserSizeBox.getSelectedItem(), this.privateGameCheckBox.isSelected(), this.autoStartCheckBox.isSelected(),this.questionList.toArray(new Question[0]));
            Response<?> response = Main.NETWORK.getPackageHandler().sendRequestWithCallBack(new Request<>(createData, RequestType.GAME_CREATE));

            if(response.getResponseType() == ResponseType.SUCCESS)
            {
                //Response data will be also a Game which has been created with a code (if privateGame)
                if(response.getData() != null)
                {
                    GameInfo gameInfo = Main.NETWORK.getPackageHandler().parseDataToClass(response.getData().toString(), GameInfo.class);
                    GameManager.addGame(gameInfo);
                    ScreenHandler.INSTANCE.changeScreen(new GameScreen(GameManager.getGame(gameInfo.getGameID())));
                }else
                {
                    throw new RuntimeException("Should have received game info data...");
                }
            }else{
                this.logger.error("Something went wrong while creating a game... Error:{}", response.getData() == null ? "nothing" : response.getData().toString());
            }
        });

        this.backButton = GUI.uiManager.createStyledButton("Back");
        this.backButton.addActionListener(a -> ScreenHandler.INSTANCE.changeScreen(ScreenHandler.ACTION_SELECTION_SCREEN));

        this.addQuestionButton = GUI.uiManager.createStyledButton("Add Question");
        this.addQuestionButton.addActionListener(a ->{
            //TODO:Options koennen nicht von gson richtig formatiert werden... Alternative finden...
           // this.questionList.add(new Question(this.questionTextField.getText(), this.answerTextField.getText(), new String[]{optionTextField1.getText(), optionTextField2.getText(), optionTextField3.getText()}, QuestionCategory.OTHER));
            this.questionList.add(new Question(this.questionTextField.getText(), this.answerTextField.getText(), QuestionCategory.OTHER));
            this.questionListModel.addElement(this.questionTextField.getText());
        });


        this.removeQuestionButton = GUI.uiManager.createStyledButton("Remove Question");
        this.removeQuestionButton.addActionListener(a ->{
            int selectedIndex = this.questionOverviewList.getSelectedIndex();
            if (selectedIndex >= 0) {
                this.questionListModel.remove(selectedIndex);
                this.questionList.remove(selectedIndex);
            }
        });

        this.privateGameCheckBox = GUI.uiManager.createStyledCheckBox("Private");
        this.autoStartCheckBox = GUI.uiManager.createStyledCheckBox("Auto Start");

        this.maxUserSizeBox = new JComboBox<>(new Integer[]{2,3,4,5,6,7,8,9,10,11,12});
        this.maxUserSizeLabel = GUI.uiManager.createStyledLabel("Max User Size:");
        this.maxUserSizeLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        this.questionTextField = GUI.uiManager.createStyledTextField("Question");
        this.answerTextField = GUI.uiManager.createStyledTextField("Correct Answer");
        this.optionTextField1 = GUI.uiManager.createStyledTextField("Option 2");
        this.optionTextField2 = GUI.uiManager.createStyledTextField("Option 3");
        this.optionTextField3 = GUI.uiManager.createStyledTextField("Option 4");

        this.questionListModel = new DefaultListModel<>();
        this.questionOverviewList = new JList<>(questionListModel);
        this.questionOverviewList.setFont(new Font("Arial", Font.PLAIN, 12));
        this.questionOverviewList.setVisibleRowCount(5);
        this.questionListScrollPane = new JScrollPane(this.questionOverviewList);

        this.mainPanel.add(this.titleLabel);
        this.mainPanel.add(this.createButton);
        this.mainPanel.add(this.addQuestionButton);
        this.mainPanel.add(this.removeQuestionButton);
        this.mainPanel.add(this.backButton);
        this.mainPanel.add(this.questionTextField);
        this.mainPanel.add(this.answerTextField);
        this.mainPanel.add(this.optionTextField1);
        this.mainPanel.add(this.optionTextField2);
        this.mainPanel.add(this.optionTextField3);
        this.mainPanel.add(this.privateGameCheckBox);
        this.mainPanel.add(this.autoStartCheckBox);
        this.mainPanel.add(this.maxUserSizeBox);
        this.mainPanel.add(this.maxUserSizeLabel);
        this.mainPanel.add(this.questionListScrollPane);

        super.setComponents(this.mainPanel
        );
    }

    @Override
    public void onEnter() {
        WIDTH = Main.GUI.getWidth();
        HEIGHT = Main.GUI.getHeight();

        int titleWidth = 300, titleHeight = 50;
        int fieldWidth = 200, fieldHeight = 30;
        int buttonWidth = 150, buttonHeight = 30;
        int labelWidth = 120, labelHeight = 30;
        int verticalGap = 10;

        this.mainPanel.setBounds(0, 0, WIDTH, HEIGHT);
        this.titleLabel.setBounds((WIDTH - (titleWidth / 2)) / 2, 20, titleWidth, titleHeight);
        this.questionTextField.setBounds((WIDTH - fieldWidth) / 2, titleLabel.getY() + titleHeight + verticalGap, fieldWidth, fieldHeight);
        this.answerTextField.setBounds((WIDTH - fieldWidth) / 2, questionTextField.getY() + fieldHeight + verticalGap, fieldWidth, fieldHeight);
        this.optionTextField1.setBounds((WIDTH - fieldWidth) / 2, answerTextField.getY() + fieldHeight + verticalGap, fieldWidth, fieldHeight);
        this.optionTextField2.setBounds((WIDTH - fieldWidth) / 2, optionTextField1.getY() + fieldHeight + verticalGap, fieldWidth, fieldHeight);
        this.optionTextField3.setBounds((WIDTH - fieldWidth) / 2, optionTextField2.getY() + fieldHeight + verticalGap, fieldWidth, fieldHeight);
        this.addQuestionButton.setBounds((WIDTH - buttonWidth) / 2, optionTextField3.getY() + fieldHeight + verticalGap, buttonWidth, buttonHeight);
        this.removeQuestionButton.setBounds((WIDTH - buttonWidth) / 2, addQuestionButton.getY() + buttonHeight + verticalGap, buttonWidth, buttonHeight);
        this.maxUserSizeLabel.setBounds((WIDTH - fieldWidth) / 2, removeQuestionButton.getY() + buttonHeight + verticalGap, labelWidth, labelHeight);
        this.maxUserSizeBox.setBounds(maxUserSizeLabel.getX() + labelWidth + 10, maxUserSizeLabel.getY(), fieldWidth - labelWidth - 10, fieldHeight);
        this.privateGameCheckBox.setBounds((WIDTH - fieldWidth) / 2, maxUserSizeBox.getY() + fieldHeight + verticalGap, fieldWidth, fieldHeight);
        this.autoStartCheckBox.setBounds((WIDTH - fieldWidth)/ 2, privateGameCheckBox.getY() + fieldHeight + verticalGap, fieldWidth, fieldHeight);
        this.questionListScrollPane.setBounds((WIDTH - fieldWidth) / 2,autoStartCheckBox.getY() + fieldHeight + verticalGap, fieldWidth, 100);
        this.createButton.setBounds((WIDTH - buttonWidth) / 2, questionListScrollPane.getY() + 100 + verticalGap, buttonWidth, buttonHeight);
        this.backButton.setBounds((WIDTH - buttonWidth) / 2, createButton.getY() + buttonHeight + verticalGap, buttonWidth, buttonHeight);

        super.onEnter();
    }

    @Override
    public void render(Graphics g) {
    }
}
