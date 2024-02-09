; a) Write twice the n-th element of a linear list. Example: for (10 20 30 40 50) and n=3 will produce (10
;    20 30 30 40 50).

(defun twiceNthElem(l n pos)
  (cond
    ((null l) nil)
    ((equal n pos) (cons (car l) (cons (car l) (twiceNthElem (cdr l) n (+ 1 pos)))))
    (t (cons (car l) (twiceNthElem (cdr l) n (+ 1 pos))))
  )
)

(defun mainA(l n)
  (twiceNthElem l n 0)
)

(mainA '(10 20 30 40 50) 3)
