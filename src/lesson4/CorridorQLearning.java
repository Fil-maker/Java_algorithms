package lesson4;

import java.util.Random;

/**
 * Шаг 2. Q-learning в коридоре — появляются СОСТОЯНИЯ.
 *
 *     [0] [1] [2] [3] [4] [5]
 *      S                   G
 *
 * Агент стоит в клетке 0, цель — клетка 5. Действия: 0 = влево, 1 = вправо.
 * Награда: −1 за каждый шаг, +10 за приход в цель. Эпизод заканчивается в цели.
 *
 * Агент не знает, где цель. Он учит таблицу Q[s][a] — «насколько хорошо
 * сделать действие a в состоянии s» — по формуле (уравнение Беллмана):
 *
 *     Q(s,a) ← Q(s,a) + α · [ r + γ · max Q(s',·) − Q(s,a) ]
 */
public class CorridorQLearning {

    static final int N = 6;              // число клеток
    static final int GOAL = N - 1;
    static final int LEFT = 0, RIGHT = 1;

    static final double ALPHA = 0.5;     // скорость обучения
    static final double GAMMA = 0.9;     // насколько важны будущие награды
    static final double EPSILON = 0.2;   // доля случайных действий

    static final Random rnd = new Random(1);

    // ===== Среда =====
    static int nextState(int s, int a) {
        int s2 = (a == RIGHT) ? s + 1 : s - 1;
        return Math.max(0, Math.min(N - 1, s2));   // в стену не выйти
    }

    static double reward(int s2) {
        return s2 == GOAL ? 10 : -1;
    }

    // ===== Агент =====
    static int bestAction(double[] q) {
        return q[RIGHT] > q[LEFT] ? RIGHT : LEFT;
    }

    static int chooseAction(double[] q) {
        if (rnd.nextDouble() < EPSILON) return rnd.nextInt(2);   // исследуем
        return bestAction(q);                                    // используем
    }

    public static void main(String[] args) {
        double[][] Q = new double[N][2];

        for (int episode = 1; episode <= 50; episode++) {
            int s = 0;
            int steps = 0;
            while (s != GOAL) {
                int a = chooseAction(Q[s]);
                int s2 = nextState(s, a);
                double r = reward(s2);

                double future = (s2 == GOAL) ? 0 : Math.max(Q[s2][LEFT], Q[s2][RIGHT]);
                Q[s][a] += ALPHA * (r + GAMMA * future - Q[s][a]);

                s = s2;
                steps++;
            }
            if (episode <= 3 || episode % 10 == 0) {
                System.out.printf("эпизод %2d: дошёл до цели за %2d шагов%n", episode, steps);
            }
        }

        System.out.println();
        System.out.println("Таблица Q (строка = клетка):");
        System.out.println("  s |   влево | вправо | лучшее");
        for (int s = 0; s < N; s++) {
            String best = s == GOAL ? "цель" : (bestAction(Q[s]) == RIGHT ? "→" : "←");
            System.out.printf("  %d | %7.2f | %6.2f | %s%n", s, Q[s][LEFT], Q[s][RIGHT], best);
        }
    }
}
