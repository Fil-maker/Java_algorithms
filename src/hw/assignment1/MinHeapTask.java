package hw.assignment1;

import java.io.*;
import java.util.*;

public class MinHeapTask {

    /**
     * Класс min-кучи на массиве.
     * Поддерживает операции:
     *   add(x)  — добавление элемента,
     *   peek()  — просмотр минимума,
     *   poll()  — извлечение минимума,
     *   size()  — размер кучи.
     */
    static class MinHeap {
        private long[] data;
        private int size;

        MinHeap(int capacity) {
            data = new long[Math.max(capacity, 1)];
            size = 0;
        }

        int size() {
            return size;
        }

        boolean isEmpty() {
            return size == 0;
        }

        long peek() {
            // Предполагается, что куча непуста (гарантировано условием задачи)
            return data[0];
        }

        void add(long value) {
            if (size == data.length) {
                grow();
            }
            data[size] = value;
            siftUp(size);
            size++;
        }

        long poll() {
            long top = data[0];
            size--;
            if (size > 0) {
                data[0] = data[size];
                siftDown(0);
            }
            return top;
        }

        /** Просеивание вверх: поднимаем элемент, пока он меньше родителя. */
        private void siftUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;
                if (data[i] >= data[parent]) break;
                swap(i, parent);
                i = parent;
            }
        }

        /** Просеивание вниз: опускаем элемент, пока он больше меньшего ребёнка. */
        private void siftDown(int i) {
            while (true) {
                int left = 2 * i + 1;
                int right = 2 * i + 2;
                int smallest = i;

                if (left < size && data[left] < data[smallest]) {
                    smallest = left;
                }
                if (right < size && data[right] < data[smallest]) {
                    smallest = right;
                }
                if (smallest == i) break;
                swap(i, smallest);
                i = smallest;
            }
        }

        private void swap(int i, int j) {
            long tmp = data[i];
            data[i] = data[j];
            data[j] = tmp;
        }

        /** Увеличение внутреннего массива вдвое. */
        private void grow() {
            data = Arrays.copyOf(data, data.length * 2);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        int n = Integer.parseInt(br.readLine().trim());
        MinHeap heap = new MinHeap(n);

        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            String op = st.nextToken();

            switch (op) {
                case "ADD": {
                    long x = Long.parseLong(st.nextToken());
                    heap.add(x);
                    break;
                }
                case "MIN": {
                    sb.append(heap.peek()).append('\n');
                    break;
                }
                case "POP": {
                    sb.append(heap.poll()).append('\n');
                    break;
                }
                case "SIZE": {
                    sb.append(heap.size()).append('\n');
                    break;
                }
                default:
                    // Неизвестная команда — по условию не встречается
                    break;
            }
        }

        System.out.print(sb);
    }
}