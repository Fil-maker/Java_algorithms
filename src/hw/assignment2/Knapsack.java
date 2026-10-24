package hw.assignment2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Задача о рюкзаке 0/1.
 *
 * Есть n предметов, у каждого вес w[i] и ценность v[i].
 * Рюкзак выдерживает вес W. Каждый предмет можно взять не более одного раза.
 * Нужно максимизировать суммарную ценность.
 *
 * Состояние: dp[i][c] — максимальная ценность, используя первые i предметов
 *            при вместимости c.
 * Переход:   dp[i][c] = max( dp[i-1][c],                         — не берём i-й
 *                            dp[i-1][c - w[i]] + v[i] )          — берём i-й (если влезает)
 * База:      dp[0][c] = 0 — нет предметов, нет ценности.
 * Ответ:     dp[n][W].
 *
 * Время O(n·W), память O(n·W) (или O(W) в одномерной версии).
 */
public class Knapsack {

    static String[] names = {"Гитара", "Магнитофон", "Ноутбук", "Айфон", "Книга"};
    static int[] weights  = {1,        4,            3,         1,       2};
    static int[] values   = {1500,     3000,         2000,      2000,    500};

    // ---------- Перебор всех подмножеств (для сравнения): O(2^n) ----------
    static int bruteForce(int[] w, int[] v, int i, int cap) {
        if (i == w.length) return 0;
        int skip = bruteForce(w, v, i + 1, cap);
        if (w[i] > cap) return skip;
        int take = v[i] + bruteForce(w, v, i + 1, cap - w[i]);
        return Math.max(skip, take);
    }

    // ---------- Двумерная таблица + восстановление ответа ----------
    static int[][] buildTable(int[] w, int[] v, int W) {
        int n = w.length;
        int[][] dp = new int[n + 1][W + 1];
        for (int i = 1; i <= n; i++) {
            for (int c = 0; c <= W; c++) {
                dp[i][c] = dp[i - 1][c];                        // не берём предмет i
                if (w[i - 1] <= c) {
                    int take = dp[i - 1][c - w[i - 1]] + v[i - 1]; // берём
                    if (take > dp[i][c]) dp[i][c] = take;
                }
            }
        }
        return dp;
    }

    // Идём от dp[n][W] назад: если значение изменилось по сравнению со строкой выше,
    // значит предмет был взят.
    static List<Integer> restore(int[][] dp, int[] w, int W) {
        List<Integer> taken = new ArrayList<>();
        int c = W;
        for (int i = w.length; i >= 1; i--) {
            if (dp[i][c] != dp[i - 1][c]) {
                taken.add(i - 1);
                c -= w[i - 1];
            }
        }
        Collections.reverse(taken);
        return taken;
    }

    // ---------- Одномерная версия: O(W) памяти ----------
    // Идём по c в обратном порядке, чтобы dp[c - w] ещё относилось к «предыдущей строке»
    // (иначе один предмет мог бы попасть в рюкзак несколько раз).
    static int knapsack1D(int[] w, int[] v, int W) {
        int[] dp = new int[W + 1];
        for (int i = 0; i < w.length; i++) {
            for (int c = W; c >= w[i]; c--) {
                dp[c] = Math.max(dp[c], dp[c - w[i]] + v[i]);
            }
        }
        return dp[W];
    }

    static void printTable(int[][] dp, int W) {
        System.out.printf("%-12s", "");
        for (int c = 0; c <= W; c++) System.out.printf("%6d", c);
        System.out.println();
        for (int i = 0; i < dp.length; i++) {
            String label = i == 0 ? "—" : names[i - 1];
            System.out.printf("%-12s", label);
            for (int c = 0; c <= W; c++) System.out.printf("%6d", dp[i][c]);
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int W = 5;

        System.out.println("Предметы:");
        for (int i = 0; i < names.length; i++) {
            System.out.printf("  %-11s вес=%d  ценность=%d%n", names[i], weights[i], values[i]);
        }
        System.out.println("Вместимость рюкзака: " + W);
        System.out.println();

        int[][] dp = buildTable(weights, values, W);
        System.out.println("Таблица dp[i][c] (строка — предметы, столбец — вместимость):");
        printTable(dp, W);
        System.out.println();

        List<Integer> taken = restore(dp, weights, W);
        int totalW = 0;
        System.out.println("Взяты предметы:");
        for (int i : taken) {
            System.out.printf("  %s (вес %d, ценность %d)%n", names[i], weights[i], values[i]);
            totalW += weights[i];
        }
        System.out.println("Итого: вес " + totalW + ", ценность " + dp[weights.length][W]);
        System.out.println();

        System.out.println("Проверка:");
        System.out.println("  2D DP       = " + dp[weights.length][W]);
        System.out.println("  1D DP       = " + knapsack1D(weights, values, W));
        System.out.println("  Перебор 2^n = " + bruteForce(weights, values, 0, W));
    }
}
