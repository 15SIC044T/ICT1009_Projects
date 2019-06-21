package ObjectHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import Containers.BasicContainer;

public abstract class BasicObject 
{
	private HashMap<String, BasicContainer> containerList = new HashMap<String, BasicContainer>();
	
	private int id;
	private int balance;
	private String name;
	private String company;
	
	private String filePath = "";
	
	public BasicObject() {};
	public BasicObject(String name, String company, int id)
	{
		this.id = id;
		this.name = name;
		this.company = company;
	}
	
	public void setName(String n) { name = n; }
	public String getName() { return name; }

	public void setCompany(String c) { company = c; }
	public String getCompany() { return company; }
	
	public void setID(int id) { this.id = id; }
	public int getID() { return id; }
	
	public void setBalance(int b) { balance = b; }
	public void addBalance(int b) { balance += b; }
	public int getBalance() { return balance; }
	
	public void setFilePath(String fp) { filePath = fp; }
	public String getFilePath() { return filePath; }
	
	public HashMap<String, BasicContainer> getContainerList() { return containerList; }
	public void addContainer(String t, BasicContainer cl) { getContainerList().put(t, cl); }
	public void deleteContainer(String key) { getContainerList().remove(key); }
	
	public int getContainerCurrentCapacity(String type) 
	{
		return getContainerList().get(type).getCurrentCapacity();
	}
	
	public int getContainerMaxCapacity(String type) 
	{
		return getContainerList().get(type).getMaxCapacity();
	}
	
	public String getContainerProperty(String type) 
	{
		return getContainerList().get(type).getSpecialProperty();
	}
	
	public int getContainerPrice(String type) 
	{
		return getContainerList().get(type).getPricePerContainer();
	}
	
	public ArrayList<String> findMissingContainerTypes(String[] maxTypes) 
	{
		ArrayList<String> newTypes = new ArrayList<String>();
		for (String type: maxTypes) {
			Set<String> keys = getContainerList().keySet();
			if (!keys.contains(type)) 
				newTypes.add(type);
		}
		return newTypes;
	}
	
	// Find out what container types this ship is currently holding.
	public String[] findCurrentContainerTypes() 
	{
		return SupportFunction.filterTypeList(getContainerList());
	}
	
	public void listContainers() 
	{
		int numOfTypes = findCurrentContainerTypes().length;
		if (numOfTypes == 0) 
			System.out.println("This customer does not have any container requirements.");
		else {
			String format = "%-15s %-20s %-20s\n";
			System.out.format(format, "Type", "Current Capacity", "Maximum Capacity");
			format = "%-15s %-20d %-20d\n";
			for (String key : getContainerList().keySet()) {
				System.out.format(format, key, getContainerCurrentCapacity(key), getContainerMaxCapacity(key));
			}
			System.out.print("\n");
		}
	}
	
	public void printInfo() {};
}
