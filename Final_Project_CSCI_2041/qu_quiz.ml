(* File from Class, Completed by Dylan Anderson *)
module F = Read_file

let containsQu (str: string) : bool = 
  (String.contains str 'q') &&
  (String.contains str 'u')

let notQu (str: string) : bool = not (containsQu str)

let explode (str: string) : char list = 
  let retVal = ref [] in
  String.iter (fun s -> if s <> 'q' && s <> 'u' then retVal := s::(!retVal)) str;
  List.sort (Char.compare) !retVal

let checkStrings (qstr: string) (str: string) : bool =
  if ((String.length qstr) - (String.length str)) = 2 
  then (explode qstr) = (explode str)
  else false

let qu_quiz (input: string) : (string * string) list = 
  let retVal = ref [] in
  let rec findPair (lst: string list) (str: string) : unit = 
    match lst with
      | [] -> ()
      | x::xs -> if checkStrings str x
                then retVal := (x, str)::(!retVal)
                else findPair xs str
  in
  let wordList = F.read_file input in
  let noQuList = List.filter notQu wordList in
  let quList = List.filter containsQu wordList in
  List.iter (findPair noQuList) quList;
  !retVal
