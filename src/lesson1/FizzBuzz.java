package lesson1;

public class FizzBuzz {
    public static void main(String[] args) {
        // Классическая задача: числа от 1 до 100
        // Кратные 3 → Fizz, кратные 5 → Buzz, кратные и 3 и 5 → FizzBuzz
        for (int i = 1; i <= 100; i++) {
            // Порядок важен: сначала проверяем кратность 15 (3*5),
            // иначе число попадёт в первый подходящий if
            if (i % 15 == 0) {
                System.out.println("FizzBuzz");
            } else if (i % 3 == 0) {
                System.out.println("Fizz");
            } else if (i % 5 == 0) {
                System.out.println("Buzz");
            } else {
                System.out.println(i);
            }
        }
    }
}