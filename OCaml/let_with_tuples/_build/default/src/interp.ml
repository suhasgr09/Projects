open Ast
open Ds

(** [eval_expr e] evaluates expression [e] *)
let rec eval_expr : expr -> exp_val ea_result =
  fun e ->
  match e with
  | Int(n) ->
    return (NumVal n)
  | Var(id) ->
    apply_env id
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
  | Let(id,def,body) ->
    eval_expr def >>= 
    extend_env id >>+
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
  | Tuple(es) ->
    sequence (List.map eval_expr es) >>= fun evs ->
    return (TupleVal evs)
  | Untuple(ids,e1,e2) ->
    eval_expr e1 >>=
    list_of_tupleVal >>= fun evs ->
    if List.length ids<>List.length evs
         then error "untuple: mismatch"
         else extend_env_list ids evs >>+
           eval_expr e2
   | Record(fs) -> let (ids,es) = List.split fs in
   sequence (List.map eval_expr es) >>= fun evs ->
   return (RecordVal (List.combine ids evs))
   | Proj(e,id) ->
    eval_expr e >>=
    (recVal_fields : exp_val -> (string * exp_val) list ea_result) >>= fun fs ->
    (match List.assoc_opt id fs with
    | Some v -> return v
    | None -> error "Field undefined")

   | Debug(_e) ->
    string_of_env >>= fun str ->
    print_endline str; 
  
    error "Debug called"
  | _ -> failwith "Not implemented yet!"

(*
 untuple <x,y> = <1+2,3+4> in  x+y
*)

(** [parse s] parses string [s] into an ast *)
let parse (s:string) : expr =
  let lexbuf = Lexing.from_string s in
  let ast = Parser.prog Lexer.read lexbuf in
  ast


(** [interp s] parses [s] and then evaluates it *)
let interp (e:string) : exp_val result =
  let c = e |> parse |> eval_expr
  in run c
  


