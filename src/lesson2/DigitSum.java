package lesson2;

public class DigitSum {

    public static int sumOfDigits(int n) {
        // База: однозначное число (включая 0)
        if (n < 10) {
            return n;
        }
        // Рекурсивный шаг: последняя цифра + сумма цифр оставшейся части
        return (n % 10) + sumOfDigits(n / 10);
    }

    public static void main(String[] args) {
        System.out.println(sumOfDigits(0));      // 0
        System.out.println(sumOfDigits(5));      // 5
        System.out.printf("%d %d %n", 123, sumOfDigits(123));
        System.out.printf("%d %d %n", 9876, sumOfDigits(9876));
        System.out.printf("%d %d %n", 1000000, sumOfDigits(1000000));
    }
}