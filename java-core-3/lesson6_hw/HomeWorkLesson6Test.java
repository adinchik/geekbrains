package ru.geekbrains.java3.lesson6_hw;

import jdk.swing.interop.SwingInterOpUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

class HomeWorkLesson6Test {

    private HomeWorkLesson6 objHomeWorkLesson6;

    @BeforeEach
    public void init() {
        objHomeWorkLesson6 = new HomeWorkLesson6();
    }

    @ParameterizedTest
    @CsvSource({"1 2 3 4 5; 5",
                "1 2 4; ",
                "1 2 4 5 4 6 3 5; 6 3 5"})
    void test(@ConvertWith(StringArrayConverter.class) String[] strings) {
        int[] result = new int[0];
        if (strings.length > 1)
            result = Arrays.stream(strings[1].split(" ")).mapToInt(Integer::parseInt).toArray();
        int array[] = Arrays.stream(strings[0].split(" ")).mapToInt(Integer::parseInt).toArray();
        Assertions.assertArrayEquals(result, objHomeWorkLesson6.extractArrayAfter4(array));
    }

    @Test
    void testError() {
        Assertions.assertThrows(RuntimeException.class, () -> objHomeWorkLesson6.extractArrayAfter4(new int[]{1, 2, 3}));
    }

    @ParameterizedTest
    @MethodSource("dataForArrayOperations")
    void isExist1or4(int[] array) {
        Assertions.assertTrue(objHomeWorkLesson6.isExist1or4(array));
    }


    public static Stream<Arguments> dataForArrayOperations() {
        List<Arguments> out = new ArrayList<>();
        out.add(Arguments.arguments(new int[] {1, 2, 3, 4, 5}));
        out.add(Arguments.arguments(new int[] {4, 5, 6}));
        out.add(Arguments.arguments(new int[] {1, 2, 3, 5}));
        return out.stream();

    }
}