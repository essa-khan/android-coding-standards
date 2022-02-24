import java.util.Random;
import Customer.java;
/*
	TODO:
		_ Confusing overloading
		_ Don't use overloaded methods to differentiate runtime types
		_ Ensure clone() calls super.clone()
		_ 
*/
public class User extends Customer{
	
	String firstName, lastName;
	String fullName;
	String username;
	String password;

	int[] coords = new int[3];

	int accountNum;
	int accountAmt;
	int userID;

	public User(String fname, String lname, String uName, int init_deposit){
		this.firstName = fname;
		this.lastName = lname;
		this.username = uName;
		this.accountNum = (new Random()).nextInt(9000) + 1000;
		this.accountAmt = init_deposit;
		this.coords = get_coords(this.coords);
		this.userID = get_customerID(); // violates accessibility (increases when shouldn't)
	}	// also violates not calling overridable methods in the constructor

	// Android Rule 12 (Expressions): EXP00-J
	// Do not ignore return value
	int deposit(int amount){
		if(amount > 0){
			this.accountAmt += amount;
			return this.accountAmt;
		}
		else{
			return amount;
		}
	}
	// Android Rule 12 (Expressions): EXP00-J
	// Do not ignore return value
	int withdrawal(int amount){
		if(amount < this.accountAmt){
			this.accountAmt -= amount;
			return this.accountAmt;
		}
		else{
			return amount;
		}
	}

	// Method you DON'T want to increase visibility of
	// can show Android Rule 12 (Expressions): EXP02-J
	// and Android Rule 22 (Methods): MET04-J
	private int[] get_coords(int[] coords){

		int[] homeCoords = [1, 2, 3];

		for(int i = 0; i < 3; i++){
			coords[i] = (new Random()).nextInt(10);
		}

		// Incorrectly checking that they are equal
		if(coords.equals(homeCoords)){ // This just checks the the location in memory
			// ALWAYs false since they are different memory locations
			return homeCoords;
		}
		else{
			return coords;
		}

		// Correct check that they are equal
		// if(Arrays.equals(homeCoords, coords)){ // This checks the actual values
		// 	return homeCoords;
		// }
		// else{
		// 	return coords;
		// }
	}
}