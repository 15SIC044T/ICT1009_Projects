package ObjectHandler;
//Import Package

import java.util.ArrayList;

public class Ship extends BasicObject
{
	// Variable declarations
	private String ownerName;
	private int valueInBillion;
	
	private boolean special = false;	//This variable check if the ship allows special (Default: False)
	private boolean status = false; 	//This variable keep track whether the ship is fully loaded (Default: False)
	
	public Ship() {};

	public Ship(String sn, String on, String c, int vib, int id, String fp) 
	{
		super(sn, c, id);
		ownerName = on;
		valueInBillion = vib;
	}
	
	public Ship(String sn, String on, String c, int vib, int id, boolean createContainer) 
	{
		super(sn, c, id);
		ownerName = on;
		valueInBillion = vib;
		if (createContainer)
			addContainer("all", SupportFunction.createShipContainerObject("all", 100));
	}
	
	// Ship access/mutate functions
	public void setOwnerName(String n) { ownerName = n; }
	public String getOwnerName() { return ownerName; } 

	public void setValueInBillion(int vib) { valueInBillion = vib; }
	public int getValueInBillion() { return valueInBillion; }
	
	public void setSpecial(boolean s) { special = s; }
	public boolean getSpecial() { return special; }
	
	public void setStatus(boolean st) { status = st; }
	public boolean getStatus() { return status; }

	// Container functions	
	public int getShipMaxCapacity() 
	{ 
		return getContainerMaxCapacity("all");
	}
	
	// Find out what other containers this ship can carry.
	public ArrayList<String> findMissingContainerTypes() 
	{
		String[] containerTypes = {"basic", "special", "heavy", "refrigerated"};
		ArrayList<String> newTypes = super.findMissingContainerTypes(containerTypes);
		return newTypes;
	}
	
	// Modify container price based on the type.
	public void modifyContainerPrice(String type, int newPrice) 
	{ 
		getContainerList().get(type).setPricePerContainer(newPrice);
	}
	
	// Modify the max capacity of the ship which is represented by the "all" container object.
	public void modifyShipCapacity(int newCapacity)
	{ 
		getContainerList().get("all").setMaxCapacity(newCapacity);
	}
	
	public void printInfo() 
	{
		System.out.println("The ship's file path is " + getFilePath() + ".");
		System.out.println("The ship's ID is " + getID() + ".");
		System.out.println("The ship's name is " + getName() + ".");
		System.out.println("The owner's name is " + getOwnerName() + ".");
		System.out.println("The company's name is " + getCompany() + ".");
		System.out.println("The ship's value is " + getValueInBillion() + " billion.");
		System.out.println("The ship has a maximum capacity of " + getShipMaxCapacity() + ".");
    	System.out.println("Below are the types of containers this ship can carry and their max loads.");
    	listContainers();
	}
	
	

	

}
