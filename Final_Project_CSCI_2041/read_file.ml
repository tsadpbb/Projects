(* You may use the definitions in your solutions on the final
   project.  Be sure to properly attribute the author of these
   functions if you use them.
 *)

let read_file (filename:string) : string list =
  let rec read_lines channel sofar =
    try 
      let ln = input_line channel
      in read_lines channel (ln :: sofar)
    with
    | End_of_file -> sofar
    | e -> raise e
  in
  try 
    let channel = open_in filename
    in 
    let lines_in_reverse = read_lines channel []
    in List.rev lines_in_reverse
  with
  | e -> raise e

let rec explode : string -> char list = function
  | "" -> []
  | s  -> String.get s 0 :: explode (String.sub s 1 ((String.length s) - 1))

let rec implode : char list -> string = function
  | []    -> ""
  | c::cs -> String.make 1 c ^ implode cs

let d1 = "words-small.txt"
let d2 = "words-google-10000.txt"
let d3 = "words-corncob.txt"
