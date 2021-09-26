package ru.geekbrains.java3.lesson1_hw;

import java.util.ArrayList;

public class Box <F extends Fruit> {
    private ArrayList<F> listOfFruits;

    public Box() {
        this.listOfFruits = new ArrayList<F>();
    }

    public void addFruit(F fruit) {
        listOfFruits.add(fruit);
    }

    public double getWeight() {
        if (listOfFruits.size() > 0)
            return listOfFruits.get(0).getWeight() * listOfFruits.size();
        else return 0;
    }

    public boolean compare(Box<?> anotherBox) {
        return Math.abs(this.getWeight() - anotherBox.getWeight()) < 0.0001;
    }

    public void putFruitsToAnotherBox(Box<F> anotherBox) {
        for (F fruit: listOfFruits){
            anotherBox.addFruit(fruit);
        }
        listOfFruits.clear();
    }
}
