package ru.geekbrains.java2.lesson3_hw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PhoneBook {

    private HashMap<String, List<String>> hm = new HashMap<>();

    public void add(String name, String phone) {
        List <String> list = new ArrayList<>();
        if (hm.containsKey(name)) {
            list = hm.get(name);
        }
        list.add(phone);
        hm.put(name, list);
    }

    public List<String> get(String name) {
        return hm.get(name);
    }
}
