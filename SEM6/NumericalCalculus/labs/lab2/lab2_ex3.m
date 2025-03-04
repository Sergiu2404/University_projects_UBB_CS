% 3 a) ln(1 + x), degree 2, 5, x from [-0.9, 1]
% To be able to run them, when running a, comment the others, when running b comment the others, for c the same but for d keep c uncommented and comment a and b
pkg load symbolic
syms x # defines x as a symbol

%f = log(1 + x)
p1_a = taylor(f, x, 0, 'Order', 2)
p2_a = taylor(f, x, 0, 'Order', 5)

%ezplot(f, [-0.9, 1])
%hold on
%ezplot(p1, [-0.9, 1])
%hold on
%ezplot(p2, [-0.9, 1])

% b)
%t = taylor(f, x, 0, 'Order', 1000)
%a = subs(t, x, 1);
%vpa(a, 5)

% c)
f1 = log(1 + x)
p1_c = taylor(f1, x, 0, 'Order', 7)
f2 = log(1 - x)
p2_c = taylor(f2, x, 0, 'Order', 7)

ezplot(p1, [-0.9, 1])
hold on
ezplot(p2, [-0.9, 1])

% d)
p1_d = p1_a - p1_c
p2_d = p2_a - p2_c

