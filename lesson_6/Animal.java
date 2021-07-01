package ru.geekbrains.java1.lesson6_hw;

public abstract class Animal {
    String name;

    public Animal() {}

    public Animal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract boolean isRun(int length);
    public abstract boolean isSwim(int length);
}
