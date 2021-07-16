package ru.geekbrains.java1.lesson6_hw;

public class Cat extends Animal{

    private final int RESTR_RUN_CAT = 200;

    public Cat() {}

    public Cat(String name) {
        this.name = name;
    }

    @Override
    public boolean isRun(int length) {
        if (length <= RESTR_RUN_CAT) {
            System.out.println("The cat " + this.name + " runs " + length + " meters");
            return true;
        }
        else {
            System.out.println("Cats can't run the distance more then " + RESTR_RUN_CAT + " meters");
            return false;
        }
    }

    @Override
    public boolean isSwim(int length) {
        System.out.println("Cats can't swim");
        return false;
    }
}
