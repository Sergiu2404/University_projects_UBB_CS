# ex 1
A = [2, 4, 2; 0, -1, 1; 0, 0, -1]
b = [8; 0; -1]
backwards(A, b)

A = [2 1 -1 -2; 4, 4, 1, 3; -6, -1, 10, 10; -2, 1, 8, 4]
b = [2; 4; -5; 1]
gauss_pivot(A, b)



# ex 2
A = [5, -1, 0; -1, 5, -1; 0, -1, 5]
b = [4; 3; 4]

[L, U, P] = lu(A)
y = forwards(L, P*b)
x = backwards(U, y)
