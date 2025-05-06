# lab 10 ex 1
f = @(x)1./x;
a = 1;
b = 2;
n = 100;

rectangle = rectangle_rule(f, a, b, n);
fprintf('rectangle: %.10f\n', rectangle);

#trapezoidal = trapezoidal_rule(f, a, b, n);
#fprintf('rectangle: %.10f\n', trapezoidal);

simpsons = simpsons_rule(f, a, b, n);
fprintf('simpsons: %.10f\n', simpsons);
