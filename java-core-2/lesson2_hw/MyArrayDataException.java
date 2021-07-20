package ru.geekbrains.java2.lesson2_hw;

public class MyArrayDataException extends Exception {
    private int i;
    private int j;

    public MyArrayDataException (String message, int i, int j) {
        super(message);
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public void info() {

    }
}
