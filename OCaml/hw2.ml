type 'a gt = Node of 'a*('a gt) list;;

let () = print_endline "This is Tree Provided to us in the assignment"
let () = print_endline ""

(****************************************************************************************************************************************)
(**************************************************************CODE**********************************************************************)
let t : int gt =
   Node (33 ,
  [ Node (12 ,[]) ;
   Node (77 ,
  [ Node (37 ,
   [ Node (14 , [])]) ;
  Node (48 , []) ;
   Node (103 , [])])
  ]);;
(****************************************************************************************************************************************)
let () = print_endline ""
let () = print_endline  "This is the tree I have created to validate against the functions"
let () = print_endline ""

(****************************************************************************************************************************************)
(**************************************************************TREE**********************************************************************)
let t2 : int gt = 
  Node (33 ,
  [ Node (12 ,[]) ;
   Node (77 ,[]);
   Node (37 ,[]) ;
  Node (48 , []) ;
   Node (103 , []);
  ]);;
(**************************************************************TREE**********************************************************************)
(****************************************************************************************************************************************)  
  let () = print_endline "  "
  let () = print_endline  "This is the Map function which applies the fucntion f to both head and tail of the list"
  let () = print_endline ""

(****************************************************************************************************************************************)
(**************************************************************CODE**********************************************************************)
  let rec map f l = 
    match l with 
    | [] -> []
    | h::t -> f h :: map f t
(**************************************************************CODE**********************************************************************)
(****************************************************************************************************************************************)


let () = print_endline "    "
let () = print_endline  "This is the mk_leaf function that given n builds a general tree that is a leaf holding n as data."
let () = print_endline "  "

(****************************************************************************************************************************************)
(**************************************************************CODE**********************************************************************)
  let mk_leaf : 'a -> 'a gt =
    fun n ->
    Node (n ,[])
(**************************************************************CODE**********************************************************************)
(****************************************************************************************************************************************)

let () = print_endline "    "
let () = print_endline "BEGIN"
let () = print_endline  "1) height: that given a general tree returns its height. The height of a tree is the length of the longest (in terms of number of nodes) path from the root to a leaf."
let () = print_endline "  "

(****************************************************************************************************************************************)
(**************************************************************CODE**********************************************************************)
(*1*)
let rec height t =
  let rec height_helper = function 
  | [] -> 0
  | h :: t -> max (height h) (height_helper t)
  in
  match t with
  |Node(_, c) -> 1 + height_helper c;;
(**************************************************************CODE**********************************************************************)
(****************************************************************************************************************************************)

let () = print_endline "  "
let () = print_endline  "Testing the height fucntion on both the trees"
let () = print_endline ""
let test_height = print_endline"Test for Tree t";;height t;;
let test_height2 = print_endline"Test for Tree t2";;height t2;;
let () = print_endline "END"



let () = print_endline "    "
let () = print_endline "BEGIN"
let () = print_endline  "2) size: that given a general tree returns its size. The size of a general tree consists of the number of nodes."
let () = print_endline ""

(****************************************************************************************************************************************)
(**************************************************************CODE**********************************************************************)
(*2*)
let rec size t =
  let rec size_helper = function
  | [] -> 0
  | h :: t -> size h + size_helper t
  in
  match t with
  |Node(_, c) -> 1 + size_helper c;;
(**************************************************************CODE**********************************************************************)
(****************************************************************************************************************************************)

let () = print_endline " "
let () = print_endline  "Testing the SIZE fucntion on both the trees"
let () = print_endline ""
let test_size = print_endline"Test for Tree t";; size t;;
let test_size2 = print_endline"Test for Tree t2";;size t2;;
let () = print_endline "END"
let () = print_endline "    "
let () = print_endline "BEGIN"
let () = print_endline  "3) paths_to_leaves t: returns a list with all the paths from the root to the leaves of the general tree t. Let n be the largest number of children of any node in t. A path is a list of numbers in the set  0, 1, . . . , n−1} such that if we follow it on the tree, it leads to a leaf. The order in which the paths are listed is irrelevant."
let () = print_endline ""


(****************************************************************************************************************************************)
(**************************************************************CODE**********************************************************************)
(*3*)
let rec paths_to_leaves t = 
  match t with 
  | Node(n, []) -> [[]]
  | Node(n, c) -> List.flatten (List.mapi (fun i j -> (List.map (fun x -> i :: x) j)) 
                                         (List.map paths_to_leaves c))
(**************************************************************CODE**********************************************************************)                                         
(****************************************************************************************************************************************)


let () = print_endline " "
  let () = print_endline  "Testing the PATH_TO_LEAVES fucntion on both the trees"
  let () = print_endline ""

let test_path_to_leaves = print_endline"Test for Tree t";; paths_to_leaves t;;
let paths_to_leaves2 = print_endline"Test for Tree t2";;paths_to_leaves t2;;
let () = print_endline "END"






let () = print_endline "    "
let () = print_endline "BEGIN"
let () = print_endline  "4) is_leaf_perfect: that determines whether a general tree is leaf perfect. A general tree is said to be leaf perfect if all leaves have the same depth."
let () = print_endline ""


(****************************************************************************************************************************************)
(**************************************************************CODE**********************************************************************)
(*4*)
let rec is_leaf_perfect t =
  let rec helper x y =
    match x with
    | [] -> true
    | h::t -> ((List.length h) = y) && helper t y
  in 
  helper(paths_to_leaves t) ((height t) - 1)
(**************************************************************CODE**********************************************************************)
(****************************************************************************************************************************************)


let () = print_endline " "
  let () = print_endline  "Testing the IS_PERFECT_LEAF fucntion on both the trees"
  let () = print_endline ""

let test_is_leaf_perfect = print_endline"Test for Tree t";; is_leaf_perfect t;;
let test_is_leaf_perfect2 = print_endline"Test for Tree t2";;is_leaf_perfect t2;;
let () = print_endline "END"


let () = print_endline "    "
let () = print_endline "BEGIN"
let () = print_endline  "5) preorder: that returns the pre-order traversal of a general tree."
let () = print_endline ""

(****************************************************************************************************************************************)
(**************************************************************CODE**********************************************************************)
(*5*)
let rec preorder t =
  match t with
  | Node(n, []) -> [n]
  | Node(n, c) -> n :: List.concat(List.map preorder c)
(**************************************************************CODE**********************************************************************)
(****************************************************************************************************************************************)

let () = print_endline " "
let () = print_endline  "Testing the PREORDER fucntion on both the trees"
let () = print_endline ""
let test_preorder = print_endline"Test for Tree t";; preorder t;;
let test_preorder = print_endline"Test for Tree t2";;preorder t2;;
let () = print_endline "END"

    


let () = print_endline "    "
let () = print_endline "BEGIN"
let () = print_endline  "6) mirror: that returns the mirror image of a general tree."
let () = print_endline ""

(****************************************************************************************************************************************)
(**************************************************************CODE**********************************************************************)
(*6*)
(*6*)  let rec mirror t =
(*6*)   match t with
(*6*)    | Node(n, []) -> Node(n, [])
(*6*)    | Node(n, c) -> Node(n, List.map mirror (List.rev c))
(**************************************************************CODE**********************************************************************)
(****************************************************************************************************************************************)


let () = print_endline " "
let () = print_endline  "Testing the MIRROR fucntion on both the trees"
let () = print_endline ""
let test_mirror = print_endline"Test for Tree t";; mirror t;;
let test_mirror2 = print_endline"Test for Tree t2";;mirror t2;;
let () = print_endline "END"

let () = print_endline "    "
let () = print_endline "BEGIN"
let () = print_endline  "7) mapt f t: that produces a general tree resulting from t by mapping function f to each data item in d."
let () = print_endline ""
(****************************************************************************************************************************************)
(**************************************************************CODE**********************************************************************)
(*7*)
(*7*)    let rec mapt f (Node(d,t)) = 
(*7*)   Node (f d, List.map (mapt f) t);;
(**************************************************************CODE**********************************************************************)
(****************************************************************************************************************************************)

let () = print_endline " "
let () = print_endline  "Testing the MAPt fucntion on both the trees"
let () = print_endline ""
let test_mapt = print_endline"Test for Tree t";; mapt ( fun i -> i >20) t;;
let test_mapt2 = print_endline"Test for Tree t2";;mapt ( fun i -> i >20) t2;;
let () = print_endline "END"
         
let () = print_endline "    "
let () = print_endline "BEGIN"
let () = print_endline  "8) foldt f t: that encodes the recursion scheme over general trees. Its type is
foldt: ('a -> 'b list -> 'b) -> 'a gt -> 'b
For example, here is how one may define sumt and memt using foldt:"
let () = print_endline ""



(****************************************************************************************************************************************)
(**************************************************************CODE**********************************************************************)
(*8*)
(*8*) let rec foldt f t = match t with
(*8*) (Node(x, y)) -> (f x (map (foldt f) y)) ;;
  
let () = print_endline "    "
let () = print_endline "SUMt"

(*8*)  let sumt t = foldt ( fun i rs -> i + List . fold_left ( fun i j -> i+j) 0 rs) t
  
 let () = print_endline "MAPt"

 (*8*)  let memt t e = foldt ( fun i rs -> i=e || List . exists ( fun i -> i) rs) t
(**************************************************************CODE**********************************************************************)
(****************************************************************************************************************************************)
let () = print_endline " "
let () = print_endline  "Testing the MAPt fucntion on both the trees"
let () = print_endline ""
let test_sumt = print_endline"Test for Tree t";; sumt t;;
let test_sumt2 = print_endline"Test for Tree t2";;sumt t2;;
let () = print_endline ""
let () = print_endline  "Testing the MAPt fucntion on both the trees"
let () = print_endline ""
let test_mapt = print_endline"Test for Tree t 12";;memt t 12;;
let test_mapt = print_endline"Test for Tree t 33";;memt t 33;;
let test_mapt = print_endline"Test for Tree t 35";;memt t 35;;
let test_mapt2 = print_endline"Test for Tree t2 12";;memt t2 12;;
let test_mapt2 = print_endline"Test for Tree t2 33";;memt t2 33;;
let test_mapt2 = print_endline"Test for Tree t2 35";;memt t2 35;;
let () = print_endline "END"
let () = print_endline "    "
let () = print_endline "BEGIN"
let () = print_endline  "9) Implement mirror' using foldt. It should behave just like Exercise 6."
let () = print_endline ""

(****************************************************************************************************************************************)
(**************************************************************CODE**********************************************************************)
let mirror' t =
  foldt (fun i  n -> Node(i, List.rev n)) t 
(**************************************************************CODE**********************************************************************)
(****************************************************************************************************************************************)

let () = print_endline " "
let () = print_endline  "Testing the Mirror' fucntion on both the trees"
let () = print_endline ""
let test_mirror' = print_endline"Test for Tree t";; mirror' t;;
let test_mirror'2 = print_endline"Test for Tree t2";;mirror' t2;;


