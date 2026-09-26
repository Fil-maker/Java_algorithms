package lesson1;

import java.util.Random;

public class ArrayStats {

    // Метод, возвращающий массив случайных чисел
    // static означает, что метод принадлежит классу, а не объекту
    static int[] generateArray(int size, int bound) {
        Random random = new Random();
        int[] arr = new int[size];      // создаём массив указанной длины
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(bound);  // доступ по индексу
        }
        return arr;                      // возвращаем ссылку на массив
    }

    // Метод находит максимум — пример обхода массива
    static int max(int[] arr) {
        int m = arr[0];                  // предполагаем первый максимальным
        for (int value : arr) {          // for-each: перебор без индексов
            if (value > m) m = value;
        }
        return m;
    }

    static int sum(int[] arr) {
        int total = 0;
        for (int value : arr) {
            total += value;              // составное присваивание
        }
        return total;
    }

    // Метод ничего не возвращает — void
    static void printArray(int[] arr) {
        System.out.print("[ ");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) System.out.print(", ");
        }
        System.out.println(" ]");
    }

    public static void main(String[] args) {
        int[] numbers = generateArray(10, 100);

        printArray(numbers);
        System.out.println("Сумма: " + sum(numbers));
        System.out.println("Максимум: " + max(numbers));
        System.out.println("Среднее: " + (double) sum(numbers) / numbers.length);

        // Двумерный массив — таблица умножения 5x5
        int[][] table = new int[5][5];
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                table[i][j] = (i + 1) * (j + 1);
            }
        }

        System.out.println("Таблица умножения:");
        for (int[] row : table) {          // внешний for-each даёт строку
            for (int cell : row) {         // внутренний — элемент строки
                System.out.printf("%4d", cell);
            }
            System.out.println();
        }
    }
}