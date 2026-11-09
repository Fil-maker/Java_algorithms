#include <iostream>
#include <string>
#include <utility>
#include <vector>

using namespace std;

class HashTable {
public:
    HashTable() : buckets(M) {}

    void put(int key, int value) {
        auto& bucket = buckets[hash_key(key)];
        for (auto& entry : bucket) {
            if (entry.first == key) {
                entry.second = value;
                return;
            }
        }
        bucket.push_back({key, value});
    }

    // Возвращает true и записывает значение в value, если ключ найден.
    bool get(int key, int& value) const {
        for (const auto& entry : buckets[hash_key(key)]) {
            if (entry.first == key) {
                value = entry.second;
                return true;
            }
        }
        return false;
    }

    void erase(int key) {
        auto& bucket = buckets[hash_key(key)];
        for (size_t i = 0; i < bucket.size(); i++) {
            if (bucket[i].first == key) {
                bucket.erase(bucket.begin() + i);
                return;
            }
        }
    }

private:
    static const int M = 100003;
    vector<vector<pair<int, int>>> buckets;

    int hash_key(int key) const {
        return ((key % M) + M) % M;
    }
};

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    int q;
    cin >> q;
    HashTable table;

    for (int i = 0; i < q; i++) {
        string command;
        int key;
        cin >> command >> key;

        if (command == "PUT") {
            int value;
            cin >> value;
            table.put(key, value);
        } else if (command == "GET") {
            int value;
            if (table.get(key, value)) cout << value << '\n';
            else cout << "NONE\n";
        } else {
            table.erase(key);
        }
    }
    return 0;
}
