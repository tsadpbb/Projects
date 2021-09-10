public class Quadratic {
	private float aValue, bValue, cValue;

	public Quadratic(float a, float b, float c) {
		aValue = a;
		bValue = b;
		cValue = c;
	} // end of constructor method

	public float getA() {
		return aValue;
	}

	public float getB() {
		return bValue; 		// A bunch of getter methods
	}

	public float getC() {
		return cValue;
	}

	public Quadratic add(Quadratic other) {
		float newA = aValue + other.getA(); 			//Simply adds everything together and returns a new quad based off that
		float newB = bValue + other.getB();
		float newC = cValue + other.getC();
		Quadratic newQ = new Quadratic(newA, newB, newC);
		return newQ;
	} // end of add

	public Quadratic subtract(Quadratic other) {
		float newA = aValue - other.getA();
		float newB = bValue - other.getB();
		float newC = cValue - other.getC(); 		//The order matters this time, but it's basically the same thing as add
		Quadratic newQ = new Quadratic(newA, newB, newC);
		return newQ;
	} // end of subtract

	public Roots findRoots() {
		Roots theRoots = new Roots(aValue, bValue, cValue);
		return theRoots;
	} //end of findRoots

	public String toString() {
		return aValue + "x^2 + " + bValue + " + " + cValue; //I started doing a weird thing with positive but nothing fancy is required
	} //end of toString 									//So why try so hard when this would suffice?

	public boolean equals(Quadratic other) {
		float diffA = Math.abs(other.getA() - aValue);
		float diffB = Math.abs(other.getB() -bValue);
		float diffC = Math.abs(other.getC() - cValue); 					//Floats are janky so I added a
		if (diffA < 0.0001 && diffB < 0.0001 && diffC < 0.0001)		//Tolerance for the equals
			return true;
		else
			return false;
	} //end of equals

	public static void main(String[] args) {
		Quadratic quadOne = new Quadratic(-1, 2, -3);
		Quadratic quadTwo = new Quadratic(5, 10, 4); 		// The following constructs three quadratic objects then uses
		Quadratic quadThree = new Quadratic(4, 12, 1);		// toString, add, subtract, and equals
		Quadratic quadFour = new Quadratic(1, 4, 4);
		System.out.println(quadOne.toString() + " added to " + quadTwo.toString() + " is " + quadOne.add(quadTwo).toString());
		System.out.println("It is " + quadThree.equals(quadOne.add(quadTwo)) + " that " + quadOne.toString() + " plus " + quadTwo.toString() + " is equal to " + quadThree.toString());
		System.out.println("It is " + quadTwo.equals(quadThree.subtract(quadOne)) + " that " + quadThree.toString() + " minus " + quadOne.toString() + " is equal to " + quadTwo.toString());
		System.out.println("The roots of " + quadFour.toString() + " are " + quadFour.findRoots().toString());
		System.out.println("The roots of " + quadOne.toString() + " are " + quadOne.findRoots().toString());

	} //end of main
}

class Roots {
	private Complex first;
	private Complex second;

	public Roots(float baseA, float baseB, float baseC) {
		float discriminant = (float)(Math.pow(baseB, 2) - 4 * baseA * baseC);
		if (discriminant > 0) {			//This here thing check if the discriminant is positive, because if it isn't, then the roots are imaginary
			first = new Complex(((-1*baseB + (float)Math.pow(discriminant, 0.5))/(2*baseA)), 0); //This is just the quadratic formula
			second = new Complex(((-1*baseB - (float)Math.pow(discriminant, 0.5))/(2*baseA)), 0);
		}
		else {		//This next part will make the imaginary imaginary and the real the real in the complex class, dicriminant needs to be positive for math.pow to work
			first = new Complex(((-1*baseB)/(2*baseA)),((float)Math.pow(Math.abs(discriminant), 0.5)/(-1*2*baseA)));
			second = new Complex(((-1*baseB)/(2*baseA)),((float)Math.pow(Math.abs(discriminant), 0.5)/(2*baseA)));
		}
	}

	public String toString() {
		String string;
		string = "Root One is " + first.toString() + " and Root Two is " + second.toString();
		return string;
	}
}

//This complex class idea was taken from the lecture's Complex4 class, it does what it needs to
class Complex {
	private float real;
	private float imag;

	public Complex (float r, float i) {
		real = r;
		imag = i;
	}

	public float getReal() {
		return real;
	}

	public float getImag() {		//Getters and a toString
		return imag;
	}

	public String toString() {
		return real + " + " + imag + "i";
	}
}