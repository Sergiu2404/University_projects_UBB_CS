def mod_exp(base, exp, mod):
    result = 1
    steps = []
    while exp > 0:
        if exp % 2 == 1:
            result = (result * base) % mod
        base = (base * base) % mod
        exp //= 2
    return result

n = 2353
s = 4  # value of s
t = 147  # value of t

bases = [2, 3, 5]

results = {
    "2^(2^0)": mod_exp(2, 2**0, n),
    "2^(2^1)": mod_exp(2, 2**1, n),
    "2^(2^2)": mod_exp(2, 2**2, n),
    "2^(2^3)": mod_exp(2, 2**3, n),
    "2^(2^4)": mod_exp(2, 2**4, n),
    "2^(2^5)": mod_exp(2, 2**5, n),
    "2^(2^6)": mod_exp(2, 2**6, n),
    "2^(2^7)": mod_exp(2, 2**7, n),
    "2^(2^8)": mod_exp(2, 2**8, n),
    "2^(2^9)": mod_exp(2, 2**9, n),
    "2^t": mod_exp(2, t, n),
    "2^(2*t)": mod_exp(2, 2 * t, n),
    "2^(2^2*t)": mod_exp(2, 2**2 * t, n),
    "2^(2^3*t)": mod_exp(2, 2**3 * t, n),
    "2^(2^4*t)": mod_exp(2, 2**4 * t, n),
    "3^t": mod_exp(3, t, n),
    "3^(2*t)": mod_exp(3, 2 * t, n),
    "3^(2^2*t)": mod_exp(3, 2**2 * t, n),
    "3^(2^3*t)": mod_exp(3, 2**3 * t, n),
    "3^(2^4*t)": mod_exp(3, 2**4 * t, n),
    "5^t": mod_exp(5, t, n),
    "5^(2*t)": mod_exp(5, 2 * t, n),
    "5^(2^2*t)": mod_exp(5, 2**2 * t, n),
    "5^(2^3*t)": mod_exp(5, 2**3 * t, n),
    "5^(2^4*t)": mod_exp(5, 2**4 * t, n),
}

for key, value in results.items():
    print(f"{key} % {n} = {value}")

print(4096%2353)
print(3038049%2353)
print(106276%2353)
print(152881%2353)
print(5239521%2353)
print('\n')

print(65536%2377)
print(1841449%2377)
print(2725801%2377)
print(3094081%2377)
print(2572816%2377)
print(813604%2377)