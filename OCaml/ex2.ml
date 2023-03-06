(* Simple examples of recursion *)
(* 
   Lecture 1 
   1 Sep 2022
*)

(* recursion on numbers *)

let rec fact n =
  match n with
  | 0 -> 1
  | m when m>0 -> m * fact (m-1)
  | _ -> failwith "fact: negative input"
     
(** [fact n] returns the factorial of [n]
    Precondition: [n] is positive *)
let rec fact n =
  match n with
  | 0 -> 1
  | m -> m * fact (m-1)

let rec repeat : int -> 'a -> 'a list =
  fun n e ->
  match n with
  | 0 -> []
  | m -> e :: repeat (m-1) e
 

(* recursion on lists *)

let rec size : 'a list -> int =
  fun l ->
  match l with
  | [] -> 0
  | h::t -> 1 + size t

let rec sum : int list -> int =
  fun l ->
  match l with
  | [] -> 0
  | h::t -> h + sum t

let rec f l =
  match l with
  | [] -> []
  | h::t -> h::h:: f t

(* take 2 [1;2;3] => [1;2]
   take 10 [1;2;3] => [1;2;3]
   take 0 [1;2;3] => []
*)
let rec take : int -> 'a list -> 'a list =
  fun n l ->
  match n,l with
  | 0,_ -> []
  | _,[] -> []
  | m,h::t -> h::take (m-1) t

(* rad [1;2;2;2;3;1;4;4;4] =>
      [1;2;3;1;4] *)
                
let rec rad l =
  match l with
  | [] -> []
  | [x] -> [x]
  | h1::h2::t when h1=h2 -> rad (h2::t)
  | h1::h2::t -> h1:: rad (h2::t)



let rec mem : 'a -> 'a list -> bool =
  fun e l ->
  match l with
  | [] -> false
  | h::t -> e=h || mem e t
              
let rec has_duplicates : 'a list -> bool =
  fun l ->
  match l with
  | [] -> false
  | h::t -> mem h t || has_duplicates t

(** [sublist l1 l2] determines whether [l1] is a sublist of [l2].
 We say [l1] is a sublist of [l2] if every element of [l1] is an element of [l2] *)

let rec sublist l1 l2 : 'a list -> 'a list = bool =
match l1 with 
| [] -> true
| h::t -> mem h l2 && sublist t l2
    

let rec rev l =
match l with 
| [] ->[]
| h::t -> rev t @ [h]

let rec rev' l a=
match l with 
| [] -> a
| h::t -> rev' t(h::a)

let rec last l :'a list -> 'a =
    match l with 
    | [] -> failtwith "last: empty list"
    |[x] - > x
    | h :: t -> last l


let rec concat l1 l2 =
    match l1 with
    | [] -> l2
    | h::t -> h :: concat t l2


let rec succl : int list -> int list = 
fun l ->
match l with 
| [] -> []
| h::t -> (h+1) :: succl t

let rec upperl : char list -> char list = 
fun l ->
match l with 
| [] -> []
| h::T -> Char.uppercase_ascii h :: Char.uppercase_ascii T

let rec is_zerol : int list -> int list -> bool list=
fun l 
match l with 
|[] ->[]


let rec gtz : int list -> int list =
  fun l ->
    match l with 
    | [] ->[]
    | h:t -> 
      if h >0
        then h::gtz t 
    else gtz t 

 

    
     
