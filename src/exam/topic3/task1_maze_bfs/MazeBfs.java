package exam.topic3.task1_maze_bfs;

import java.io.*;
import java.util.*;

public class MazeBfs {

    static final int[] DR = {-1, 1, 0, 0};
    static final int[] DC = {0, 0, -1, 1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int rows = Integer.parseInt(st.nextToken());
        int cols = Integer.parseInt(st.nextToken());

        String[] maze = new String[rows];
        int startR = 0, startC = 0, goalR = 0, goalC = 0;
        for (int r = 0; r < rows; r++) {
            maze[r] = br.readLine().trim();
            for (int c = 0; c < cols; c++) {
                if (maze[r].charAt(c) == 'S') { startR = r; startC = c; }
                if (maze[r].charAt(c) == 'G') { goalR = r; goalC = c; }
            }
        }

        int[][] dist = new int[rows][cols];
        for (int[] row : dist) Arrays.fill(row, -1);

        // Клетка (r, c) хранится в очереди как одно число r * cols + c.
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        dist[startR][startC] = 0;
        queue.add(startR * cols + startC);

        while (!queue.isEmpty()) {
            int cell = queue.poll();
            int r = cell / cols, c = cell % cols;

            for (int d = 0; d < 4; d++) {
                int nr = r + DR[d], nc = c + DC[d];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;
                if (maze[nr].charAt(nc) == '#' || dist[nr][nc] != -1) continue;

                dist[nr][nc] = dist[r][c] + 1;
                queue.add(nr * cols + nc);
            }
        }

        System.out.println(dist[goalR][goalC]);
    }
}
