import sys


def argmax(values):
    """Индекс наибольшего элемента; при равенстве — наименьший индекс."""
    best = 0
    for a in range(1, len(values)):
        if values[a] > values[best]:
            best = a
    return best


def main():
    data = sys.stdin.read().split()
    n, m = int(data[0]), int(data[1])
    alpha, gamma = float(data[2]), float(data[3])
    k = int(data[4])

    Q = [[0.0] * m for _ in range(n)]

    pos = 5
    for _ in range(k):
        s, a = int(data[pos]), int(data[pos + 1])
        r = float(data[pos + 2])
        s2, done = int(data[pos + 3]), int(data[pos + 4])
        pos += 5

        target = r
        if done == 0:
            target += gamma * max(Q[s2])
        Q[s][a] += alpha * (target - Q[s][a])

    for row in Q:
        print(" ".join(f"{q:.3f}" for q in row))
    print(" ".join(str(argmax(row)) for row in Q))


main()
