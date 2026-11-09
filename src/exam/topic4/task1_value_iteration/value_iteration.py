import sys


def main():
    data = sys.stdin.read().split()
    n = int(data[0])
    gamma = float(data[1])
    r = [float(x) for x in data[2:2 + n]]

    V = [0.0] * n  # V[n - 1] всегда равно 0
    while True:
        delta = 0.0
        for s in range(n - 1):
            left = max(s - 1, 0)
            right = s + 1
            best = max(r[left] + gamma * V[left], r[right] + gamma * V[right])

            delta = max(delta, abs(best - V[s]))
            V[s] = best
        if delta < 1e-9:
            break

    print(" ".join(f"{v:.3f}" for v in V))


main()
