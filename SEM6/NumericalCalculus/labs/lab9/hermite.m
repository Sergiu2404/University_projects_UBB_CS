function h = hermite(x0, f0, df0, x)
  [z, t] = divdiff2(x0, f0, df0);
  #d(1, :);
  n = zeros(size(x));

  for j=1:length(x0)
    p = t(1, j);
    for i=1:(j - 1)
      p = p .* (x - x0(i))
    endfor

    n = n + p;
  endfor

  h = n;
