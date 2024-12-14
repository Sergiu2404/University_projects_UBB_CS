#page 1
def rsa_encrypt(plaintext, p, q, e, k, l):
    # Step 1: Calculate n and φ(n)
    n = p * q
    phi_n = (p - 1) * (q - 1)

    # Validate e
    if e <= 1 or e >= phi_n or gcd(e, phi_n) != 1:
        raise ValueError("Invalid encryption exponent e.")

    # Step 2: Map plaintext to numerical equivalents
    alphabet = "_ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    letter_to_num = {letter: index for index, letter in enumerate(alphabet)}
    num_to_letter = {index: letter for index, letter in enumerate(alphabet)}

    # Convert plaintext to numerical equivalents
    plaintext_numbers = [letter_to_num[char] for char in plaintext]

    # Group into blocks of size k and convert to numerical equivalents
    blocks = []
    for i in range(0, len(plaintext_numbers), k):
        block = plaintext_numbers[i:i + k]
        block_value = sum(num * (27 ** (k - j - 1)) for j, num in enumerate(block))
        blocks.append(block_value)

    # Step 3: Encrypt each block
    encrypted_blocks = []
    for block in blocks:
        encrypted_block = pow(block, e, n)  # Encryption formula: c = b^e mod n
        encrypted_blocks.append(encrypted_block)

    # Step 4: Convert encrypted blocks into blocks of l letters
    ciphertext_blocks = []
    for encrypted_block in encrypted_blocks:
        block_letters = []
        value = encrypted_block
        for _ in range(l):
            remainder = value % 27
            block_letters.append(num_to_letter[remainder])
            value //= 27
        ciphertext_blocks.append("".join(reversed(block_letters)))

    # Join all blocks to form the ciphertext
    ciphertext = " ".join(ciphertext_blocks)
    return ciphertext, n, phi_n, blocks, encrypted_blocks, ciphertext_blocks


def gcd(a, b):
    """Helper function to compute GCD using Euclid's algorithm."""
    while b:
        a, b = b, a % b
    return a


# Parameters
p = 43
q = 47
e = 5
k = 2  # Block size for plaintext
l = 3  # Block size for ciphertext
plaintext = "MEXICO"

# Encryption
ciphertext, n, phi_n, blocks, encrypted_blocks, ciphertext_blocks = rsa_encrypt(plaintext, p, q, e, k, l)

# Output results
print(f"n = {n}")
print(f"φ(n) = {phi_n}")
print(f"Plaintext blocks (numerical) = {blocks}")
print(f"Encrypted blocks (numerical) = {encrypted_blocks}")
print(f"Ciphertext blocks (letters) = {ciphertext_blocks}")
print(f"Final Ciphertext = {ciphertext}")


#page 2
print("PAGE 2 ========================================")

def modular_inverse(e, phi_n):
    """Find modular inverse of e mod phi_n using the Extended Euclidean Algorithm."""
    t, new_t = 0, 1
    r, new_r = phi_n, e
    while new_r != 0:
        quotient = r // new_r
        t, new_t = new_t, t - quotient * new_t
        r, new_r = new_r, r - quotient * new_r
    if r > 1:
        raise ValueError("e is not invertible")
    if t < 0:
        t += phi_n
    return t


def modular_exponentiation(base, exp, mod):
    """Efficiently compute (base^exp) % mod."""
    result = 1
    base = base % mod
    while exp > 0:
        if exp % 2 == 1:  # If exp is odd
            result = (result * base) % mod
        exp = exp // 2
        base = (base * base) % mod
    return result


def rsa_decrypt(ciphertext, p, q, e, l, k):
    # Step 1: Compute n and φ(n)
    n = p * q
    phi_n = (p - 1) * (q - 1)

    # Step 2: Compute decryption exponent d
    d = modular_inverse(e, phi_n)

    # Step 3: Convert ciphertext blocks to numerical equivalents
    alphabet = "_ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    letter_to_num = {letter: index for index, letter in enumerate(alphabet)}
    num_to_letter = {index: letter for index, letter in enumerate(alphabet)}

    # Split ciphertext into blocks of l letters
    ciphertext_blocks = [ciphertext[i:i + l] for i in range(0, len(ciphertext), l)]

    # Convert blocks into numerical equivalents
    ciphertext_numerical = []
    for block in ciphertext_blocks:
        value = sum(letter_to_num[block[i]] * (27 ** (l - i - 1)) for i in range(len(block)))
        ciphertext_numerical.append(value)

    # Step 4: Decrypt numerical ciphertext blocks
    decrypted_blocks = []
    for c in ciphertext_numerical:
        b = modular_exponentiation(c, d, n)
        decrypted_blocks.append(b)

    # Step 5: Convert numerical blocks back to plaintext (k-letter blocks)
    plaintext_blocks = []
    for b in decrypted_blocks:
        letters = []
        for _ in range(k):
            remainder = b % 27
            letters.append(num_to_letter[remainder])
            b //= 27
        plaintext_blocks.append("".join(reversed(letters)))

    # Combine plaintext blocks into final plaintext
    plaintext = "".join(plaintext_blocks)
    return n, phi_n, e, d, ciphertext_blocks, ciphertext_numerical, decrypted_blocks, plaintext_blocks, plaintext


# Parameters
p = 53
q = 61
e = 7  # Smallest valid encryption exponent
l = 3  # Block size for ciphertext
k = 2  # Block size for plaintext
ciphertext = "AMOCLEAJE"  # Ciphertext to decrypt

# Decrypt the ciphertext
(
    n,
    phi_n,
    e,
    d,
    ciphertext_blocks,
    ciphertext_numerical,
    decrypted_blocks,
    plaintext_blocks,
    plaintext,
) = rsa_decrypt(ciphertext, p, q, e, l, k)

# Output results
print(f"n = {n}")
print(f"φ(n) = {phi_n}")
print(f"e = {e}")
print(f"d = {d}")
print(f"Ciphertext blocks: {ciphertext_blocks}")
print(f"Numerical equivalents of ciphertext blocks: {ciphertext_numerical}")
print(f"Decrypted numerical blocks: {decrypted_blocks}")
print(f"Decrypted blocks of k letters: {plaintext_blocks}")
print(f"Decrypted plaintext: {plaintext}")