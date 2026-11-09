package exam.topic3.task2_dijkstra;

import java.io.*;
import java.util.*;

public class Dijkstra {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        int s = Integer.parseInt(st.nextToken());

        // graph.get(u) — список рёбер из u в виде пар {v, w}
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) graph.add(new ArrayList<>());

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            graph.get(u).add(new int[]{v, w});
        }

        long[] dist = new long[n + 1];
        Arrays.fill(dist, -1);
        dist[s] = 0;

        // Элемент очереди — пара {расстояние, вершина}
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(a[0], b[0]));
        pq.add(new long[]{0, s});

        while (!pq.isEmpty()) {
            long[] top = pq.poll();
            long d = top[0];
            int u = (int) top[1];
            if (d != dist[u]) continue;   // устаревшая запись

            for (int[] edge : graph.get(u)) {
                int v = edge[0];
                long candidate = d + edge[1];
                if (dist[v] == -1 || candidate < dist[v]) {
                    dist[v] = candidate;
                    pq.add(new long[]{candidate, v});
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int v = 1; v <= n; v++) {
            if (v > 1) sb.append(' ');
            sb.append(dist[v]);
        }
        System.out.println(sb);
    }
}
