# LAB 9
#ex 1
xi = -2:4;
x = linspace(-2, 4);
fi = (xi + 1) ./ (3 * xi.^2 + 2 .* xi + 1);
f = (x + 1) ./ (3 * x .^ 2 + 2 .* x + 1);

plot(x, f);

hold on
n = newton(xi, fi, x);
plot(x, n);

hold on
deri = @(xi) - (3 * xi .^ 2 + 6 * xi + 1) ./ (3 * xi .^ 2 + 2 * xi + 1);
h = hermite(xi, fi, deri(xi), x);

plot(x, h);
