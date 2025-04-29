function[z, t] = divdiff2(x, f, df)
  z = repelem(x,2); n = length(z); t = zeros(n);
  t(:,1)=repelem(f,2)'; # repelem for [1, 2, 3] returns [1, 1, 2, 2, 3, ]
  t(1:2:n-1, 2) = df';
  t(2:2:n-2, 2) = (diff(f)./diff(x))';
  for j = 3:n
    t(1:n-j+1, j) = diff(t(1:n-j+2, j-1))./(z(j:n)-z(1:n-j+1))';
  endfor
end

