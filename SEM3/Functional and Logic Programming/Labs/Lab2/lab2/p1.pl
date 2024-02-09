%-a) compute the union of 2 given sets
%-b) given a list, determine the set of all the pairs in the list


%a)
union([], Set, Set). %base case: flow(i,i,o)
union([X|Set1], Set2, Union) :-
    (   member(X, Set2) -> 
        Set3=Union
    ;   not(member(X, Set2)) ->
        [X|Set3]=Union
    ),
    union(Set1, Set2, Set3).



%b)
pairs([], []). %flow(i,o)


pairs([X|Rest], Pairs) :- %create pairs
    combine(X, Rest, XPairs),    
    pairs(Rest, RestPairs),      
    append(XPairs, RestPairs, Pairs).


combine(_, [], []). %combine element with rest of the list

combine(X, [Y|Rest], [[X, Y]|Combined]) :- 
    combine(X, Rest, Combined).