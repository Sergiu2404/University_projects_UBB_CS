% 2 a) sin x, degree 3, 5 x from [-pi, pi].
pkg load symbolic
syms x

f = sin(x)
p1 = taylor(f, x, 0, 'Order', 3)
p2 = taylor(f, x, 0, 'Order', 5)

ezplot(f, [-pi, pi])
hold on
ezplot(p1, [-pi, pi])
hold on
ezplot(p2, [-pi, pi])

% b)
n = 10
for i = 2:n
    t = taylor(f, x, 0, 'Order', i);
    a = subs(t, x, pi/5); i
    vpa(a, 6)
end

n = 10
for i = 2:n
    t = taylor(f, x, 0, 'Order', i);
    a = subs(t, x, -pi/3); i
    vpa(a, 5)
end
