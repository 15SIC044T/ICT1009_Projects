package ObjectHandler;

public class Scheduler {

	//To be removed! I ended up using another approach, like database, just add, faster
	//Ship ship;
	//ArrayList<Customer> customerList = new ArrayList<Customer>();
	//END OF TO BE REMOVED
	
	
	int ship; 	//Store the index of the ship ID
	int customer;	//Store the index of the customer ID
	int loadCapacity;
	String containerType;
	
	public Scheduler() {};
	public Scheduler(int s, int c, int lc, String ct)
	{
		ship = s;
		customer = c;
		loadCapacity = lc;
		containerType = ct;
	}
	
	public void setShip(int s) { ship = s;}
	public int getShip() { return ship; }

	public void setCustomer(int c) { customer = c;}
	public int getCustomer() { return customer; }

	public void setLoadCapacity(int lc) { loadCapacity = lc;}
	public int getLoadCapacity() { return loadCapacity; }
	
	public void setContainerType(String ct) { containerType = ct;}
	public String getContainerType() { return containerType; }

	public void ScheduleMinShips() {};
	
	public void ScheduleMinPrice() 
	{
		
		
		
		
		
		
	}
	
}
