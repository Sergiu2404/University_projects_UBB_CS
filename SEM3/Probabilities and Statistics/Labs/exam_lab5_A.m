%LAB 5 A
x_val = 20:1:27;
x_freq = [2, 1, 3, 6, 5, 9, 2, 2];

y_val = 75:1:82;
y_freq = [3, 2, 2, 5, 8, 8, 1, 1];

%a)
meanX = sum(x_val .* x_freq) / sum(x_freq);
meanY = sum(y_val .* y_freq) / sum(y_freq);

%b)
varX = sum((x_val - meanX).^2 .* x_freq) / sum(x_freq);
varY = sum((y_val - meanY).^2 .* y_freq) / sum(y_freq);

%c)
covarXY = sum((x_val - meanX) .* (y_val - meanY) .* x_freq) / sum(x_freq);

%d)
correlation_coefXY = covarXY / sqrt(varX * varY);
disp(correlation_coefXY);



