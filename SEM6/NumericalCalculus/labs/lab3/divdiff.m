function t = divdiff(x, f)
  n = length(x); t = zeros(n);
  t(:,1) = f';
  for j = 2:n
    t(1:n-j+1, j) = diff(t(1:n-j+2, j-1))./(x(j:n)-x(1:n-j+1))';
  endfor
end


