def repeated_squaring_modular_exponentiation(base, exponent, modulus):
    # compute the remainder of base^exponent divided by modulus
    if modulus == 1:
        return 0
    remainder = 1
    base = base % modulus
    while exponent > 0:
        if exponent % 2 == 1:
            remainder = (remainder * base) % modulus  # remainder is multiplied by base and we get the new remainder
        exponent = exponent >> 1  # use bit shifting
        base = (base * base) % modulus  # continue squaring
    return remainder


def miller_rabin_test(n, base):
    # For a pseudoprime, we only need to check if base^(n-1) ≡ 1 (mod n)
    if repeated_squaring_modular_exponentiation(base, n - 1, n) == 1:
        return True
    return False


def test_bases(n):
    bases = []
    for i in range(2, n - 1):
        if miller_rabin_test(n, i):
            bases.append(i)
    return bases


def main():
    n = int(input("Enter odd composite number n: "))
    result = test_bases(n)
    if len(result) == 0:
        print("Number is not pseudoprime to any base")
    else:
        print("Number is pseudoprime to bases: ", result)


if __name__ == '__main__':
    main()
