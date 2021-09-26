package ru.geekbrains.java1.lesson8_hw;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Scanner;

public class TicTacToe extends JPanel {
    public static final int MODE_VS_AI = 0;
    public static final int MODE_VS_HUMAN = 1;
    public static final int DOT_EMPTY = 0;
    public static final int DOT_AI = 2;
    public static final int DOT_HUMAN = 1;
    private static final int DOT_PADDING = 7;
    private static final int STATE_DRAW = 0;
    private static final int STATE_WIN_HUMAN = 1;
    private static final int STATE_WIN_AI = 2;

    private int stateGameOver;
    private int[][] map;
    private int fieldSizeX;
    private int fieldSizeY;
    private int winLength;
    private int cellWidth;
    private int cellHeight;
    private boolean isGameOver;
    private boolean isInitialized;
    private int gameMode;
    private int playerNumTurn;

    public static Random rand = new Random();

    public TicTacToe() {
        isInitialized = false;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                update(e);
            }
        });
    }

    private void update(MouseEvent e) {
        System.out.println("!!!!!");
        if (isGameOver || !isInitialized) return;
        int dot = gameMode == MODE_VS_AI ? DOT_HUMAN : playerNumTurn == 1 ?  DOT_HUMAN : DOT_AI;
        if (!playerTurn(e, dot)) return;
        if (gameCheck(dot, STATE_WIN_HUMAN)) return;
        if (gameMode == MODE_VS_AI) {
            aiTurn();
            playerNumTurn = playerNumTurn == 2 ? 1 : 2;
            repaint();
            if (gameCheck(DOT_AI, STATE_WIN_AI)) return;
        }
    }

    private boolean playerTurn(MouseEvent event, int dot) {
        int x = event.getX() / cellWidth;
        int y = event.getY() / cellHeight;
        System.out.println(y + " " + x);
        if (!isCellValid(y, x) || map[y][x] != DOT_EMPTY) return false;
        System.out.println(y + " " + x);
        map[y][x] = dot;
        repaint();
        playerNumTurn = playerNumTurn == 2 ? 1 : 2;
        return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }

    private void render(Graphics g){
        if (!isInitialized) return;
        int width = getWidth();
        int height = getHeight();
        cellHeight = height/ fieldSizeY;
        cellWidth = width / fieldSizeX;
        g.setColor(Color.BLACK);
        for(int i = 0; i < fieldSizeY; i++) {
            int y = i * cellHeight;
            g.drawLine(0, y, width, y);
        }
        for (int i = 0; i < fieldSizeX; i++) {
            int x = i * cellWidth;
            g.drawLine(x, 0, x, height);
        }

        for (int y = 0; y < fieldSizeY; y++)
            for (int x = 0; x < fieldSizeX; x++) {
                if (isCellValid(y, x)) continue;
                if (map[y][x] == DOT_HUMAN) {
                    g.setColor(Color.BLUE);
                    g.fillOval(x * cellWidth + DOT_PADDING, y * cellHeight + DOT_PADDING,
                            cellWidth - DOT_PADDING * 2, cellHeight - DOT_PADDING * 2);
                }
                else {
                    g.setColor(Color.BLUE);
                    g.fillRect(x * cellWidth + DOT_PADDING, y * cellHeight + DOT_PADDING,
                            cellWidth - DOT_PADDING * 2, cellHeight - DOT_PADDING * 2);
                }
            }

        if (isGameOver) {
            showGameOverMessage(g);
        }
    }

    private void showGameOverMessage(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, getHeight() / 2 - 60, getWidth(), 120);
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 58));
        switch (stateGameOver) {
            case STATE_DRAW -> g.drawString("DRAW", getWidth() / 4, getHeight() / 2);
            case STATE_WIN_AI -> g.drawString("AI WINS", getWidth() / 4, getHeight() / 2);
            case STATE_WIN_HUMAN -> g.drawString("HUMAN WINS", getWidth() / 4, getHeight() / 2);
        }
    }

    public void startNewGame(int gameMode, int fieldSize, int winLength) {
        this.gameMode = gameMode;
        this.fieldSizeX = this.fieldSizeY = fieldSize;
        this.winLength = winLength;
        this.playerNumTurn = 1;
        map = new int[fieldSizeX][fieldSizeY];
        initMap();
        isInitialized = true;
        isGameOver = false;
        repaint();
    }

    private boolean gameCheck(int dot, int stateGameOver) {
        if (checkWin(dot)) {
            this.stateGameOver = stateGameOver;
            isGameOver = true;
            repaint();
            return true;
        }
        if (isMapFull()) {
            this.stateGameOver = STATE_DRAW;
            isGameOver = true;
            repaint();
            return true;
        }
        return false;
    }

    /*private void mainMethod() {
        initMap();
        while (true) {
            if (checkWin(DOT_HUMAN)) {
                System.out.println("Победил человек");
                break;
            }
            if (isMapFull()) {
                System.out.println("Ничья");
                break;
            }
            aiTurn();
            if (checkWin(DOT_AI)) {
                System.out.println("Победил Искуственный Интеллект");
                break;
            }
            if (isMapFull()) {
                System.out.println("Ничья");
                break;
            }
        }
        System.out.println("Игра закончена");
    }*/

    // Add checkLine
    private boolean checkLine(int x, int y, int symb, int changeX, int changeY) {
        int i = x, j = y;
        for (int k = 0; k < winLength; k++) {
            if (i < 0 || i >= fieldSizeX || j < 0 || j >= fieldSizeY || map[i][j] != symb) return false;
            i += changeX;
            j += changeY;
        }
        return true;
    }

    // Update checkWin
    private boolean checkWin(int symb) {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (checkLine(i, j, symb,-1, 1)) return true;
                if (checkLine(i, j, symb,0, 1)) return true;
                if (checkLine(i, j, symb,1, 1)) return true;
                if (checkLine(i, j, symb,1, 0)) return true;
            }
        }
        return false;
    }
    private boolean isMapFull() {
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                if (map[i][j] == DOT_EMPTY) return false;
            }
        }
        return true;
    }

    // Add scanMap
    private int[] scanMap(int symb) {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (map[i][j] == DOT_EMPTY) {
                    map[i][j] = symb;
                    boolean res = checkWin(symb);
                    map[i][j] = DOT_EMPTY;
                    if (res) return new int[]{j, i};
                }
            }
        }
        return new int[] {-1, -1};
    }

    // Update aiTurn
    private void aiTurn() {
        int x, y;
        do {
            int res[];
            res = scanMap(DOT_AI);
            if (res[0] == -1) {
                res = scanMap(DOT_HUMAN);
                if (res[0] == -1) {
                    x = rand.nextInt(fieldSizeX);
                    y = rand.nextInt(fieldSizeY);
                } else {
                    x = res[0]; y = res[1];
                }
            } else {
                x = res[0]; y = res[1];
            }
        } while (!isCellValid(y, x));
        //System.out.println("Компьютер походил в точку " + (x + 1) + " " + (y + 1));
        map[y][x] = DOT_AI;
    }

    private boolean isCellValid(int y, int x) {
        if (x < 0 || x >= fieldSizeX || y < 0 || y >= fieldSizeY) return false;
        if (map[y][x] == DOT_EMPTY) return true;
        return false;
    }
    private void initMap() {
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                map[i][j] = DOT_EMPTY;
            }
        }
    }
    /*public static void printMap() {
        for (int i = 0; i <= field; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }*/
}
