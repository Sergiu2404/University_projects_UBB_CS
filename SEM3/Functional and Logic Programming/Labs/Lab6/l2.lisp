;5. Return the level (depth) of a node in a tree of type (1). The level of the root element is 0.

(defun parcurg_st (l nrNoduri nrMuchii)
  (cond
    ((null l) nil)
    ((= nrNoduri ( + 1 nrMuchii)) nil) 
    (t (cons (car l) (cons (cadr l) (parcurg_st (cddr l) (+ 1 nrNoduri) (+ (cadr l) nrMuchii)))))
  )
)

(defun parcurg_dr (l nrNoduri nrMuchii)
  (cond
    ((null l) nil)
    ((= nrNoduri (+ 1 nrMuchii)) l) 
    (t (parcurg_dr (cddr l) (+ 1 nrNoduri) (+ (cadr l) nrMuchii)))
    ;traverse reucrsively right subtree
  )
)

(defun stang(l)
  (parcurg_st (cddr l) 0 0)
)

(defun drept(l)
  (parcurg_dr (cddr l) 0 0)
)

(defun findDepth(l elem level)
  (cond
    ((null l) 0)
    ((equal (car l) elem) level)
    (t (+ (findDepth (stang l) elem (+ 1 level)) (findDepth (drept l) elem (+ 1 level))))
  )
)


%(findDepth '(10 2 11 0 12 2 13 0 14 0) 14 0)
