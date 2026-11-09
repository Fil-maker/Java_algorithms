package exam.topic1.task1_merge_sort;

import java.io.*;
import java.util.*;

public class MergeSort {

    /** Сортирует полуинтервал [left, right). */
    static void mergeSort(int[] a, int[] buffer, int left, int right) {
        if (right - left <= 1) return;

        int mid = (left + right) / 2;
        mergeSort(a, buffer, left, mid);
        mergeSort(a, buffer, mid, right);
        merge(a, buffer, left, mid, right);
    }

    /** Сливает отсортированные части [left, mid) и [mid, right). */
    static void merge(int[] a, int[] buffer, int left, int mid, int right) {
        int i = left, j = mid, k = left;
        while (i < mid && j < right) {
            if (a[i] <= a[j]) buffer[k++] = a[i++];
            else buffer[k++] = a[j++];
        }
        while (i < mid) buffer[k++] = a[i++];
        while (j < right) buffer[k++] = a[j++];

        for (k = left; k < right; k++) a[k] = buffer[k];
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine().trim());
        StringTokenizer st = new StringTokenizer(br.readLine());

        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = Integer.parseInt(st.nextToken());

        mergeSort(a, new int[n], 0, n);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (i > 0) sb.append(' ');
            sb.append(a[i]);
        }
        System.out.println(sb);
    }
}
