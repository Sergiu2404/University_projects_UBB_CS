%EXAM
%manufacturer of gunpowder has developed a new powder which was tested in 12 shells
%The resulting muzzle velocities in m/s are:
X = [1001.7 975.0 978.3 988.3 978.7 988.9 1000.3 979.2 968.9 983.5...
999.2 985.6];
n = length(X);

%a)find a 95% confidence interval for the average velocity of shells of this type
alpha = 0.95; %confidence interval percent
%assuming sigma unknown => we use second formula from conf_int that uses T(n-1) as quantile
left = mean(X) - std(X) / sqrt(n) * tinv(1-alpha/2,n-1); %left side of interval
right = mean(X) - std(X) / sqrt(n) * tinv(alpha/2,n-1); %right side of interval

printf("a) confidence interval for the population mean is: (%1.2f, %1.2f)\n", left, right);


%b) at the 1% significance level, does the data siggests that, on average,
%the muzzles are faster than 995 m/s?
sign = 0.1; %significance level
m0 = 995; %value on which we compute the test
%H0: sigma1 = 995
%H1: sigma1 > 995

%check muzzles average significantly faster than 995 (right tailed test), sigma unknown => we use ttest
[h, p, ci, stats] = ttest(X, m0, "alpha", sign, "tail", "right");

printf("b)\n");
if h == 1
  printf("null hypothesis rejected, muzzles faster on average than 995\n");
else
  printf("null hypothesis not rejected\n");
endif

%for the rejection region we need T(n-1) quantile => tinv(alpha,n-1)
%right tailed test => rejection region = (quantile,inf)
q = tinv(alpha, n-1);

printf("rejection region is: (%1.2f, %1.2f)\n",q,inf);
printf("p-value(probability of statistic): %1.2f\n", p);
printf("confidence interval: %1.2f\n", ci);
printf("statistic test value: %1.2f\n", stats.tstat);
%returned stats is a structure and we need only the z-val(statistic test value)
%so we get .tstat




