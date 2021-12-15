package ru.geekbrains.java3.lesson7_hw;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

public class HomeWork7 {

    public static boolean isAnnotationAppearedOnce(Method[] methods, Class c) {
        int cnt = 0;
        for (Method method: methods) {
            if (method.isAnnotationPresent(c)) {
                cnt++;
            }
        }
        if (cnt == 1) return true;
        else return false;
    }

    public static void start(Class testClass) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        Constructor constructor = testClass.getConstructor();
        Object obj = constructor.newInstance();
        Method[] methods = testClass.getDeclaredMethods();
        TreeMap<Integer, Method> treeMap = new TreeMap<>();
        boolean f;

        f = isAnnotationAppearedOnce(methods, BeforeSuite.class);
        if (!f) throw new RuntimeException("BeforeSuite appears more than one!");

        f = isAnnotationAppearedOnce(methods, AfterSuite.class);
        if (!f) throw new RuntimeException("AfterSuite appears more than one!");

        for (Method method: methods) {
            method.setAccessible(true);
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                method.invoke(obj);
                System.out.println(method.getName() + " is invoked");
            }
        }

        for (Method method: methods) {
            method.setAccessible(true);
            if (method.isAnnotationPresent(Test.class)) {
                treeMap.put(method.getAnnotation(Test.class).priority(), method);
            }
        }

        for(Map.Entry map : treeMap.entrySet()){
            Method method = (Method) map.getValue();
            method.invoke(obj);
            System.out.println(method.getName() + " is invoked");
        }

        for (Method method: methods) {
            method.setAccessible(true);
            if (method.isAnnotationPresent(AfterSuite.class)) {
                method.invoke(obj);
                System.out.println(method.getName() + " is invoked");
            }
        }

    }

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        HomeWork7.start(ClassForTest.class);
    }
}
