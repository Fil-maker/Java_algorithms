package lesson2;

import java.util.ArrayList;
import java.util.List;

public class GenerateParentheses {

    public static List<String> generate(int n) {
        List<String> result = new ArrayList<>();
        backtrack(result, new StringBuilder(), 0, 0, n);
        return result;
    }

    private static void backtrack(List<String> result, StringBuilder sb,
                                  int open, int close, int n) {
        // База: длина строки равна 2 * n — последовательность готова
        if (sb.length() == 2 * n) {
            result.add(sb.toString());
            return;
        }

        // Можно добавить '(' , если открывающих ещё меньше n
        if (open < n) {
            sb.append('(');
            backtrack(result, sb, open + 1, close, n);
            sb.deleteCharAt(sb.length() - 1); // откат
        }

        // Можно добавить ')' , если закрывающих меньше, чем открывающих
        if (close < open) {
            sb.append(')');
            backtrack(result, sb, open, close + 1, n);
            sb.deleteCharAt(sb.length() - 1); // откат
        }
    }

    public static void main(String[] args) {
        for (String s : generate(4)) {
            System.out.println(s);
        }
    }
}