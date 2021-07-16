package ru.geekbrains.java1.lesson1_hw;

public class HomeWorkApp {

    public static void main(String[] args) {
        printThreeWords();
        checkSumSign();
        printColor();
        compareNumbers();
    }

    public static void printThreeWords() {
        System.out.println("_Orange");
        System.out.println("_Banana");
        System.out.println("_Apple");
    }

    public static void checkSumSign() {
        int a = 5;
        int b = 6;
        if (a + b > 0)
            System.out.println("The sum is positive");
        else
            System.out.println("The sum is negative");
    }

    public static void printColor() {
        int value = -100;
        if (value <= 0)
            System.out.println("Red");
        else if (value > 0 && value <= 100)
            System.out.println("Yellow");
        else
            System.out.println("Green");
    }

    public static void compareNumbers() {
        int a = 200;
        int b = 500;
        if (a >= b)
            System.out.println("a >= b");
        else
            System.out.println("a < b");
    }
}
