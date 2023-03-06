
(* The type of tokens. *)

type token = 
  | TIMES
  | THEN
  | SET_ARRAY
  | SETREF
  | SET
  | SEMICOLON
  | RPAREN
  | RBRACE
  | PROC
  | PLUS
  | NEWREF
  | MINUS
  | MAKE_ARRAY
  | LPAREN
  | LETREC
  | LET
  | LENGTH_ARRAY
  | LBRACE
  | ISZERO
  | INT of (int)
  | IN
  | IF
  | ID of (string)
  | GET_ARRAY
  | EQUALS
  | EOF
  | END
  | ELSE
  | DIVIDED
  | DEREF
  | DEBUG
  | COMMA
  | BEGIN

(* This exception is raised by the monolithic API functions. *)

exception Error

(* The monolithic API. *)

val prog: (Lexing.lexbuf -> token) -> Lexing.lexbuf -> (Ast.expr)
