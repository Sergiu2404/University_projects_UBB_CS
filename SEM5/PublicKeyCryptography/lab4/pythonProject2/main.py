import random


def euler_function(p, q):
    return (p - 1) * (q - 1)
def nBitRandom(n):
    # Returns a random number
    # between 2**(n-1)+1 and 2**n-1'''
    return (random.randrange(2 ** (n - 1) + 1, 2 ** n - 1))

if __name__ == '__main__':
    #RSA
    p = nBitRandom(11)
    q = nBitRandom(11)

    n = p * q
    euler_result = euler_function(p, q)



