package ru.geekbrains.java2.lesson1_hw;

public class Cat implements Runable, Jumpable{

    private String name;
    private int limitRunningLength;
    private int limitJumpingHeight;

    public Cat() {}

    public Cat(String name, int limitRunningLength, int limitJumpingHeight) {
        this.limitRunningLength = limitRunningLength;
        this.limitJumpingHeight = limitJumpingHeight;
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("Cat is running");
    }

    @Override
    public void jump() {
        System.out.println("Cat is jumping");
    }

    public int getLimitJumpingHeight() {
        return limitJumpingHeight;
    }

    public int getLimitRunningLength() {
        return limitRunningLength;
    }

    public void setLimitJumpingHeight(int limitJumpingHeight) {
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
