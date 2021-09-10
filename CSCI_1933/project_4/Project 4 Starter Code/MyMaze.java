// Names: Dylan Anderson, Josh Swedzinski
// x500s: and08395, swedz015

import java.util.Random;

public class MyMaze{
    Cell[][] maze;

    public MyMaze(int rows, int cols){ //assumes rows and cols are both positive
        maze = new Cell[rows][cols];
        for (int i = 0; i < rows; i++){ //simple nested for loop to assign each spot in maze to a cell
            for (int j = 0; j < cols; j++){
                maze[i][j] = new Cell();
            }
        }
    }//constructor

    //Creates an int array stack to store coordinates
    //Pushes the first coordinate {0,0}
    //Repeats the following until the stack is empty:
    //Takes the top of the stack and sets as visited
    //Creates an int array to store values cooresponding to cells nearby the top
    //Adds any valid cell coordinates to the array
    //Randomly selects an element from the array and adds it to the stack
    //A switch block does the pushing to stack
    //When the stack is empty, all cells are set to unvisited and the exit is "created"
    public static MyMaze makeMaze(int rows, int cols) { //assumes both rows and cols are positive
        MyMaze newMaze = new MyMaze(rows, cols);
        Stack1Gen<int[]> stack = new Stack1Gen<int[]>(); //stack that will be used to house coordinates of cells
        stack.push(new int[] {0,0}); //starting coordinates

        while (!stack.isEmpty()) {
            int[] top = stack.top();
            newMaze.maze[top[1]][top[0]].setVisited(true);
            int count = 0;
            int[] temp = new int[4];
            if (top[0] > 0 && !newMaze.maze[top[1]][top[0]-1].getVisited()) {
                temp[count] = 0;
                count++;
            }
            if (top[0]+1 < newMaze.maze[0].length && !newMaze.maze[top[1]][top[0]+1].getVisited()) {
                temp[count] = 1;
                count++;
            }
            if (top[1] > 0 && !newMaze.maze[top[1]-1][top[0]].getVisited()) {
                temp[count] = 2;
                count++;
            }
            if (top[1]+1 < newMaze.maze.length && !newMaze.maze[top[1]+1][top[0]].getVisited()) {
                temp[count] = 3;
                count++;
            }
            if (count == 0) stack.pop();
            else {
                int random = (int) (count*Math.random());
                int val = temp[random];
                int[] retVal;
                switch(val) {
                    case 0:
                        retVal = new int[] {top[0]-1, top[1]};
                        newMaze.maze[top[1]][top[0]-1].setRight(false);
                        break;
                    case 1:
                        retVal = new int[] {top[0]+1, top[1]};
                        newMaze.maze[top[1]][top[0]].setRight(false);
                        break;
                    case 2:
                        retVal = new int[] {top[0], top[1]-1};
                        newMaze.maze[top[1]-1][top[0]].setBottom(false);
                        break;
                    default:
                        retVal = new int[] {top[0], top[1]+1};
                        newMaze.maze[top[1]][top[0]].setBottom(false);
                        break;
                }
                stack.push(retVal);
            }
        }
        for (int i = 0; i < rows; i++){ //set all cells visit values to false
            for (int j = 0; j < cols; j++){
                newMaze.maze[i][j].setVisited(false);
            }
        }
        newMaze.maze[newMaze.maze.length-1][newMaze.maze[0].length-1].setRight(false);
        return newMaze;
    }//makeMaze

    public void printMaze(boolean path) {
        String output = ""; //used for the total output of the maze, rather than using a bunch of print statements
        int i, j; //counters
        int trueRow = -1; //true row count, describes the row we are on relative to the asterisk rows
        for (i = 0; i <= maze.length*2; i++){ //doubled maze rows so asterisk/non-asterisk rows can be dealt with separately
            if (i%2!=0){ //this signifies the actual row we are on (when looking at it from the asterisk row perspective)
                trueRow++;
            }
            for (j = 0; j < maze[0].length; j++){ //goes through columns
                if (i%2==0){ //non-asterisk row
                    if (j==0){ //left boundary of maze
                        output += "|";
                    }
                    if ((i==0)||(i==(maze.length*2))){ //top or bottom of maze
                        output += "---|";
                    }
                    else{ //not top or bottom of maze
                        if (maze[trueRow][j].getBottom()){ //bottom border present
                            //System.out.println(i/2);
                            output += "---|";
                        }
                        else{ //open bottom border
                            output += "   |";
                        }
                    }
                }
                else{ //asterisk row
                    if (j==0){ //takes care of starting col index
                        if (i==1){ //entrance
                            output += "  ";
                        }
                        else{ //not entrance
                            output += "| ";
                        }
                    }
                    if ((path)&&(maze[trueRow][j].getVisited())){ //show path (asterisks)
                        output += "* ";
                    }
                    else{ // no asterisk
                        output += "  ";
                    }
                    if (j == (maze[0].length-1)){ //far right border
                        if (i==(maze.length*2-1)){ //exit
                            output += " ";
                        }
                        else{
                            output += "|";
                        }
                    }
                    else if (maze[trueRow][j].getRight()){ //right boundary present
                        output += "| ";
                    }
                    else{ //open right boundary
                        output += "  ";
                    }
                }
            }
            output+="\n";
        }
        System.out.println(output);
    }//printMaze

    //Creates an int array queue using Q1Gen to store coordinates
    //Adds the first coordinate {0,0}
    //Repeats the following until the loop is empty:
    //Takes the top of the queue
    //Sets the cell at these coordinates as visited
    //Checks if the top of the is the exit, if it is, it breaks the while loop
    //Checks the neccesary conditions and adds any valid cell coordinates to the queue
    public void solveMaze() {
        Q1Gen<int[]> queue = new Q1Gen<int[]>();
        queue.add(new int[] {0,0}); //adds beginning coordinates
        while (!queue.isEmpty()) {
            int[] val = queue.remove();
            maze[val[1]][val[0]].setVisited(true);
            if (maze[val[1]][val[0]].equals(maze[maze.length-1][maze[0].length-1])) break;
            if (val[0] > 0 && !maze[val[1]][val[0]-1].getRight() && !maze[val[1]][val[0]-1].getVisited())
                queue.add(new int[] {val[0]-1, val[1]});
            if (val[0]+1 < maze[0].length && !maze[val[1]][val[0]].getRight() && !maze[val[1]][val[0]+1].getVisited())
                queue.add(new int[] {val[0]+1, val[1]});
            if (val[1] > 0 && !maze[val[1]-1][val[0]].getBottom() && !maze[val[1]-1][val[0]].getVisited())
                queue.add(new int[] {val[0], val[1]-1});
            if (val[1]+1 < maze.length && !maze[val[1]][val[0]].getBottom() && !maze[val[1]+1][val[0]].getVisited())
                queue.add(new int[] {val[0], val[1]+1});
        }
        printMaze(true);
    }//solveMaze

    public static void main(String[] args){
        /* Any testing can be put in this main function */
        MyMaze maze = makeMaze(5,20);
        maze.printMaze(false);
        maze.solveMaze();

    }//main
}//myMaze
