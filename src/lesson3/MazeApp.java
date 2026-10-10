package lesson3;

import java.util.*;

/**
 * Генератор лабиринтов с поиском пути.
 *
 * Запуск:
 *   javac MazeApp.java
 *   java MazeApp [ширина] [высота] [seed]
 *
 * Пример:
 *   java MazeApp 20 10 42
 */
public class MazeApp {

    static final char WALL  = '█';
    static final char PASS  = ' ';
    static final char ROUTE = '·';

    static class Maze {
        final int cols, rows;   // размеры сетки (2w+1) x (2h+1)
        final int[][] grid;     // 1 = стена, 0 = проход

        Maze(int cols, int rows) {
            this.cols = cols;
            this.rows = rows;
            this.grid = new int[rows][cols];
            for (int[] r : grid) Arrays.fill(r, 1);
        }
    }

    /**
     * Recursive backtracker (итеративный DFS со стеком).
     * По построению даёт «идеальный» лабиринт — между любыми
     * двумя клетками ровно один путь, изолированных участков нет.
     */
    /** Доля внутренних стен, которые сносятся для создания циклов (0..1). */
    static final double BRAID_CHANCE = 0.30;

    static Maze generate(int w, int h, long seed) {
        Random rnd = new Random(seed);
        Maze m = new Maze(2 * w + 1, 2 * h + 1);
        boolean[][] visited = new boolean[h][w];

        Deque<int[]> stack = new ArrayDeque<>();
        visited[0][0] = true;
        m.grid[1][1] = 0;                 // открываем стартовую клетку
        stack.push(new int[]{0, 0});

        final int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        List<int[]> cand = new ArrayList<>(4);

        while (!stack.isEmpty()) {
            int[] cur = stack.peek();
            int x = cur[0], y = cur[1];

            cand.clear();
            for (int[] d : dirs) {
                int nx = x + d[0], ny = y + d[1];
                if (nx >= 0 && nx < w && ny >= 0 && ny < h && !visited[ny][nx]) {
                    cand.add(new int[]{nx, ny, d[0], d[1]});
                }
            }
            if (cand.isEmpty()) {
                stack.pop();
                continue;
            }

            int[] pick = cand.get(rnd.nextInt(cand.size()));
            int nx = pick[0], ny = pick[1], dx = pick[2], dy = pick[3];
            visited[ny][nx] = true;

            m.grid[2 * y + 1 + dy][2 * x + 1 + dx] = 0;
            m.grid[2 * ny + 1][2 * nx + 1] = 0;

            stack.push(new int[]{nx, ny});
        }

        // === NEW: braid-шаг — сносим часть внутренних стен, чтобы
        //     между точками появились альтернативные маршруты. ===
        // Вертикальные стены (между клетками, соседними по X): y нечётное, x чётное.
        for (int y = 1; y < m.rows - 1; y += 2) {
            for (int x = 2; x < m.cols - 1; x += 2) {
                if (m.grid[y][x] == 1 && rnd.nextDouble() < BRAID_CHANCE) {
                    m.grid[y][x] = 0;
                }
            }
        }
        // Горизонтальные стены (между клетками, соседними по Y): y чётное, x нечётное.
        for (int y = 2; y < m.rows - 1; y += 2) {
            for (int x = 1; x < m.cols - 1; x += 2) {
                if (m.grid[y][x] == 1 && rnd.nextDouble() < BRAID_CHANCE) {
                    m.grid[y][x] = 0;
                }
            }
        }

        // Вход сверху и выход снизу
        m.grid[0][1] = 0;
        m.grid[m.rows - 1][m.cols - 2] = 0;
        return m;
    }
    /** Проверка, что все проходы достижимы из одного (BFS). */
    static boolean allReachable(Maze m) {
        int total = 0, sx = -1, sy = -1;
        for (int y = 0; y < m.rows; y++)
            for (int x = 0; x < m.cols; x++)
                if (m.grid[y][x] == 0) {
                    total++;
                    if (sx == -1) { sx = x; sy = y; }
                }
        if (sx == -1) return true;

        boolean[][] vis = new boolean[m.rows][m.cols];
        Deque<int[]> q = new ArrayDeque<>();
        q.add(new int[]{sx, sy});
        vis[sy][sx] = true;

        final int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        int reached = 0;
        while (!q.isEmpty()) {
            int[] c = q.poll();
            reached++;
            for (int[] d : dirs) {
                int nx = c[0] + d[0], ny = c[1] + d[1];
                if (nx < 0 || nx >= m.cols || ny < 0 || ny >= m.rows) continue;
                if (vis[ny][nx] || m.grid[ny][nx] != 0) continue;
                vis[ny][nx] = true;
                q.add(new int[]{nx, ny});
            }
        }
        return reached == total;
    }

    /**
     * BFS-поиск кратчайшего пути между двумя точками сетки.
     *
     * Возвращает список координат {x, y} от старта до финиша включительно,
     * либо null, если старт/финиш — стены или путь не существует.
     *
     * Работает по сетке лабиринта: проход — это grid[y][x] == 0.
     * Так как BFS обходит узлы «волной» по уровням, найденный путь
     * гарантированно кратчайший (по числу шагов).
     */
    static List<int[]> findPath(Maze m, int sx, int sy, int ex, int ey) {
        // Если старт или финиш стоят в стене — пути быть не может.
        if (m.grid[sy][sx] != 0 || m.grid[ey][ex] != 0) return null;

        // prev[y][x] хранит упакованные координаты клетки-предшественника
        // (откуда мы пришли в клетку (x, y)). -1 означает «не посещали».
        // Упаковка двух координат в одно int экономит память и ускоряет доступ.
        int[][] prev = new int[m.rows][m.cols];
        for (int[] r : prev) Arrays.fill(r, -1);

        // vis[y][x] — была ли клетка уже добавлена в очередь (защита от повторной обработки).
        boolean[][] vis = new boolean[m.rows][m.cols];

        // Очередь BFS. Храним пары {x, y}.
        Deque<int[]> q = new ArrayDeque<>();
        q.add(new int[]{sx, sy});
        vis[sy][sx] = true;

        // Четыре направления обхода: вправо, влево, вниз, вверх.
        final int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        // Основной цикл BFS: обрабатываем клетки «слой за слоем».
        while (!q.isEmpty()) {
            int[] c = q.poll();

            // Дошли до финиша — восстанавливаем путь, идя по prev назад к старту.
            if (c[0] == ex && c[1] == ey) {
                LinkedList<int[]> path = new LinkedList<>();
                int cx = ex, cy = ey;

                // Идём от финиша к старту; addFirst даёт правильный порядок (start → end).
                while (cx != -1) {
                    path.addFirst(new int[]{cx, cy});
                    int p = prev[cy][cx];
                    if (p == -1) break;    // дошли до старта — у него prev == -1
                    cx = p >> 16;          // распаковка координаты x из старших 16 бит
                    cy = p & 0xFFFF;       // распаковка координаты y из младших 16 бит
                }
                return path;
            }

            // Перебираем четырёх соседей текущей клетки.
            for (int[] d : dirs) {
                int nx = c[0] + d[0], ny = c[1] + d[1];

                // Отсекаем выход за границы сетки.
                if (nx < 0 || nx >= m.cols || ny < 0 || ny >= m.rows) continue;

                // Пропускаем уже посещённые клетки и стены.
                if (vis[ny][nx] || m.grid[ny][nx] != 0) continue;

                // Помечаем клетку как посещённую и запоминаем, откуда пришли.
                vis[ny][nx] = true;
                prev[ny][nx] = (c[0] << 16) | c[1];   // упаковка (x, y) в один int
                q.add(new int[]{nx, ny});
            }
        }

        // Очередь опустела, а финиш так и не достигнут — пути нет.
        return null;
    }

    static void print(Maze m, List<int[]> path) {
        boolean[][] onPath = new boolean[m.rows][m.cols];
        if (path != null) for (int[] p : path) onPath[p[1]][p[0]] = true;

        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < m.rows; y++) {
            for (int x = 0; x < m.cols; x++) {
                char c;
                if (m.grid[y][x] == 1) c = WALL;
                else if (onPath[y][x]) c = ROUTE;
                else c = PASS;
                sb.append(c);
            }
            sb.append('\n');
        }
        System.out.print(sb);
    }

    public static void main(String[] args) {
        int w = 7, h = 4;
        long seed = System.nanoTime();
        if (args.length >= 1) w = Integer.parseInt(args[0]);
        if (args.length >= 2) h = Integer.parseInt(args[1]);
        if (args.length >= 3) seed = Long.parseLong(args[2]);

        if (w < 1 || h < 1) {
            System.err.println("Размеры должны быть >= 1");
            System.exit(1);
        }

        Maze m = generate(w, h, seed);

        System.out.println("Лабиринт " + w + "x" + h + " клеток, seed=" + seed);
        System.out.println("Все проходы достижимы: " + allReachable(m));

        // Вход — сверху (сетка [0][1]), выход — снизу ([rows-1][cols-2])
        int sx = 1,              sy = 0;
        int ex = m.cols - 2,     ey = m.rows - 1;

        List<int[]> path = findPath(m, sx, sy, ex, ey);
        if (path != null) {
            System.out.println("Длина пути: " + (path.size() - 1) + " шагов");
        } else {
            System.out.println("Путь не найден!");
        }
        print(m, path);
    }
}