
(* The type of tokens. *)

type token = 
  | TIMES
  | THEN
  | SND
  | SETREF
  | SET
  | SEMICOLON
  | RPAREN
  | RBRACE
  | PROC
  | PLUS
  | PAIR
  | NOT
  | NEWREF
  | MINUS
  | MAX
  | LPAREN
  | LETREC
  | LET
  | LBRACE
  | ISZERO
  | INT of (int)
  | IN
  | IF
  | ID of (string)
  | FST
  | EQUALS
  | EOF
  | END
  | ELSE
  | DIVIDED
  | DEREF
  | DEBUG
  | COMMA
  | BEGIN
  | ABS

(* This exception is raised by the monolithic API functions. *)

exception Error

(* The monolithic API. *)

val prog: (Lexing.lexbuf -> token) -> Lexing.lexbuf -> (Ast.expr)
