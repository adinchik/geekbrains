package ru.geekbrains.java3.lesson5_hw;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainClass {
    public static final int CARS_COUNT = 4;
    public static CyclicBarrier barrier = new CyclicBarrier(CARS_COUNT + 1);
    public static Semaphore semaphore = new Semaphore(CARS_COUNT / 2, true);
    public static CountDownLatch cdl = new CountDownLatch(CARS_COUNT);

    public static void main(String[] args) {


        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10));
        }
        for (int i = 0; i < cars.length; i++) {

            new Thread(cars[i]).start();
        }

        try {
            while (barrier.getNumberWaiting() < CARS_COUNT) {
                Thread.sleep(100);
            }
            barrier.await();
            while (barrier.getNumberWaiting() < CARS_COUNT) {
                Thread.sleep(100);
            }
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
            barrier.await();
            cdl.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}
