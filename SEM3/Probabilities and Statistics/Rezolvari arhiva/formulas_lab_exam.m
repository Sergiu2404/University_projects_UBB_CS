%FORMULAS
%mean(miu): mean(X) - average of all the data
%variance(sigma^2): var(X) - sum of squares of diff of all nr and the mean over the nr of all the elems in the set of data
%standard deviation(sigma): std(X) - square root of the vairance

%covariance: cov(x1,x2)
%correlation_coef(rho) = corrcoef(data_matrix)
%where data_matrix = [x' y']; (transpose x and y to create 2 columns matrix)
meanX = sum(x_val .* x_freq) / sum(x_freq);
varX = sum((x_val - meanX).^2 .* x_freq) / sum(x_freq);
covarXY = sum((x_val - meanX) .* (y_val - meanY) .* x_freq) / sum(x_freq);
std_devX = sqrt(varX);
correlation_coefXY = covarXY / sqrt(varX * varY);
%OR
correlation_coefXY = covarXY / std(X) * std(Y);
%1 => perf positive linear relationship
%-1 => perf negative linear relationship
%0 =>no linear relationship between the variables



%H0: always take null hyp the equality: theta = theta0
%H1: theta < theta0 => left tailed test
%    theta > theta0 => right tailed test
%    theta != theta0 => 2 tailed test

alpha in (0,1) => significance level

TS -> statistics test
Ts0 = TS(theta = theta0) -> observed value of the statistic test
RR -> rejection region
p-val -> probab value
sigma^2 is the variance, sigma is the std_dev



%OPTIONAL ARGS FOR THE TEST HAVE TO BE IN NAME/VALUE ORDER
%--ZTEST-- USED WHEN WE WANT TO TEST MEAN OF A POPULATION AND STANDARD DEV OR SIGMA KNOWN
[h,p,ci,zval]=ztest(X,m,sigma,"alpha",alpha,"tail","left"); %ZTEST USED WHEN STANDARD DEV OR SIGMA KNOWN
%x-vector, m-testing value, sigma-given in stmt or computed(std(x)), alpha-sign level
tail = -1 or "left"=> left tailed
%if we want to check if the population parameter is less than a certain value
tail = 0 "both"=> 2 tailed
%if we want to check if there is any difference(lower/higher) from the null hyp
tail = 1 "right"=> right tailed
%if we want to test if the pop parameter si significantly greater than a a certain value
RR - for ztest we use norminv:
  left tailed: RR = (-inf, z) where z = norminv(alpha, 0, 1);
  right tailed: RR = (z, inf) where z = norminv(1-alpha, 0, 1);
  2 tailed: RR = (-inf, z1) U (z2, inf) where z1=norminv(alpha/2,0,1); z2=norminv(1-alpha/2,0,1)
  %???with 0 and 1 as params or not
p<alpha => statistically significant(rejection of null hypothesis)



%TTEST USED WHEN SIGMA OR STD DEV IS NOT KNOWN, MORE ROBUST FOR SMALL SIZE SAMPLES
[h, p, ci, ttest_zval] = ttest(X, n0, "alpha", alpha, "tail", "right");
%alpha-sign level(usually 5%)
%[h, p, ci, zval] = ttest(X, n0, alpha, tail);
%stats-test statistics: .tstat = TS0 -> val of statistic test
                        .df-degrees of freedom
                        .sd-estimated population standard deviation
RR - for ttest we use student distribution T(n-1), tinv
  left tailed: RR = (-inf, z) where z = tinv(alpha, n - 1);
  right tailed: RR = (z, inf) where z = tinv(1 - alpha, n - 1);
  2 tailed: RR = (-inf, z1) U (z2, inf) where z1=norminv(alpha/2, n-1); z2=norminv(1-alpha/2, n-1)
%!!!
zval = ttest_zval = ttest_stats.tstat;



%VARTEST2 - ABOUT THE EQUALITY OF 2 POPULATION VARIANCES, DETERMINES IF THE VARIANCES FROM 2 POPULATIONS ARE SIGNIFICANTLY DIFFERENT
%USES F-DISTIRBUTION
[h , p, ci, stats] = vartest2(X1, X2, 'alpha', alpha, 'tail', 'both'); %by default alpha = 0.05, tail = 'both'
%alpha-sign level usually 5%
%stats: .fstat -> the val of statistic test
%        .df1 -> numerator degrees of function of the test
%        .df2 -> denominator degrees of function of the test
RR - for vartest2 we use finv
  left tailed: RR = (-inf, f) where f = finv(alpha, n1 - 1, n2 - 1);
  right tailed: RR = (f, inf) where f = finv(1-alpha, n1 - 1, n2 - 1);
  2 tailed: RR = (-inf, f1) U (f2, inf) where f1 = finv(alpha / 2, n1 - 1, n2 - 1); f2 = finv(1 - alpha / 2, n1 - 1, n2 - 1);
%!!!
zval = stats.fstat;
f1 = finv(alpha / 2, n1 - 1, n2 - 1);
f2 = finv(1 - alpha / 2, n1 - 1, n2 - 1); %2 sample f test for equal variances



%TTEST2 USED WHEN WE HAVE TO TEST THE DIFFERENCE OF MEANS OF 2 POPULATIONS (ON AVERAGE IS MENTIONED)
[h, p, ci, stats] = ttest2(X1, X2, 'alpha', alpha, 'tail', 'right'); %+ 'vartype','unequal' if variances not equal from previous subpoint
zval = stats.tstat;
RR - for ttest2 we use tinv, but we have more cases
  A. if the pop variances are equal (H from vartest2 is 0):
    1) left tailed: RR = (-inf, z), n=n1+n2-2, where z = tinv(alpha,n);
    2) right tailed: RR = (z, inf), n=n1+n2-2, where z = tinv(1-alpha,n);
    3) 2 tailed: RR = (-inf, z1) U (z2, inf) where z1 = tinv(alpha / 2,n); z2 = tinv(1 - alpha / 2,n);
  B. if the pop variances are different (H from vartest2 is 1)
    c=var((x1)/n1) / (var(x1)/n1 + var(x2)/n2);
    n = c.^2/(n-1) + (1-c).^2/(n2-1);
    n=1/n;
    1) left tailed: RR = (-inf, z) where z = tinv(alpha,n);
    2) right tailed: RR = (z, inf) where z = tinv(1-alpha,n);
    3) 2 tailed: RR = (-inf, z1) U (z2, inf) where z1 = tinv(alpha / 2,n); z2 = tinv(1 - alpha / 2,n);


%CONFIDENCE INTERVALS
1) for a pop mean
  a) sigma is known:
  n=length(x), m=mean(x), q=norminv(1-alpha/2,0,1);
  left = m - sigma/sqrt(n) * q;   right = m + sigma/sqrt(n) * q;

  b)sigma unknown:
  n=length(x), q=tinv(1-alpha/2,n-1), m=mean(x), s=std(x)
  left = m-s/sqrt(n) * q;     right = m+s/sqrt(n) * q;


2) for a pop variance
  n=length(x), q1 = chi2inv(1-alpha/2,n-1), q2 = chi2inv(alpha/2,n-1), v=var(x)
  left = ((n-1) * v) / q1;      right = ((n-1) * v) / q2;


3) for the diff of 2 population meansq
  a)both sigma known
  n1,n2-lengths, m1,m2-means, q=norminv(1-alpha/2,0,1);
  left = m1 - m2 - q * sqrt(sigma1.^2/n1 + sigma2.^2/n2);
  right = m1 - m2 + q * sqrt(sigma1.^2/n1 + sigma2.^2/n2);

  b)sigma unknown and equal:
  n1,n2-lengths, m1,m2-means, v1,v2-variances
  n = n1_n2-2
  q=tinv(1-alpha/2,n);
  sp=sqrt(((n1-1)*v1 + (n2-1)*v2) / n);
  left = m1 - m2 - q * sp * sqrt(1/n1 + 1/n2);
  right = m1 - m2 + q * sp * sqrt(1/n1 + 1/n2);

  c)
  sigma unknown and different:
  n1,n2-lengths, m1,m2-means, v1,v2-variances
  c=(v1/n1)/(v1/n1 + v2/n2);
  n=c.^2/(n1-1)+(1-c).^2/(n2-1);
  n=1/n;
  q=tinv(1-alpha/2,n);
  left = m1 - m2 - q * sqrt(v1/n1 + v2/n2);
  left = m1 - m2 + q * sqrt(v1/n1 + v2/n2);


4) for th ratio of 2 pop variances
  n1,n2-lengths, v1,v2-variances
  q1=finv(1-alpha/2,n1-1,n2-1);
  q2=finv(alpha/2,n1-1,n2-1);
  left = 1/q1 * v1/v2;     right = 1/q2 * v1/v2;





The significance level, often denoted as alpha (α), is a threshold
 used in hypothesis testing to determine whether a result is statistically
 significant. It represents the probability of rejecting the null hypothesis
 when it is true. Commonly used significance levels are 0.05 (5%) and 0.01 (1%).

In hypothesis testing, if the p-value (the probability of observing the data or
 something more extreme if the null hypothesis is true) is less than or equal to
 the chosen significance level, then the results are considered statistically
 significant. This leads to the rejection of the null hypothesis in favor of the
 alternative hypothesis.

Both confidence intervals and significance levels are fundamental concepts in
 statistics, aiding in inference and decision-making based on sample data.
 Confidence intervals provide a range of plausible values for a population
 parameter, while significance levels help in determining whether observed
 differences or effects are likely due to a real effect or simply due to chance.





A confidence interval is a range of values that is estimated to contain the true
 value of a population parameter, such as a population mean or proportion. It is
 calculated from sample data and is used to quantify the uncertainty or the level
 of confidence associated with the estimation.

For example, if you're estimating the mean height of all students in a school
 using a sample of students, a 95% confidence interval of [150 cm, 160 cm] means
 that you are 95% confident that the true mean height of all students falls between
 150 cm and 160 cm.
