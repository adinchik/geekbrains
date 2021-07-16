package ru.geekbrains.java1.lesson4_hw;

import java.util.Random;
import java.util.Scanner;

public class TicTacToe {
    public static int SIZE = 5;
    public static int DOTS_TO_WIN = 5;
    public static int WIN_LEN = 4;
    public static final char DOT_EMPTY = '•';
    public static final char DOT_X = 'X';
    public static final char DOT_O = 'O';
    public static char[][] map;
    public static Scanner sc = new Scanner(System.in);
    public static Random rand = new Random();
    public static void main(String[] args) {
        initMap();
        printMap();
        while (true) {
            humanTurn();
            printMap();
            if (checkWin(DOT_X)) {
                System.out.println("Победил человек");
                break;
            }
            if (isMapFull()) {
                System.out.println("Ничья");
                break;
            }
            aiTurn();
            printMap();
            if (checkWin(DOT_O)) {
                System.out.println("Победил Искуственный Интеллект");
                break;
            }
            if (isMapFull()) {
                System.out.println("Ничья");
                break;
            }
        }
        System.out.println("Игра закончена");
    }

    // Add checkLine
    public static boolean checkLine(int x, int y, char symb, int changeX, int changeY) {
        int i = x, j = y;
        for (int k = 0; k < WIN_LEN; k++) {
            if (i < 0 || i >= SIZE || j < 0 || j >= SIZE || map[i][j] != symb) return false;
            i += changeX;
            j += changeY;
        }
        return true;
    }

    // Update checkWin
    public static boolean checkWin(char symb) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (checkLine(i, j, symb,-1, 1)) return true;
                if (checkLine(i, j, symb,0, 1)) return true;
                if (checkLine(i, j, symb,1, 1)) return true;
                if (checkLine(i, j, symb,1, 0)) return true;
            }
        }
        return false;
    }
    public static boolean isMapFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (map[i][j] == DOT_EMPTY) return false;
            }
        }
        return true;
    }

    // Add scanMap
    public static int[] scanMap(char symb) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
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
    public static void aiTurn() {
        int x, y;
        do {
            int res[];
            res = scanMap(DOT_O);
            if (res[0] == -1) {
                res = scanMap(DOT_X);
                if (res[0] == -1) {
                    x = rand.nextInt(SIZE);
                    y = rand.nextInt(SIZE);
                } else {
                    x = res[0]; y = res[1];
                }
            } else {
                x = res[0]; y = res[1];
            }
        } while (!isCellValid(x, y));
        System.out.println("Компьютер походил в точку " + (x + 1) + " " + (y + 1));
        map[y][x] = DOT_O;
    }
    public static void humanTurn() {
        int x, y;
        do {
            System.out.println("Введите координаты в формате X Y");
            x = sc.nextInt() - 1;
            y = sc.nextInt() - 1;
        } while (!isCellValid(x, y));
        map[y][x] = DOT_X;
    }
    public static boolean isCellValid(int x, int y) {
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) return false;
        if (map[y][x] == DOT_EMPTY) return true;
        return false;
    }
    public static void initMap() {
        map = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                map[i][j] = DOT_EMPTY;
            }
        }
    }
    public static void printMap() {
        for (int i = 0; i <= SIZE; i++) {
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
    }
}
