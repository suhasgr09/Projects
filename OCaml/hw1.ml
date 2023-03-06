type program = int list
let square : program = [0; 2; 2; 3; 3; 4; 4; 5; 5; 1]
let letter_e : program = [0;2;2;3;3;5;5;4;3;5;4;3;3;5;5;1]

let rec map f = function
  | [] -> []
  | h :: t -> f h :: map f t

  let rec last : 'a list -> 'a =
    fun l ->
    match l with
    | [] -> failwith "last: empty list"
    | [x] -> x
    | h::t -> last t
  
(*2.2 Exercises*)

(*mirror_image : int list -> int list*)

let rec mirror_image : int list -> int list = 
  fun l ->
  match l with 
  | [] -> []
  | (h::t) when h = 2 -> 4 :: mirror_image t
  | (h::t) when h = 4 -> 2 :: mirror_image t
  | (h::t) when h = 3 -> 5 :: mirror_image t
  | (h::t) when h = 5 -> 3 :: mirror_image t
  | (h::t) -> h::(mirror_image t) 

(*This will rotate the letter by 90 degree *)
let rec rotate_90_letter : int list -> int list = 
  fun l ->
    match l with 
    | [] -> []
    | (h::t) when h = 2 -> 3 :: rotate_90_letter t
    | (h::t) when h = 4 -> 5 :: rotate_90_letter t
    | (h::t) when h = 3 -> 4 :: rotate_90_letter t
    | (h::t) when h = 5 -> 2 :: rotate_90_letter t
    | (h::t) -> h::(rotate_90_letter t) 

(*This will rotate the word by 90 degree *)
let rec rotate_90_word : int list list -> int list list = 
fun l ->
  match l with 
  | [] -> []
  | (h::t) -> rotate_90_letter h :: rotate_90_word t   
    
(*This will take an input and sprint in n No times  *)
let rec repeat : int -> 'a -> 'a list = 
    fun n e ->
      match n with
      | 0 -> []
      | n -> e :: repeat (n-1) e


(*This is a helper fucntion which I have created being used in pantpgraph and uncompress *)
 let rec repeat_int  : int -> int  -> int list = 
   fun n e ->
     match n with
     | 0 -> []
     | n -> if e != 1 then e :: repeat_int (n-1) e else [e]


(*This is a fucntion which will enlrage the input by n times *)     
let rec  pantograph : int -> int list -> int list = 
fun n p -> 
match p with 
| [] -> []
| h::t when h = 0 -> h :: pantograph n t
| h::t -> (repeat_int n h) @ pantograph n t


(*This is a helper fucntion to plot the steps in co-ordinated being used in coverage*)
let rec next_step : int*int -> int -> int * int =
  fun c n ->
    match c with 

    |(x,y) -> if n = 2 then  (x,(y+1))
              else if n = 3 then ((x+1),y)
              else if n = 4 then (x,(y-1))
              else if n = 5 then ((x-1),y)
              else (x,y)  

(*This takes the input list and then gives a list of tuples which are co-ordinated*)
let rec coverage : int*int -> int list -> (int*int) list = 
  fun c l ->
  match l with 
  | [] -> []
  | h::t -> (next_step c h) :: coverage ((next_step c h)) t



(*This takes a list of tuples whcih will be converted from  the original list form. *)
  let compress l =
    let rec aux count acc = function
      | [] -> []
      | [x] -> (count+1, x) :: acc 
      | h1 :: (h2 :: _ as t) -> if h1 = h2 then aux (count + 1) acc t
                              else aux 0 ((h1,count+1) :: acc) t in
    List.rev (aux 0 [] l);;

 
 
  

(*This takes a the compressed list and prints in   the original list form. *)
  let rec uncompress : (int*int) list -> int list = 
    fun l ->
    match l with  
    | [] -> []
    | (x,y)::t -> (repeat_int x y)@uncompress t


(*This removes redundant steps and keep only the valid steps in the list. *)    
    let rec optimize: program -> program = 
      fun l ->
      match l with 
      |[] -> []
      |h1::[] -> []
      |h1::h2::t -> if h1 = h2 then optimize(h2::t) else h1::h2::optimize(t)

(*
Outputs 
Type #utop_help for help about using utop.


utop # #use "hw1.ml";;
type program = int list
val square : program = [0; 2; 2; 3; 3; 4; 4; 5; 5; 1]
val letter_e : program = [0; 2; 2; 3; 3; 5; 5; 4; 3; 5; 4; 3; 3; 5; 5; 1]
val map : ('a -> 'b) -> 'a list -> 'b list = <fun>
val last : 'a list -> 'a = <fun>
val mirror_image : int list -> int list = <fun>
val rotate_90_letter : int list -> int list = <fun>
val rotate_90_word : int list list -> int list list = <fun>
val repeat : int -> 'a -> 'a list = <fun>
val repeat_int : int -> int -> int list = <fun>
val pantograph : int -> int list -> int list = <fun>
val next_step : int * int -> int -> int * int = <fun>
val coverage : int * int -> int list -> (int * int) list = <fun>
val compress : int list -> (int * int) list = <fun>
val uncompress : (int * int) list -> int list = <fun>
val optimize : program -> program = <fun>
─( 23:34:35 )─< command 1 

utop # mirror_image letter_e ;;
- : int list = [0; 4; 4; 5; 5; 3; 3; 2; 5; 3; 2; 5; 5; 3; 3; 1]
─( 23:34:49 )─< command 2 

utop # rotate_90_letter letter_e ;;
- : int list = [0; 3; 3; 4; 4; 2; 2; 5; 4; 2; 5; 4; 4; 2; 2; 1]
─( 23:35:09 )─< command 3 

utop # rotate_90_word [ letter_e ; letter_e ];;
- : int list list =
[[0; 3; 3; 4; 4; 2; 2; 5; 4; 2; 5; 4; 4; 2; 2; 1];
 [0; 3; 3; 4; 4; 2; 2; 5; 4; 2; 5; 4; 4; 2; 2; 1]]
─( 23:35:26 )─< command 4 

utop # repeat 3 " hello " ;;
- : string list = [" hello "; " hello "; " hello "]
─( 23:35:41 )─< command 5 

utop # pantograph 2 letter_e ;;
- : int list =
[0; 2; 2; 2; 2; 3; 3; 3; 3; 5; 5; 5; 5; 4; 4; 3; 3; 5; 5; 4; 4; 3; 3; 3; 3; 5;
 5; 5; 5; 1]
─( 23:35:57 )─< command 6 

utop # pantograph 3 letter_e ;;
- : int list =
[0; 2; 2; 2; 2; 2; 2; 3; 3; 3; 3; 3; 3; 5; 5; 5; 5; 5; 5; 4; 4; 4; 3; 3; 3; 5;
 5; 5; 4; 4; 4; 3; 3; 3; 3; 3; 3; 5; 5; 5; 5; 5; 5; 1]
─( 23:36:08 )─< command 7 

utop # coverage (0 ,0) letter_e ;;
- : (int * int) list =
[(0, 0); (0, 1); (0, 2); (1, 2); (2, 2); (1, 2); (0, 2); (0, 1); (1, 1);
 (0, 1); (0, 0); (1, 0); (2, 0); (1, 0); (0, 0); (0, 0)]
─( 23:36:13 )─< command 8 

utop # compress letter_e ;;
- : (int * int) list =
[(0, 1); (2, 2); (3, 2); (5, 2); (4, 1); (3, 1); (5, 1); (4, 1); (3, 2);
 (5, 2); (1, 1)]
─( 23:36:23 )─< command 9 

utop # uncompress ( compress letter_e );;
- : int list =
[2; 2; 2; 2; 2; 2; 2; 2; 2; 2; 1; 1; 1; 1; 2; 2; 2; 2; 2; 2; 2; 2; 1]

utop # optimize [1];;
- : program = []
─( 23:39:13 )─< command 27 

utop # 
optimize [1;1;1;1];;
- : program = []
─( 23:39:30 )─< command 28 

utop # 
optimize [1;1;1;1;0];;
- : program = [1; 0]
─( 23:39:30 )─< command 29 

utop # 
optimize [1;1;1;1;0;1;0;1];;
- : program = [1; 0; 1; 0]
─( 23:39:31 )─< command 30 

utop # 
optimize [1;1;1;1;0;1;0;1;1;1;1];;
- : program = [1; 0; 1; 0]
─( 23:39:31 )─< command 31 

utop # 
optimize [0;1;0;1];;
- : program = [0; 1; 0; 1]
─( 23:39:31 )─< command 32 

utop # 
 optimize [2;3;4;5];;
- : program = [2; 3; 4; 5]
*)