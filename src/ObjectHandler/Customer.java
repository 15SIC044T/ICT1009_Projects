package ObjectHandler;
//Import Package
import java.util.ArrayList;

public class Customer extends BasicObject 
{
	private int age;
	
	public Customer() {};
	public Customer(int id, String n, String c, int a) 
	{
		super(n, c, id);
		age = a;
	}
	
	public Customer(int id, String n, String c, int a, String fp) 
	{
		super(n, c, id);
		age = a;
	}

	public void setAge(int n) { age = n; }
	public int getAge() { return age; }
	
	// Find out what other containers this ship can carry.
	public ArrayList<String> findMissingContainerTypes() {
		String[] containerTypes = {"basic", "basic_special", "heavy", "heavy_special", "refrigerated", "refrigerated_special"};
		ArrayList<String> newTypes = super.findMissingContainerTypes(containerTypes);
		return newTypes;
	}
	
	public void printInfo() {
		System.out.println("The customer's id is " + getID() + ".");
		System.out.println("The customer's name is " + getName() + ".");
		System.out.println("The customer's age is " + getAge() + ".");
		System.out.println("The company's name is " + getCompany() + ".");
    	System.out.println("Below are the customer container requirements.");
    	listContainers();
	}
}
