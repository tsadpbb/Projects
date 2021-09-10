//Written by and08395
//This is problem 1 of project 1
import java.util.Scanner;

public class Random {
	private int seed;
	private final int mValue;
	private final int prime1; //I made them final for two reasons 1: I was told to 2: I don't want them changed
	private final int prime2; //Initialize the variables neccesary for everything to work

	public static void main(String[] args) {
		Random test = new Random(7919, 65537, 102611);
		Scanner scan = new Scanner(System.in);
		for (int i=0; i<30; i++) {  	 //I noticed that the randomness isn't that great right off the bat
			test.random();		//so I did this to spice things up a bit
		}
		System.out.println("Welcome to Dylan Anderson's Sequential RNG!!!\n\nOptions:");
		System.out.println("Random Integer: 'Integer'\nRandom Boolean: 'Boolean'");
		System.out.println("Random Double: 'Double'\n\nJust type 'quit' if you don't love me\n");
		boolean playTime = true;
		do {	//I want to make things cool, plus it helps me debug.
			System.out.print("Input: ");
			String question = scan.nextLine();
			switch (question) {
			case "Integer":
				System.out.print("Enter the first integer of your range: ");
				int lower = Integer.parseInt(scan.nextLine());
				System.out.print("Between " + lower + " and ");
				int upper = Integer.parseInt(scan.nextLine());
				System.out.print("How many do you want?: ");
				int num = Integer.parseInt(scan.nextLine()); 	//I wanted to use scan.nextInt() but some weird
				for (int i=0; i<num; i++) { 			//Things happen, it goes to default
					System.out.print(test.randomInteger(lower, upper)+" ");
				}
				System.out.println("\n");
				break;
			case "Double":
				System.out.print("Enter the first number of your range: ");
				double dlower = Double.parseDouble(scan.nextLine());
				System.out.print("Between " + dlower + " and ");
				double dupper = Double.parseDouble(scan.nextLine());
				System.out.print("And how many would you like?: ");
				int dnum = Integer.parseInt(scan.nextLine());
				for (int i=0; i<dnum; i++) {
					System.out.print(test.randomDouble(dlower, dupper)+"\n");
				}
				System.out.print("\n");
				break;
			case "Boolean":
				boolean rand = test.randomBoolean();
				System.out.print("How many would make you happy?: ");
				int bnum = Integer.parseInt(scan.nextLine());
				for (int i=0; i<bnum; i++) {
					System.out.print(test.randomBoolean()+" ");
				}
				System.out.println("\n");
				break;
			case "quit":
				System.out.println("Goodbye!");
				playTime = false;
				break;
			default:
				System.out.println("Invalid Input: Remember that case and spelling matter!\n");
				break;
			}
		}
		while (playTime);
	} //end of main	

	public Random(int p1, int p2, int m) {
		prime1 = p1;
		prime2 = p2;
		mValue = m;
		seed = 0;
	} //end of constructor

	public void setSeed(int newSeed) {
		seed = newSeed;	
	} //end of setSeed

	public int getMaximum() {
		return mValue;	
	} //end of getMaximum

	public int random() {
		int newSeed = ((prime1*seed)+prime2) % mValue; //generate the random number using the equation provided	
		setSeed(newSeed); //Set the new seed so that the next number is random as well
		return newSeed;
	} //end of random

	public int randomInteger(int lower, int upper) {
		int lowNum, upNum, rand, diffNum;
		if (upper>lower) {
			lowNum = lower;
			upNum = upper;
		}			// This chunk makes sure that the low is the low and the high is the high	
		else {
			lowNum = upper;
			upNum = lower;
		} 							//I also noticed some problems with multipying by the reciprocal so I have to convert to double and back to int
		rand = random(); 			// The premise behind this is that it will make a random number rand
		diffNum = upNum - lowNum + 1;		// Then it will take that number and use division to ensure that
		rand = (int)(rand*((double)diffNum/(double)getMaximum())); 	// It has the same range as lower and upper, What it would divide is mValue/diffNum,
		rand += lowNum;				// The problem is if diffNum is greater than mValue, then there is a divide by zero error, so I just multiply by the reciprocal, it works 
		return rand; 				// Then it adds lowNum so that it actually falls in it's range, don't forget about any off by one errors
	} //end of randomInteger

	public boolean randomBoolean() {
		int rand = randomInteger(0,1);
		if (rand == 1)
			return true;
		else
			return false;	
	} //end of randomBoolean

	public double randomDouble(double lower, double upper) {
		double upNum,lowNum,rand,diffNum;
		if (upper>lower) {
			lowNum = lower;
			upNum = upper;
		}			// This chunk makes sure that the low is the low and the high is the high	
		else {
			lowNum = upper;
			upNum = lower;
		}
		rand = random();
		diffNum = upNum - lowNum; 		//We don't have to deal with integer rounding so the off by one problem
		rand = rand*(diffNum/(double)getMaximum());	//suddenly . . . . disappears!!!!
		rand += lowNum; 					//I've also had problems with int overflow so hopefully changing the place of parentheses
		return rand;
	} //end of randomDouble
}
