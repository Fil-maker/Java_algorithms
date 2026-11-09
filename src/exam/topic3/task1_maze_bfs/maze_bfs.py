import sys
from collections import deque


def main():
    data = sys.stdin.read().split()
    rows, cols = int(data[0]), int(data[1])
    maze = data[2:2 + rows]

    start = goal = None
    for r in range(rows):
        for c in range(cols):
            if maze[r][c] == "S":
                start = (r, c)
            if maze[r][c] == "G":
                goal = (r, c)

    dist = [[-1] * cols for _ in range(rows)]
    dist[start[0]][start[1]] = 0
    queue = deque([start])

    while queue:
        r, c = queue.popleft()
        for dr, dc in ((-1, 0), (1, 0), (0, -1), (0, 1)):
            nr, nc = r + dr, c + dc
            if nr < 0 or nr >= rows or nc < 0 or nc >= cols:
                continue
            if maze[nr][nc] == "#" or dist[nr][nc] != -1:
                continue
            dist[nr][nc] = dist[r][c] + 1
            queue.append((nr, nc))

    print(dist[goal[0]][goal[1]])


main()
