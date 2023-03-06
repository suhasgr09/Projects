
(* The type of tokens. *)

type token = 
  | UNTUPLE
  | UNPAIR
  | TIMES
  | THEN
  | SND
  | SETREF
  | SET
  | SEMICOLON
  | RPAREN
  | RBRACE
  | RANGLE
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
  | LANGLE
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
  | DOT
  | DIVIDED
  | DEREF
  | DEBUG
  | COMMA
  | BEGIN

(* This exception is raised by the monolithic API functions. *)

exception Error

(* The monolithic API. *)

val prog: (Lexing.lexbuf -> token) -> Lexing.lexbuf -> (Ast.expr)
