package Containers;

public class BasicContainer 
{
	//Number can be either Number/Max Load
	private int maxCapacity;
	private int currentCapacity;
	
	//Special Property for ContainerRequirement(Customer)
	private String specialProperty = null;
	
	//Price Per Container for ShipInformation(Owner)
	private int pricePerContainer;
	
	public BasicContainer() {};
	public BasicContainer(int n)	//This constructor can use for either Customer/Owner, number/maxload
	{
		maxCapacity = n;
		currentCapacity = maxCapacity;
	}
	
	public BasicContainer (int n, String sp)	//This constructor is for Customer (ContainerRequirement)
	{
		maxCapacity = n;
		specialProperty = sp;
		currentCapacity = maxCapacity;
	}
	
	public BasicContainer (int ml, int ppc)	//This constructor is for Owner (ShipInformation)
	{
		maxCapacity = ml;
		pricePerContainer = ppc;
		currentCapacity = maxCapacity;
	}
	
	
	public void setMaxCapacity(int mc) { maxCapacity = mc; }
	public int getMaxCapacity() { return maxCapacity; }
	
	public void setCurrentCapacity(int cc) { currentCapacity = cc; }
	public int getCurrentCapacity() { return currentCapacity; }

	public void setSpecialProperty(String n) { specialProperty = n; }
	public String getSpecialProperty() { return specialProperty; }
	
	public void setPricePerContainer(int ppc) { pricePerContainer = ppc; }
	public int getPricePerContainer() { return pricePerContainer; }
	
	public int getRemainingCapacity() { return (maxCapacity - currentCapacity); }
	public int calculateRemainingValue(int value) { return currentCapacity - value; }
	
	public int calculatePrice(int value) {return (pricePerContainer * value); }
	
	public void loadContainer()
	{
		
	}
}
