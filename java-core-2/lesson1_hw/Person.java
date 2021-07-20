package ru.geekbrains.java2.lesson1_hw;

public class Person implements Runable, Jumpable {

    private String name;
    private int limitRunningLength;
    private int limitJumpingHeight;

    public Person() {}

    public Person(String name, int limitRunningLength, int limitJumpingHeight) {
        this.limitRunningLength = limitRunningLength;
        this.limitJumpingHeight = limitJumpingHeight;
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("Person is running");
    }
    @Override
    public void jump() {
        System.out.println("Person is jumping");
    }

    public int getLimitJumpingHeight() {
        return limitJumpingHeight;
    }

    public int getLimitRunningLength() {
        return limitRunningLength;
    }

    public void setLimitJumpingHeight(int limitJumpingLength) {
        this.limitJumpingHeight = limitJumpingHeight;
    }

    public void setLimitRunningLength(int limitRunningLength) {
        this.limitRunningLength = limitRunningLength;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
