package lesson1;

import java.util.Random;
import java.util.Scanner;

public class GuessNumber {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        // Случайное число от 1 до 100 включительно
        int secret = random.nextInt(100) + 1;
        int attempts = 0;
        int guess;

        System.out.println("Я загадал число от 1 до 100. Угадай!");

        // Цикл while выполняется, пока условие истинно
        do {
            System.out.print("Твой вариант: ");
            guess = scanner.nextInt();
            attempts++;

            if (guess < secret) {
                System.out.println("Больше!");
            } else if (guess > secret) {
                System.out.println("Меньше!");
            } else {
                System.out.println("Угадал за " + attempts + " попыток!");
            }
        } while (guess != secret);  // продолжаем, пока не угадал

        scanner.close();
    }
}