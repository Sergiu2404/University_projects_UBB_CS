# 2
years = [1980, 1990, 2000, 2010, 2020];
pop = [4451, 5287, 6090, 6970, 7821];

xi = [2005, 2015];

inter_pop = my_lagrange_bar(years, pop, xi);

actual_pop = [6474, 7405];

relative_errors = abs((inter_pop - actual_pop) ./ actual_pop) * 100;

printf('%.2f\n', relative_errors(1));
printf('%.2f\n', relative_errors(2));

