//written by and08395 and swedz015

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int[] results = battle(); //result will be either total turns and cannon shots or that they quit
        if (results[0] == -1) System.out.println("Goodbye!");
        else System.out.println("Final turn count: " + results[0] + ". Total number of cannon shots: " + results[1] + ".");
    }//main
    public static int[] battle() {
        Board board1; //main board to be used
        String input; //manages whether the user wants to fire cannon/missile, drone, print, or quit
        String rowCol; //utilized in drone to determine if user scans a row or col
        Scanner scan = new Scanner (System.in);

        System.out.println("Welcome to BattleBoats! Please select your desired mode.");
        System.out.println("Type 1 for standard or 2 for expert.");
        String mode = scan.nextLine();
        mode = mode.toLowerCase();
        while (!mode.equals("1") && !mode.equals("2")){ //checking for incorrect input and then proceeding once correct
            System.out.println("Incorrect input, please try again. Type 1 for standard mode or 2 for expert mode.");
            mode = scan.nextLine();
            mode = mode.toLowerCase();
        }
        if (mode.equals("1")){ //standard mode
            System.out.println("Standard mode selected, the game will now begin!");
            board1 = new Board(1);
        }
        else { //expert mode
            System.out.println("Expert mode selected, the game will now begin!");
            board1 = new Board(2);
        }
        System.out.println("Current number of boats on board at start: " + board1.getBoats());
        System.out.println("To fire your cannon on a single spot, you type in coordinates in the form of \"x y\" w/o quotations. ie. 4 2");
        System.out.println("To fire your missile, type \"missile\" w/o quotations. Afterwards, use the same format stated above for the coordinates, ie. 4 2");
        System.out.println("To drone, type in \"drone\" w/o quotations.");
        System.out.println("To print, type in \"print\" w/o quotations.");
        System.out.println("To quit the program, type in \"quit\" w/o quotations.");
        System.out.println(board1.display());
        while (board1.getBoats() > 0){ //once there are 0 boats left, the game is over
            System.out.println("Fire your cannon, missile, drone, print, or quit using the above commands.");
            input = scan.nextLine();
            input = input.toLowerCase();
            switch (input){ //used to discern what the user wants
                case "missile":
                    if (board1.isMissile()){ //checks to see if user has enough missiles
                        System.out.print("Please enter a coordinate:");
                        String coordInput = scan.nextLine(); //initial input
                        String[] coordsArr = coordInput.split(" "); //splits input into 2 strings in a string[]
                        int[] coords = new int[2]; //coords to be used in fireMissile
                        coords[0] = Integer.parseInt(coordsArr[0]);
                        coords[1] = Integer.parseInt(coordsArr[1]); //the below while loop is used to make sure the user enters coords within bounds
                        while ((coords[0] > board1.getCoordSize())||(coords[0] < 0)||(coords[1] > board1.getCoordSize())||(coords[1] < 0)){
                            System.out.println("Invalid input, please enter a coordinate:");
                            coordInput = scan.nextLine();
                            coordsArr = coordInput.split(" ");
                            coords[0] = Integer.parseInt(coordsArr[0]);
                            coords[1] = Integer.parseInt(coordsArr[1]);
                        }
                        board1.fireMissile(coords[0], coords[1]); //sends to fireMissile
                        System.out.println(board1.display()); //board state update output
                    }
                    else{
                        System.out.println("Out of Missiles!");
                    }
                    break;
                case "drone":
                    int result;
                    if (board1.isDrone()){ //checks to see if user has drones available
                        System.out.println("Please type \"row\" or \"col\" w/o quotations to scan the desired segment.");
                        rowCol = scan.nextLine();
                        rowCol = rowCol.toLowerCase();
                        while (!rowCol.equals("row") && !rowCol.equals("col")){ //checking for incorrect input and then proceeding once correct
                            System.out.println("Incorrect input, please try again.");
                            rowCol = scan.nextLine();
                            rowCol = rowCol.toLowerCase();
                        }
                        if (rowCol.equals("row")){
                            System.out.println("Please enter a row from 0 to " + board1.getCoordSize() + " to scan.");
                            int row = Integer.parseInt(scan.nextLine());
                            while (row > board1.getCoordSize() || row < 0){ //checks for invalid row input
                                System.out.println("Invalid row number, please try again.");
                                row = Integer.parseInt(scan.nextLine());
                            }
                            result = board1.droneSearch(true, row); //sends to droneSearch method w/ true so a row is scanned
                        }
                        else {
                            System.out.println("Please enter a column from 0 to " + board1.getCoordSize() + " to scan.");
                            int col = Integer.parseInt(scan.nextLine());
                            while (col > board1.getCoordSize() || col < 0){ //checks for invalid col input
                                System.out.println("Invalid column number, please try again.");
                                col = Integer.parseInt(scan.nextLine());
                            }
                            result = board1.droneSearch(false, col); //sends to drone method w/ false so a col is scanned
                        }
                        System.out.println("Drone scanned " + result + " targets in the specified area.");
                    }
                    else{
                        System.out.println("Out of Drones!");
                    }
                    break;
                case "print":
                    System.out.println(board1); //uses toString method to reveal entire board
                    break;
                case "quit":
                    return new int[] {-1, 0}; //returns quit conditions for main to identify, 0 is a placeholder
                default: //gotta discern invalid inputs and valid coordinates
                    String[] newCoord;
                    int[] newHit = new int[2]; //used for parameters in fireCannon method
                    try { //checks for valid input
                        newCoord = input.split(" ");
                        newHit[0] = Integer.parseInt(newCoord[0]);
                        newHit[1] = Integer.parseInt(newCoord[1]);
                    } catch (Exception e) {
                        System.out.println("Invalid Input");
                        break;
                    }
                    board1.fireCannon(newHit[0], newHit[1], true); //sends to fireCannon method to fire a single cannon
                    System.out.println(board1.display()); //board state update output
                    break;
            }
        }
        scan.close();
        return new int[]{board1.getTurn(), board1.getShots()}; //returns results from playtime including turns and cannons fired
    }//battle
}//Main
