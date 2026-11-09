import sys


class MaxHeap:
    def __init__(self):
        self.data = []

    def max(self):
        return self.data[0]

    def add(self, x):
        self.data.append(x)
        self._sift_up(len(self.data) - 1)

    def pop(self):
        top = self.data[0]
        last = self.data.pop()
        if self.data:
            self.data[0] = last
            self._sift_down(0)
        return top

    def _sift_up(self, i):
        """Поднимаем элемент, пока он больше родителя."""
        data = self.data
        while i > 0:
            parent = (i - 1) // 2
            if data[i] <= data[parent]:
                break
            data[i], data[parent] = data[parent], data[i]
            i = parent

    def _sift_down(self, i):
        """Опускаем элемент, пока он меньше большего из детей."""
        data = self.data
        size = len(data)
        while True:
            left = 2 * i + 1
            right = 2 * i + 2
            largest = i

            if left < size and data[left] > data[largest]:
                largest = left
            if right < size and data[right] > data[largest]:
                largest = right
            if largest == i:
                break

            data[i], data[largest] = data[largest], data[i]
            i = largest


def main():
    lines = sys.stdin.read().splitlines()
    q = int(lines[0])
    heap = MaxHeap()
    output = []

    for line in lines[1:q + 1]:
        parts = line.split()
        if parts[0] == "ADD":
            heap.add(int(parts[1]))
        elif parts[0] == "MAX":
            output.append(heap.max())
        else:
            output.append(heap.pop())

    if output:
        print("\n".join(map(str, output)))


main()
