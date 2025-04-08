function N = newton(x0, f0, x) #x vector
  d = divdiff(x0, f0);
  #d(1, :);
  n = zeros(size(x));

  for j=1:length(x0)
    p = d(1, j);
    for i=1:(j - 1)
      p = p .* (x - x0(i))
    endfor

    n = n + p;
  endfor

  N = n;
