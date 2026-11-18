"""
Задача 3. Гонка оптимизаторов: GD, Momentum, RMSProp, Adam.

Функция-«овраг»:  f(x, y) = x^2 + 50 * y^2,  минимум в (0, 0).
По y она в 50 раз круче, чем по x — обычный GD тут «скачет» поперёк оврага
и медленно ползёт вдоль него.

Каждый оптимизатор — это функция step(theta, g, state) -> новый theta.
В state хранится «память» метода (векторы v, m) — это та самая
дополнительная память из лекции: Momentum +1 вектор, Adam +2 вектора.
"""
import math


def f(p):
    x, y = p
    return x * x + 50 * y * y


def grad(p):
    x, y = p
    return [2 * x, 100 * y]


# ---------- оптимизаторы ----------

def make_gd(lr):
    def step(p, g, state):
        return [pi - lr * gi for pi, gi in zip(p, g)]
    return step


def make_momentum(lr, beta=0.9):
    def step(p, g, state):
        v = state.setdefault("v", [0.0] * len(p))
        for i in range(len(p)):
            v[i] = beta * v[i] + (1 - beta) * g[i]
        return [pi - lr * vi for pi, vi in zip(p, v)]
    return step


def make_rmsprop(lr, beta=0.9, eps=1e-8):
    def step(p, g, state):
        v = state.setdefault("v", [0.0] * len(p))
        new_p = []
        for i in range(len(p)):
            v[i] = beta * v[i] + (1 - beta) * g[i] ** 2
            new_p.append(p[i] - lr * g[i] / (math.sqrt(v[i]) + eps))
        return new_p
    return step


def make_adam(lr, b1=0.9, b2=0.999, eps=1e-8):
    def step(p, g, state):
        m = state.setdefault("m", [0.0] * len(p))
        v = state.setdefault("v", [0.0] * len(p))
        state["t"] = t = state.get("t", 0) + 1
        new_p = []
        for i in range(len(p)):
            m[i] = b1 * m[i] + (1 - b1) * g[i]
            v[i] = b2 * v[i] + (1 - b2) * g[i] ** 2
            m_hat = m[i] / (1 - b1 ** t)       # поправка на смещение
            v_hat = v[i] / (1 - b2 ** t)
            new_p.append(p[i] - lr * m_hat / (math.sqrt(v_hat) + eps))
        return new_p
    return step


# ---------- запуск ----------

def run(step, start=(-8.0, 2.0), tol=1e-3, max_steps=5000):
    """Сколько шагов нужно, чтобы f(p) < tol."""
    p, state = list(start), {}
    for k in range(max_steps):
        if f(p) < tol:
            return k
        if max(abs(c) for c in p) > 1e6:     # разошёлся
            return max_steps
        p = step(p, grad(p), state)
    return max_steps


def main():
    racers = {
        # GD: lr ограничен крутым направлением y — больше 0.02 нельзя
        "GD (lr=0.019)":      make_gd(0.019),
        "GD (lr=0.021)":      make_gd(0.021),
        "Momentum (lr=0.05)": make_momentum(0.05),
        "RMSProp (lr=0.2)":   make_rmsprop(0.2),
        "Adam (lr=0.5)":      make_adam(0.5),
    }
    print("Шагов до f < 0.001, старт (-8, 2):")
    for name, step in racers.items():
        k = run(step)
        print(f"  {name:20} {k if k < 5000 else 'не сошёлся'}")


if __name__ == "__main__":
    main()
