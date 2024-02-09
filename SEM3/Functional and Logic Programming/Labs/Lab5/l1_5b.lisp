; b) Write a function to return an association list with the two lists given as parameters.
;    Example: (A B C) (X Y Z) --> ((A.X) (B.Y) (C.Z)).

(defun myAppend(l p)
  (cond
    ((null l) p)
    (t (cons (car l) (myAppend (cdr l) p)))
  )
)


(defun association(l p)
  (cond
    ((and (null l) (null p)) nil)
    ((null l) (myAppend (list (cons nil (car p))) (association nil (cdr p) )))
    ((null p) (myAppend (list (cons (car l) nil)) (association (cdr l) nil )))
    (t (myAppend (list (cons (car l) (car p))) (association (cdr l) (cdr p) )))
  )
)

(association '(A B C) '(X Y Z))