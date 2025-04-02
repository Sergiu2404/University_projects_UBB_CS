# 1 a)
f = @(x) (x + 1) ./ (3 .* x .^ 2 + 2 .* x + 1);
nodes = linspace(-2, 4, 10);
f_nodes = f(nodes);
x = linspace(-2, 4, 500);

# b
fi = my_lagrange(nodes, f_nodes, x);
#plot(x, fi)

# c
f_nodes = f(x);
error = abs(f_nodes - fi);
plot(x, error);

# d
y = f(1/2);
#nodes = linspace(-2, 4, 500);
l9_y = my_lagrange(nodes, f_nodes, x);
printf("%d", abs(y - l9_y))
