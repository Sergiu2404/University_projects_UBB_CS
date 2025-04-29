#ex2
xi = [-1, -1/2, 0, 1/2, 1, 3/2];
x = linspace(-1, 3/2);
f = @(x)x .* sin(pi * x);

s1 = spline(xi, f(xi), x);
s2 = spline(xi, [pi, f(xi), -1], x);

p = pchip(xi, f(xi), x);
plot(xi, f(xi), 'o');

hold on
plot(x, f(x));

hold on
plot(x, s1);

hold on
plot(x, s2);

hold on
plot(xi, p);
