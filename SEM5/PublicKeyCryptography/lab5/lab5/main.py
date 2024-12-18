import random
from math import gcd

from gmpy2 import gmpy2, mpz
from sympy import mod_inverse, isprime

# Character mapping for the alphabet
CHAR_MAP = {char: idx for idx, char in enumerate(' ABCDEFGHIJKLMNOPQRSTUVWXYZ')}
REV_CHAR_MAP = {idx: char for char, idx in CHAR_MAP.items()}


def generate_keys(bit_length=512):
    """Generates a pair of public and private keys."""
    # Generate two large distinct primes p and q
    p = generate_large_prime(bit_length)
    q = generate_large_prime(bit_length)
    while p == q:
        q = generate_large_prime(bit_length)
    n = p * q
    n_squared = n * n
    g = random.randint(1, n_squared)  # Random g in Z*(n^2)

    # Ensure gcd(L(g^lambda mod n^2), n) = 1
    lambda_val = (p - 1) * (q - 1)
    mu = mod_inverse(lambda_val, n)

    public_key = (n, g)
    private_key = (lambda_val, mu)
    return public_key, private_key


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

    # Convert plaintext to numerical representation
    plaintext_num = text_to_number(plaintext)
    print("plaintext ")
    print(plaintext_num)
    if plaintext_num >= n:
        raise ValueError("Plaintext too large for the given key.")

    r = random.randint(1, n - 1)
    while gcd(r, n) != 1:
        r = random.randint(1, n - 1)

    c = (pow(g, plaintext_num, n_squared) * pow(r, n, n_squared)) % n_squared
    return c

def decrypt_paillier(ciphertext, private_key, public_key):
    """
    Decrypts a ciphertext using the Paillier cryptosystem.

    Args:
        ciphertext (int): The ciphertext to decrypt.
        private_key (tuple): The private key (lambda, mu).
        public_key (tuple): The public key (n, g).

    Returns:
        int: The plaintext.
    """
    n, g = public_key
    lambda_, mu = private_key

    # Compute n^2
    n_squared = n * n

    # Compute L(c^lambda mod n^2)
    c_lambda = gmpy2.powmod(mpz(ciphertext), mpz(lambda_), mpz(n_squared))
    l_c_lambda = (c_lambda - 1) // n

    # Decrypt plaintext using L(c^lambda mod n^2) * mu mod n
    plaintext = (l_c_lambda * mu) % n

    return int(plaintext)


def text_to_number(text):
    """Converts text to a compact numerical representation."""
    return int(''.join(f"{CHAR_MAP[char]:02}" for char in text))

def number_to_text(number):
    """Converts a compact numerical representation back to text."""
    num_str = str(number)
    if len(num_str) % 2 != 0:
        num_str = num_str.zfill(len(num_str) + 1)

    try:
        return ''.join(REV_CHAR_MAP[int(num_str[i:i + 2])] for i in range(0, len(num_str), 2))
    except KeyError as e:
        raise ValueError(f"Decryption failed: Unmapped value {e}.")



# Example Usage
if __name__ == "__main__":
    print("Generating keys...")
    public_key, private_key = generate_keys()
    print("Public Key:", public_key)
    print("Private Key:", private_key)

    plaintext = "HELLO WORLD"
    print(f"text to encrypt {plaintext}")
    ciphertext = encrypt(public_key, plaintext)
    print("Ciphertext:", ciphertext)

    try:
        decrypted_text = decrypt_paillier(ciphertext, private_key, public_key)
        print("Decrypted Text:", decrypted_text)
    except ValueError as e:
        print("Error during decryption:", e)