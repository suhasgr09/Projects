open Ast
open Ds
(*Bethel Hall*)
let rec eval_expr : expr -> exp_val ea_result =
  fun e ->
  match e with
  | Int(n) -> return (NumVal n)
  | Var(id) -> apply_env id
  | Add(e1,e2) ->
    eval_expr e1 >>=
    int_of_numVal >>= fun n1 ->
    eval_expr e2 >>=
    int_of_numVal >>= fun n2 ->
    return (NumVal (n1+n2))
  | Sub(e1,e2) ->
    eval_expr e1 >>=
    int_of_numVal >>= fun n1 ->
    eval_expr e2 >>=
    int_of_numVal >>= fun n2 ->
    return (NumVal (n1-n2))
  | Mul(e1,e2) ->
    eval_expr e1 >>=
    int_of_numVal >>= fun n1 ->
    eval_expr e2 >>=
    int_of_numVal >>= fun n2 ->
    return (NumVal (n1*n2))
  | Div(e1,e2) ->
    eval_expr e1 >>=
    int_of_numVal >>= fun n1 ->
    eval_expr e2 >>=
    int_of_numVal >>= fun n2 ->
    if n2==0
    then error "Division by zero"
    else return (NumVal (n1/n2))
  | Let(v,def,body) ->
    eval_expr def >>= 
    extend_env v >>+
    eval_expr body 
  | ITE(e1,e2,e3) ->
    eval_expr e1 >>=
    bool_of_boolVal >>= fun b ->
    if b 
    then eval_expr e2
    else eval_expr e3
  | IsZero(e) ->
    eval_expr e >>=
    int_of_numVal >>= fun n ->
    return (BoolVal (n = 0))
  | Pair(e1,e2) ->
    eval_expr e1 >>= fun ev1 ->
    eval_expr e2 >>= fun ev2 ->
    return (PairVal(ev1,ev2))
  | Fst(e) ->
    eval_expr e >>=
    pair_of_pairVal >>= fun p ->
    return (fst p)
  | Snd(e) ->
    eval_expr e >>=
    pair_of_pairVal >>= fun p ->
    return (snd p)
  | Proc(id,e)  ->
    lookup_env >>= fun en ->
    return (ProcVal(id,e,en))
  | App(e1,e2)  -> 
    eval_expr e1 >>=
    clos_of_procVal >>= fun (id,e,en) ->
    eval_expr e2 >>= fun w ->
    return en >>+
    extend_env id w >>+
    eval_expr e
  | Letrec(id,par,e1,e2) ->
    extend_env_rec id par e1 >>+
    eval_expr e2

  | MakeArray(len, def) ->
    eval_expr len >>=
    int_of_numVal >>= fun n ->
    eval_expr def >>= fun v ->
    return (ArrayVal (List.init n (fun _ -> v)))

  | LengthArray(arr) ->
    eval_expr arr >>=
    list_of_arrayVal >>= fun a ->
    return (NumVal (List.length a))

  | SetArray(arr, idx, e) ->
    eval_expr arr >>=
    list_of_arrayVal >>= fun a ->
    eval_expr idx >>=
    int_of_numVal >>= fun i ->
    eval_expr e >>= fun v ->
      (*insert into a list at ith index*)
    let rec insert l i v =
      match l with
      | [] -> []
      | h::t -> if i = 0 then v::t else h::(insert t (i-1) v)
    in
    return (ArrayVal (insert a i v))

  | GetArray(arr, idx) ->
    eval_expr arr >>=
    list_of_arrayVal >>= fun a ->
    eval_expr idx >>=
    int_of_numVal >>= fun i ->
    (*get the ith element*)
    let rec get l i =
      match l with
      | [] -> error "Index out of bounds"
      | h::t -> if i = 0 then return h else get t (i-1)
    in  
    get a i
    

  | Debug(_e) ->
    string_of_env >>= fun str ->
    print_endline str; 
    error "Debug called"
  | _ -> error "Not implemented"

(** [parse s] parses string [s] into an ast *)
let parse (s:string) : expr =
  let lexbuf = Lexing.from_string s in
  let ast = Parser.prog Lexer.read lexbuf in
  ast

(** [interp s] parses [s] and then evaluates it *)
let interp (s:string) : exp_val result =
  let c = s |> parse |> eval_expr
  in run c


