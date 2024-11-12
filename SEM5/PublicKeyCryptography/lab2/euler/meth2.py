import math

# Step 1: Define Euler's Totient function
def euler_totient(n):
    result = n
    p = 2

    # Check for all numbers less than sqrt(n)
    while p * p <= n:
        # Check if p is a divisor of n
        if n % p == 0:
            # Remove all occurrences of p in n
            while n % p == 0:
                n //= p
            # Multiply result by (1 - 1/p)
            result -= result // p
        p += 1

    # If n is still greater than 1, then it's a prime number
    if n > 1:
        result -= result // n

    return result

# Step 2: Function to find all numbers whose Euler's function equals v
def find_numbers_with_totient(v, b):
    matching_numbers = []

    # Loop through all numbers less than the bound b
    for n in range(1, b):
        # Calculate Euler's Totient function for n
        if euler_totient(n) == v:
            matching_numbers.append(n)

    return matching_numbers

# Step 3: Example usage
if __name__ == "__main__":
    v = 8  # The given value for Euler's Totient function
    b = 50  # The upper bound

    result = find_numbers_with_totient(v, b)
    print(f"Numbers less than {b} with Euler's Totient value {v}: {result}")
