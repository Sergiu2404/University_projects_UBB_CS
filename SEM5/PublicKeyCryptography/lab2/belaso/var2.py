def bellaso_encrypt(plaintext, key):
    encrypted_text = []
    key_length = len(key)

    # Loop through each character in the plaintext
    for i in range(len(plaintext)):
        # Shift each character in the plaintext by the value of the corresponding key character
        key_char = key[i % key_length]
        shift = ord(key_char)

        # Encrypt the current character
        encrypted_char = chr((ord(plaintext[i]) + shift) % 256)  # 256 is used for extended ASCII
        encrypted_text.append(encrypted_char)

    return ''.join(encrypted_text)


def bellaso_decrypt(ciphertext, key):
    decrypted_text = []
    key_length = len(key)

    # Loop through each character in the ciphertext
    for i in range(len(ciphertext)):
        # Shift each character in the ciphertext by the value of the corresponding key character
        key_char = key[i % key_length]
        shift = ord(key_char)

        # Decrypt the current character
        decrypted_char = chr((ord(ciphertext[i]) - shift) % 256)  # 256 is used for extended ASCII
        decrypted_text.append(decrypted_char)

    return ''.join(decrypted_text)


# Example usage
if __name__ == "__main__":
    plaintext = "Hello, Bellaso Cipher!"
    key = "KEY"

    # Encrypt the plaintext
    encrypted_text = bellaso_encrypt(plaintext, key)
    print("Encrypted:", encrypted_text)

    # Decrypt the ciphertext
    decrypted_text = bellaso_decrypt(encrypted_text, key)
    print("Decrypted:", decrypted_text)
