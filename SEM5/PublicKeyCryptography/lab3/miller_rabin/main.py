import random

def modular_exponentiation(x, y, p):
    res = 1
    x = x % p  # Update x if it is more than or equal to p
    while y > 0:
        # If y is odd, multiply x with result
        if y & 1:
            res = (res * x) % p
        # y must be even now
        y = y >> 1  # y = y // 2
        x = (x * x) % p  # Change x to x^2
    return res

def miller_rabin(n, k=5):
    if n <= 1:
        return False
    if n == 2 or n == 3:
        return True
    if n % 2 == 0:
        return False

    # Write n-1 as d * 2^r
    r, d = 0, n - 1
    while d % 2 == 0:
        d //= 2
        r = r+1

    for _ in range(k):
        # Pick a random number a in the range [2, n-2]
        a = random.randint(2, n - 2)
        # a^d % n
        x = modular_exponentiation(a, d, n)
        if x == 1 or x == n - 1:
            continue

        for _ in range(r - 1):
            x = (x * x) % n
            if x == n - 1:
                break
        else:
            return False
    return True

num = 20
if miller_rabin(num, k=10):
    print(f"{num} is probably prime.")
else:
    print(f"{num} is composite.")
