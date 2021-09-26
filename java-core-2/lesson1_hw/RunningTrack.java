package ru.geekbrains.java2.lesson1_hw;

public class RunningTrack extends Obstacle {
    private int length;

    public RunningTrack() {}

    public RunningTrack(int length) {
        this.length = length;
    }

    @Override
    public void info() {
        System.out.println("Obstacle is a running track with " + this.getLength() + " meters");
    }

    @Override
    public boolean goPerson(Person p) {
        if (p.getLimitRunningLength() < this.length) {
            System.out.println("Person " + p.getName() + " can't run distance with " + this.getLength() + " meters, drops out of the competition");
            return false;
        }
        else {
            System.out.println("Person " + p.getName() + " successfully cope with this obstacle");
            return true;
        }
    }

    @Override
    public boolean goRobot(Robot r) {
        if (r.getLimitRunningLength() < this.length) {
            System.out.println("Robot " + r.getName() + " can't run distance with " + this.getLength() + " meters, drops out of the competition");
            return false;
        }
        else {
            System.out.println("Robot " + r.getName() + " successfully cope with this obstacle");
            return true;
        }
    }

    @Override
    public boolean goCat(Cat c) {
        if (c.getLimitRunningLength() < this.length) {
            System.out.println("Cat " + c.getName() + " can't run distance with " + this.getLength() + " meters, drops out of the competition");
            return false;
        }
        else {
            System.out.println("Cat " + c.getName() + " successfully cope with this obstacle");
            return true;
        }
    }


    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
