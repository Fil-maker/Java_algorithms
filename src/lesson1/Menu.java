package lesson1;

import java.util.Scanner;

public class Menu {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        // Бесконечный цикл, выход — через break
        while (running) {
            System.out.println("\n=== Меню ===");
            System.out.println("1 - Сложить два числа");
            System.out.println("2 - Возвести в квадрат");
            System.out.println("3 - Чётное/нечётное");
            System.out.println("0 - Выход");
            System.out.print("Выбор: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1: {
                    System.out.print("a = ");
                    String a = scanner.next();
                    int n;
                    try {
                        n = Integer.parseInt(a);
                    } catch (NumberFormatException e){
                        System.out.println("Haha 67");
                        return;
                    }
                    System.out.print("b = ");
                    int b = scanner.nextInt();
                    System.out.println("Сумма = " + (n + b));
                    break;
                }
                case 2: {
                    System.out.print("n = ");
                    int n = scanner.nextInt();
                    System.out.println("Квадрат = " + (n * n));
                    break;
                }
                case 3: {
                    System.out.print("n = ");
                    int n = scanner.nextInt();
                    // Тернарный оператор: условие ? если true : если false
                    System.out.println(n % 2 == 0 ? "Чётное" : "Нечётное");
                    break;
                }
                case 0:
                    running = false;   // завершим цикл
                    System.out.println("Пока!");
                    break;
                default:
                    System.out.println("Нет такого пункта.");
            }
        }

        scanner.close();
    }
}