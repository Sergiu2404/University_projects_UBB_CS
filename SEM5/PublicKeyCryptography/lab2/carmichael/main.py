def gcd(a, b):
    while b:
        a, b = b, a % b
    return a


def is_square_free(n):
    """Check if n is square-free."""
    for i in range(2, int(n ** 0.5) + 1):
        if n % (i * i) == 0:
            return False
    return True


def prime_factors(n):
    """Return the list of prime factors of n."""
    factors = []
    # Check for number of 2s
    if n % 2 == 0:
        factors.append(2)
        while n % 2 == 0:
            n //= 2
    # Check for odd factors from 3 to sqrt(n)
    for i in range(3, int(n ** 0.5) + 1, 2):
        if n % i == 0:
            factors.append(i)
            while n % i == 0:
                n //= i
    if n > 2:
        factors.append(n)
    return factors


def is_carmichael(n):
    """Check if n is a Carmichael number."""
    if n < 2 or is_square_free(n) is False:
        return False

    factors = prime_factors(n)
    for p in factors:
        if (n - 1) % (p - 1) != 0:
            return False
    return True


def find_carmichael_numbers(bound):
    """Find all Carmichael numbers less than the given bound."""
    carmichael_numbers = []
    for n in range(2, bound):
        if not is_square_free(n):
            continue
        if is_carmichael(n):
            carmichael_numbers.append(n)
    return carmichael_numbers


# Driver code
if __name__ == "__main__":
    bound = 10000  # Change this value for a different bound
    carmichael_numbers = find_carmichael_numbers(bound)
    print(f"Carmichael numbers less than {bound}:")
    print(carmichael_numbers)
