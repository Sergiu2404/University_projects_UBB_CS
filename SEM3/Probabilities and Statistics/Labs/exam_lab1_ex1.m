%PREGATIRE EXAMEN LABORATOR
%1

A = [
1 -1 -2
2 1 3
1 1 1
];
disp(A(1:2, 2:3)); %rows: 1,2; cols: 2,3
disp(A(1,1)); %elem from row 1 col 1
disp(A); %print in the command window
disp(prod(A(:)));
disp(sum(A(1:5))); %sum of elements 1 to 5


%2
X = 0:1:3; % start, step, end
%function y1 = f1(x)
%  y1 = x.^5 / 10;
%endfunction

figure(1); %for plotting in first window
plot(X, exam_f1(X), 'blue');
title('graph for f1');
xlabel('x');
ylabel('x^5/10');
%hold on
figure(2); %for printing in another window
plot(X, exam_f2(X), 'r-.');
title('graph for f2');
xlabel('x');
ylabel('sin(x)');

figure(3); %another one for both graphs
plot(X, exam_f1(X), 'blue');
hold on
plot(X, exam_f2(X), 'r-.');
legend('f1', 'f2');



