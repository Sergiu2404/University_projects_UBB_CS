import random


ALPHABET = list(" ABCDEFGHIJKLMNOPQRSTUVWXYZ")


def is_prime(n):
    """
    Check if a number is prime.
    :param n: Integer to check.
    :return: True if n is prime, False otherwise.
    """
    if n <= 1:
        return False
    if n <= 3:
        return True
    if n % 2 == 0 or n % 3 == 0:
        return False
    i = 5
    while i * i <= n:
        if n % i == 0 or n % (i + 2) == 0:
            return False
        i += 6
    return True


def generate_prime(interval):
    """
    Generate a random prime number within a specified interval.
    :param interval: Tuple specifying the range (inclusive).
    :return: A prime number within the interval.
    """
    while True:
        candidate = random.randint(*interval)
        if is_prime(candidate):
            return candidate


def gcd(a, b):
    """
    Compute the greatest common divisor of a and b.
    """
    while b:
        a, b = b, a % b
    return a


def mod_inverse(a, m):
    """
    Compute the modular inverse of a number under modulo m.
    :param a: Integer.
    :param m: Modulus.
    :return: Modular inverse of a under mod m.
    """
    #(a*moduloInverse modulo m ) modulo m must be 1
    m0, x0, x1 = m, 0, 1
    while a > 1:
        q = a // m
        m, a = a % m, m
        x0, x1 = x1 - q * x0, x0
    return x1 + m0 if x1 < 0 else x1


def generate_keys():
    """
    Generate RSA public and private keys.
    :return: Public key (e, n), Private key (d, n).
    """
    # Generate two distinct primes p and q
    p = generate_prime((50, 100))
    q = generate_prime((50, 100))
    while q == p:
        q = generate_prime((50, 100))

    # Compute n and Euler's function φ(n)
    n = p * q
    phi = (p - 1) * (q - 1)

    # Choose e such that 1 < e < φ(n) and gcd(e, φ(n)) = 1 (e and phi to have a common divisor)
    e = random.randint(2, phi - 1)
    while gcd(e, phi) != 1:
        e = random.randint(2, phi - 1)

    # Compute d, the modular inverse of e under mod φ(n)
    d = mod_inverse(e, phi)

    return (e, n), (d, n)


def encrypt(public_key, plaintext):
    """
    Encrypt plaintext using the public key.
    :param public_key: Tuple (e, n).
    :param plaintext: String to encrypt.
    :return: Encrypted message as a list of integers.
    """
    e, n = public_key
    # Convert plaintext to numerical values based on the ALPHABET
    numerical_values = [ALPHABET.index(char) for char in plaintext]
    # Encrypt each numerical value: c = m^e mod n
    encrypted_values = [pow(num, e, n) for num in numerical_values]
    return encrypted_values


# ---------- Decryption ----------
def decrypt(private_key, ciphertext):
    """
    Decrypt ciphertext using the private key.
    :param private_key: Tuple (d, n).
    :param ciphertext: List of integers to decrypt.
    :return: Decrypted plaintext
    """
    d, n = private_key
    # Decrypt each ciphertext value: m = c^d mod n
    decrypted_values = [pow(c, d, n) for c in ciphertext]
    # Convert numerical values back to characters
    plaintext = ''.join(ALPHABET[num] for num in decrypted_values)
    return plaintext


if __name__ == "__main__":
    # Generate keys
    public_key, private_key = generate_keys()
    print(f"Public Key: {public_key}")
    print(f"Private Key: {private_key}")

    plaintext = input("Enter plaintext (A-Z and space only): ").upper()
    if all(char in ALPHABET for char in plaintext):
        print(f"Plaintext: {plaintext}")

        # Encrypt the plaintext
        ciphertext = encrypt(public_key, plaintext)
        print(f"Ciphertext: {ciphertext}")

        # Decrypt the ciphertext
        decrypted_text = decrypt(private_key, ciphertext)
        print(f"Decrypted Text: {decrypted_text}")
    else:
        print("Invalid input. Use A-Z and spaces only.")