package ru.geekbrains.java2.lesson1_hw;

public abstract class Obstacle {
    public abstract void info();
    public abstract boolean goPerson(Person p);
    public abstract boolean goRobot(Robot r);
    public abstract boolean goCat(Cat c);
}
