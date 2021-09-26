package ru.geekbrains.java2.lesson5_hw;

import java.sql.SQLOutput;
import java.util.Arrays;

public class HomeWorkLesson5 {
    static final int SIZE = 10000000;
    static final int HALF = SIZE / 2;
    static float[] arr1 = new float[SIZE];
    static float[] arr2 = new float[SIZE];

    public static void main(String[] args) {
        firstMethod();
        secondMethod();
        System.out.println(Arrays.equals(arr1, arr2));
    }

    public static void firstMethod() {

        for (int i = 0; i < arr1.length; i++) {
            arr1[i] = 1.0f;
        }
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < arr1.length; i++) {
            arr1[i] = (float) (arr1[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }

        System.out.println("First thread time: " + (System.currentTimeMillis() - startTime) + " ms.");
    }

    public static void secondMethod() {
        float[] a1 = new float[SIZE / 2];
        float[] a2 = new float[SIZE / 2];
        for (int i = 0; i < arr2.length; i++) {
            arr2[i] = 1.0f;
        }
        long startTime = System.currentTimeMillis();
        System.arraycopy(arr2, 0, a1, 0, HALF);
        System.arraycopy(arr2, HALF, a2, 0, HALF);

        Thread t1 = new Thread(() ->{
            for (int i = 0; i < a1.length; i++) {
                a1[i] = (float) (a1[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });

        Thread t2 = new Thread(() ->{
            for (int i = 0; i < a2.length; i++) {
                a2[i] = (float) (a2[i] * Math.sin(0.2f + (i + HALF) / 5) * Math.cos(0.2f + (i + HALF) / 5) * Math.cos(0.4f + (i + HALF) / 2));
            }
        });

        try {
            t1.start(); t2.start();
            t1.join(); t2.join();
            System.arraycopy(a1, 0, arr2, 0, HALF);
            System.arraycopy(a2, 0, arr2, HALF, HALF);
            System.out.println("Second thread time: " + (System.currentTimeMillis() - startTime) + " ms.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
