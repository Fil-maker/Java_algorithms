package lesson1;

import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Читаем два числа
        System.out.print("Введите первое число: ");
        double a = scanner.nextDouble();

        System.out.print("Введите оператор (+, -, *, /): ");
        // next() берёт следующий токен как строку; charAt(0) даёт первый символ
        char op = scanner.next().charAt(0);

        System.out.print("Введите второе число: ");
        double b = scanner.nextDouble();

        double result;

        // switch по символу
        switch (op) {
            case '+':
                result = a + b;
                break;      // без break выполнение "провалится" дальше
            case '-':
                result = a - b;
                break;
            case '*':
                result = a * b;
                break;
            case '/':
                // Проверка деления на ноль
                if (b == 0) {
                    System.out.println("На ноль делить нельзя!");
                    scanner.close();
                    return;  // выход из метода
                }
                result = a / b;
                break;
            default:
                // Сюда попадаем, если оператор не распознан
                System.out.println("Неизвестный оператор: " + op);
                scanner.close();
                return;
        }

        System.out.printf("Результат: %.2f %c %.2f = %.2f%n", a, op, b, result);

        scanner.close();  // закрываем Scanner, чтобы освободить ресурс
    }
}