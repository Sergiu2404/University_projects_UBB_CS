function x = backwards(A, b)
  n = length(b); x = zeros(size(b));
  for i = n : -1 : 1
    x(i) = (b(i) - A(i, i+1:n)*x(i+1:n))/A(i,i);
  endfor
end
