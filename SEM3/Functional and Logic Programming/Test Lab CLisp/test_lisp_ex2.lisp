; problem 2: caesar cypher
; write a caesar cypher decoder that converts a string ("hash") to a decoded string ("plaintext") given a constant rotation of all characters
; of the plaintext (eg. "adi" becomes "cfk" with a rotation of 2; to decode, you rotate each ASCII code of the letter back 2 chars, using "rem"  for modulo 58 remainder)
; cypher is given as list; obtain a list with the chars of plaintext using the functions "char-code" and "code-char" that return
; (char-code #\A) => 65 and (char-code #\z) => 122 with the interval 122-65=58 characters in ASCII letters
; tests:
; > (caesar '(#\C #\F #\A) 2)
; (#\A #\D #\y)
; > (caesar '(#\C #\F #\K) 2)
; (#\A #\D #\I)

(defun caesar (cypher rot)
  (mapcar (lambda (char)(code-char (mod(-(char-code char) rot(-(char-code #\A))) 58))) cypher)
)
(caesar '(#\C #\F #\K) 2)