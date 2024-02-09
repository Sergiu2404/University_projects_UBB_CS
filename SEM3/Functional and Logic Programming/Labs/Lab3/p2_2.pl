%5
first_element([F|_], F).


replace_element([], _, _, []).
replace_element([H|T], H, NewList, [NewList|Rest]) :-
    replace_element(T, H, NewList, Rest).
replace_element([H|T], First, NewList, [H|Rest]) :-
    H \= First, 
    replace_element(T, First, NewList, Rest).



replace_first_elements([], _, []).
%check for every element if is sublist
replace_first_elements([H|T], NewList, [NewH|Rest]) :- 
    is_list(H),  
    [First|_] = H, 
    replace_element(H, First, NewList, NewH),
    replace_first_elements(T, NewList, Rest).
%if head not a sublist then just add it to the result
replace_first_elements([H|T], NewList, [H|Rest]) :-
    \+ is_list(H),
    replace_first_elements(T, NewList, Rest).

%C:\Users\Sergiu\Desktop\SEM3\plf\lab3_p2\p2_2.pl
