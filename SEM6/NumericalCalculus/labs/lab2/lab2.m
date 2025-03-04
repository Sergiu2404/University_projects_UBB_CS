% lab2
%a

pkg load symbolic
syms x

f = exp(x)
p1 = taylor(f, x, 0, 'order', 2)
p2 = taylor(f, x, 0, 'order', 3)
p3 = taylor(f, x, 0, 'order', 4)
p4 = taylor(f, x, 0, 'order', 5)

ezplot(f, [-3, 3])


%b
vpa(exp(1), 6)
vpa(subs(p4, x, 1), 6)
