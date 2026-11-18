"""
Задача 5. Сколько памяти нужно оптимизатору?

Обозначения из лекции:
    d   — число параметров модели
    |B| — размер мини-батча
    f   — число признаков у одного примера
    FP32 — 4 байта на число

Каждый вектор длины d в FP32 занимает 4*d байт.
    SGD:      параметры + градиент                 = 2 вектора
    Momentum: + вектор скорости v                  = 3 вектора
    RMSProp:  + вектор EMA квадратов v             = 3 вектора
    Adam(W):  + первый m и второй v моменты         = 4 вектора
Плюс сам батч: 4 * |B| * f байт.
"""

VECTORS = {"SGD": 2, "Momentum": 3, "RMSProp": 3, "Adam": 4, "AdamW": 4}


def memory_bytes(optimizer, d, batch, features, bytes_per_number=4):
    return bytes_per_number * (VECTORS[optimizer] * d + batch * features)


def human(n):
    # десятичные единицы, как в лекции: 1 ГБ = 10^9 байт
    for unit in ["Б", "КБ", "МБ", "ГБ", "ТБ"]:
        if n < 1000:
            return f"{n:.1f} {unit}"
        n /= 1000
    return f"{n:.1f} ПБ"


def main():
    d, batch, features = 10 ** 8, 256, 100
    print(f"d = {d:.0e}, |B| = {batch}, f = {features}")
    for opt in VECTORS:
        print(f"  {opt:9} {human(memory_bytes(opt, d, batch, features))}")

    # Пример побольше: модель на 7 млрд параметров
    d = 7 * 10 ** 9
    print(f"\nd = 7e9 (7B модель), только векторы оптимизатора:")
    for opt in VECTORS:
        print(f"  {opt:9} {human(4 * VECTORS[opt] * d)}")


if __name__ == "__main__":
    main()
