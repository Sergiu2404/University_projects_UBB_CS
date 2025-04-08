# 1 c, d
x = linspace(0, 1);
x0 = [0, 1/3, 1/2, 1];
f = @(x)cos(pi * x);

plot(x, f(x)); hold on
plot(x, newton(x0, f(x0), x));

fd = 1/5
newton(x0, f(x0), x);
