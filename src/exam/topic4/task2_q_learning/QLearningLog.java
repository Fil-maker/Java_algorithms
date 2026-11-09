package exam.topic4.task2_q_learning;

import java.io.*;
import java.util.*;

public class QLearningLog {

    /** Индекс наибольшего элемента; при равенстве — наименьший индекс. */
    static int argmax(double[] values) {
        int best = 0;
        for (int a = 1; a < values.length; a++) {
            if (values[a] > values[best]) best = a;
        }
        return best;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        double alpha = Double.parseDouble(st.nextToken());
        double gamma = Double.parseDouble(st.nextToken());
        int k = Integer.parseInt(st.nextToken());

        double[][] Q = new double[n][m];

        for (int i = 0; i < k; i++) {
            st = new StringTokenizer(br.readLine());
            int s = Integer.parseInt(st.nextToken());
            int a = Integer.parseInt(st.nextToken());
            double r = Double.parseDouble(st.nextToken());
            int s2 = Integer.parseInt(st.nextToken());
            int done = Integer.parseInt(st.nextToken());

            double target = r;
            if (done == 0) target += gamma * Q[s2][argmax(Q[s2])];
            Q[s][a] += alpha * (target - Q[s][a]);
        }

        StringBuilder sb = new StringBuilder();
        for (int s = 0; s < n; s++) {
            for (int a = 0; a < m; a++) {
                if (a > 0) sb.append(' ');
                sb.append(String.format(Locale.US, "%.3f", Q[s][a]));
            }
            sb.append('\n');
        }
        for (int s = 0; s < n; s++) {
            if (s > 0) sb.append(' ');
            sb.append(argmax(Q[s]));
        }
        System.out.println(sb);
    }
}
