package lesson2;

public class Signal {

    // n — номер участка (n >= 0)
    // first — уровень на нулевом участке
    // difference — постоянное изменение на каждом следующем участке
    public static int signal(int n, int first, int difference) {
        // База: нулевой участок
        if (n == 0) {
            return first;
        }
        // Рекурсивный шаг: уровень на предыдущем участке + difference
        return signal(n - 1, first, difference) + difference;
    }

    public static void main(String[] args) {
        System.out.println(signal(0, 10, 3)); // 10
        System.out.println(signal(1, 10, 3)); // 13
        System.out.println(signal(2, 10, 3)); // 16
        System.out.println(signal(4, 10, 3)); // 22
        System.out.println(signal(5, -2, 4)); // 18
    }
}