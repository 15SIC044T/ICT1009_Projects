package Containers;

public class RefrigeratedContainer extends HeavyContainer{

	public RefrigeratedContainer() {};
	public RefrigeratedContainer(int n)		//This constructor can use for either Customer/Owner, number/maxload
	{
		super(n);
	}
	
	public RefrigeratedContainer(int n, String sp) 	//This constructor is for Customer (ContainerRequirement)
	{
		super(n, sp);
	}
	
	public RefrigeratedContainer(int ml, int ppc)	//This constructor is for Owner (ShipInformation)
	{
		super(ml, ppc);
	}

}
