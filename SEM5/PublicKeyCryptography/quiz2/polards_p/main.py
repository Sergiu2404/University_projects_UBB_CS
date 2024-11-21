import math

def gcd(a, b):
    while b:
        a, b = b, a % b
    return a
def f(n):
    return n**2 + 1

#page 1 polard p
n=6283
print(f(5963) % n)
print(gcd(abs(1873-531), n))

#page 2 fermat
def calculate_t0(n):
    return math.ceil(math.sqrt(n))

def is_perfect_square(n):
    if n < 0:
        return False
    root = int(math.sqrt(n))
    return root * root == n

print("\n\n===page2===fermat===")

n2 = 7009
print(calculate_t0(n2))
print()
t_iteration = 103**2 - n2
print(t_iteration)
print(is_perfect_square(t_iteration))

print((103 + 60), (103 - 60))