%LAB6 1) a)
X = [
  7 7 4 5 9 9 ...
  4 12 8 1 8 7 ...
  3 13 2 1 17 7 ...
  12 5 6 2 1 13 ...
  14 10 2 4 9 11 ...
  3 5 12 6 10 7
];
alpha = input("give significance level: ");
n = length(X);

printf("this is a left tailed test for the mean when sigma is known\n");
#the null hypothesis is h0:miu=8.5(it is together with miu>8.5,and the interpretation is the efficiency standard is met)
#the alternative hypothesis h1:miu<8.5(the efficiency standard is not met)
#this is a left tailed test for miu

sigma = 5;
%value of comparison is 8.5
n0 = 8.5; %testing value

[h, p, ci, zval] = ztest(X, n0, sigma, "alpha", alpha, "tail", "left");
%h - outcome of the hyphotesis test: 1=>null hyp rejected, 0=> failure to reject null hyp
%p-value=probab of observing the test statistics
%ci=conf int associated with the hyp test (range of values where the mean is likely to lie)
%zval=observed sample mean subtracted by the assumed population mean, divided by the standard error at the meanz = norminv(alpha, 0, 1);

#"left" gives the value of the tail
#if we have this pb we delete the tail without " "
z = norminv(alpha, 0, 1);
RR = [-inf, z];

printf("the value of h is %d \n",h);
if h == 1
  printf("null hyp is rejected, data suggests the standard is not met\n");
else
  printf("null hyp is not rejected, the standard is met\n");
endif

printf("the rejection region is:(%4.4f float,%4.4f float)\n",RR);
printf("the value of the test statistic is%4.4f float\n",zval);
printf("the pvalue of the test is %4.4f float\n",p);
