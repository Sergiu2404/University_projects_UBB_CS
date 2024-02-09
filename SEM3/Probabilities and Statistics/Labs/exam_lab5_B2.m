%LAB5 2)
X1 = sort([
  22.4 21.7 24.5 23.4 21.6 ...
  23.3 22.4 21.6 24.8 20.0
]);
X2 = sort([
  17.7 14.8 24.5 23.4 21.6 ...
  23.3 22.4 21.6 24.8 20.0
]);

n1 = length(X1);
n2 = length(X2);

oneMinusAlpha = input("give confidence level: ");
alpha = 1 - oneMinusAlpha;

%a) we use T(n1 + n2 - 2) distrib and sp^2 from conf_int
sp = sqrt((n1 - 1) * std(X1).^2 + (n2 - 1) * std(X2).^2) / (n1 + n2 - 2);
miu1 = mean(X1) - mean(X2) - tinv(1 - alpha / 2, n1 + n2 - 2) * sp * sqrt(1/n1 + 1/n2);
miu2 = mean(X1) - mean(X2) + tinv(1 - alpha / 2, n1 + n2 - 2) * sp * sqrt(1/n1 + 1/n2);

printf("confidence interval for difference between the means (sigma1 = sigma2) is: (%1.6f, %1.6f)\n", miu1, miu2);

%b) we use T(n) distribution
c = (std(X1).^2 / n1) / (std(X1).^2 / n1 + std(X2).^2 / n2);
n = (((n1 - 1) * (n2 - 1)) / c.^2 * (n2 - 1) + (n1 - 1) * (1 - c).^2);

miu1b = mean(X1) - mean(X2) - tinv(1 - alpha / 2, n) * sqrt(std(X1).^2 / n1 + std(X2).^2 / n2);
miu2b = mean(X1) - mean(X2) + tinv(1 - alpha / 2, n) * sqrt(std(X1).^2 / n1 + std(X2).^2 / n2);

printf("confidence interval for difference between the means (sigma1 != sigma2) is: (%1.6f, %1.6f)\n", miu1b, miu2b);

%c) we use Fischer(n1 - 1, n2 - 1)
sigma1 = 1 / finv(1 - alpha / 2, n1 - 1, n2 - 1) * (std(X1).^2 / std(X2).^2);
sigma2 = 1 / finv(alpha / 2, n1 - 1, n2 - 1) * (std(X1).^2 / std(X2).^2);
printf("confidence interval for the ratio of the variances is: (%1.6f, %1.6f)\n", sigma1, sigma2);
