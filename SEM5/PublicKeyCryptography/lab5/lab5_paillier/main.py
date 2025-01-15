import random
from math import gcd

from gmpy2 import gmpy2, mpz
from sympy import mod_inverse, isprime

# Character mapping for the alphabet (starting from 1 instead of 0)
CHAR_MAP = {char: idx + 1 for idx, char in enumerate(' ABCDEFGHIJKLMNOPQRSTUVWXYZ')}
REV_CHAR_MAP = {idx: char for char, idx in CHAR_MAP.items()}


def generate_keys(bit_length=512):
    """Generates a pair of public and private keys."""
    p = generate_large_prime(bit_length)
    q = generate_large_prime(bit_length)
    while p == q:
        q = generate_large_prime(bit_length)
    n = p * q
    n_squared = n * n

    # Choose g more carefully to ensure proper encryption/decryption
    g = n + 1  # This is a valid choice for g in Paillier cryptosystem

    lambda_val = lcm(p - 1, q - 1)  # Using lcm instead of product for better efficiency

    # Calculate mu using L(g^λ mod n^2) where L(x) = (x-1)/n (modular exponentiation)
    g_lambda = gmpy2.powmod(mpz(g), mpz(lambda_val), mpz(n_squared))
    l_val = (g_lambda - 1) // n
    mu = mod_inverse(int(l_val), n)

    public_key = (n, g)
    private_key = (lambda_val, mu)
    return public_key, private_key


def lcm(a, b):
    """Calculate the least common multiple of a and b."""
    return abs(a * b) // gcd(a, b)


def generate_large_prime(bit_length):
    """Generates a large prime number."""
    while True:
        num = random.getrandbits(bit_length)
        if isprime(num):
            return num


def encrypt(public_key, plaintext):
    """Encrypts plaintext using the public key."""
    n, g = public_key
    n_squared = n * n

    plaintext_num = text_to_number(plaintext)
    print(f"Debug - Original numerical representation: {plaintext_num}")

    if plaintext_num >= n:
        raise ValueError("Plaintext too large for the given key.")

    # Generate random r where gcd(r,n) = 1
    r = random.randint(1, n - 1)
    while gcd(r, n) != 1:
        r = random.randint(1, n - 1)

    # Encrypt using c = g^m * r^n mod n^2
    g_m = gmpy2.powmod(mpz(g), mpz(plaintext_num), mpz(n_squared))
    r_n = gmpy2.powmod(mpz(r), mpz(n), mpz(n_squared))
    c = (g_m * r_n) % n_squared

    return int(c)


def decrypt_paillier(ciphertext, private_key, public_key):
    """Decrypts a ciphertext using the Paillier cryptosystem."""
    n, g = public_key
    lambda_val, mu = private_key
    n_squared = n * n

    # Decrypt using m = L(c^λ mod n^2) * μ mod n
    c_lambda = gmpy2.powmod(mpz(ciphertext), mpz(lambda_val), mpz(n_squared))
    l_val = (c_lambda - 1) // n
    plaintext = (l_val * mu) % n

    print(f"Debug - Decrypted numerical value: {plaintext}")
    return int(plaintext)

def text_to_number(text):
    """Converts text to a numerical representation."""
    nums = []
    for char in text.upper():
        if char not in CHAR_MAP:
            raise ValueError(f"Invalid character '{char}' in input text")
        nums.append(f"{CHAR_MAP[char]:02}")
    result = ''.join(nums)
    return int(result)


def number_to_text(number):
    """Converts a numerical representation back to text."""
    num_str = str(number)
    if len(num_str) % 2 != 0:
        num_str = '0' + num_str

    print(f"Debug - Number string to decode: {num_str}")
    result = []

    for i in range(0, len(num_str), 2):
        value = int(num_str[i:i + 2])
        if value not in REV_CHAR_MAP:
            raise ValueError(
                f"Decryption failed: Unmapped value {value}. Valid values are {sorted(REV_CHAR_MAP.keys())}")
        result.append(REV_CHAR_MAP[value])

    return ''.join(result)



if __name__ == "_main_":
    print("Generating keys...")
    public_key, private_key = generate_keys()
    print("Public Key:", public_key)
    print("Private Key:", private_key)

    plaintext = "HELLO WORLD"
    print(f"\nOriginal text: {plaintext}")

    print("\nEncrypting...")
    ciphertext = encrypt(public_key, plaintext)
    print("Ciphertext:", ciphertext)

    print("\nDecrypting...")
    try:
        decrypted_number = decrypt_paillier(ciphertext, private_key, public_key)
        decrypted_text = number_to_text(decrypted_number)
        print("\nDecrypted text:", decrypted_text)
    except ValueError as e:
        print("Error during decryption:", e)
        print("\nValid character mappings:")
        for value, char in sorted(REV_CHAR_MAP.items()):
            print(f"{value}: '{char}'")