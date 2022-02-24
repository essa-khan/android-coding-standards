import java.util.Random;

public interface Customer{
	
	int customerID = (new Random()).nextInt(900)+100;
	String username;
	String password;

	private int get_customerID(){

		return customerID;
	}
}