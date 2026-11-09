#include <cstdio>
#include <iostream>
#include <vector>

using namespace std;

// Индекс наибольшего элемента; при равенстве — наименьший индекс.
int argmax(const vector<double>& values) {
    int best = 0;
    for (int a = 1; a < (int) values.size(); a++) {
        if (values[a] > values[best]) best = a;
    }
    return best;
}

int main() {
    int n, m, k;
    double alpha, gamma;
    cin >> n >> m >> alpha >> gamma >> k;

    vector<vector<double>> Q(n, vector<double>(m, 0.0));

    for (int i = 0; i < k; i++) {
        int s, a, s2, done;
        double r;
        cin >> s >> a >> r >> s2 >> done;

        double target = r;
        if (done == 0) target += gamma * Q[s2][argmax(Q[s2])];
        Q[s][a] += alpha * (target - Q[s][a]);
    }

    for (int s = 0; s < n; s++) {
        for (int a = 0; a < m; a++) {
            if (a > 0) printf(" ");
            printf("%.3f", Q[s][a]);
        }
        printf("\n");
    }
    for (int s = 0; s < n; s++) {
        if (s > 0) printf(" ");
        printf("%d", argmax(Q[s]));
    }
    printf("\n");
    return 0;
}
