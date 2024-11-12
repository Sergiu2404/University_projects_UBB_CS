# Python program to print all primes smaller than n using Sieve of Eratosthenes

def sieveOfEratosthenes(n):
    # Create a boolean array "prime[0..n-1]" and initialize all entries as True.
    # A value in prime[i] will finally be False if i is not a prime, else True.
    prime = [True for i in range(n)]
    p = 2
    while (p * p < n):
        # If prime[p] is not changed, then it is a prime
        if (prime[p] == True):
            # Update all multiples of p
            for i in range(p * p, n, p):
                prime[i] = False
        p += 1

    # all prime numbers smaller than n
    for p in range(2, n):
        if prime[p]:
            print(p)

# Driver code
if __name__ == '__main__':
    n = 30
    print(f"Following are the prime numbers smaller than {n}:")
    sieveOfEratosthenes(n)
