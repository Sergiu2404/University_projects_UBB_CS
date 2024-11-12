def mod_exp(base, exp, mod):
    result = 1
    base = base % mod
    while exp > 0:
        if exp % 2 == 1:  # If exp is odd, multiply base with result
            result = (result * base) % mod
        exp = exp >> 1    # Divide exp by 2
        base = (base * base) % mod  # Square the base
    return result

def miller_rabin_test(n, b):
    if n % 2 == 0 or n == 1:
        return False

    # Write n - 1 as 2^s * d
    d = n - 1
    s = 0
    while d % 2 == 0:
        d //= 2
        s += 1

    # Compute b^d mod n
    x = mod_exp(b, d, n)

    if x == 1 or x == n - 1:
        return True

    # Square x repeatedly up to s-1 times
    for _ in range(s - 1):
        x = mod_exp(x, 2, n)
        if x == n - 1:
            return True

    return False

def find_strong_pseudoprime_bases(n):
    bases = []
    # Check all bases from 2 to n-1
    for b in range(2, n):
        if miller_rabin_test(n, b):
            bases.append(b)
    return bases

# Example usage:
if __name__ == "__main__":
    n = 91  # Example odd composite number
    bases = find_strong_pseudoprime_bases(n)
    print(f"Bases where {n} is a strong pseudoprime: {bases}")
