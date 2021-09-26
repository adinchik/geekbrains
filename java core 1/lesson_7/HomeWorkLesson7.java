package ru.geekbrains.java1.lesson7_hw;

public class HomeWorkLesson7 {
    public static void main(String[] args) {
        Cat cats[] = new Cat[3];
        Plate plate = new Plate(100);
        cats[0] = new Cat("Barsik", 50);
        cats[1] = new Cat("Philip", 40);
        cats[2] = new Cat("Persik", 90);
        plate.info();
        for (Cat cat: cats) {
            System.out.println(cat.getName() + " tries to eat from the plate");
            cat.eat(plate);
            if (cat.getSatiety())
                System.out.println(cat.getName() + " is full. The number of food in the plate is " + plate.getFood());
            else
                System.out.println(cat.getName() + " want's to eat. The number of food in the plate is " + plate.getFood());
        }
    }
}
