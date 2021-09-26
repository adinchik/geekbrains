package ru.geekbrains.java2.lesson2_hw;

public class HomeWorkLesson2 {

    public static final int SIZE = 2;

    public static int goArray(String arr[][]) throws MyArraySizeException, MyArrayDataException {
        if (arr.length != SIZE || arr[0].length != SIZE) throw new MyArraySizeException(new String("Size is not equal to " + SIZE));
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                int num = 0;
                for (int k = 0; k < arr[i][j].length(); k++) {
                    if (!(arr[i][j].charAt(k) >= '0' && arr[i][j].charAt(k) <= '9'))
                        throw new MyArrayDataException("Data is not a number", i, j);
                    num = num * 10 + (arr[i][j].charAt(k) - '0');
                }
                sum += num;
            }
        }
        return sum;
    }

    public static void main(String[] args) {

//        String arr[][] = new String[1][1];
//        arr[0][0] = new String("00000");

        String arr[][] = new String[SIZE][SIZE];
        arr[0][0] = new String("00000");
        arr[0][1] = new String("125f");
        arr[1][0] = new String("2");
        arr[1][1] = new String("55658");

        try {
            int sum = goArray(arr);
            System.out.println(sum);
        } catch (MyArraySizeException e){
            System.err.println(e.getMessage());
        } catch (MyArrayDataException e) {
            System.err.println(e.getMessage());
            System.err.println("The problem happened in " + e.getI() + " row and " + e.getJ() + " column");
        }
    }
}
