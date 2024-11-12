def permutation_cipher(msg: str, key: int, decrypt: bool=False) -> str:
    key = str(key)
    key_len = len(key)
    result = ''

    # Pad the message with spaces if needed
    msg += ' ' * ((key_len - (len(msg) % key_len)) % key_len)

    # Calculate the number of blocks
    num_blocks = int(len(msg) / key_len)

    # Process each block
    for num_block in range(num_blocks):
        # Get the current block
        start_idx = num_block * key_len
        block = [x for x in msg[start_idx:(start_idx + key_len)]]

        # Rearrange the characters from this block into a new block
        new_block = [' '] * key_len
        for idx1, idx2 in enumerate(key):
            idx2 = int(idx2) - 1
            if decrypt:
                new_block[idx2] = block[idx1]
            else:
                new_block[idx1] = block[idx2]

        # Join the new block into the result message
        result += ''.join(new_block)

    return result


# Usage example
key = 31254
message = 'THE HACKING BLOG ROCKS'

encrypted = permutation_cipher(message, key)
print(encrypted)   # Should print "ETHH KACNIBG OLRG CO KS  "

decrypted = permutation_cipher(encrypted, key, decrypt=True)
print(decrypted)   # Should print "THE HACKING BLOG ROCKS   "