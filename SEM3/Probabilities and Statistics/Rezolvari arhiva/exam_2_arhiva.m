%MODEL 2 / ARHIVA
x1 = [46, 37, 39, 48, 47, 44, 35, 31, 44, 37];
x2 = [35, 33, 31, 35, 34, 30, 27, 32, 31, 31];

n1 = length(x1);
n2 = length(x2);



%a) at 5% significance level, do the population variances eem to differ?
alpha = 0.05;
% The null hypothesis H0: sigma1^2 = sigma2^2
% The alt. hypothesis H1:  sigma1^2 != sigma2^2
%2 tailed test for comparing the variances

q1 = finv(alpha/2, n1-1, n2-1);
q2 = finv(1-alpha/2, n1-1, n2-1);

[h,p,ci,stats] = vartest2(x1,x2,"alpha",alpha,"tail","both");

if h == 0
  printf("H0 not rejected, variances seem to be equal\n");
else
  printf("H0 rejected, variances seem to differ significantly\n");
endif

printf('the rejection region for F is (%6.4f,%6.4f)U(%6.4f,%6.4f)\n', -inf, q1, q2, inf);
printf('the value of the test statistic F is %6.4f\n', stats.fstat);
printf('the P-value for the variances test is %6.4f\n', p);


%b) find a 95% conf int for the difference of the average assembling times
%from a we have that the variances differ => std deviations not equal
%oneMinusAlpha = 0.95 => alpha remains 0.05
c = (var(x1) / n1) / (var(x1) / n1 + var(x2) / n2);
nn = 1 / (c.^2/(n1-1) + (1-c).^2/(n2-1));
meanDiff = mean(x1) - mean(x2);
s = sqrt(var(x1)/n1 + var(x2)/n2);

q = tinv(1-alpha/2,nn);
miu1 = meanDiff - q * s;
miu2 = meanDiff + q * s;

printf('Conf. interval for diff. of means, variances not equal (mm1, mm2) = (%4.3f, %4.3f)\n',miu1, miu2);
