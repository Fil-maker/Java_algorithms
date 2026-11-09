#include <iostream>
#include <vector>

using namespace std;

// Сливает отсортированные части [left, mid) и [mid, right).
void merge_parts(vector<int>& a, vector<int>& buffer, int left, int mid, int right) {
    int i = left, j = mid, k = left;
    while (i < mid && j < right) {
        if (a[i] <= a[j]) buffer[k++] = a[i++];
        else buffer[k++] = a[j++];
    }
    while (i < mid) buffer[k++] = a[i++];
    while (j < right) buffer[k++] = a[j++];

    for (k = left; k < right; k++) a[k] = buffer[k];
}

// Сортирует полуинтервал [left, right).
void merge_sort(vector<int>& a, vector<int>& buffer, int left, int right) {
    if (right - left <= 1) return;

    int mid = (left + right) / 2;
    merge_sort(a, buffer, left, mid);
    merge_sort(a, buffer, mid, right);
    merge_parts(a, buffer, left, mid, right);
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int n;
    cin >> n;
    vector<int> a(n), buffer(n);
    for (int i = 0; i < n; i++) cin >> a[i];

    merge_sort(a, buffer, 0, n);

    for (int i = 0; i < n; i++) {
        if (i > 0) cout << ' ';
        cout << a[i];
    }
    cout << '\n';
    return 0;
}
