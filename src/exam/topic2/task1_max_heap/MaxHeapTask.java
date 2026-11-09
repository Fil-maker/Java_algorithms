package exam.topic2.task1_max_heap;

import java.io.*;
import java.util.*;

public class MaxHeapTask {

    static class MaxHeap {
        private final int[] data;
        private int size = 0;

        MaxHeap(int capacity) {
            data = new int[capacity];
        }

        int max() {
            return data[0];
        }

        void add(int x) {
            data[size] = x;
            siftUp(size);
            size++;
        }

        int pop() {
            int top = data[0];
            size--;
            data[0] = data[size];
            siftDown(0);
            return top;
        }

        /** Поднимаем элемент, пока он больше родителя. */
        private void siftUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;
                if (data[i] <= data[parent]) break;
                swap(i, parent);
                i = parent;
            }
        }

        /** Опускаем элемент, пока он меньше большего из детей. */
        private void siftDown(int i) {
            while (true) {
                int left = 2 * i + 1;
                int right = 2 * i + 2;
                int largest = i;

                if (left < size && data[left] > data[largest]) largest = left;
                if (right < size && data[right] > data[largest]) largest = right;
                if (largest == i) break;

                swap(i, largest);
                i = largest;
            }
        }

        private void swap(int i, int j) {
            int tmp = data[i];
            data[i] = data[j];
            data[j] = tmp;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        int q = Integer.parseInt(br.readLine().trim());
        MaxHeap heap = new MaxHeap(q);

        for (int i = 0; i < q; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            String command = st.nextToken();

            if (command.equals("ADD")) {
                heap.add(Integer.parseInt(st.nextToken()));
            } else if (command.equals("MAX")) {
                sb.append(heap.max()).append('\n');
            } else {
                sb.append(heap.pop()).append('\n');
            }
        }

        System.out.print(sb);
    }
}
