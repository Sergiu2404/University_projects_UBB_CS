% a. Sort a list with removing the double values. E.g.: [4 2 6 2 3 4] --> [2 3 4 6]

insert([],P,P).
insert([H|T],P,[H|R]):-
       insert(T,P,R).

insertOK([],E,[E]).
insertOK([H|T],E,[H|T]):-
    H=:=E.
insertOK([H|T],E,RI):-
    E<H,
    insert([E],[H|T],RI).
insertOK([H|T],E,RI):-
    insert([H],R,RI),
    insertOK(T,E,R).

sort_list([],[]).
sort_list([H|T],R1):-
    sort_list(T,R),
    insertOK(R,H,R1).

%b. For a heterogeneous list, formed from integer numbers and list of numbers,
%   write a predicate to sort every sublist with removing the doubles.
sortHetList([],[]).
sortHetList([H|T],[RS|R]):-
    is_list(H),
    !,
    sort_list(H,RS),
    sortHetList(T,R).
sortHetList([H|T],[H|R]):-
    sortHetList(T,R).