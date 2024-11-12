
keyMatrix = [[0] * 2 for i in range(2)]

# Generate vector for the message
messageVector = [[0] for i in range(2)]

# Generate vector for the cipher
cipherMatrix = [[0] for i in range(2)]

# Function to generate the key matrix from the key string
def getKeyMatrix(key):
    k = 0
    for i in range(2):
        for j in range(2):
            keyMatrix[i][j] = ord(key[k]) % 65
            k += 1

# Function to encrypt the message
def encrypt(messageVector):
    for i in range(2):
        for j in range(1):
            cipherMatrix[i][j] = 0
            for x in range(2):
                cipherMatrix[i][j] += (keyMatrix[i][x] * messageVector[x][j])
            cipherMatrix[i][j] = cipherMatrix[i][j] % 26

# Hill Cipher function
def HillCipher(message, key):
    # Get key matrix from the key string
    getKeyMatrix(key)

    # Generate vector for the message
    for i in range(2):
        messageVector[i][0] = ord(message[i]) % 65

    # Encrypt the message
    encrypt(messageVector)

    # Generate the encrypted text from the encrypted vector
    CipherText = []
    for i in range(2):
        CipherText.append(chr(cipherMatrix[i][0] + 65))

    # Print the ciphertext
    print("Ciphertext: ", "".join(CipherText))

# Driver Code
def main():
    # Get the message to be encrypted (2 characters long)
    message = "HI"

    # Get the key (4 characters long)
    key = "GYBN"

    HillCipher(message, key)

if __name__ == "__main__":
    main()
