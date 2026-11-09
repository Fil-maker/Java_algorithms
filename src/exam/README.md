# Экзамен: практические задачи

**Формат:** 3 задачи за 40 минут, на компьютере, без LLM и интернета. Язык на выбор: Java, Python или C++.

Решение читает данные из стандартного ввода и печатает ответ в стандартный вывод.

> Это папка преподавателя: решения лежат рядом с условиями. Студентам выдаются только файлы `README.md` из папок задач.

## Задачи

| № | Тема | Задача | Что проверяется |
|---|------|--------|-----------------|
| 1.1 | Асимптотика и рекуррентные соотношения | [Сортировка слиянием](topic1/task1_merge_sort/README.md) | `T(n) = 2T(n/2) + Θ(n)` |
| 1.2 | Асимптотика и рекуррентные соотношения | [Быстрое возведение в степень](topic1/task2_fast_power/README.md) | `T(b) = T(b/2) + O(1)` |
| 2.1 | Амортизационный анализ и структуры данных | [Двоичная max-куча](topic2/task1_max_heap/README.md) | просеивание вверх и вниз |
| 2.2 | Амортизационный анализ и структуры данных | [Хеш-таблица с цепочками](topic2/task2_hash_table/README.md) | разрешение коллизий |
| 3.1 | Графовые алгоритмы | [Кратчайший путь в лабиринте](topic3/task1_maze_bfs/README.md) | BFS на сетке |
| 3.2 | Графовые алгоритмы | [Алгоритм Дейкстры](topic3/task2_dijkstra/README.md) | кратчайшие пути во взвешенном графе |
| 4.1 | ДП и обучение с подкреплением | [Value Iteration в коридоре](topic4/task1_value_iteration/README.md) | уравнение Беллмана |
| 4.2 | ДП и обучение с подкреплением | [Q-learning по журналу опыта](topic4/task2_q_learning/README.md) | формула обновления Q |

## Запуск решений

Команды выполняются из корня репозитория, на примере задачи 1.1.

Java:

```bash
javac -encoding UTF-8 -d out src/exam/topic1/task1_merge_sort/MergeSort.java
```

```bash
java -cp out exam.topic1.task1_merge_sort.MergeSort
```

Python:

```bash
python src/exam/topic1/task1_merge_sort/merge_sort.py
```

C++ (папка `out` должна существовать):

```bash
mkdir -p out
```

```bash
g++ -std=c++17 -O2 -o out/merge_sort src/exam/topic1/task1_merge_sort/merge_sort.cpp
```

```bash
./out/merge_sort
```
