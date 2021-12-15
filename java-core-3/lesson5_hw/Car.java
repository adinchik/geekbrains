package ru.geekbrains.java3.lesson5_hw;

import java.util.concurrent.CyclicBarrier;

public class Car implements Runnable {
    private static int CARS_COUNT;
    private Race race;
    private int speed;
    private String name;
    private static String winner = "";
    private static Object mon = new Object();

    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }
    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }
    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            MainClass.barrier.await();
            System.out.println(this.name + " готов");
            MainClass.barrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }

        synchronized (mon) {
            if (winner.isEmpty()) {
                winner = this.name;
                System.out.println(winner + " - WIN");
            }
        }
        MainClass.cdl.countDown();
    }
}
