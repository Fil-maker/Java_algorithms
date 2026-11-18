"""
Задача 4. Один нейрон учит логическое ИЛИ (OR) — backprop вручную.

    z = w1*x1 + w2*x2 + b          (линейная часть)
    a = sigmoid(z)                 (активация)
    L = (a - y)^2                  (ошибка)

Цепное правило (chain rule):

    dL/dw1 = dL/da * da/dz * dz/dw1 = 2(a - y) * a(1 - a) * x1
    dL/dw2 = ...                    = 2(a - y) * a(1 - a) * x2
    dL/db  = ...                    = 2(a - y) * a(1 - a) * 1

sigmoid'(z) = sigmoid(z) * (1 - sigmoid(z)) — поэтому при forward
сохраняем a: на backward пересчитывать z не нужно (это и есть «память
под активации» из лекции).
"""
import math

DATA = [((0, 0), 0), ((0, 1), 1), ((1, 0), 1), ((1, 1), 1)]   # OR


def sigmoid(z):
    return 1 / (1 + math.exp(-z))


def train(lr=1.0, epochs=1000):
    w1, w2, b = 0.0, 0.0, 0.0
    for epoch in range(epochs + 1):
        total = 0.0
        g1 = g2 = gb = 0.0
        for (x1, x2), y in DATA:
            # forward
            a = sigmoid(w1 * x1 + w2 * x2 + b)
            total += (a - y) ** 2
            # backward
            delta = 2 * (a - y) * a * (1 - a)      # dL/dz
            g1 += delta * x1
            g2 += delta * x2
            gb += delta
        if epoch % 200 == 0:
            print(f"эпоха {epoch:4}: ошибка = {total:.4f}")
        # шаг градиентного спуска
        w1 -= lr * g1
        w2 -= lr * g2
        b -= lr * gb
    return w1, w2, b


def main():
    w1, w2, b = train()
    print(f"\nw1 = {w1:.2f}, w2 = {w2:.2f}, b = {b:.2f}")
    for (x1, x2), y in DATA:
        a = sigmoid(w1 * x1 + w2 * x2 + b)
        print(f"  {x1} OR {x2} = {y}   нейрон: {a:.3f}")


if __name__ == "__main__":
    main()
