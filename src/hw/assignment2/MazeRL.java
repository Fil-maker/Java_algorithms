package hw.assignment2;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Random;

/**
 * Поиск пути в лабиринте: графовый поиск (BFS) против обучения с подкреплением (RL).
 *
 * Лабиринт — сетка клеток. Агент стоит в клетке S и должен дойти до G.
 * Действия: вверх, вниз, влево, вправо. Шаг в стену — агент остаётся на месте.
 *
 * 1. BFS — классический графовый поиск. Знает всю карту, за O(V + E)
 *    находит кратчайший путь.
 *
 * 2. Value Iteration — динамическое программирование по уравнению Беллмана.
 *    Тоже знает модель (карту), но вместо обхода графа итеративно уточняет
 *    ценность каждой клетки:
 *        V(s) = max_a [ r(s, a) + γ · V(s') ]
 *    Это мост между ДП и RL: RL решает то же уравнение, но без знания модели.
 *
 * 3. Q-learning — RL без модели. Агент НЕ знает карту, он только пробует
 *    действия и получает награду. Обновление (временная разность):
 *        Q(s, a) ← Q(s, a) + α · [ r + γ · max_a' Q(s', a') − Q(s, a) ]
 *    Это то же уравнение Беллмана, только ожидание заменено выборкой
 *    из реального опыта. Выбор действия — ε-жадный: с вероятностью ε
 *    случайное (исследование), иначе лучшее по Q (использование).
 *
 * Награды: −1 за каждый шаг, +100 за достижение цели. Чем короче путь,
 * тем больше суммарная награда, поэтому оптимальная политика = кратчайший путь.
 */
public class MazeRL {

    static final String[] MAZE = {
            "###############",
            "#S    #       #",
            "# ### # ##### #",
            "#   #   #   # #",
            "### ##### # # #",
            "#   #     #   #",
            "# ### ### #####",
            "#     #       #",
            "# ##### ##### #",
            "#     #     #G#",
            "###############",
    };

    static final int[] DR = {-1, 1, 0, 0};
    static final int[] DC = {0, 0, -1, 1};
    static final char[] ARROW = {'↑', '↓', '←', '→'};

    static final double STEP_REWARD = -1;
    static final double GOAL_REWARD = 100;
    static final double GAMMA = 0.95;

    static int rows, cols;
    static long bfsWork, viWork, qlWork;   // число элементарных операций каждого метода
    static int startR, startC, goalR, goalC;

    static boolean isWall(int r, int c) {
        return r < 0 || r >= rows || c < 0 || c >= cols || MAZE[r].charAt(c) == '#';
    }

    static int id(int r, int c) {
        return r * cols + c;
    }

    // ===================== 1. BFS =====================
    static int[] bfs() {
        int[] parent = new int[rows * cols];
        Arrays.fill(parent, -2);           // -2 = не посещена
        ArrayDeque<Integer> q = new ArrayDeque<>();
        int s = id(startR, startC), g = id(goalR, goalC);
        parent[s] = -1;
        q.add(s);
        int visited = 0;
        while (!q.isEmpty()) {
            int u = q.poll();
            visited++;
            if (u == g) break;
            int r = u / cols, c = u % cols;
            for (int a = 0; a < 4; a++) {
                int nr = r + DR[a], nc = c + DC[a];
                if (isWall(nr, nc)) continue;
                int v = id(nr, nc);
                if (parent[v] == -2) {
                    parent[v] = u;
                    q.add(v);
                }
            }
        }
        bfsWork = visited;
        System.out.println("BFS: обработано вершин = " + visited);
        return parent;
    }

    static boolean[] pathFromParents(int[] parent) {
        boolean[] onPath = new boolean[rows * cols];
        for (int v = id(goalR, goalC); v >= 0; v = parent[v]) onPath[v] = true;
        return onPath;
    }

    // ===================== 2. Value Iteration (ДП) =====================
    static double[] valueIteration() {
        double[] V = new double[rows * cols];
        int g = id(goalR, goalC);
        int sweeps = 0;
        long updates = 0;
        while (true) {
            sweeps++;
            double delta = 0;
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (isWall(r, c) || id(r, c) == g) continue;   // цель — терминальная, V(G) = 0
                    double best = Double.NEGATIVE_INFINITY;
                    for (int a = 0; a < 4; a++) {
                        int[] t = step(r, c, a);
                        double val = t[2] == 1 ? GOAL_REWARD : STEP_REWARD + GAMMA * V[id(t[0], t[1])];
                        best = Math.max(best, val);
                    }
                    updates++;
                    delta = Math.max(delta, Math.abs(best - V[id(r, c)]));
                    V[id(r, c)] = best;
                }
            }
            if (delta < 1e-6) break;
        }
        viWork = updates;
        System.out.println("Value Iteration: проходов по всем клеткам = " + sweeps + ", обновлений V = " + updates);
        return V;
    }

    // ===================== 3. Q-learning (RL) =====================

    // Среда: по клетке и действию возвращает {новая строка, новый столбец, 1 если цель}
    static int[] step(int r, int c, int a) {
        int nr = r + DR[a], nc = c + DC[a];
        if (isWall(nr, nc)) { nr = r; nc = c; }      // упёрлись в стену — стоим на месте
        boolean done = nr == goalR && nc == goalC;
        return new int[]{nr, nc, done ? 1 : 0};
    }

    static int argmax(double[] q) {
        int best = 0;
        for (int a = 1; a < q.length; a++) if (q[a] > q[best]) best = a;
        return best;
    }

    static double[][] qLearning(int episodes, long seed) {
        Random rnd = new Random(seed);
        double[][] Q = new double[rows * cols][4];
        double alpha = 0.5;
        double epsilon = 1.0;               // в начале действуем почти случайно
        double epsMin = 0.05, epsDecay = 0.99;
        int maxSteps = 2000;
        long totalSteps = 0;

        System.out.println("Q-learning, длина эпизода (шагов до цели):");
        for (int ep = 1; ep <= episodes; ep++) {
            int r = startR, c = startC;
            int steps = 0;
            boolean done = false;
            while (!done && steps < maxSteps) {
                int s = id(r, c);
                int a = rnd.nextDouble() < epsilon ? rnd.nextInt(4) : argmax(Q[s]);
                int[] t = step(r, c, a);
                done = t[2] == 1;
                double reward = done ? GOAL_REWARD : STEP_REWARD;
                int s2 = id(t[0], t[1]);
                double target = done ? reward : reward + GAMMA * Q[s2][argmax(Q[s2])];
                Q[s][a] += alpha * (target - Q[s][a]);
                r = t[0];
                c = t[1];
                steps++;
            }
            totalSteps += steps;
            epsilon = Math.max(epsMin, epsilon * epsDecay);
            if (ep == 1 || ep % 50 == 0) {
                System.out.printf("  эпизод %4d: %5d шагов, ε = %.3f%n", ep, steps, epsilon);
            }
        }
        qlWork = totalSteps;
        System.out.println("Q-learning: всего шагов-взаимодействий со средой = " + totalSteps);
        return Q;
    }

    // Жадно идём по выученной политике (без случайности)
    static boolean[] greedyPath(double[][] Q) {
        boolean[] onPath = new boolean[rows * cols];
        int r = startR, c = startC;
        onPath[id(r, c)] = true;
        for (int i = 0; i < rows * cols; i++) {
            if (r == goalR && c == goalC) return onPath;
            int[] t = step(r, c, argmax(Q[id(r, c)]));
            r = t[0];
            c = t[1];
            if (onPath[id(r, c)]) break;        // зациклились — политика не доучена
            onPath[id(r, c)] = true;
        }
        return (r == goalR && c == goalC) ? onPath : null;
    }

    // ===================== Вывод =====================
    static int countPath(boolean[] onPath) {
        int n = 0;
        for (boolean b : onPath) if (b) n++;
        return n - 1;   // шагов на 1 меньше, чем клеток
    }

    static void printPath(String title, boolean[] onPath) {
        System.out.println(title + (onPath == null ? ": путь не найден" : ": длина пути = " + countPath(onPath)));
        if (onPath == null) return;
        for (int r = 0; r < rows; r++) {
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < cols; c++) {
                char ch = MAZE[r].charAt(c);
                if (ch == ' ' && onPath[id(r, c)]) ch = '·';
                sb.append(ch == '#' ? '█' : ch);
            }
            System.out.println("  " + sb);
        }
        System.out.println();
    }

    static void printPolicy(String title, double[][] Q) {
        System.out.println(title);
        for (int r = 0; r < rows; r++) {
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < cols; c++) {
                char ch = MAZE[r].charAt(c);
                if (ch == '#') sb.append('█');
                else if (ch == 'G') sb.append('G');
                else sb.append(ARROW[argmax(Q[id(r, c)])]);
            }
            System.out.println("  " + sb);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        rows = MAZE.length;
        cols = MAZE[0].length();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (MAZE[r].charAt(c) == 'S') { startR = r; startC = c; }
                if (MAZE[r].charAt(c) == 'G') { goalR = r; goalC = c; }
            }
        }

        System.out.println("===== 1. BFS (графовый поиск, карта известна) =====");
        boolean[] bfsPath = pathFromParents(bfs());
        printPath("BFS", bfsPath);

        System.out.println("===== 2. Value Iteration (ДП по Беллману, карта известна) =====");
        double[] V = valueIteration();
        // Политика из V: в каждой клетке шагаем туда, где V больше
        double[][] Qvi = new double[rows * cols][4];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (isWall(r, c)) continue;
                for (int a = 0; a < 4; a++) {
                    int[] t = step(r, c, a);
                    Qvi[id(r, c)][a] = t[2] == 1 ? GOAL_REWARD : STEP_REWARD + GAMMA * V[id(t[0], t[1])];
                }
            }
        }
        printPath("Value Iteration", greedyPath(Qvi));

        System.out.println("===== 3. Q-learning (RL, карта НЕ известна) =====");
        double[][] Q = qLearning(500, 42);
        System.out.println();
        printPolicy("Выученная политика (стрелка — лучшее действие в клетке):", Q);
        boolean[] rlPath = greedyPath(Q);
        printPath("Q-learning", rlPath);

        System.out.println("===== Сравнение =====");
        System.out.printf("%-16s | %-14s | %-12s | %s%n", "Метод", "Длина пути", "Операций", "Нужна карта?");
        System.out.printf("%-16s | %-14d | %-12d | %s%n", "BFS", countPath(bfsPath), bfsWork, "да");
        System.out.printf("%-16s | %-14d | %-12d | %s%n", "Value Iteration", countPath(greedyPath(Qvi)), viWork, "да");
        System.out.printf("%-16s | %-14s | %-12d | %s%n", "Q-learning",
                rlPath == null ? "—" : String.valueOf(countPath(rlPath)), qlWork, "нет");
        System.out.println("(операции: BFS — извлечённые из очереди вершины, VI — обновления V(s),");
        System.out.println(" Q-learning — шаги агента в среде за всё обучение)");
        System.out.println();
        System.out.println("Выводы:");
        System.out.println(" - Если карта известна и награда — просто длина пути, BFS быстрее и проще всех.");
        System.out.println(" - Value Iteration решает то же уравнение Беллмана, что и RL, но с известной моделью;");
        System.out.println("   даёт политику сразу для ВСЕХ клеток, а не путь из одной.");
        System.out.println(" - Q-learning не знает карту и учится на опыте проб и ошибок: тратит на порядки больше");
        System.out.println("   шагов, но работает там, где модели нет (неизвестная/стохастическая среда,");
        System.out.println("   сложные награды), и тоже приходит к кратчайшему пути.");
    }
}
