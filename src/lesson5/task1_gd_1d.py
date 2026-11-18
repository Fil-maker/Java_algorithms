"""
Задача 1. Градиентный спуск для функции одной переменной.

    f(x) = (x - 3)^2 + 1        минимум в точке x* = 3, f(x*) = 1
    f'(x) = 2 * (x - 3)

Правило шага:  x <- x - lr * f'(x)

Что посмотреть:
  * как learning rate (lr) влияет на число шагов;
  * при каком lr спуск начинает «прыгать» через минимум и расходиться;
  * совпадает ли численная производная с аналитической.
"""


def f(x):
    return (x - 3) ** 2 + 1


def grad(x):
    return 2 * (x - 3)


def numeric_grad(func, x, h=1e-6):
    # центральная разность: (f(x+h) - f(x-h)) / 2h
    return (func(x + h) - func(x - h)) / (2 * h)


def gradient_descent(lr, x0=0.0, eps=1e-6, max_steps=1000):
    """Возвращает (точку, число шагов). Останавливаемся, когда |f'(x)| < eps."""
    x = x0
    for step in range(max_steps):
        g = grad(x)
        if abs(g) < eps:
            return x, step
        x = x - lr * g
        if abs(x) > 1e6:          # улетели — дальше считать бессмысленно
            return x, step + 1
    return x, max_steps


def main():
    print("Проверка производной в x = 0.5:")
    print(f"  аналитическая = {grad(0.5):.6f}")
    print(f"  численная     = {numeric_grad(f, 0.5):.6f}")
    print()

    print(f"{'lr':>5} | {'шагов':>6} | {'x':>14} | результат")
    print("-" * 46)
    for lr in [0.01, 0.1, 0.5, 0.9, 1.0, 1.1]:
        x, steps = gradient_descent(lr)
        if abs(x - 3) < 1e-3:
            verdict = "сошёлся"
        elif abs(x) > 1e6:
            verdict = "РАЗОШЁЛСЯ"
        else:
            verdict = "не сошёлся"
        print(f"{lr:>5} | {steps:>6} | {x:>14.6g} | {verdict}")


if __name__ == "__main__":
    main()
