cipherMatrix = [0 for _ in range(4)]

def encrypt(messageVector, key):
    for i in range(2):
        for j in range(2):
            for x in range(2):
                cipherMatrix[i*2+j] += (messageVector[i][x] * key[x][j])
            cipherMatrix[i*2+j] = cipherMatrix[i*2+j] % 27


def HillCipher(message, key):
    messageVector = [[0,0] for _ in range(2)]
    for i in range(2):
        for j in range(2):
            messageVector[i][j] = ord(message[i*2+j]) - 96

    encrypt(messageVector, key)

    CipherText = []
    for i in range(4):
        CipherText.append(chr(cipherMatrix[i] + 64))

    print("Ciphertext:", "".join(CipherText))


def main():
    # n=27, m=2

    message = "four"
    keyMatrix = [[11, 8], [3, 7]]

    HillCipher(message, keyMatrix)


if __name__ == "__main__":
    main()