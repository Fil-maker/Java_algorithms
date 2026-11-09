#include <iostream>

using namespace std;

const long long MOD = 1000000007LL;

long long power(long long a, long long b) {
    if (b == 0) return 1;
    if (b % 2 == 0) {
        long long half = power(a, b / 2);
        return half * half % MOD;
    }
    return a * power(a, b - 1) % MOD;
}

int main() {
    long long a, b;
    cin >> a >> b;
    cout << power(a % MOD, b) << '\n';
    return 0;
}
