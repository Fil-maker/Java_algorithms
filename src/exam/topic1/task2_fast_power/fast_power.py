MOD = 1_000_000_007


def power(a, b):
    if b == 0:
        return 1
    if b % 2 == 0:
        half = power(a, b // 2)
        return half * half % MOD
    return a * power(a, b - 1) % MOD


def main():
    a, b = map(int, input().split())
    print(power(a % MOD, b))


main()
