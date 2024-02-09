%LAB2
%2
%A)
%distrib function of x has params: n(trials), p(succes probab)
%=> n = 3, p = 0.5
x = 0:1:3; %nr of heads that could appear(outcomes)
pdfx = binopdf(x, 3, 0.5);
plot(x, pdfx, 'red');


%B)
cdfx = binocdf(x, 3, 0.5);
plot(x, cdfx, 'blue-.');


%C)
pdfx1 = binopdf(0, 3, 0.5); %P(x=0)
pdfx2 = 1 - binopdf(1, 3, 0.5); %P(x!=1)


%D
cdfx1 = binocdf(2, 3, 0.5); %p(x<=2)
cdfx2 = binocdf(1, 3, 0.5); %p(x<2)


%E
cdfx3 = 1 - binocdf(0, 3, 0.5); %p(x>=1) = 1 - p(x=0)
cdfx4 = 1 - binocdf(1, 3, 0.5); %p(x>1) = 1 - p(x<1)


%F
N = input("give nr of simulations: ");
U = rand(3,N); %generates matrix with 3 rows and N cols, values range between 0,1
Y = (U<0.5); %consider heads values between 0 and 0.5 and take those inside Y
X = sum(Y); %sum of all heads
disp(U);
disp(Y);
printf("nr of heads is: ");
disp(X);
