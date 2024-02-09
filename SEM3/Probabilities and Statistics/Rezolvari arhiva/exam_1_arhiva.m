%MODEL 1 / ARHIVA
X = [4.6 0.7 4.2 1.9 4.8 6.1 4.7 5.5 5.4];
Y = [2.5 1.3 2.0 1.8 2.7 3.2 3.0 3.5 3.4];

%a) at 5% sign level, do the population variances seem to differ
%H0: sigma1.^2 = sigma2.^2
%H1: sigma1 != sigma2 => 2 tailed test
alpha = 0.05;
tail = 0;
[h, p, ci, stats] = vartest2(X,Y,"alpha",alpha,"tail","both");

if h == 1
  printf("H0 is rejected, population variances differ\n");
else
  printf("H0 is not rejected, population varainces are equal\n");
endif

q1 = finv(alpha/2, stats.df1, stats.df2);
q2 = finv(1-alpha/2, stats.df1, stats.df2);

printf("observed value is: %1.6f\n", stats.fstat);
printf("p value is: %1.6f\n", p);
printf("rejection region: (%1.6f, %1.6f) U (%1.6f, %1.6f)",-inf, q1, q2, inf);


%b) at the same significance level, does it seem that on average, steel pipes lose more heat than glass pipes?
%h0: miu1  = miu2
%h1: miu1 > miu2 - rigth-tailed test
[h, p, ci, stats] = ttest2(X,Y,"alpha",alpha,"tail","right");

if h == 0
  fprintf('H0 is not rejected. Steel pipes do NOT lose more heat than glass.\n')
else
  fprintf('H0 is rejected. Steel pipes DO lose more heat than glass pipes.\n')
endif

printf("p val= %1.6f\n", p);
printf("observed value= %1.6f\n", p);
printf("rejection region: (%1.6f, %1.6f)",q1,inf);
