package lesson1;

public class TypesDemo {
    public static void main(String[] args) {
        // Примитивные типы данных
        byte b = 100;              // от -128 до 127
        short s = 30000;           // от -32768 до 32767
        int i = 2_000_000;         // целое число (по умолчанию)
        long l = 9_000_000_000L;   // длинное целое (суффикс L)
        float f = 3.14f;           // 4 байта (суффикс f)
        double d = 3.14159;        // 8 байт (по умолчанию для дробных)
        char c = 'A';              // один символ в одинарных кавычках
        boolean flag = true;       // true / false

        // Ссылочный тип
        String name = "Java";      // строка

        // Арифметические операторы
        int sum = i + b;
        int diff = i - b;
        int mult = i * 2;
        int div = i / 3;           // целочисленное деление
        double div_d = i / 3.0;
        int mod = i % 7;           // остаток от деления

        // Операторы сравнения: > < >= <= == != (возвращают boolean)
        boolean isBigger = i > b;

        // Логические операторы: && || !
        boolean logic = isBigger && flag;

        // Инкремент/декремент
        i++;  // i = i + 1
        b--;  // b = b - 1

        // Приведение типов
        double fromInt = i;              // автоматически (расширение)
        int fromDouble = (int) 3.99;     // вручную (сужение) → 3

        System.out.println("div=" + div + ", div_d=" + div_d + ", name=" + name);
    }
}