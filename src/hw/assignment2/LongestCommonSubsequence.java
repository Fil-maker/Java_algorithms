package hw.assignment2;

/**
 * Наибольшая общая подпоследовательность (LCS).
 *
 * Подпоследовательность получается вычёркиванием некоторых символов
 * (порядок сохраняется, но символы не обязаны идти подряд).
 * Пример: LCS("ABCBDAB", "BDCABA") = 4, например "BCBA".
 *
 * Состояние: dp[i][j] — длина LCS префиксов a[0..i) и b[0..j).
 * Переход:
 *   если a[i-1] == b[j-1]:  dp[i][j] = dp[i-1][j-1] + 1
 *   иначе:                  dp[i][j] = max(dp[i-1][j], dp[i][j-1])
 * База: dp[0][*] = dp[*][0] = 0.
 *
 * Время O(n·m), память O(n·m).
 */
public class LongestCommonSubsequence {

    static int[][] buildTable(String a, String b) {
        int n = a.length(), m = b.length();
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp;
    }

    // Восстановление самой подпоследовательности: идём из dp[n][m] к dp[0][0]
    static String restore(int[][] dp, String a, String b) {
        StringBuilder sb = new StringBuilder();
        int i = a.length(), j = b.length();
        while (i > 0 && j > 0) {
            if (a.charAt(i - 1) == b.charAt(j - 1)) {
                sb.append(a.charAt(i - 1));   // символ входит в LCS — идём по диагонали
                i--;
                j--;
            } else if (dp[i - 1][j] >= dp[i][j - 1]) {
                i--;                          // ответ пришёл сверху
            } else {
                j--;                          // ответ пришёл слева
            }
        }
        return sb.reverse().toString();
    }

    // Только длина, O(min(n, m)) памяти — храним две строки таблицы
    static int lcsLength(String a, String b) {
        if (a.length() < b.length()) { String t = a; a = b; b = t; }
        int m = b.length();
        int[] prev = new int[m + 1];
        int[] cur = new int[m + 1];
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= m; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) cur[j] = prev[j - 1] + 1;
                else cur[j] = Math.max(prev[j], cur[j - 1]);
            }
            int[] t = prev; prev = cur; cur = t;
        }
        return prev[m];
    }

    static void printTable(int[][] dp, String a, String b) {
        System.out.print("       ");
        for (int j = 0; j < b.length(); j++) System.out.printf("%3c", b.charAt(j));
        System.out.println();
        for (int i = 0; i <= a.length(); i++) {
            System.out.printf("%3s", i == 0 ? " " : String.valueOf(a.charAt(i - 1)));
            for (int j = 0; j <= b.length(); j++) System.out.printf("%3d", dp[i][j]);
            System.out.println();
        }
    }

    static void solve(String a, String b, boolean showTable) {
        int[][] dp = buildTable(a, b);
        System.out.println("a = \"" + a + "\"");
        System.out.println("b = \"" + b + "\"");
        if (showTable) printTable(dp, a, b);
        String lcs = restore(dp, a, b);
        System.out.println("LCS = \"" + lcs + "\", длина " + lcs.length()
                + " (проверка O(m) памяти: " + lcsLength(a, b) + ")");
        System.out.println();
    }

    public static void main(String[] args) {
        solve("ABCBDAB", "BDCABA", true);
        solve("AGGTAB", "GXTXAYB", true);
        solve("динамическое", "программирование", false);
        solve("ACCGGTCGAGTGCGCGGAAGCCGGCCGAA", "GTCGTTCGGAATGCCGTTGCTCTGTAAA", false);
    }
}
