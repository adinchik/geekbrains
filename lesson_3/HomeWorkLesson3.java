package ru.geekbrains.java1.lesson3_hw;

import java.util.Random;
import java.util.Scanner;

public class HomeWorkLesson3 {

    public static void printArray(int[] a) {
        for (int i = 0; i < a.length; i++)
            System.out.print(a[i] + " ");
        System.out.println();
    }

    public static int[] createArray(int len, int initialValue) {
        int a[] = new int[len];
        for (int i = 0; i < len; i++)
            a[i] = initialValue;
        return a;
    }

    public static boolean checkBalance(int[] a) {
        int rS = 0;
        for (int i = 1; i < a.length; i++)
            rS += a[i];
        int lS = a[0], i = 0;
        while (i + 1 < a.length && lS != rS) {
            i++;
            lS += a[i]; rS -= a[i];
        }
        return i + 1 < a.length;
    }

    public static void main(String[] args) {

        //task 1
        int a1[] = {1, 1, 0, 0, 1, 0, 1, 1, 0, 0};
        for (int i = 0; i < a1.length; i++) {
            if (a1[i] == 0) a1[i] = 1;
            else a1[i] = 0;
        }
        printArray(a1);

        //task 2
        int a2[] = new int[100];
        for (int i = 0; i < 100; i++)
            a2[i] = i + 1;
        printArray(a2);

        //task 3
        int a3[] = {1, 5, 3, 2, 11, 4, 5, 2, 4, 8, 9, 1};
        for (int i = 0; i < a3.length; i++) {
            if (a3[i] < 6) a3[i] *= 2;
        }
        printArray(a3);


        //task 4
        int dA[][] = new int[5][5];
        for (int i = 0; i < 5; i++) {
            dA[i][i] = 1;
            dA[i][5 - i - 1] = 1;
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++)
                System.out.print(dA[i][j] + " ");
            System.out.println();
        }

        //task 5
        int a5[] = createArray(5, 10);
        printArray(a5);

        //task 6
        int a6[] = new int[5];
        Random rand = new Random();
        int max = -1, min = 100;
        for (int i = 0; i < 5; i++) {
            a6[i] = rand.nextInt(100);
            if (a6[i] > max) max = a6[i];
            if (a6[i] < min) min = a6[i];
        }
        System.out.println(min + " " + max);
        printArray(a6);

        //task 7
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int a7[] = new int[n];
        for (int i = 0; i < n; i++)
            a7[i] = sc.nextInt();
        System.out.println(checkBalance(a7));

        //task 8
        int m = sc.nextInt();
        int a8[] = new int[m * 2];
        for (int i = 0; i < m; i++)
            a8[i] = sc.nextInt();
        n = sc.nextInt();
        n = n % m;
        if (n > 0) {
            for (int i = m - 1; i >= 0; i--)
                a8[i + n] = a8[i];
            for (int i = m; i < n + m; i++)
                a8[i - m] = a8[i];
        }
        else {
            n = -n;
            for (int i = m; i < m * 2; i++)
                a8[i] = a8[i - m];
            for (int i = n; i < n + m; i++)
                a8[i - n] = a8[i];
        }
        for (int i = 0; i < m; i++)
            System.out.print(a8[i] + " ");
        System.out.println();
    }
}
