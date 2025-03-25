function [x, nit]=jacobi(A,b,x_0,err,maxnit)
  n=length(b); M=diag(diag(A)); N=M-A
  c=inv(M)*b; T=inv(M)*N
  x=x_0
  for k=1:maxnit
    x=T*x_0+c
    if norm(x-x_0,inf)<=err * ((1-norm(T,inf))/norm(T,inf))
      nit=k
      return
    endif
    x_0=x

  endfor
end
