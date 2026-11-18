"""
Задача 2. Линейная регрессия y = A*x + B: точная формула, GD и SGD.

Данные — пять точек из лекции. Минимизируем среднеквадратичную ошибку

    L(A, B) = 1/n * sum (A*x_i + B - y_i)^2

Частные производные (chain rule):

    dL/dA = 2/n * sum (A*x_i + B - y_i) * x_i
    dL/dB = 2/n * sum (A*x_i + B - y_i)

GD  — градиент по всем точкам на каждом шаге:     O(n) на шаг.
SGD — градиент по одной случайной точке за шаг:  O(1) на шаг, но «шумно».
"""
import random

XS = [1, 3, 5, 7, 9]
YS = [4.5, 5.0, 12.0, 13.0, 20.0]


def loss(a, b):
    n = len(XS)
    return sum((a * x + b - y) ** 2 for x, y in zip(XS, YS)) / n


def exact_solution():
    """Решение СЛАУ dL/dA = 0, dL/dB = 0 (метод наименьших квадратов)."""
    n = len(XS)
    sx, sy = sum(XS), sum(YS)
    sxx = sum(x * x for x in XS)
    sxy = sum(x * y for x, y in zip(XS, YS))
    a = (n * sxy - sx * sy) / (n * sxx - sx * sx)
    b = (sy - a * sx) / n
    return a, b


def gd(lr=0.01, steps=5000):
    a, b = 0.0, 0.0
    n = len(XS)
    for _ in range(steps):
        da = db = 0.0
        for x, y in zip(XS, YS):
            err = a * x + b - y
            da += 2 * err * x / n
            db += 2 * err / n
        a -= lr * da
        b -= lr * db
    return a, b


def sgd(lr=0.005, steps=5000, seed=0):
    rnd = random.Random(seed)
    a, b = 0.0, 0.0
    for _ in range(steps):
        i = rnd.randrange(len(XS))          # одна случайная точка
        err = a * XS[i] + b - YS[i]
        a -= lr * 2 * err * XS[i]
        b -= lr * 2 * err
    return a, b


def main():
    for name, (a, b) in [("Точно (СЛАУ)", exact_solution()),
                         ("GD", gd()),
                         ("SGD", sgd())]:
        print(f"{name:13}  A = {a:.4f}  B = {b:.4f}  L = {loss(a, b):.4f}")


if __name__ == "__main__":
    main()
