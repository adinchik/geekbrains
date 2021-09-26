package ru.geekbrains.java2.lesson1_hw;

public class HomeWork1 {
    public static void main(String[] args) {
        Person persons[] = new Person[2];
        persons[0] = new Person("Anna", 5, 40);
        persons[1] = new Person("Dima", 10, 100);
        Robot robots[] = new Robot[2];
        robots[0] = new Robot("Alfa", 100, 1000);
        robots[1] = new Robot("KazRobo", 200, 600);
        Cat cats[] = new Cat[2];
        cats[0] = new Cat("Persik", 20, 80);
        cats[1] = new Cat("Karol", 25, 55);
        Obstacle obstacles[] = new Obstacle[5];
        obstacles[0] = new RunningTrack(2);
        obstacles[1] = new Wall(30);
        obstacles[2] = new RunningTrack(23);
        obstacles[3] = new Wall(50);
        obstacles[4] = new RunningTrack(600);

        for (Person person: persons) {
            for (Obstacle obstacle: obstacles) {
                obstacle.info();
                if (!obstacle.goPerson(person)) {
                    break;
                }
            }
        }

        System.out.println();

        for (Robot robot: robots) {
            for (Obstacle obstacle: obstacles) {
                obstacle.info();
                if (!obstacle.goRobot(robot)) {
                    break;
                }
            }
        }

        System.out.println();

        for (Cat cat: cats) {
            for (Obstacle obstacle: obstacles) {
                obstacle.info();
                if (!obstacle.goCat(cat)) {
                    break;
                }
            }
        }
    }
}
