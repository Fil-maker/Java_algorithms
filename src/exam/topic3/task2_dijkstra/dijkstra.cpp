#include <functional>
#include <iostream>
#include <queue>
#include <utility>
#include <vector>

using namespace std;

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int n, m, s;
    cin >> n >> m >> s;

    // graph[u] — список рёбер из u в виде пар {v, w}
    vector<vector<pair<int, int>>> graph(n + 1);
    for (int i = 0; i < m; i++) {
        int u, v, w;
        cin >> u >> v >> w;
        graph[u].push_back({v, w});
    }

    vector<long long> dist(n + 1, -1);
    dist[s] = 0;

    // Элемент очереди — пара {расстояние, вершина}, сверху минимальное расстояние
    using Item = pair<long long, int>;
    priority_queue<Item, vector<Item>, greater<Item>> pq;
    pq.push({0, s});

    while (!pq.empty()) {
        auto [d, u] = pq.top();
        pq.pop();
        if (d != dist[u]) continue;  // устаревшая запись

        for (auto [v, w] : graph[u]) {
            long long candidate = d + w;
            if (dist[v] == -1 || candidate < dist[v]) {
                dist[v] = candidate;
                pq.push({candidate, v});
            }
        }
    }

    for (int v = 1; v <= n; v++) {
        if (v > 1) cout << ' ';
        cout << dist[v];
    }
    cout << '\n';
    return 0;
}
