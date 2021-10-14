package ru.geekbrains.java3.lesson6_hw;

public class HomeWorkLesson6 {

    public int[] extractArrayAfter4(int[] array) {
        int pos = -1;
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] == 4) {
                pos = i;
                break;
            }
        }
        int[] arrayResult = new int[array.length - pos - 1];
        if (pos == -1) throw new RuntimeException();
        else {
            int j = 0;
            for (int i = pos + 1; i < array.length; i++) {
                arrayResult[j] = array[i];
                j++;
            }
            return arrayResult;
        }
    }

    public boolean isExist1or4(int[] array) {
        int cnt1 = 0, cnt4 = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == 1) cnt1++;
            if (array[i] == 4) cnt4++;
        }
        if (cnt1 > 0 || cnt4 > 0) return true;
        else return false;
    }
}
