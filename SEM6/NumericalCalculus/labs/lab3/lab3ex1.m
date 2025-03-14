# 1 a)
x = [0, 1, 2]
f = 1./(1+x)
divdiff(x, f)

# 1 b)
df = -1./(1+x).^2
[z, t] = divdiff2(x, f, df)

# c)
x = linspace(1, 2, 11)
f = 1./(1+x)
fd = -1./(1+x).^2
divdiff(x, f)
[z, t] = divdiff2(x, f, fd)
