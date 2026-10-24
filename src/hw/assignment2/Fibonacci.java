package hw.assignment2;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Числа Фибоначчи: наивная рекурсия, мемоизация (top-down),
 * табуляция (bottom-up) и табуляция с O(1) памяти.
 *
 * Для каждого подхода считаем количество вызовов / шагов и время,
 * чтобы наглядно увидеть разницу в затратах.
 *
 *   Подход              Время      Память
 *   ------------------  ---------  ---------------------------
 *   Наивная рекурсия    O(φ^n)     O(n)  — глубина стека
 *   Мемоизация          O(n)       O(n)  — кэш + стек рекурсии
 *   Табуляция           O(n)       O(n)  — таблица dp
 *   Табуляция (2 перем) O(n)       O(1)
 *
 * φ ≈ 1.618 — золотое сечение.
 */
public class Fibonacci {

    // Счётчики для анализа затрат
    static long naiveCalls = 0;
    static long memoCalls = 0;
    static long tabSteps = 0;

    // ---------- 1. Наивная рекурсия ----------
    // Одни и те же подзадачи решаются заново много раз:
    // fib(5) -> fib(4) + fib(3), fib(4) -> fib(3) + fib(2) ... fib(3) считается дважды и т.д.
    static long fibNaive(int n) {
        naiveCalls++;
        if (n <= 1) return n;
        return fibNaive(n - 1) + fibNaive(n - 2);
    }

    // ---------- 2. Мемоизация (сверху вниз) ----------
    // Та же рекурсия, но результат каждой подзадачи запоминается в memo.
    // Каждое fib(i) вычисляется ровно один раз.
    static long fibMemo(int n, long[] memo) {
        memoCalls++;
        if (n <= 1) return n;
        if (memo[n] != -1) return memo[n];          // уже считали — берём из кэша
        memo[n] = fibMemo(n - 1, memo) + fibMemo(n - 2, memo);
        return memo[n];
    }

    static long fibMemo(int n) {
        long[] memo = new long[n + 1];
        Arrays.fill(memo, -1);
        return fibMemo(n, memo);
    }

    // ---------- 3. Табуляция (снизу вверх) ----------
    // Заполняем таблицу от маленьких подзадач к большим, без рекурсии.
    static long fibTab(int n) {
        if (n <= 1) return n;
        long[] dp = new long[n + 1];
        dp[0] = 0;
        dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            tabSteps++;
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }

    // ---------- 4. Табуляция с O(1) памяти ----------
    // Для dp[i] нужны только два предыдущих значения — храним только их.
    static long fibTabO1(int n) {
        if (n <= 1) return n;
        long prev = 0, cur = 1;
        for (int i = 2; i <= n; i++) {
            long next = prev + cur;
            prev = cur;
            cur = next;
        }
        return cur;
    }

    // long переполняется после fib(92), для больших n используем BigInteger
    static BigInteger fibBig(int n) {
        BigInteger prev = BigInteger.ZERO, cur = BigInteger.ONE;
        if (n == 0) return prev;
        for (int i = 2; i <= n; i++) {
            BigInteger next = prev.add(cur);
            prev = cur;
            cur = next;
        }
        return cur;
    }

    public static void main(String[] args) {
        System.out.println("=== Сравнение затрат ===");
        System.out.printf("%4s | %22s | %14s | %12s | %10s | %10s%n",
                "n", "fib(n)", "naive вызовов", "naive, мс", "memo выз.", "tab шагов");
        System.out.println("-----+------------------------+----------------+--------------+------------+-----------");

        int[] ns = {10, 20, 30, 35, 40};
        for (int n : ns) {
            naiveCalls = 0;
            memoCalls = 0;
            tabSteps = 0;

            long t0 = System.nanoTime();
            long a = fibNaive(n);
            long naiveMs = (System.nanoTime() - t0) / 1_000_000;

            long b = fibMemo(n);
            long c = fibTab(n);
            long d = fibTabO1(n);

            if (a != b || b != c || c != d) throw new AssertionError("Результаты не совпадают для n=" + n);

            System.out.printf("%4d | %22d | %14d | %12d | %10d | %10d%n",
                    n, a, naiveCalls, naiveMs, memoCalls, tabSteps);
        }

        System.out.println();
        System.out.println("Наивная рекурсия: число вызовов растёт как ~1.618^n (каждые +5 к n — примерно x11).");
        System.out.println("Мемоизация: 2n-1 вызов. Табуляция: n-1 итерация цикла.");

        // Быстрые методы спокойно работают для больших n
        System.out.println();
        System.out.println("=== Быстрые методы на больших n ===");
        int big = 90;
        long t0 = System.nanoTime();
        long memoRes = fibMemo(big);
        long memoNs = System.nanoTime() - t0;
        t0 = System.nanoTime();
        long tabRes = fibTab(big);
        long tabNs = System.nanoTime() - t0;
        t0 = System.nanoTime();
        long o1Res = fibTabO1(big);
        long o1Ns = System.nanoTime() - t0;
        System.out.printf("fib(%d): memo=%d (%d нс), tab=%d (%d нс), O(1)=%d (%d нс)%n",
                big, memoRes, memoNs, tabRes, tabNs, o1Res, o1Ns);
        System.out.println("fib(93) уже не помещается в long: " + fibTabO1(93) + " (переполнение!)");
        System.out.println("fib(200) через BigInteger = " + fibBig(200));

        // Ограничение мемоизации: глубина рекурсии
        System.out.println();
        System.out.println("=== Ограничение мемоизации: стек рекурсии ===");
        try {
            fibMemo(100_000);
            System.out.println("fibMemo(100000) — выполнилось");
        } catch (StackOverflowError e) {
            System.out.println("fibMemo(100000) -> StackOverflowError: рекурсия слишком глубокая.");
            System.out.println("Табуляция этой проблемы не имеет — она итеративная.");
        }
    }
}
