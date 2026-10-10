package lesson3;

import java.util.*;

public class BreadthFirstSearch {

    private final int n;                     // number of vertices (0..n-1)
    private final List<List<Integer>> adj;   // adjacency list

    public BreadthFirstSearch(int n) {
        this.n = n;
        this.adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    }

    /** Adds an undirected edge. For a directed graph, remove the second line. */
    public void addEdge(int u, int v) {
        adj.get(u).add(v);
        adj.get(v).add(u);
    }

    /** BFS traversal order starting from `start`. */
    public List<Integer> bfs(int start) {
        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new ArrayDeque<>();
        List<Integer> order = new ArrayList<>();

        visited[start] = true;
        queue.offer(start);

        while (!queue.isEmpty()) {
            int v = queue.poll();
            order.add(v);

            for (int to : adj.get(v)) {
                if (!visited[to]) {
                    visited[to] = true;
                    queue.offer(to);
                }
            }
        }
        return order;
    }

    /** Distances (in edges) from start; -1 means unreachable. */
    public int[] bfsDistances(int start) {
        int[] dist = new int[n];
        Arrays.fill(dist, -1);
        Queue<Integer> queue = new ArrayDeque<>();

        dist[start] = 0;
        queue.offer(start);

        while (!queue.isEmpty()) {
            int v = queue.poll();
            for (int to : adj.get(v)) {
                if (dist[to] == -1) {
                    dist[to] = dist[v] + 1;
                    queue.offer(to);
                }
            }
        }
        return dist;
    }

    /** Shortest path (fewest edges) in an unweighted graph, or empty list if none. */
    public List<Integer> shortestPath(int start, int target) {
        boolean[] visited = new boolean[n];
        int[] parent = new int[n];
        Arrays.fill(parent, -1);

        Queue<Integer> queue = new ArrayDeque<>();
        visited[start] = true;
        queue.offer(start);

        while (!queue.isEmpty()) {
            int v = queue.poll();
            if (v == target) break;

            for (int to : adj.get(v)) {
                if (!visited[to]) {
                    visited[to] = true;
                    parent[to] = v;
                    queue.offer(to);
                }
            }
        }

        if (!visited[target]) return Collections.emptyList();

        LinkedList<Integer> path = new LinkedList<>();
        for (int v = target; v != -1; v = parent[v]) {
            path.addFirst(v);
        }
        return path;
    }

    public static void main(String[] args) {
        //        0
        //       / \
        //      1   2
        //     / \   \
        //    3   4   5
        //         \
        //          6
        BreadthFirstSearch g = new BreadthFirstSearch(7);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 3);
        g.addEdge(1, 4);
        g.addEdge(2, 5);
        g.addEdge(4, 6);

        System.out.println("BFS order:          " + g.bfs(0));
        System.out.println("Distances from 0:   " + Arrays.toString(g.bfsDistances(0)));
        System.out.println("Shortest path 0->6: " + g.shortestPath(0, 6));
    }
}