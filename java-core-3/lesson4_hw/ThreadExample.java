package ru.geekbrains.java3.lesson4_hw;

public class ThreadExample {

    public static final Object mon = new Object();
    public static int f = 0;

    public void printA() {
        synchronized (mon) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (f != 0)
                        mon.wait();
                    System.out.print("A");
                    f = 1;
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void printB() {
        synchronized (mon) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (f != 1)
                        mon.wait();
                    System.out.print("B");
                    f = 2;
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void printC() {
        synchronized (mon) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (f != 2)
                        mon.wait();
                    System.out.print("C");
                    f = 0;
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        ThreadExample objThreadExample = new ThreadExample();
        Thread t1 = new Thread(() -> {
            objThreadExample.printA();
        });
        Thread t2 = new Thread(() -> {
            objThreadExample.printB();
        });
        Thread t3 = new Thread(() -> {
            objThreadExample.printC();
        });
        t1.start();
        t2.start();
        t3.start();
    }



}
