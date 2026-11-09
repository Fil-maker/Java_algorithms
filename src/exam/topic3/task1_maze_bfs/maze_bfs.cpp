#include <iostream>
#include <queue>
#include <string>
#include <utility>
#include <vector>

using namespace std;

int main() {
    int rows, cols;
    cin >> rows >> cols;

    vector<string> maze(rows);
    int start_r = 0, start_c = 0, goal_r = 0, goal_c = 0;
    for (int r = 0; r < rows; r++) {
        cin >> maze[r];
        for (int c = 0; c < cols; c++) {
            if (maze[r][c] == 'S') { start_r = r; start_c = c; }
            if (maze[r][c] == 'G') { goal_r = r; goal_c = c; }
        }
    }

    const int DR[] = {-1, 1, 0, 0};
    const int DC[] = {0, 0, -1, 1};

    vector<vector<int>> dist(rows, vector<int>(cols, -1));
    queue<pair<int, int>> q;
    dist[start_r][start_c] = 0;
    q.push({start_r, start_c});

    while (!q.empty()) {
        auto [r, c] = q.front();
        q.pop();

        for (int d = 0; d < 4; d++) {
            int nr = r + DR[d], nc = c + DC[d];
            if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;
            if (maze[nr][nc] == '#' || dist[nr][nc] != -1) continue;

            dist[nr][nc] = dist[r][c] + 1;
            q.push({nr, nc});
        }
    }

    cout << dist[goal_r][goal_c] << '\n';
    return 0;
}
