;d) Write a function to return the number of all numerical atoms in a list at superficial level.

(defun getAllNumericalAtoms (l)
  (cond
    ((null l) 0)
    ((numberp (car l)) (+ 1 (getAllNumericalAtoms (cdr l))))
    (t (getAllNumericalAtoms (cdr l)))
  )
)