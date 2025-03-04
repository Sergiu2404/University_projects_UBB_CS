%lab ex1
% a) plot e^x and its Taylor polyonmials of degree 1, 2, 3, and 4, x from [-3, 3].
pkg load symbolic
syms x # defines x as a symbol


% MacLaurin always x0 is 0
f = exp(x)
p1 = taylor(f, x, 0, 'Order', 2)
p2 = taylor(f, x, 0, 'Order', 3)
p3 = taylor(f, x, 0, 'Order', 4)
p4 = taylor(f, x, 0, 'Order', 5) # 5 terms

xlim([-3,3])
fplot(f, [-3,3])
hold on
ezplot(p1, [-3, 3])
hold on
ezplot(p2, [-3, 3])
hold on
ezplot(p3, [-3, 3])
hold on
ezplot(p4, [-3, 3])

% b)

n = 10
for i = 2:n
    t = taylor(f, x, 0, 'Order', i);
    a = subs(t, x, 1); i
    result = double(vpa(a, 7));
    %result = double(a);
    %result = round(result, 6);
    fprintf('Order %d: %.6f\n', i-1, result);
end
