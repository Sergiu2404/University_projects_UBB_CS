; 5. Write a function that computes the sum of even numbers and then decrease the sum of odd numbers from that sum,
;    at any level of a list.


(defun parity(a)
  (cond
    ((equal 0 (mod a 2)) a)
    (t (- a))
  )
)

; totalSum(l)
; = findSign(l), if l is a number
; = 0, if l is an atom
; = totalSum(l1) + totalSum(l2) + ... + totalSum(ln), where l is a list of type l = l1l2...ln

(defun totalSum(l)
  (cond
    ((numberp l) (parity l))
    ((atom l) 0)
    (t (apply '+ (mapcar #'totalSum l)))
  )
)

(totalSum '(1 2 3 4 5 6 7 8))