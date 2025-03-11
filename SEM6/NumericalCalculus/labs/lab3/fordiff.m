function t = fordiff(f)
  n = length(f); t = zeros(n);
  t(:,1) = f';
  for j = 2:n
    t(1:n-j+1,j) = diff(t(1:n-j+2,j-1));
  endfor
end
