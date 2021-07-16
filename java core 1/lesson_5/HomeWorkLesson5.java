package ru.geekbrains.java1.lesson5_hw;

public class HomeWorkLesson5 {
    public static void main(String[] args) {
        Employee[] employees = new Employee[5];
        employees[0] = new Employee("Ivanov Ivan", "Engineer", "ivivan@mailbox.com", "892312312", 30000, 30);
        employees[1] = new Employee("Il'in Il'ya", "Engineer", "ilya@mailbox.com", "892312313", 40000, 40);
        employees[2] = new Employee("Petrov Petr", "Engineer", "petr@mailbox.com", "892312314", 50000, 35);
        employees[3] = new Employee("Nikolaev Nikolay", "Engineer", "nikolay@mailbox.com", "892312315", 60000, 45);
        employees[4] = new Employee("Egorov Egor", "Engineer", "egor@mailbox.com", "892312316", 70000, 50);
        for (Employee employee:employees
             ) {
            if (employee.getAge() > 40)
                employee.showInfo();
        }
    }
}
