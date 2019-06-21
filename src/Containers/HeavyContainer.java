package Containers;

public class HeavyContainer extends BasicContainer{

	public HeavyContainer() {};
	public HeavyContainer(int n)	//This constructor can use for either Customer/Owner, number/maxload
	{
		super(n);
	}
	public HeavyContainer(int n, String sp) //This constructor is for Customer (ContainerRequirement)
	{
		super(n, sp);
	}
	
	public HeavyContainer(int ml, int ppc)	//This constructor is for Owner (ShipInformation)
	{
		super(ml, ppc);
	}

}
