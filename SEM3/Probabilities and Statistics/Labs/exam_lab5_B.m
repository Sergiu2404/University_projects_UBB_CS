%LAB5 B 1)
X = [
     7 7 4 5 9 9 ...
     4 12 8 1 8 7 ...
     3 13 2 1 17 7 ...
     12 5 6 2 1 15 ...
     14 10 2 4 9 11 ...
     3 5 12 6 10 7
];

n = length(X);
oneMinusAlpha = input("give confidence interval: ");
alpha = 1 - oneMinusAlpha;

%a) we use N(0, 1) distribution
sigma = 5;
miu1 = mean(X) - sigma / sqrt(n) * norminv(1-alpha / 2, 0, 1);
miu2 = mean(X) - sigma / sqrt(n) * norminv(alpha / 2, 0, 1);

printf("confidence interval for the mean when you know sigma is: (%1.6f, %1.6f)\n", miu1, miu2);


%b) we use T(n - 1) distribution
%sigma not known => use standard deviation std(X)
miu1b = mean(X) - std(X) / sqrt(n) * tinv(1 - alpha / 2, n-1);
miu2b = mean(X) - std(X) / sqrt(n) * tinv(alpha / 2, n-1);

printf("confidence interval for the mean when you know standard deviation is: (%1.6f, %1.6f)\n", miu1b, miu2b);

%c) we will use chi-squared(n-1) distribution
var1 = ((n-1) * var(X)) / sqrt(n) * chi2inv(1 - alpha/2, n-1);
var2 = ((n-1) * var(X)) / sqrt(n) * chi2inv(alpha/2, n-1);

printf("confidence interval for the variance is: (%1.6f, %1.6f)\n", var1, var2);
s1 = sqrt(var1);
s2 = sqrt(var2);

printf("confidence interval for the standard deviation is: (%1.6f, %1.6f)\n", s1, s2);
