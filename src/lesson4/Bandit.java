package lesson4;

import java.util.Random;

/**
 * Шаг 1. Многорукий бандит — самая простая задача RL.
 *
 * Есть 3 игровых автомата. У каждого своя (скрытая от агента!) вероятность выигрыша.
 * Агент дёргает ручки и по наградам учится понимать, какой автомат лучший.
 *
 * Здесь ещё нет состояний — только действия и награды.
 * Главная идея: баланс между ИССЛЕДОВАНИЕМ (попробовать другой автомат)
 * и ИСПОЛЬЗОВАНИЕМ (дёргать тот, что сейчас кажется лучшим) — ε-жадная стратегия.
 */
public class Bandit {

    static final double[] WIN_PROB = {0.2, 0.5, 0.8};   // агент этого НЕ знает

    static final Random rnd = new Random(42);

    // Среда: дёрнули ручку a — получили награду 1 (выигрыш) или 0
    static int pull(int a) {
        return rnd.nextDouble() < WIN_PROB[a] ? 1 : 0;
    }

    static int argmax(double[] values) {
        int best = 0;
        for (int i = 1; i < values.length; i++) if (values[i] > values[best]) best = i;
        return best;
    }

    /** Играем steps раз, с вероятностью epsilon выбираем случайный автомат. */
    static int play(int steps, double epsilon, boolean print) {
        double[] estimate = new double[WIN_PROB.length];   // Q(a) — наша оценка автомата
        int[] count = new int[WIN_PROB.length];            // сколько раз дёргали
        int total = 0;

        for (int t = 0; t < steps; t++) {
            int a = rnd.nextDouble() < epsilon
                    ? rnd.nextInt(WIN_PROB.length)   // исследуем
                    : argmax(estimate);              // используем лучшее
            int reward = pull(a);
            total += reward;

            // Обновление среднего: Q ← Q + (1/n)·(r − Q)
            count[a]++;
            estimate[a] += (reward - estimate[a]) / count[a];
        }

        if (print) {
            System.out.printf("ε = %.2f, всего выигрышей: %d из %d%n", epsilon, total, steps);
            for (int a = 0; a < WIN_PROB.length; a++) {
                System.out.printf("  автомат %d: оценка %.2f (на самом деле %.2f), дёргали %d раз%n",
                        a, estimate[a], WIN_PROB[a], count[a]);
            }
            System.out.println();
        }
        return total;
    }

    public static void main(String[] args) {
        play(1000, 1.0, true);    // только случайные действия
        play(1000, 0.0, true);    // только жадность: может «залипнуть» на плохом автомате
        play(1000, 0.1, true);    // баланс
    }
}
