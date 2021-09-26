package ru.geekbrains.java3.lesson1_hw;

import java.util.ArrayList;
import java.util.Collections;

public class HomeWorkLesson1 {

    static<T> void changeTwoElements(T[] ar, int i, int j) {
        T temp = ar[i];
        ar[i] = ar[j];
        ar[j] = temp;
    }

    static<T> ArrayList<T> transformToArrayList(T[] ar) {
        ArrayList<T> newAL = new ArrayList<T>();
        Collections.addAll(newAL, ar);
        return newAL;
    }

    public static void main(String[] args) {
        Integer arrayInt[] = {1, 2, 3, 4, 5};
        changeTwoElements(arrayInt, 1, 2);
        for (Integer element: arrayInt) {
            System.out.print(element + " ");
        }
        System.out.println();

        String arrayString[] = {"Adina", "Adeliya", "Ayana"};
        changeTwoElements(arrayString, 0, 2);
        for (String element: arrayString) {
            System.out.print(element + " ");
        }
        System.out.println();

        ArrayList<String> listOfStrings = new ArrayList<String>();
        listOfStrings = transformToArrayList(arrayString);
        System.out.println(listOfStrings);

    }
}
