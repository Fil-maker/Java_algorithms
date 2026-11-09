#include <iostream>
#include <string>
#include <utility>
#include <vector>

using namespace std;

class MaxHeap {
public:
    int max() const {
        return data[0];
    }

    void add(int x) {
        data.push_back(x);
        sift_up((int) data.size() - 1);
    }

    int pop() {
        int top = data[0];
        data[0] = data.back();
        data.pop_back();
        if (!data.empty()) sift_down(0);
        return top;
    }

private:
    vector<int> data;

    // Поднимаем элемент, пока он больше родителя.
    void sift_up(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (data[i] <= data[parent]) break;
            swap(data[i], data[parent]);
            i = parent;
        }
    }

    // Опускаем элемент, пока он меньше большего из детей.
    void sift_down(int i) {
        int size = (int) data.size();
        while (true) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int largest = i;

            if (left < size && data[left] > data[largest]) largest = left;
            if (right < size && data[right] > data[largest]) largest = right;
            if (largest == i) break;

            swap(data[i], data[largest]);
            i = largest;
        }
    }
};

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int q;
    cin >> q;
    MaxHeap heap;

    for (int i = 0; i < q; i++) {
        string command;
        cin >> command;

        if (command == "ADD") {
            int x;
            cin >> x;
            heap.add(x);
        } else if (command == "MAX") {
            cout << heap.max() << '\n';
        } else {
            cout << heap.pop() << '\n';
        }
    }
    return 0;
}
