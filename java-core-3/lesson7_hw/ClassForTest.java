package ru.geekbrains.java3.lesson7_hw;


public class ClassForTest {
    @Test(priority = 2)
    public void testMethod1() {
        System.out.println("Test method priority is 2");
    }

    @Test(priority = 1)
    public void testMethod2() {
        System.out.println("Test method priority is 1");
    }

    @Test(priority = 4)
    public void testMethod3() {
        System.out.println("Test method priority is 4");
    }

    @Test(priority = 3)
    public void testMethod4() {
        System.out.println("Test method priority is 3");
    }

    @AfterSuite
    public void afterSuiteMethod() {
        System.out.println("AfterSuite method");
    }

    @BeforeSuite
    public void beforeSuiteMethod() {
        System.out.println("BeforeSuite method");
    }

    /*@BeforeSuite
    public void beforeSuiteMethod2() {
        System.out.println("BeforeSuite method");
    }*/
}
