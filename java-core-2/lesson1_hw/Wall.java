package ru.geekbrains.java2.lesson1_hw;

public class Wall extends Obstacle{
    private int height;

    public Wall(){}

    public Wall(int height) {
        this.height = height;
    }

    @Override
    public void info() {
        System.out.println("Obstacle is a wall with " + this.getHeight() + " centimeters");
    }

    @Override
    public boolean goPerson(Person p) {
        if (p.getLimitJumpingHeight() < this.height) {
            System.out.println("Person " + p.getName() + " can't jump " + this.getHeight() + " centimeters, drops out of the competition");
            return false;
        }
        else {
            System.out.println("Person " + p.getName() + " successfully cope with this obstacle");
            return true;
        }
    }

    @Override
    public boolean goRobot(Robot r) {
        if (r.getLimitJumpingHeight() < this.height) {
            System.out.println("Robot " + r.getName() + " can't jump " + this.getHeight() + " centimeters, drops out of the competition");
            return false;
        }
        else {
            System.out.println("Robot " + r.getName() + " successfully cope with this obstacle");
            return true;
        }
    }

    @Override
    public boolean goCat(Cat c) {
        if (c.getLimitJumpingHeight() < this.height) {
            System.out.println("Cat " + c.getName() + "can't jump " + this.getHeight() + " centimeters");
            return false;
        }
        else {
            System.out.println("Cat " + c.getName() + " successfully cope with this obstacle");
            return true;
        }
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
