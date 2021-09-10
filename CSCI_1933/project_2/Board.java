//written by and08395 and swedz015

public class Board {
    private int[][] board; //array used to keep track of specific spots and board state
    private int turnCount = 0; //keeps track of how many turns it takes the user to destroy all boats
    private int numBoats; //keeps track of the number of boats currently on the board
    private int missileCount; //starts at 1 or 2 depending on mode, limits missile usage
    private int droneCount; //starts at 1 or 2 depending on mode, limits drone usage
    private int cannonShots; //keeps track of total shots fired (excluding missile)
    private String[] droneSearches; //This array will keep track of rows and column's searched

    public Board(int num) { //num can be 1 or 2, changes values of instance variables depending on num
        int index = 4 + num*4;
        numBoats = 5*num;
        missileCount = num;
        droneCount = num;
        droneSearches = new String[droneCount];
        board = new int[index][index];  //instantiate board w/ correct mode selection
        generateBoats();  //here we randomly generate boats on the board
    }//Board constructor

    //getters
    public int getCoordSize(){
        return board.length-1;
    } //returns 7 or 11 depending on mode, used to show 0-7 or 0-11 coords

    public int getTurn() {
        return turnCount;
    }

    public int getBoats (){ //getter method to tell user how many boats are currently left on the board
        return numBoats;
    }

    public int getShots(){
        return cannonShots;
    }

    public boolean isMissile() {
        return missileCount > 0;
    } //checks if user has enough missiles

    public boolean isDrone() {
        return droneCount > 0;
    } //checks if user has enough drones

    public void generateBoats(){ //method to help randomly place boats on board
        double randDoubX, randDoubY; //used in generating a random starting location for the boat
        int xCord = 0; //int version of randDoubX
        int yCord = 0; //int version of randDoubY
        int randDir; //randDir helps decide direction of boat (horiz. or vert.)
        int flag; //helps determine whether a boat is good to fit on the board
        double boundX, boundY; //bounds that helps limit a boats placement so it doesn't generate outside of the board
        int boatNum = 2; //specific number of the boat, determined by boat length, starts at 2 since its the smallest possible
        for (int i = 0; i < numBoats; i++){ //loop for generating 5 or 10 boats depending on mode
            flag = 0; //used to check empty spaces ahead of boat
            randDir = (int)(Math.floor(Math.random()*10)%2); //determines if the boat will be horiz. or vert. on the board. 50/50 chance
            if (randDir == 1){ //vert. case
                boundX = board[0].length; //since vert., boat can be placed on any X within bounds
                boundY = board.length-boatNum+1; //limited by size of boat on Y axis
                while (flag < boatNum){ //if flag is >= boatNum, then the boat can be placed correctly w/o worrying about overlapping of boats
                    flag = 0;
                    randDoubX = Math.random()*(boundX/10); //correctly scales to fit within indexes along X
                    xCord = (int)Math.floor(randDoubX*10); //converts to int
                    randDoubY = Math.random()*(boundY/10); //correctly scales to fit within indexes along Y
                    yCord = (int)Math.floor(randDoubY*10); //converts to int
                    for (int r = 0; r < boatNum; r++){ //check vertical availability for spaces
                        if (board[yCord+r][xCord] == 0){ //checks to see if spaces in front of boat are empty
                            flag++;
                        }
                    }
                }
                for (int r = 0; r < boatNum; r++){ //loop designed to place specific boat on board
                    board[yCord+r][xCord] = boatNum;
                }
            }
            else { //horiz. case
                boundX = board[0].length-boatNum+1; //limited by size of boat on X axis
                boundY = board.length; //since horiz., boat can be placed on any Y within bounds
                while (flag < boatNum){ //if flag is >= boatNum, then the boat can be placed correctly w/o worrying about overlapping of boats
                    flag = 0;
                    randDoubX = Math.random() * (boundX / 10); //correctly scales to fit within indexes along X
                    xCord = (int) Math.floor(randDoubX * 10); //converts to int
                    randDoubY = Math.random() * (boundY / 10); //correctly scales to fit within indexes along Y
                    yCord = (int) Math.floor(randDoubY * 10); //converts to int
                    for (int r = 0; r < boatNum; r++) { //check horizontal availability for spaces
                        if (board[yCord][xCord + r] == 0) { //checks to see if spaces in front of boat are empty
                            flag++;
                        }
                    }
                }
                for (int r = 0; r < boatNum; r++) { //loop designed to place specific boat on board
                    board[yCord][xCord + r] = boatNum;
                }
            }
            if ((numBoats == 5)&&((i==0)||(i==2)||(i==3)))//changing boat lengths to ensure placement of all required boats
                boatNum++;
            if ((numBoats == 10)&&((i==1)||(i==5)||(i==7)))//same thing as above if statement, but specifically for expert mode
                boatNum++;
        }
    }//generateBoats

    //turn makes it so that penalty, miss and hit aren't printed when turn is false aka when missile is used
    public void fireCannon(int row, int col, boolean turn) {
        if (turn){ //used specifically when the user is firing a cannon, not for missile
            turnCount++;
            cannonShots++;
        }
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) { //checks for out of bounds, ignores if missile shot
            if (turn) {
                System.out.println("Penalty");
                turnCount++;
            }
        }
        else { //valid coord case
            switch(board[row][col]) {
                case 0: //empty spot changes to a 1 to signify it was a hit and a miss
                    if (turn) System.out.println("Miss");
                    board[row][col] = 1;
                    break;
                case 1: //both case 1/6 result in a penalty since user has already hit there, ignores if missile shot
                case 6:
                    if (turn) {
                        System.out.println("Penalty");
                        turnCount++;
                    }
                    break;
                default: //case where user hits one of the boats
                    int remainder = getNumNum(board[row][col]) % board[row][col]; //calculates total num of spots of that type of boat and checks if its one of the last in a set
                    if (remainder != 1 && turn) System.out.println("Hit"); //if its not one of the last of that ship, it just hits
                    else if (remainder == 1) { //if the remainder of boats is 1, then that particular ship is sunk
                        System.out.println("Sunk");
                        numBoats--;
                    }
                    board[row][col] = 6; //board updates that spot to signify a boat has been hit there
                    break;
            }
        }
    }//fireCannon

    public int getNumNum(int num) { //helper method that calculates how many of one type of ship is on the board. num is specific type of boat
        int ret = 0; //total number of spots that type of ship occupies
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == num) ret++;
            }
        }
        return ret;
    }//getNumNum

    //If val is true, it searches row and if its false it searches column. Search is the row/col that is searched
    public int droneSearch(boolean val, int search) {
        String rowcol;
        if (val) rowcol = "Row ";
        else rowcol = "Col ";
        droneSearches[droneSearches.length-droneCount] = rowcol + search + " Searched";
        int num = 0; //total num of spots that are ships
        turnCount++;
        droneCount--;
        if (val) { //scans row
            for (int i = 0; i < board[search].length; i++) {
                if (board[search][i] > 1) num++;
            }
        }
        else { //scans col
            for (int i = 0; i < board.length; i++) {
                if (board[i][search] > 1) num++;
            }
        }
        return num;
    }//droneSearch

    public void fireMissile(int row, int col) { //method that performs a 3x3 fireCannon pattern in one turn
        turnCount++;
        missileCount--;
        fireCannon(row-1, col-1, false); //these are all sent with false to make sure it doesnt print hit/miss on each shot
        fireCannon(row-1, col, false);        //additionally, if spots are 1 or 6 they do not incur a penalty
        fireCannon(row-1, col+1, false);
        fireCannon(row+1, col-1, false);
        fireCannon(row+1, col, false);
        fireCannon(row+1, col+1, false);
        fireCannon(row, col-1, false);
        fireCannon(row, col, false);
        fireCannon(row, col+1, false);
    }//fireMissile

    public String display(){ //used for displaying current board state to player each turn
        String output = "";
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[0].length; j++){
                switch (board[i][j]){
                    case 1: //hit and miss case
                        output += "O ";
                        break;
                    case 6: //hit boat case
                        output += "X ";
                        break;
                    default: //boats or empty spot case
                        output += "- ";
                        break;
                }
            }
            output += "\n";
        }
        for (int i = 0; i < droneSearches.length-droneCount; i++) {
            output += droneSearches[i] + "\n";
        }
        return output;
    }//display

    //Key is empty(0), hitspace(1), boatlen2(2), boatlen3(3), boatlen4(4), boatlen5(5), hitboat(6)
    public String toString(){ //used to fully reveal board
        String output = "";
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[0].length; j++){
                switch (board[i][j]){
                    case 0: //empty spot case
                        output+= "- ";
                        break;
                    case 1: //hit and miss case
                        output += "O ";
                        break;
                    case 6: //hit boat case
                        output += "X ";
                        break;
                    default: //prints boat type (ie. 2, 3, 4 ,5) onto board to show boat's location
                        output += board[i][j] + " ";
                        break;
                }
            }
            output += "\n";
        }
        return output;
    }//toString
}//Board
