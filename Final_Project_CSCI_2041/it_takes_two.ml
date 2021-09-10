(* File from class and completed by Dylan Anderson *)
module F = Read_file


let cleanList (lst: (string * string) list) : (string * string) list =
  let check (s1, s2) = s2 <> "No Match" in
  List.filter check lst

let explode (str: string) : char list = 
  let retVal = ref [] in
  String.iter (fun s -> retVal := s::(!retVal)) str;
  !retVal

let checkWord (fstr: string) (sstr: string) : bool =
  List.rev(List.tl(List.rev(List.tl(explode sstr)))) = (explode fstr)

let it_takes_two (input: string) : (string * string) list =
  let retVal = ref [] in
  let rec findWord (lst: string list) (str: string) : unit = 
    match lst with
      | [] -> ()
      | x::xs -> if checkWord str x
                then retVal := (str, x)::(!retVal)
                else findWord xs str
  in
  let wordList = F.read_file input in
  let fourWord = List.filter (fun s -> (String.length s) = 4) wordList in
  let sixWord = List.filter (fun s -> (String.length s) = 6) wordList in
  List.iter (findWord sixWord) fourWord;
  !retVal
  