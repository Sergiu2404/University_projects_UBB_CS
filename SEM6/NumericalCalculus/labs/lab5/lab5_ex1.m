#lab5

n = 7
A = 5*eye(n)-diag(ones(n-1,1),1)-diag(ones(n-1,1),-1)
b = [4; 3*ones(n-2,1);4]
x0 = zeros(size(b))
maxnit = 1000
err = 10^(-5)


[x, nit] = jacobi(A, b, x0, err, maxnit)
[x, nit] = gauss(A, b, x0, err, maxnit)
