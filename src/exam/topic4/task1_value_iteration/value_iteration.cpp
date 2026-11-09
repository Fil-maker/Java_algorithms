#include <algorithm>
#include <cmath>
#include <cstdio>
#include <iostream>
#include <vector>

using namespace std;

int main() {
    int n;
    double gamma;
    cin >> n >> gamma;

    vector<double> r(n);
    for (int i = 0; i < n; i++) cin >> r[i];

    vector<double> V(n, 0.0);  // V[n - 1] всегда равно 0
    double delta;
    do {
        delta = 0;
        for (int s = 0; s < n - 1; s++) {
            int left = max(s - 1, 0);
            int right = s + 1;
            double best = max(r[left] + gamma * V[left], r[right] + gamma * V[right]);

            delta = max(delta, fabs(best - V[s]));
            V[s] = best;
        }
    } while (delta >= 1e-9);

    for (int s = 0; s < n; s++) {
        if (s > 0) printf(" ");
        printf("%.3f", V[s]);
    }
    printf("\n");
    return 0;
}
