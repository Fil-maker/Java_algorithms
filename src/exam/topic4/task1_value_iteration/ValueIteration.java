package exam.topic4.task1_value_iteration;

import java.io.*;
import java.util.*;

public class ValueIteration {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        double gamma = Double.parseDouble(st.nextToken());

        st = new StringTokenizer(br.readLine());
        double[] r = new double[n];
        for (int i = 0; i < n; i++) r[i] = Double.parseDouble(st.nextToken());

        double[] V = new double[n];   // V[n - 1] всегда равно 0
        double delta;
        do {
            delta = 0;
            for (int s = 0; s < n - 1; s++) {
                int left = Math.max(s - 1, 0);
                int right = s + 1;
                double best = Math.max(r[left] + gamma * V[left], r[right] + gamma * V[right]);

                delta = Math.max(delta, Math.abs(best - V[s]));
                V[s] = best;
            }
        } while (delta >= 1e-9);

        StringBuilder sb = new StringBuilder();
        for (int s = 0; s < n; s++) {
            if (s > 0) sb.append(' ');
            sb.append(String.format(Locale.US, "%.3f", V[s]));
        }
        System.out.println(sb);
    }
}
