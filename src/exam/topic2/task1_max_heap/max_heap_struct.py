"""Та же max-куча, но оформленная как структура данных и набор функций.

Сравните с max_heap.py, где данные и операции объединены в класс.
"""
import sys
from dataclasses import dataclass, field


@dataclass
class Heap:
    """Структура: только данные, никакого поведения."""
    data: list = field(default_factory=list)


def sift_up(h, i):
    """Поднимаем элемент, пока он больше родителя."""
    while i > 0:
        parent = (i - 1) // 2
        if h.data[i] <= h.data[parent]:
            break
        h.data[i], h.data[parent] = h.data[parent], h.data[i]
        i = parent


def sift_down(h, i):
    """Опускаем элемент, пока он меньше большего из детей."""
    size = len(h.data)
    while True:
        left, right = 2 * i + 1, 2 * i + 2
        largest = i
        if left < size and h.data[left] > h.data[largest]:
            largest = left
        if right < size and h.data[right] > h.data[largest]:
            largest = right
        if largest == i:
            break
        h.data[i], h.data[largest] = h.data[largest], h.data[i]
        i = largest


def heap_add(h, x):
    h.data.append(x)
    sift_up(h, len(h.data) - 1)


def heap_max(h):
    return h.data[0]


def heap_pop(h):
    top = h.data[0]
    last = h.data.pop()
    if h.data:
        h.data[0] = last
        sift_down(h, 0)
    return top


def main():
    lines = sys.stdin.read().splitlines()
    q = int(lines[0])
    heap = Heap()
    output = []

    for line in lines[1:q + 1]:
        parts = line.split()
        if parts[0] == "ADD":
            heap_add(heap, int(parts[1]))
        elif parts[0] == "MAX":
            output.append(heap_max(heap))
        else:
            output.append(heap_pop(heap))

    if output:
        print("\n".join(map(str, output)))


main()
