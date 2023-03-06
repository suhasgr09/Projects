type 'a result = Ok of 'a | Error of string

let (>>=) c f =
  match c with
  | Error s -> Error s
  | Ok v -> f v

let return v =
  Ok v

let error s =
  Error s

type exp_val =
  | BoolVal of bool
  | NumVal of int
  | PairVal of exp_val*exp_val
    
type env =
  | EmptyEnv
  | ExtendEnv of string*exp_val*env

(* 

  ExtendEnv("x",NumVal 3,
     ExtendEnv("y",BoolVal true,
       EmptyEnv)) :: env

[x =3, y=true]
*)

let rec lookup_env : string -> env -> exp_val result =
  fun id en ->
  match en with
  | EmptyEnv -> Error (id^" not bound")
  | ExtendEnv(id',ev,t) ->
    if id=id'
    then Ok ev
    else lookup_env id t
        
let extend_env id ev en =
  ExtendEnv(id,ev,en)

       
let int_of_numVal : exp_val -> int result =
  fun ev ->
  match ev with
  | NumVal n -> return n
  | _ -> error "type error: expected a number"

let bool_of_boolVal ev =
  match ev with
  | BoolVal b -> return b
  | _ -> error "type error: expected a boolean"

let pair_of_pairVal : exp_val -> (exp_val*exp_val) result =
  fun ev ->
  match ev with
  | PairVal(ev1,ev2) -> return (ev1,ev2)
  | _ -> error "type error: expected a pair"
