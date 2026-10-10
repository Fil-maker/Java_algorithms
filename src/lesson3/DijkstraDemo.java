package lesson3;

import java.util.*;

public class DijkstraDemo {

    // ============================================================
    //  ВСПОМОГАТЕЛЬНЫЕ СТРУКТУРЫ ДАННЫХ
    //  https://www.youtube.com/watch?v=71Z-Jpnm3D4 Алгоритм Дейкстры (тяжело)
    //  https://www.youtube.com/watch?v=-L-WgKMFuhE&list=PLFt_AvWsXl0cq5Umv3pMC9SPnKjfp9eGW A* (просто)
    // ============================================================

    /** Ребро графа: вершина-назначение и вес. */
    static class Edge {
        final int to;        // куда ведёт ребро
        final int weight;    // вес ребра (неотрицательный!)
        Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
        @Override
        public String toString() {
            return "→" + to + "(w=" + weight + ")";
        }
    }

    /** Пара (вершина, расстояние) для приоритетной очереди. */
    static class Node implements Comparable<Node> {
        final int vertex;
        final int distance;
        Node(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
        @Override
        public int compareTo(Node other) {
            // Чем меньше расстояние — тем выше приоритет
            return Integer.compare(this.distance, other.distance);
        }
        @Override
        public String toString() {
            return "(" + vertex + ", d=" + distance + ")";
        }
    }

    /** Результат работы алгоритма. */
    static class Result {
        final int[] dist;   // dist[v] — кратчайшее расстояние от источника до v
        final int[] prev;   // prev[v] — предыдущая вершина на кратчайшем пути
        Result(int[] dist, int[] prev) {
            this.dist = dist;
            this.prev = prev;
        }
    }

    /** «Бесконечность» — расстояние до недостижимой вершины. */
    private static final int INF = Integer.MAX_VALUE;

    // ============================================================
    //  ВИЗУАЛИЗАЦИЯ ГРАФА
    // ============================================================

    /**
     * Печатает список смежности графа.
     * Например:  0 → [→1(w=4), →2(w=1)]
     */
    static void printAdjacency(List<List<Edge>> graph) {
        System.out.println("Структура графа (список смежности):");
        for (int v = 0; v < graph.size(); v++) {
            System.out.printf("  %d %s%n", v, graph.get(v));
        }
        System.out.println();
    }

    /**
     * Рисует ASCII-схему графа в виде матрицы весов.
     * Ячейка [i][j] = вес ребра i→j, или " . " если ребра нет.
     */
    static void printMatrix(List<List<Edge>> graph) {
        int n = graph.size();
        int[][] w = new int[n][n];
        for (int[] row : w) Arrays.fill(row, -1); // -1 = нет ребра

        for (int u = 0; u < n; u++) {
            for (Edge e : graph.get(u)) {
                w[u][e.to] = e.weight;
            }
        }

        System.out.println("Матрица весов (строка = откуда, столбец = куда):");
        System.out.print("      ");
        for (int j = 0; j < n; j++) System.out.printf("%4d", j);
        System.out.println();
        System.out.print("      ");
        for (int j = 0; j < n; j++) System.out.print("----");
        System.out.println();

        for (int i = 0; i < n; i++) {
            System.out.printf("  %2d |", i);
            for (int j = 0; j < n; j++) {
                if (w[i][j] < 0) System.out.print("   .");
                else             System.out.printf("%4d", w[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Рисует граф в виде ASCII-иллюстрации.
     * Это простой вариант для наглядности: вершины расположены в ряд,
     * а рёбра показаны текстом под ними. Для неориентированного графа
     * дублирующиеся рёбра (u→v и v→u) выводятся один раз.
     */
    static void printAsciiGraph(List<List<Edge>> graph) {
        int n = graph.size();

        // Верхний ряд — вершины
        StringBuilder top = new StringBuilder("   ");
        for (int i = 0; i < n; i++) {
            top.append("(").append(i).append(")   ");
        }
        System.out.println("ASCII-схема графа:");
        System.out.println(top);

        // Нижний блок — рёбра (без дублей)
        Set<String> seen = new HashSet<>();
        System.out.println("  Рёбра:");
        for (int u = 0; u < n; u++) {
            for (Edge e : graph.get(u)) {
                int a = Math.min(u, e.to);
                int b = Math.max(u, e.to);
                String key = a + "-" + b;
                if (seen.add(key)) {
                    System.out.printf("    %d --- %d   (вес %d)%n", a, b, e.weight);
                }
            }
        }
        System.out.println();
    }

    // ============================================================
    //  ОСНОВНОЙ АЛГОРИТМ
    // ============================================================

    /**
     * Классический алгоритм Дейкстры.
     *
     * @param graph   список смежности (graph.get(u) — рёбра из u)
     * @param source  стартовая вершина
     * @param verbose если true — печатает пошаговый лог
     * @return расстояния и массив предков для восстановления пути
     */
    static Result dijkstra(List<List<Edge>> graph, int source, boolean verbose) {
        int n = graph.size();

        // ---------- Инициализация ----------
        int[] dist = new int[n];   // кратчайшие известные расстояния
        int[] prev = new int[n];   // предки для восстановления пути
        Arrays.fill(dist, INF);    // все вершины пока «недостижимы»
        Arrays.fill(prev, -1);     // предка нет
        dist[source] = 0;          // до источника — 0

        // Очередь с приоритетом: всегда достаём вершину с минимальным dist
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(source, 0));

        boolean[] visited = new boolean[n]; // «финализированные» вершины

        if (verbose) {
            System.out.println("=== Запуск алгоритма Дейкстры ===");
            System.out.println("Источник: " + source);
            System.out.println("Начальные dist: " + Arrays.toString(dist));
            System.out.println("Начальная очередь: " + pq);
            System.out.println();
        }

        // ---------- Основной цикл ----------
        int step = 1;
        while (!pq.isEmpty()) {
            Node cur = pq.poll();     // вершина с минимальным расстоянием
            int u = cur.vertex;

            // В очереди могли остаться устаревшие записи — пропускаем их.
            // Такое бывает, когда мы улучшили dist[v] уже после добавления v в очередь.
            if (cur.distance > dist[u] || visited[u]) {
                if (verbose) {
                    System.out.printf("  [шаг %d] пропуск устаревшей записи %s%n", step, cur);
                }
                continue;
            }
            visited[u] = true;        // финализируем вершину u

            if (verbose) {
                System.out.printf("[шаг %d] Извлекаем вершину %d (dist=%d). Очередь: %s%n",
                        step, u, cur.distance, pq);
            }

            // ---------- Релаксация всех рёбер из u ----------
            for (Edge e : graph.get(u)) {
                int v = e.to;
                int newDist = dist[u] + e.weight;

                if (newDist < dist[v]) {
                    if (verbose) {
                        System.out.printf("    релаксация %d→%d: %d + %d = %d < %s → обновляем%n",
                                u, v, dist[u], e.weight, newDist,
                                (dist[v] == INF ? "∞" : dist[v]));
                    }
                    dist[v] = newDist;
                    prev[v] = u;
                    pq.add(new Node(v, newDist));
                } else if (verbose) {
                    System.out.printf("    ребро %d→%d: %d + %d = %d ≥ %s → без изменений%n",
                            u, v, dist[u], e.weight, newDist,
                            (dist[v] == INF ? "∞" : dist[v]));
                }
            }

            if (verbose) {
                System.out.println("    dist после шага: " + Arrays.toString(dist));
                System.out.println();
            }
            step++;
        }

        if (verbose) System.out.println("=== Алгоритм завершён ===\n");
        return new Result(dist, prev);
    }

    // ============================================================
    //  ВОССТАНОВЛЕНИЕ ПУТИ
    // ============================================================

    /**
     * Восстанавливает путь от источника до target по массиву предков.
     * Если target недостижим — вернёт пустой список.
     */
    static List<Integer> buildPath(int[] prev, int target) {
        LinkedList<Integer> path = new LinkedList<>();
        for (int at = target; at != -1; at = prev[at]) {
            path.addFirst(at);
        }
        return path;
    }

    // ============================================================
    //  УТИЛИТЫ ДЛЯ ПОСТРОЕНИЯ ГРАФА
    // ============================================================

    /** Добавляет неориентированное ребро (u—v с весом w). */
    static void addEdge(List<List<Edge>> graph, int u, int v, int w) {
        graph.get(u).add(new Edge(v, w));
        graph.get(v).add(new Edge(u, w));
    }

    // ============================================================
    //  ТОЧКА ВХОДА
    // ============================================================

    public static void main(String[] args) {
        // ------- 1. Создаём граф -------
        int n = 7;
        List<List<Edge>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) graph.add(new ArrayList<>());

        // Пример взвешенного неориентированного графа:
        //
        //         4         1
        //   (0) -------- (1) ----- (3)
        //    |  \        /  \      / |
        //  1 |   \ 2   2/   5\   /   | 8
        //    |    \   /      \ /     |
        //   (2)     \ /       (4)---(5)
        //            X         3   |   \
        //                           2   1
        //                            \   \
        //                             (6)
        addEdge(graph, 0, 1, 4);
        addEdge(graph, 0, 2, 1);
        addEdge(graph, 1, 3, 1);
        addEdge(graph, 2, 1, 2);
        addEdge(graph, 2, 3, 5);
        addEdge(graph, 3, 4, 3);
        addEdge(graph, 4, 5, 2);
        addEdge(graph, 3, 5, 8);
        addEdge(graph, 5, 6, 1);

        // ------- 2. Показываем граф тремя способами -------
        System.out.println("############ ИСХОДНЫЙ ГРАФ ############\n");
        printAdjacency(graph);
        printMatrix(graph);
        printAsciiGraph(graph);

        // ------- 3. Запускаем алгоритм с логом -------
        int source = 0;
        int target = 6;

        Result result = dijkstra(graph, source, /* verbose = */ true);

        // ------- 4. Печатаем итоговые расстояния -------
        System.out.println("############ РЕЗУЛЬТАТ ############\n");
        System.out.println("Кратчайшие расстояния от вершины " + source + ":");
        for (int v = 0; v < n; v++) {
            String d = (result.dist[v] == INF) ? "недостижима" : String.valueOf(result.dist[v]);
            System.out.printf("  dist[%d] = %s%n", v, d);
        }

        // ------- 5. Восстанавливаем и печатаем путь -------
        List<Integer> path = buildPath(result.prev, target);
        System.out.println();
        if (path.isEmpty() || path.get(0) != source) {
            System.out.println("Вершина " + target + " недостижима из " + source);
        } else {
            System.out.println("Кратчайший путь " + source + " -> " + target + ": " + path);
            System.out.println("Длина пути: " + result.dist[target]);
            System.out.println("Массив предков prev: " + Arrays.toString(result.prev));
        }
    }
}