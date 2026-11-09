package exam.topic1.task2_fast_power;

import java.io.*;
import java.util.*;

public class FastPower {

    static final long MOD = 1_000_000_007L;

    static long power(long a, long b) {
        if (b == 0) return 1;
        if (b % 2 == 0) {
            long half = power(a, b / 2);
            return half * half % MOD;
        }
        return a * power(a, b - 1) % MOD;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        long a = Long.parseLong(st.nextToken());
        long b = Long.parseLong(st.nextToken());

        System.out.println(power(a % MOD, b));
    }
}
