package hw.assignment1;

import java.io.*;
import java.util.StringTokenizer;

public class FastSort {

    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner();

        int n = fs.nextInt();
        int[] a = new int[n];

        for (int i = 0; i < n; i++) {
            a[i] = fs.nextInt();
        }

        int[] temp = new int[n];
        mergeSort(a, temp, 0, n - 1);

        PrintWriter out = new PrintWriter(System.out);
        for (int i = 0; i < n; i++) {
            if (i > 0) out.print(' ');
            out.print(a[i]);
        }
        out.println();
        out.flush();
    }

    // Сортировка слиянием
    private static void mergeSort(int[] a, int[] temp, int left, int right) {
        if (left >= right) {
            return;
        }

        int mid = (left + right) >>> 1;

        mergeSort(a, temp, left, mid);
        mergeSort(a, temp, mid + 1, right);

        int i = left;
        int j = mid + 1;
        int k = left;

        while (i <= mid && j <= right) {
            if (a[i] <= a[j]) {
                temp[k++] = a[i++];
            } else {
                temp[k++] = a[j++];
            }
        }

        while (i <= mid) {
            temp[k++] = a[i++];
        }

        while (j <= right) {
            temp[k++] = a[j++];
        }

        for (i = left; i <= right; i++) {
            a[i] = temp[i];
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