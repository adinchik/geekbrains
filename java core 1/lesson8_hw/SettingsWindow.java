package ru.geekbrains.java1.lesson8_hw;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class SettingsWindow extends JFrame {
    private static final int WINDOW_WIDTH = 350;
    private static final int WINDOW_HEIGHT = 300;
    private static final int MIN_WIN_LENGTH = 3;
    private static final int MIN_FIELD_SIZE = 3;
    private static final int MAX_FIELD_SIZE = 10;
    private static final String FIELD_SIZE_PREFIX = "Field size: ";
    private static final String WIN_LENGTH_PREFIX = "Win length: ";

    private JSlider sliderWinLength;
    private JSlider sliderFieldSize;
    private JRadioButton humanVsAI;
    private JRadioButton humanVsHuman;
    private GameWindow gameWindow;

    public SettingsWindow(GameWindow gameWindow) {
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.gameWindow = gameWindow;
        setLocationRelativeTo(this.gameWindow);
        setResizable(false);
        setTitle("Settings. Create your new game");
        setLayout(new GridLayout(10, 1));

        addChooseGameMode();
        addSetFieldSize();

        JButton buttonStart = new JButton("Start new game");
        buttonStart.addActionListener(e -> {
            submitSettings(gameWindow);
        });
        add(buttonStart);
    }

    private void submitSettings(GameWindow gameWindow) {
        int gameMode;
        if (humanVsAI.isSelected()) {
            gameMode = TicTacToe.MODE_VS_AI;
        } else {
            gameMode = TicTacToe.MODE_VS_HUMAN;
        }
        int fieldSize = sliderFieldSize.getValue();
        int winLength = sliderWinLength.getValue();
        gameWindow.startGame(gameMode, fieldSize, winLength);
        setVisible(false);
    }

    private void addSetFieldSize() {
        JLabel labelFieldSize = new JLabel(FIELD_SIZE_PREFIX + MIN_FIELD_SIZE);
        JLabel labelWinLength = new JLabel(WIN_LENGTH_PREFIX + MIN_WIN_LENGTH);

        sliderFieldSize = new JSlider(MIN_FIELD_SIZE, MAX_FIELD_SIZE, MIN_FIELD_SIZE);
        sliderWinLength = new JSlider(MIN_WIN_LENGTH, MAX_FIELD_SIZE, MIN_FIELD_SIZE);

        sliderFieldSize.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int currentValue = sliderFieldSize.getValue();
                labelFieldSize.setText(FIELD_SIZE_PREFIX + currentValue);
                sliderWinLength.setMaximum(currentValue);
            }
        });

        sliderWinLength.addChangeListener(e -> labelWinLength.setText(WIN_LENGTH_PREFIX + sliderWinLength.getValue()));
        add(new JLabel("Choose field size: "));
        add(labelFieldSize);
        add(sliderFieldSize);
        add(new JLabel("Choode win length: "));
        add(labelWinLength);
        add(sliderWinLength);
    }

    private void addChooseGameMode() {
        add(new JLabel("Choose game mood: "));
        humanVsAI = new JRadioButton("Human versus AI", true);
        humanVsHuman = new JRadioButton(("Human versus human"));
        ButtonGroup gameMode = new ButtonGroup();
        gameMode.add(humanVsAI);
        gameMode.add(humanVsHuman);
        add(humanVsAI);
        add(humanVsHuman);
    }
}
