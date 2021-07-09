package ru.geekbrains.java1.lesson7_hw;

public class Plate {
    private int food;
    public Plate(int food) {
        this.food = food;
    }

    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public boolean decreaseFood(int n) {
        if (food - n < 0) {
            System.out.println("The food in the plate can't be negative");
            return false;
        }
        else {
            food -= n;
            return true;
        }
    }
    public void info() {
        System.out.println("plate: " + food);
    }

}
