package ru.geekbrains.java2.lesson3_hw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeWorkLesson3 {

    public static void main(String[] args) {
        String arr[] = {"One", "Two", "Three", "Four", "Five", "One", "Two", "Three", "Four", "Six"};
        HashMap<String, Integer> map = new HashMap<>();
        for (String item: arr) {
            int k = 0;
            if (map.containsKey(item)) {
                k = map.get(item);
            }
            map.put(item, k + 1);
        }
        System.out.println("Task 1:");
        System.out.println(map);

        PhoneBook phoneBook = new PhoneBook();
        phoneBook.add("Adina", "+77054627318");
        phoneBook.add("Adina", "+77084807800");
        phoneBook.add("Saule", "+77772743972");
        phoneBook.add("Tuleu", "+77773753105");

        System.out.println("Task 2:");
        System.out.println("Adina's phones: " + phoneBook.get("Adina"));

    }
}
