A=[10, 7, 8, 7; 7, 5, 6, 5; 8, 6, 10, 9; 7, 5, 9, 10]
b=[32; 23; 33; 31]
Ai=[10, 7, 8.1, 7.2; 7.8, 5.04, 6, 5; 8, 5.98, 9.89, 9; 6.99, 4.99, 9, 9.98]
bi=[32.1; 22.9; 33.1; 30.9]

#a
solution_a = inv(A) * b

#b
solution_bi = inv(A) * bi
err_b1 = norm(bi - b) / norm(b)
err_b2 = norm(solution_a - solution_bi) / norm(solution_a)

#c
solution_c = inv(Ai) * b
err_c1 = norm(Ai - A) / norm(A)
err_c2 = norm(solution_c - solution_a) / norm(solution_a)

#d
cond(A)
