# Function to encrypt using a permutation cipher
def permutation_encrypt(message, key):
    key_length = len(key)
    message_length = len(message)

    # Padding the message if necessary to fit the key length
    if message_length % key_length != 0:
        padding_length = key_length - (message_length % key_length)
        message += 'X' * padding_length  # Padding with 'X'

    # Encrypt the message in blocks
    ciphertext = ''
    for i in range(0, len(message), key_length):
        block = message[i:i + key_length]
        # Rearranging the block using the permutation key
        encrypted_block = ''.join([block[key[j] - 1] for j in range(key_length)])
        ciphertext += encrypted_block

    return ciphertext


# Function to decrypt using a permutation cipher
def permutation_decrypt(ciphertext, key):
    key_length = len(key)

    # Find the inverse of the key to decrypt
    inverse_key = [0] * key_length
    for i, k in enumerate(key):
        inverse_key[k - 1] = i + 1

    # Decrypt the ciphertext in blocks
    decrypted_text = ''
    for i in range(0, len(ciphertext), key_length):
        block = ciphertext[i:i + key_length]
        decrypted_block = ''.join([block[inverse_key[j] - 1] for j in range(key_length)])
        decrypted_text += decrypted_block

    return decrypted_text


# Driver code
if __name__ == "__main__":
    # Example message
    message = "HELLOPERMUTATIONCIPHER"

    # Key defining the permutation (1-based index)
    key = [3, 1, 4, 2]  # This defines the order of rearrangement

    # Encrypt the message
    encrypted_message = permutation_encrypt(message, key)
    print("Encrypted Message:", encrypted_message)

    # Decrypt the message
    decrypted_message = permutation_decrypt(encrypted_message, key)
    print("Decrypted Message:", decrypted_message)

