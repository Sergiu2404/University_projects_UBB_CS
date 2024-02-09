%MODEL 4 ARHIVA
A = [1021 980 1017 988 1005 998 ...
1014 985 1004 1030 1015 995 1023];
B = [1070 970 993 1013 1006 1002 1014 ...
997 1002 1010 975];
n1 = length(A);
n2 = length(B);

%a) at the 5% sign level, do the population variances seem to differ
alpha = 0.05;
%pop variances seem to differ => vartest2
%H0: sigma1 = sigma2
%H1: sigma1 != sigma2

[h, p, ci, stats] = vartest2(A, B, "alpha", alpha, "tail", "both");

if h == 0
  printf("null hyphotesis not rejected, variances seem to be equal\n");
else
  printf("null hyp rejected, variances differ significantly equal");
endif


%from the test results sigma1 = sigma2 => we use T(n1+n2-2)
q1 = tinv(alpha/2, n1+n2-2);
q2 = tinv(1-alpha/2, n1+n2-2);

printf("rejection region for both tailed test is: (%1.2f, %1.2f) U (%1.2f, %1.2f)\n", - inf, q1, q2, inf);
printf("probability value is: %1.2f\n", p);
printf("confidence interval is: %1.2f\n", ci);
printf("statistic test value z-val: %1.2f\n", stats.fstat);

%b) for the same sign level, on average, does supplier A seem to be more reliable?
%on average => ttest2
%H0: miu1 = miu2
%H1: miu1 > miu2
[h1, p1, ci1, stats1] = ttest2(A, B, "alpha", alpha, "tail", "right");

if h == 0
  printf("null hyphotesis not rejected, supplier A more reliable\n");
else
  printf("null hyp rejected, supplier A is not more reliable");
endif

q3 = tinv(1-alpha/2, n1+n2-2);
printf("rejection region is: (%1.2f,%1.2f)\n", q3, inf);
