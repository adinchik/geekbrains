package ru.geekbrains.java3.lesson1_hw;

public class SortFruits {
    public static void main(String[] args) {
        Box<Apple> boxApples1 = new Box<Apple>();
        Box<Apple> boxApples2 = new Box<Apple>();
        Box<Orange> boxOranges1 = new Box<Orange>();
        boxApples1.addFruit(new Apple());
        System.out.println(boxApples1.compare(boxApples2));
        System.out.println(boxApples2.compare(boxOranges1));
        boxApples1.putFruitsToAnotherBox(boxApples2);
        //boxApples2.putFruitsToAnotherBox(boxOranges1);  - not work
    }
}
