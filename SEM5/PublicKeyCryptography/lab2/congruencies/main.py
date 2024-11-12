# Function to compute the modular inverse of a number
def modular_inverse(a, m):
    # Using extended Euclidean algorithm to find the modular inverse
    m0, x0, x1 = m, 0, 1
    if m == 1:
        return 0
    while a > 1:
        # q is the quotient
        q = a // m
        m, a = a % m, m
        x0, x1 = x1 - q * x0, x0
    if x1 < 0:
        x1 += m0
    return x1


# Function to solve the system of congruences using the Chinese Remainder Theorem
def chinese_remainder_theorem(a, m):
    # a: list of remainders
    # m: list of moduli
    M = 1
    n = len(a)

    # Step 1: Compute M (the product of all moduli)
    for i in range(n):
        M *= m[i]

    result = 0

    # Step 2-4: Compute the solution using the CRT formula
    for i in range(n):
        # Mi = M / m[i]
        Mi = M // m[i]

        # Find the modular inverse of Mi modulo m[i]
        yi = modular_inverse(Mi, m[i])

        # Add the term a[i] * Mi * yi to the result
        result += a[i] * Mi * yi

    # The result should be taken modulo M
    return result % M, M


# Driver code
if __name__ == "__main__":
    # Example system of congruences:
    # x ≡ 2 (mod 3)
    # x ≡ 3 (mod 5)
    # x ≡ 2 (mod 7)

    a = [2, 3, 2]  # List of remainders
    m = [3, 5, 7]  # List of moduli

    # Solve the system of congruences
    solution, M = chinese_remainder_theorem(a, m)
    print(f"The solution to the system of congruences is x ≡ {solution} (mod {M})")
