package lesson2;

public class Subsets {

    // a       — исходный массив
    // current — буфер для текущего подмножества
    // size    — сколько элементов уже накоплено в current
    // index   — какой элемент массива сейчас рассматриваем
    private static void generate(int[] a, int[] current, int size, int index) {
        // База: все элементы рассмотрены — выводим текущее подмножество
        if (index == a.length) {
            System.out.print("{ ");
            for (int i = 0; i < size; i++) {
                System.out.print(current[i] + " ");
            }
            System.out.println("}");
            return;
        }

        // Вариант 1: НЕ включаем a[index]
        generate(a, current, size, index + 1);

        // Вариант 2: включаем a[index]
        current[size] = a[index];
        generate(a, current, size + 1, index + 1);
        // откат не обязателен: следующий вызов всё равно перезапишет current[size]
    }

    public static void main(String[] args) {
        int[] a = {1, 2, 3};
        generate(a, new int[a.length], 0, 0);
    }
}