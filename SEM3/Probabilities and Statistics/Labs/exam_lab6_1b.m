%LAB6 1) b)
%same as point a, but sigma is unknown
alpha=input('enter the significance level (alpha is a probability): ');
X=[7 7 4 5 9 9 ...
  4 12 8 1 8 7 ...
  3 13 2 1 17 7 ...
  12 5 6 2 1 13 ...
  14 10 2 4 9 11 ...
  3 5 12 6 10 7]
n=length(X);

n0 = 5.5;
%tail = 1;%right tailed test
[h, p, ci, zval] = ttest(X, n0, "alpha", alpha, "tail", "right");
%[h, p, ci, zval] = ttest(X, n0, alpha, tail);

ttest_zval = ttest_stats.tstat;

z = tinv(1 - alpha, n - 1);
RR = [z, inf];

if h == 1
  printf("null hyp is rejected, data suggests the average exceeds 5.5\n");
else
  printf("null hyp is not rejected, the average does not exceed 5.5\n");
endif

printf("the rejection region is:(%4.4f float,%4.4f float)\n",RR);
printf("the value of the test statistic is %4.4f float\n",ttest_zval);
printf("the pvalue of the test is %4.4f float\n",p);
