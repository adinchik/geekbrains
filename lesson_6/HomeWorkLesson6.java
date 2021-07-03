package ru.geekbrains.java1.lesson6_hw;

public class HomeWorkLesson6 {

    public static void main(String[] args) {
        int numCats = 0, numDogs = 0;

        Obstacle obstacles[] = new Obstacle[3];
        obstacles[0] = new ObstacleRoad(100);
        obstacles[1] = new ObstacleRoad(300);
        obstacles[2] = new ObstacleWater(5);

        Animal animals[] = new Animal[3];
        animals[0] = new Cat("Cat 1");
        animals[1] = new Dog("Dog 1");
        animals[2] = new Dog("Dog 2");

        for (Animal animal:animals) {
            if (animal instanceof Cat)
                numCats++;
            if (animal instanceof Dog)
                numDogs++;
            for (Obstacle obstacle: obstacles) {
                if (obstacle instanceof ObstacleRoad) {
                    System.out.println("Obstacle of running with " + obstacle.getLength() + " meters:");
                    if (!animal.isRun(obstacle.getLength())) {
                        System.out.println(animal.getName() + " drop out from the competition");
                        break;
                     }
                }
                else {
                    System.out.println("Obstacle of swimming with " + obstacle.getLength() + " meters:");
                    if (!animal.isSwim(obstacle.getLength())) {
                        System.out.println(animal.getName() + " drop out from the competition");
                        break;
                    }
                }
            }
            System.out.println();
        }
        System.out.println("The number of cats in the competition was " + numCats);
        System.out.println("The number of dogs in the competition was " + numDogs);
    }
}
