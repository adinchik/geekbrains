package ru.geekbrains.java1.lesson8_hw;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 640;
    private TicTacToe gameMap;

    public GameWindow() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setTitle("Tic Tac Toe");
        setResizable(false);
        JButton btnStart = new JButton("<html><body><b>Start</b></body></html>");
        JButton btnStop = new JButton("<html><body><b>Stop</b></body></html>");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        buttonPanel.add(btnStart, btnStop);

        gameMap = new TicTacToe();
        add(gameMap, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);

        SettingsWindow settingsWindow = new SettingsWindow(this);
        btnStart.addActionListener(e -> settingsWindow.setVisible(true));
        btnStop.addActionListener(e -> System.exit(0));
    }

    public void startGame(int gameMode, int fieldSize, int winLength) {
        gameMap.startNewGame(gameMode, fieldSize, winLength);
    }
}
