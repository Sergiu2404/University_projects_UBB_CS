n = 7
A = 5 * eye(n) - diag(ones(n-1,1),1) - diag(ones(n-1,1),-1)
b = [4; 3*ones(n-2,1); 4]

[L, U, P] = lu(A)

y = forwards(L, P * b) # solve Ly = b vector after applying P row changes
x = backwards(U, y) # solve Ux = y

# LU factorizaiton
disp(x)
