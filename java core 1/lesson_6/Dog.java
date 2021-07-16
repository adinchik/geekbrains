package ru.geekbrains.java1.lesson6_hw;

public class Dog extends Animal{
    private final int RESTR_RUN_DOG = 500;
    private final int RESTR_SW_DOG = 10;

    public Dog() {}

    public Dog(String name) {
        this.name = name;
    }

    @Override
    public boolean isRun(int length) {
        if (length <= RESTR_RUN_DOG) {
            System.out.println("The dog " + this.name + " runs " + length + " meters");
            return true;
        }
        else {
            System.out.println("Dogs can't run the distance more then " + RESTR_RUN_DOG + " meters");
            return false;
        }
    }

    @Override
    public boolean isSwim(int length) {
        if (length <= RESTR_SW_DOG) {
            System.out.println("The dog " + this.name + " swims " + length + " meters");
            return true;
        }
        else {
            System.out.println("Dogs can't swim the distance more then " + RESTR_SW_DOG + " meters");
            return false;
        }
    }
}
