function [x, nit] = gauss(A, b, x0, err, maxnit)
  M = tril(A)
  N = -triu(A,1)
  n=length(b)
  c=inv(M)*b
  T=inv(M)*N
  x=x0
  for k=1:maxnit
    x=T*x0+c
    if norm(x-x0,inf)<=((1-norm(T,inf))/norm(T,inf))*err
      nit=k
      return
    endif
    x0=x

  endfor
end
