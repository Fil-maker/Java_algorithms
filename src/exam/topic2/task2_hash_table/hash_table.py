import sys

M = 100_003


class HashTable:
    def __init__(self):
        self.buckets = [[] for _ in range(M)]

    def _bucket(self, key):
        return self.buckets[key % M]

    def put(self, key, value):
        bucket = self._bucket(key)
        for pair in bucket:
            if pair[0] == key:
                pair[1] = value
                return
        bucket.append([key, value])

    def get(self, key):
        """Возвращает значение или None, если ключа нет."""
        for pair in self._bucket(key):
            if pair[0] == key:
                return pair[1]
        return None

    def delete(self, key):
        bucket = self._bucket(key)
        for i in range(len(bucket)):
            if bucket[i][0] == key:
                bucket.pop(i)
                return


def main():
    lines = sys.stdin.read().splitlines()
    q = int(lines[0])
    table = HashTable()
    output = []

    for line in lines[1:q + 1]:
        parts = line.split()
        key = int(parts[1])
        if parts[0] == "PUT":
            table.put(key, int(parts[2]))
        elif parts[0] == "GET":
            value = table.get(key)
            output.append("NONE" if value is None else str(value))
        else:
            table.delete(key)

    if output:
        print("\n".join(output))


main()
