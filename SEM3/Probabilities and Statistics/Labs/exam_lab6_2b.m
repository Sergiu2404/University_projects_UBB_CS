%LAB6 2) a) testing for equal variances
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
alpha = input("give significance level: ");

[h, p, ci, stat] = vartest2(X1, X2, 'alpha', alpha, 'tail', 'both');

f1 = finv(alpha/2, n1-1, n2-1);
f2 = finv(1-alpha/2, n1-1, n2-1);

if h == 1
  fprintf("The hyp is rejected\n")
else
  fprintf("Not rejected\n")
end


%b) testing for difference in means,
%variances aee equal based on the first subpoint
% H0: miu1 = miu2
% H1: miu1 > miu2
[h, p, ci, stat] = ttest2(X1, X2, 'alpha', alpha, 'tail', 'right');
%premium gasoline => higher mileage?

TToneMinusAlpha = tinv(1 - alpha, n1 + n2 - 2);
RR = [TToneMinusAlpha, inf];

if h == 1
  printf("hyp is rejected\n");
else
  printf("hyp not rejected\n");
endif

printf("rejection region: (%1.6f, %1.6f)\n", RR);
printf("value of statistic = %1.6f\n", stat.tstat);
printf("p val = %1.6f\n", p);


