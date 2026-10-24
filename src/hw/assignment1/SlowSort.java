package hw.assignment1;

import java.io.*;
import java.util.StringTokenizer;

public class SlowSort {

    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner();

        int n = fs.nextInt();
        int[] a = new int[n];

        for (int i = 0; i < n; i++) {
            a[i] = fs.nextInt();
        }

        bubbleSort(a);

        PrintWriter out = new PrintWriter(System.out);
        for (int i = 0; i < n; i++) {
            if (i > 0) out.print(' ');
            out.print(a[i]);
        }
        out.println();
        out.flush();
    }

    // Сортировка пузырьком — O(N^2)
    private static void bubbleSort(int[] a) {
        int n = a.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (a[j] > a[j + 1]) {
                    int tmp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = tmp;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    // Быстрый ввод
    private static class FastScanner {
        private final BufferedReader br;
        private StringTokenizer st;

        FastScanner() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (st == null || !st.hasMoreTokens()) {
                String line = br.readLine();
                if (line == null) {
                    return null;
                }
                st = new StringTokenizer(line);
            }
            return st.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}