import heapq
import sys


def main():
    data = sys.stdin.read().split()
    n, m, s = int(data[0]), int(data[1]), int(data[2])

    graph = [[] for _ in range(n + 1)]
    pos = 3
    for _ in range(m):
        u, v, w = int(data[pos]), int(data[pos + 1]), int(data[pos + 2])
        graph[u].append((v, w))
        pos += 3

    dist = [-1] * (n + 1)
    dist[s] = 0
    pq = [(0, s)]

    while pq:
        d, u = heapq.heappop(pq)
        if d != dist[u]:  # устаревшая запись
            continue
        for v, w in graph[u]:
            candidate = d + w
            if dist[v] == -1 or candidate < dist[v]:
                dist[v] = candidate
                heapq.heappush(pq, (candidate, v))

    print(" ".join(map(str, dist[1:])))


main()
