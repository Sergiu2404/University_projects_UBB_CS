%LAB6 2) a)
X1 = [
  22.4 21.7 24.5 23.4 21.6 * ones(1,2) ...
  23.3 22.4 24.8 20.0
];
X2 = [
  17.7 14.8 19.6 * ones(1,2) 12.1 ...
  14.8 15.4 12.6 14.0 12.2
];
n1 = length(X1);
n2 = length(X2);
% H0: (sigma1)^2 = (sigma2)^2
% H1: (sigma1)^2 != (sigma2)^2

alpha = input("significance level: ");
tail = 0;
%2 tailed test because the alternative hyp could be either
%that the vairance of the prem gas is significantly greater/smaller
%than of the reg gas


%[h , p, ci, zval] = vartest2(X1, X2, tail);
[h , p, ci, val] = vartest2(X1, X2, 'alpha', alpha, 'tail', 'both'); %by default alpha = 0.05, tail = 'both'

f1 = finv(alpha / 2, n1 - 1, n2 - 1);
f2 = finv(1 - alpha / 2, n1 - 1, n2 - 1);

%null hyp: the variances of the 2 pop are equal
if h == 1
  printf("null hyp rejected\n");
else
  printf("null hyp not rejected\n");
endif

printf("rejection region is: (%1.6f, %1.6f) U (%1.6f, %1.6f)\n", -inf, f1, f2, inf);
printf("value of test statistic: %1.6f\n", zval.fstat);
printf("p for the variances test is: %1.6f\n", p);
printf("confidence interval for the variances test is: %1.6f\n", ci);
