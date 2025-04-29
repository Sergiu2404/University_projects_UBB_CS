xi=[0.5, 1.5, 2, 3, 3.5, 4.5, 5, 6, 7, 8];
fi=[5, 5.8, 5.8, 6.8, 6.9, 7.6, 7.8, 8.2, 9.2, 9.9];

x = linspace(0, 5, 8);
hold on
scatter(xi,fi);
p = polyfit(xi,fi,1);

plot(xi,fi,'o');
plot(x, polyval(p,x));

n=norm(fi-polyval(p,xi))

approx=polyval(p,4)
