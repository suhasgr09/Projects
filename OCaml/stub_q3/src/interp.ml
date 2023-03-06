open Ast
open Ds
(*Bethel Hall*)
(*Suhas Gorur Ravi Kumar*)

(** [eval_expr e] evaluates expression [e] producing a result *)
let rec eval_expr : expr -> env -> exp_val result =
  fun e en ->
  match e with
  | Int n -> return (NumVal n)
  | Var id -> lookup_env id en
  | Sub(e1,e2) ->
    eval_expr e1 en >>= 
    int_of_numVal >>= fun n ->
    eval_expr e2 en >>=
    int_of_numVal >>= fun m ->
    return (NumVal (n-m))
  | Add(e1,e2) ->
    eval_expr e1 en >>= 
    int_of_numVal >>= fun n ->
    eval_expr e2 en >>=
    int_of_numVal >>= fun m ->
    return (NumVal (n+m))
  | Div(e1,e2) ->
    eval_expr e1 en >>=
    int_of_numVal >>= fun m ->
    eval_expr e2 en >>= 
    int_of_numVal >>= fun n ->
    if n=0
    then error "div by zero"
    else return (NumVal (m/n))
  | IsZero(e) ->
    eval_expr e en >>=
    int_of_numVal >>= fun n ->
    return (BoolVal (n=0))
  | ITE(e1,e2,e3) ->
    eval_expr e1 en >>=
    bool_of_boolVal >>= fun b ->
    if b
    then eval_expr e2 en
    else eval_expr e3 en
  | Let(id,e1,e2) ->
    eval_expr e1 en >>= fun w ->
    eval_expr e2 (extend_env id w en)
  | Pair(e1,e2) ->
    eval_expr e1 en >>= fun v1 ->
    eval_expr e2 en >>= fun v2 ->
    return (PairVal(v1,v2))
  | Fst(e) ->
    eval_expr e en >>=
    pair_of_pairVal >>= fun (l,_) ->
    return l
  | Snd(e) ->
    eval_expr e en >>=
    pair_of_pairVal >>= fun (_,r) ->
    return r

  | Max(e1,e2) ->
    eval_expr e1 en >>= 
    int_of_numVal >>=fun a ->
    eval_expr e2 en >>= 
    int_of_numVal >>= fun b ->
    return(NumVal(max a b))
   
 

    
(** [parse s] parses [s] into an ast *)
let parse s =
  let lexbuf = Lexing.from_string s in
  let ast = Parser.prog Lexer.read lexbuf in
  ast


(** [interp e] interpret expression [e] returning a result *)
let interp (e:string)  =
  eval_expr (parse e) EmptyEnv
  


