%LAB4 2)
%c) GEOMETRIC DISTRIB
N3=input('enter the number of simulations: ');
p3=input('enter the probability, a number between 0 and 1: ');

x3 = zeros(1,N3); %geo variable: nr of failures needed to get 1st success
for i=1:N3 %parse every simulation
  x3(i) = 0; %init trial count for each simulation
  while rand >= p3
    x3(i) = x3(i) + 1; %increment trials until success
  endwhile
endfor

k3 = 0:20;
p_k3 = geopdf(k3, p3);
u_x3 = unique(x3);
n_x3 = hist(x3,length(u_x3));
rel_freq3 = n_x3 / N3;
plot(u_x3,rel_freq3,'--',k3,p_k3,'o');


%d) PASCAL DISTRIB
N4 = input("give nr of simulations: ");
p4 = input("give probability from (0,1): ");
r4 = input("give number of successes desired: ");

x4 = zeros(1, N4);
for i=1:N4 %parse every simulation
  x4(i) = 0; %init trial count for each simulation
  for j=1:n
    while rand >= p4
      x4(i) = x4(i) + 1; %increment trials until success
    endwhile
  endfor
endfor

k4 = 0:20;
p_k4 = nbinpdf(k4, r4, p4);
u_x4 = unique(x4);
n_x4 = hist(x4, length(u_x4));
rel_freq4 = n_x4 / N4;
plot(u_x4,rel_freq4,'--',k4,p_k4,'o');


%b) BINOMIAL DISTRIB
N2=input('enter the number of simulations: ');
n2=input('enter the number of trials: ');
p2=input('enter the probability, a number between 0 and 1: ');

u2 = rand(n2,N2); %generates matrix: n rows and N cols
x2 = sum(u2<p2); %binomial variable

k2 = 0:n2;
p_k2 = binopdf(k2, n2, p2);
u_x2 = unique(u2);
n_x2 = hist(x2, u_x2);
rel_freq2 = n_x2 / N2;
plot(u_x2,rel_freq2,'--',k2,p_k2,'o');

%a) BERNOULLI DISTRIB
n = input("give nr of simulations: ");
p = input("give probability from (0,1):");

u = rand(1,n); %generates random array u of n elements
x = u<p; %creates array of: 1 if elem of u < p, else 0
u_x = [0 1];
n_x = hist(x, length(u_x));
%hist returns the unique elements and the edges of intervals
rel_freq = n_x/n;
%calculates rel freq for each nr dividing the frequency of the elem by the total nr of elems
plot(u_x, rel_freq,'*');

