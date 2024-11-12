from timeit import default_timer as time

def phiFractions(n):
    result = n
    p = 2
    while p * p <= n:
        if n % p == 0:
            while n % p == 0:
                n = n // p
            result = result * (1.0 - (1.0 / float(p)))
        p = p + 1

    if n > 1:
        result = result * (1.0 - (1.0 / float(n)))

    return int(result)


def phi(n):
    result = n
    p = 2

    while p * p <= n:
        if n % p == 0:
            while n % p == 0:
                n = n // p
            result -= result // p
        p += 1

    if n > 1:
        result -= result // n

    return result


def runner(values, bounds, function):
    start = time()
    for k in range(len(bounds)):
        result = []
        for i in range(1, bounds[k]):
            if function(i) == values[k]:
                result.append(i)
        print("(" + str(values[k]) + "," + str(bounds[k]) + ")->" + str(result))
    end = time()
    return end - start


def main():
    values = [12, 24, 36, 48, 128]
    bounds = [468, 666, 41423, 12312, 842342]

    print("Results for euler's formula with fractions: ")

    rt1 = runner(values, bounds, phiFractions)
    print("Running time for euler's product function: " + str(rt1))

    print("\nResults for euler's formula with fractions, without floating point issues: ")

    rt2 = runner(values, bounds, phi)
    print("Running time for euler's product function, without floating point issues: " + str(rt2))

    print("\nAs we can see, the difference between the two is quite significant, namely " + str(abs(rt1 - rt2)))


main()