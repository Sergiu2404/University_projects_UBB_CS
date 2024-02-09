%MODEL 3 ARHIVA
X = [3.26, 1.89, 2.42, 2.03, 3.07, 2.95, 1.39, 3.06, 2.46 ...
3.35, 1.56, 1.79, 1.76, 3.82, 2.42, 2.96];
n = length(X);


%a) find a 95% conf int for the average size of nickel particles
oneMinusAlpha = 0.95;
%significance level
alpha = 1 - oneMinusAlpha;

%sigma not known =>
s_dev = std(X);

m1 = mean(X) - tinv(1-alpha/2, n-1) * s_dev/sqrt(n);
m2 = mean(X) + tinv(alpha/2, n-1) * s_dev/sqrt(n);

printf("conf int for the mean (sigma unknown) is: (%1.6f, %1.6f)\n", m1, m2);


%b) at 1% sign level, on average, do these nickel particles seem to be smaller than 3?
%H0: miu = 3
%H1: miu < 3
alpha = 0.01;

m0 = 3;
[h, p, ci, stats] = ttest(X, m0, "alpha", alpha, "tail", "left");
%even if we calculated s_dev at point a), we use ttest bc s_dev is calculated for point a but not given

q = tinv(alpha, n-1);
RR = [-inf, q];

if h == 1
  printf("null hyp rejected, data suggests that average is lower than 3\n");
else
  printf("null hyp not rejected\n");
endif

printf("conf int: (%1.6f, %1.6f)\n", ci);
printf("RR is (%1.6f, %1.6f)\n", RR);
printf("p value: %1.6f\n", p);
printf("value of the statistic: %1.6f", stats.tstat);



