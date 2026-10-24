package lesson3;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/** Ребро в списке смежности */
class Edge {
    int to, weight;
    Edge(int to, int weight) { this.to = to; this.weight = weight; }
}

/** Результат работы алгоритма Прима */
class PrimResult {
    List<int[]> mstEdges;   // рёбра MST в виде {u, v, weight}
    int totalWeight;        // суммарный вес

    PrimResult(List<int[]> edges, int w) {
        this.mstEdges = edges;
        this.totalWeight = w;
    }
}

public class PrimVisualization {

    /**
     * Алгоритм Прима.
     * @param graph список смежности: для каждой вершины — список рёбер
     * @param n     количество вершин
     * @return      рёбра минимального остовного дерева и их суммарный вес
     * @throws IllegalStateException если граф несвязный
     */
    public static PrimResult prim(List<List<Edge>> graph, int n) {
        boolean[] inMST = new boolean[n];                 // вершина уже в дереве?
        // pq хранит {вершина, вес ребра, откуда пришли}
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.add(new int[]{0, 0, -1});                      // стартуем с вершины 0, вес 0, родителя нет

        List<int[]> mstEdges = new ArrayList<>();
        int totalWeight = 0;
        int count = 0;                                    // сколько вершин уже в MST

        while (!pq.isEmpty() && count < n) {
            int[] cur = pq.poll();
            int u = cur[0];        // текущая вершина
            int w = cur[1];        // вес ребра, которым к ней пришли
            int parent = cur[2];   // откуда пришли

            // Если вершина уже в MST — пропускаем (устаревшая запись в очереди)
            if (inMST[u]) continue;

            inMST[u] = true;
            totalWeight += w;
            count++;

            if (parent != -1) {
                mstEdges.add(new int[]{parent, u, w});
            }

            // Кладём в очередь все рёбра из u к ещё не посещённым вершинам
            for (Edge e : graph.get(u)) {
                if (!inMST[e.to]) {
                    pq.add(new int[]{e.to, e.weight, u});
                }
            }
        }

        if (count != n) {
            throw new IllegalStateException("Граф несвязный — остовного дерева не существует");
        }
        return new PrimResult(mstEdges, totalWeight);
    }

    // ================================================================
    //                     ВИЗУАЛИЗАЦИЯ (Swing)
    // ================================================================

    /** Панель для отрисовки графа и MST */
    static class GraphPanel extends JPanel {
        final int n;
        final List<int[]> allEdges;   // все рёбра графа {u, v, weight}
        final List<int[]> mstEdges;   // рёбра MST {u, v, weight}
        final int[][] coords;         // координаты вершин

        GraphPanel(int n, List<int[]> allEdges, List<int[]> mstEdges) {
            this.n = n;
            this.allEdges = allEdges;
            this.mstEdges = mstEdges;
            this.coords = circularLayout(n);
            setPreferredSize(new Dimension(760, 720));
            setBackground(Color.WHITE);
        }

        /** Раскладываем вершины равномерно по окружности */
        private int[][] circularLayout(int n) {
            int[][] c = new int[n][2];
            int cx = 380, cy = 380, R = 260;
            for (int i = 0; i < n; i++) {
                double angle = 2 * Math.PI * i / n - Math.PI / 2;
                c[i][0] = cx + (int) (R * Math.cos(angle));
                c[i][1] = cy + (int) (R * Math.sin(angle));
            }
            return c;
        }

        @Override
        protected void paintComponent(Graphics g0) {
            super.paintComponent(g0);
            Graphics2D g = (Graphics2D) g0;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // 1) Все рёбра графа — серые
            g.setColor(new Color(190, 190, 190));
            g.setStroke(new BasicStroke(1.5f));
            for (int[] e : allEdges) drawEdge(g, e[0], e[1], e[2]);

            // 2) Рёбра MST — красные, толстые
            g.setColor(new Color(200, 30, 30));
            g.setStroke(new BasicStroke(3.5f));
            for (int[] e : mstEdges) drawEdge(g, e[0], e[1], e[2]);

            // 3) Вершины
            for (int i = 0; i < n; i++) {
                int x = coords[i][0], y = coords[i][1];
                g.setColor(new Color(70, 130, 200));
                g.fillOval(x - 22, y - 22, 44, 44);
                g.setColor(Color.WHITE);
                g.setStroke(new BasicStroke(2f));
                g.drawOval(x - 22, y - 22, 44, 44);

                g.setColor(Color.WHITE);
                g.setFont(new Font("SansSerif", Font.BOLD, 18));
                String label = String.valueOf(i);
                FontMetrics fm = g.getFontMetrics();
                g.drawString(label,
                        x - fm.stringWidth(label) / 2,
                        y + fm.getAscent() / 2 - 3);
            }

            // 4) Легенда
            g.setFont(new Font("SansSerif", Font.PLAIN, 13));
            g.setColor(Color.DARK_GRAY);
            g.drawString("Серые рёбра — исходный граф", 20, 24);
            g.setColor(new Color(200, 30, 30));
            g.drawString("Красные рёбра — минимальное остовное дерево", 20, 44);
            g.setColor(Color.BLACK);
            g.drawString("Суммарный вес MST: " + mstEdges.stream()
                    .mapToInt(e -> e[2]).sum(), 20, 64);
        }

        /** Рисует ребро между двумя вершинами с подписью веса */
        private void drawEdge(Graphics2D g, int u, int v, int weight) {
            int x1 = coords[u][0], y1 = coords[u][1];
            int x2 = coords[v][0], y2 = coords[v][1];

            double dx = x2 - x1, dy = y2 - y1;
            double len = Math.hypot(dx, dy);
            if (len == 0) return;
            double r = 22; // радиус вершины

            int sx = (int) (x1 + dx / len * r);
            int sy = (int) (y1 + dy / len * r);
            int ex = (int) (x2 - dx / len * r);
            int ey = (int) (y2 - dy / len * r);

            g.drawLine(sx, sy, ex, ey);

            // Подпись веса по центру ребра с белым фоном
            int mx = (sx + ex) / 2, my = (sy + ey) / 2;
            Color old = g.getColor();
            g.setColor(Color.BLACK);
            g.setFont(new Font("SansSerif", Font.BOLD, 12));
            String ws = String.valueOf(weight);
            FontMetrics fm = g.getFontMetrics();
            int tw = fm.stringWidth(ws);

            g.setColor(new Color(255, 255, 255, 220));
            g.fillRect(mx - tw / 2 - 4, my - 10, tw + 8, 16);

            g.setColor(old == Color.WHITE ? Color.BLACK : old);
            g.drawString(ws, mx - tw / 2, my + 3);
        }
    }

    // ================================================================
    //                          ПРИМЕР ВЫЗОВА
    // ================================================================

    public static void main(String[] args) {
        int n = 6;

        // Список смежности
        List<List<Edge>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) graph.add(new ArrayList<>());

        // Рёбра: {u, v, weight}
        int[][] edges = {
                {0, 1, 4}, {0, 2, 3},
                {1, 2, 1}, {1, 3, 2},
                {2, 3, 4}, {2, 4, 5},
                {3, 4, 7}, {3, 5, 6},
                {4, 5, 8}, {0, 5, 20}
        };

        for (int[] e : edges) {
            graph.get(e[0]).add(new Edge(e[1], e[2]));
            graph.get(e[1]).add(new Edge(e[0], e[2])); // граф неориентированный
        }

        // Запуск алгоритма
        PrimResult result = prim(graph, n);

        System.out.println("Рёбра минимального остовного дерева:");
        for (int[] e : result.mstEdges) {
            System.out.printf("  %d — %d  (вес %d)%n", e[0], e[1], e[2]);
        }
        System.out.println("Суммарный вес: " + result.totalWeight);

        // Подготовим список всех рёбер для визуализации
        List<int[]> allEdges = new ArrayList<>(Arrays.asList(edges));

        // Открываем окно с визуализацией
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Алгоритм Прима — визуализация");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new GraphPanel(n, allEdges, result.mstEdges));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}