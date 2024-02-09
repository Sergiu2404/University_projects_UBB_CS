%LAB 3 ex1

n = input("give function degree: ");

%a)
cdf1 = tcdf(0,n);
printf("P(X<=0)= %1.6f\n", cdf1);

cdf2 = 1 - cdf1; %inverse of prev
printf("P(X>=0)= %1.6f\n", cdf2);

%b)
%P(a<=X<=b) = F(b) - F(a)
cdf3 = tcdf(1,n) - tcdf(-1,n);
printf("P(-1<=X<=1)= %1.6f\n", cdf3);

cdf4 = 1 - cdf3;
printf("P(X<=-1 or X>=1)= %1.6f\n", cdf4);

%c)
%for finding the quantile, we use inverse of the distribution
a=input('enter a number between 0 and 1: ');
b=input('enter a number between 0 and 1: ');

invcdf1 = tinv(a,n);
printf('P(X<=-xa or X>=xa)=%1.6f\n',invcdf1);
invcdf2 = tinv(b,n);
printf('P(X<xa)=P(X<=xa)=%1.6f\n)=%1.6f\n',invcdf2);

invcdf3 = tinv(1-b,n);
printf('P(X>xb)=P(X<=xa)=%1.6f\n',invcdf3);
