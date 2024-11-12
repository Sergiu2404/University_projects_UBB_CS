from math import gcd

def is_prime(number):
    if number <= 2:
        return number == 2

    if number % 2 == 0:
        return False

    for divisor in range(3, int(number ** 0.5) + 1, 2):
        if number % divisor == 0:
            return False

    return True

def is_carmichael(n):

    # a Carmichael number is an odd composite number
    if n <= 2 or n % 2 == 0 or is_prime(n):
        return False

    for a in range(3, n, 2):
        if gcd(a, n) == 1:
            if pow(a, n - 1, n) != 1:
                return False

    return True

def print_carmichael(maximum):
    for number in range(maximum):
        if is_carmichael(number):
            print(number)

print_carmichael(100_000)