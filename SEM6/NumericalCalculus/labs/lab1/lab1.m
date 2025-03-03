A = [1 2 3; 4 5 6; 7 8 10]
det(A)
inv(A)

A * inv(A)

A * A
A .* A
A ^ 2
A .^ 2

v = 1:100
v1 = 1:2:100

v .^ 2
v .* v

% ex 1
% a)
x = -4:0.1:7.2
px=x.^5-5*x.^4-16*x.^3+16*x.^2-17*x+21;
plot(x,px)

% b)
p=[1,-5,16,16,-17,21]
polyval(p,-2.5)

% ex2
x2=0:0.1:2*pi
f=sin(x);
g=sin(2*x);
h=sin(3*x);

subplot(3,1,1)
plot(x,f)
legend('sinx')
subplot(3,1,2)
plot(x,g)
subplot(3,1,3)
plot(x,h)

% ex3
% for each t x and y coord
t=0:0.01*pi:10*pi;
R=3.8;
r=1;
xt=(R+r)+cos(t)-r*cos((R/r+1)*t);
yt=(R+r)+sin(t)-r*sin((R/r+1)*t);
plot(xt,yt)%both of them vectors

%ex4
[x,y]=meshgrid(-2:0.1:2, 0.5:0.1:4.5);
z=sin(exp(x)).*cos(log(y)); %. because they are 2 vect
mesh(x,y,z);
