(*

   Quiz 5 - Mutable Data Structures in OCaml
   3 Nov 2022
*)

type 'a node = {
  mutable data:'a;
  mutable next: 'a node option}

type 'a ll = {
  mutable head: 'a node option;
  mutable size: int}

let l1 =
  { head = Some ({data=12; next = Some ({data=33; next=None})});
    size = 2}

let l2 =
  { head = Some ({data=22; next = Some ({data=43; next=None})});
    size = 2}

let add_first: 'a ll -> 'a -> unit =
  fun ll e ->
  ll.head <- Some({data=e; next=ll.head});
  ll.size <- ll.size+1

let string_of_ll : 'a ll -> ('a->string) -> string =
  fun ll f ->
  let rec helper no =
    match no with
    | Some n -> f n.data ^ "," ^ helper n.next
    | None -> ""
  in helper ll.head

  let mem : 'a -> 'a ll -> bool =
    fun e ll ->
    let rec helper no =
      match no with
      | Some n -> if n.data = e then true else helper n.next
      | None -> false
    in helper ll.head;;
    
  let append : 'a ll -> 'a ll -> unit =
    fun ll1 ll2 ->
    let rec helper no =
      match no with
      | Some n -> if n.next = None then n.next <- ll2.head else helper n.next
      | None -> ()
    in helper ll1.head;
    ll1.size <- ll1.size + ll2.size

  let add_at: 'a ll -> 'a -> int -> unit =
   
      fun ll e i ->
        if i < 0 then failwith "Index out of bounds"
        else
          let rec helper no j =
            match no with
            | Some n -> if j = i then n.next <- Some({data=e; next=n.next}) else helper n.next (j+1)
            | None -> failwith "Index out of bounds"
          in helper ll.head 0


  