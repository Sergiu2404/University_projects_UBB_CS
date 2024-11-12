import random
from functools import reduce


def compute_gcd(x, y):
    while y:
        x, y = y, x % y
    return x


def compute_lcm(x, y):
    # Least common multiple (LCM)
    return (x * y) // compute_gcd(x, y)


def compute_lcm_list(numbers):
    # Computes the LCM of the list of numbers from 1 to B
    return reduce(compute_lcm, numbers, 1)


def compute_modular_squaring(a, k, n):
    # This computes a^k mod n without calculating large number a^k
    return pow(a, k, n)


def pollard_p_1(n, B=13, a=2):
    # Pollard's p-1 algorithm to find a non-trivial factor of the composite number n.
    # Compute the least common multiple (LCM) of numbers from 1 to B.
    # Choose a base `a` and compute a^k mod n, where k is the LCM.
    # Compute the GCD of a^k - 1 with n to find a factor of n.

    # Compute the LCM of numbers from 1 to B (B is used for finding k computing the lowest common multiple of all integers lower than Bound)
    k_list = list(range(1, B + 1))
    k = compute_lcm_list(k_list)

    # If the base is not 2, pick a random base `a` such that gcd(a, n) = 1 (a and n are coprime)
    if a != 2:
        a = random.randint(2, n - 2)
        while compute_gcd(a, n) != 1: #repeat until gcd(a, n) = 1 => a and n coprime
            a = random.randint(2, n - 2)

    # Compute a^k mod n using modular exponentiation
    a = compute_modular_squaring(a, k, n)

    # Compute the GCD of (a^k - 1) and n to find a non-trivial factor
    d = compute_gcd(a - 1, n)

    # Return the factor, which is a non-trivial divisor of n if found
    return d


def get_nontrivial_factor(n):
    # repeatedly try Pollard's p-1 to find a non-trivial factor of n.

    while True:
        # User inputs the bound B for Pollard's p-1
        B = int(input("Enter the bound: "))

        # If B is 0, use the default bound (13)
        if B == 0:
            f = pollard_p_1(n)
        else:
            f = pollard_p_1(n, B)

        # Non-trivial factor not found (f == 1 or f == n), retry
        if f == 1 or f == n:
            print("Failure. Trying again...")
        else:
            # Non-trivial factor found
            print(f"Nontrivial factor found: {f} => {n} = {f} * {n // f}")


def main():
    # Main function to prompt user for an odd composite number and start the factorization process.
    n = int(input("Enter an odd composite number: "))
    get_nontrivial_factor(n)


if __name__ == "__main__":
    # Run the main function when the script is executed.
    main()
