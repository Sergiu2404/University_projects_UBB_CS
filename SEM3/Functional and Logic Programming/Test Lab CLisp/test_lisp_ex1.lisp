; problem 2: map of fns
; count how many of a list ("ps") of predicates "p" returned true for a given numerical value "n" by calling (funcall p n) on each predicate
; test: 
; > (num-true 2 (list (lambda (x) (< 1 x)) (lambda (x) (= 3 x)) (lambda (x) (< x 5))))
; 2


(defun num-true (n predicates)
  (count t (mapcar (lambda (p) (funcall p n)) predicates)))

(num-true 2 (list (lambda (x) (< 1 x)) (lambda (x) (= 3 x)) (lambda (x) (< x 5))))
