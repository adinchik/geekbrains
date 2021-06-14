package ru.geekbrains.java1.lesson2_hw;

public class HomeWorkLesson2 {

    public static boolean isSumInInterval(int a, int b) {
        if (a + b >= 10 && a + b <= 20)
            return true;
        else return false;
    }

    public static void printPositiveOrNegative(int n) {
        if (n >= 0)
            System.out.println("Positive");
        else System.out.println("Negative");
    }

    public static boolean isNumberNegative(int n) {
        if (n < 0)
            return true;
        else return false;
    }

    public static void printStrings(String s, int n) {
        for(int i = 1; i <= n; i++)
            System.out.println(s);
    }

    public static boolean isYearLeap(int year) {
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
            return true;
        else return false;
    }

    public static void main(String[] args) {
        System.out.println(isSumInInterval(5, 10));
        printPositiveOrNegative(0);
        System.out.println(isNumberNegative(-9));
        printStrings("Adina", 5);
        System.out.println(isYearLeap(2000));
    }
}
