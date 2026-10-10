package lesson3;

import java.util.*;

public class DepthFirstSearch {

    private final int n;                     // number of vertices (0..n-1)
    private final List<List<Integer>> adj;   // adjacency list

    public DepthFirstSearch(int n) {
        this.n = n;
        this.adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    }

    /** Adds an undirected edge. For a directed graph, remove the second line. */
    public void addEdge(int u, int v) {
        adj.get(u).add(v);
        adj.get(v).add(u);
    }

    /** Recursive DFS traversal order. */
    public List<Integer> dfsRecursive(int start) {
        List<Integer> order = new ArrayList<>();
        dfs(start, new boolean[n], order);
        return order;
    }

    private void dfs(int v, boolean[] visited, List<Integer> order) {
        visited[v] = true;
        order.add(v);
        for (int to : adj.get(v)) {
            if (!visited[to]) dfs(to, visited, order);
        }
    }

    /** Iterative DFS traversal order using an explicit stack. */
    public List<Integer> dfsIterative(int start) {
        List<Integer> order = new ArrayList<>();
        boolean[] visited = new boolean[n];
        Deque<Integer> stack = new ArrayDeque<>();

        stack.push(start);

        while (!stack.isEmpty()) {
            int v = stack.pop();
            if (visited[v]) continue;

            visited[v] = true;
            order.add(v);

            // Push neighbours in reverse so they are popped in the original order
            List<Integer> neighbors = adj.get(v);
            for (int i = neighbors.size() - 1; i >= 0; i--) {
                int to = neighbors.get(i);
                if (!visited[to]) stack.push(to);
            }
        }
        return order;
    }

    public static void main(String[] args) {
        //        0
        //       / \
        //      1   2
        //     / \   \
        //    3   4   5
        //         \
        //          6
        DepthFirstSearch g = new DepthFirstSearch(7);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 3);
        g.addEdge(1, 4);
        g.addEdge(2, 5);
        g.addEdge(4, 6);

        System.out.println("DFS (recursive):    " + g.dfsRecursive(0));
        System.out.println("DFS (iterative):    " + g.dfsIterative(0));
    }
}