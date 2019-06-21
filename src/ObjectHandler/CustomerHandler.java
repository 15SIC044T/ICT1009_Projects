package ObjectHandler;
//Import Package
import Containers.*;
import xml.XMLWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerHandler extends BasicHandler
{

	public void addNewCustomer(int customerID, String customerName, String company, int age ) {
		Customer customer = new Customer(customerID, customerName, company, age);
		addObject(customer);
	}
	
	public void displayMenu() {
		for (int i = 0; i < getList().size(); i++) 
			System.out.println(i+1 + ") " + getObjWithIndex(i).getName());
		System.out.println("0) Back");
	}
	
	
	public void addMenu() {
		int newID = createUniqueID();
		String customerName = SupportFunction.handleStringInput("Please enter customer's name: ");
		int customerAge = SupportFunction.handleIntInput("Please enter customer's age: ");
		String companyName = SupportFunction.handleStringInput("Please enter company's name: ");
		
		addNewCustomer(newID, customerName, companyName, customerAge);
		System.out.println("A new customer has been added with the following information: ");
		BasicObject newCustomer = (Customer)getObjWithIndex(getList().size()-1);
		newCustomer.printInfo();
		
		String filePath = getDirectory() + "\\" + newID + ".xml";
		newCustomer.setFilePath(filePath);
		XMLWriter.addNewCustomerXML(newCustomer);
	}
	
	public void deleteMenu() {
		int numOfCustomers = getList().size();
		if (numOfCustomers > 0) {
			System.out.println("Please choose which ship to delete.");
			displayMenu();
			
			int deleteOpt = SupportFunction.checkInputNum(numOfCustomers);
			if (deleteOpt == 0) 
				return;
				
			Customer deletedCustomer = (Customer)getObjWithIndex(deleteOpt-1);
			System.out.println("The following customer has been deleted.");
			deletedCustomer.printInfo();
			
			XMLWriter.deleteXMLFile(deletedCustomer.getFilePath());
			
			deleteObjWithIndex(deleteOpt-1);
		}
		else
			System.out.println("There are no ships left!");
	}
	
	public void updateMenu() {
		while (true) {
			int numOfCustomers = getList().size();
			if (numOfCustomers > 0) {
				System.out.println("Please choose which ship to edit.");
				displayMenu();
				int shipChoice = SupportFunction.checkInputNum(numOfCustomers);
				if (shipChoice == 0) break;
			
				// Once ship is selected
				while (true) {
					Customer selected = (Customer)getList().get(shipChoice-1);
					HashMap<String, BasicContainer>	containerList =	selected.getContainerList();
	            	selected.printInfo();
					
					System.out.println("Select an option.");
					System.out.println("1) Add a new container type.");
					System.out.println("2) Delete a container type.");
					System.out.println("3) Edit container information.");
					System.out.println("4) Modify name.");
					System.out.println("5) Modify age.");
					System.out.println("6) Modify company's name.");
					System.out.println("7) Save customer information to XML.");
					System.out.println("0) Back");
					int opt = SupportFunction.checkInputNum(7);
					if (opt == 0) break;
					else {
						switch (opt) {
							case 1:
								// Once filter out which types are already available,
								ArrayList<String> unusedTypes = selected.findMissingContainerTypes();
								int length = unusedTypes.size();
								if (length == 0) {
									System.out.println("This ship can carry all possible types!");
									break;
								}
								else {
									String[] unusedTypesArr = unusedTypes.toArray(new String[unusedTypes.size()]);
									SupportFunction.displayArrayAsMenu(unusedTypesArr, "These are the types of containers you can add:");
									
									int choice = SupportFunction.checkInputNum(unusedTypesArr.length);
									if (choice == 0) 
										break;
									
									String chosenType = unusedTypesArr[choice-1];
									
									int input = SupportFunction.handleIntInput("Enter max load for the type " + chosenType);
									selected.addContainer(chosenType, SupportFunction.createCustomerContainerObject(chosenType, input));
								}					
								break;
							case 2:
								int numOfKeys = containerList.keySet().size();
								if (numOfKeys == 0) {
									System.out.println("There are no container types left!");
									break;
								}
								else {
									String[] types = selected.findCurrentContainerTypes();
									SupportFunction.displayArrayAsMenu(types, "Please select a container type to delete:");
									
									int choice = SupportFunction.checkInputNum(types.length);
									if (choice == 0) 
										break;
									else 
										selected.deleteContainer(types[choice-1]);
								}
								break;
							case 3:
								int keySize = containerList.keySet().size();
								if (keySize == 0) {
									System.out.println("There are no container types left!");
									break;
								}
								else {
									String[] types = SupportFunction.filterTypeList(containerList);
									SupportFunction.displayArrayAsMenu(types, "Please select a container type to update:");
									
									int choice = SupportFunction.checkInputNum(types.length);
									if (choice == 0) 
										break;
									
									BasicContainer currType = containerList.get(types[choice-1]);
									
									int input = SupportFunction.handleIntInput("Enter new max capacity for " + types[choice-1] + " type. (Current max capacity is: " + currType.getMaxCapacity() + ")");
									currType.setMaxCapacity(input);
								}
								break;
							case 4:
								String newName = SupportFunction.handleStringInput("Enter new name. (Current name is " + selected.getName() + ")");
								selected.setName(newName);
								break;
							case 5:
								int newVal = SupportFunction.handleIntInput("Enter new age. (Current age is " + selected.getAge() + ")");
								selected.setAge(newVal);
								break;
							case 6:
								String newCName = SupportFunction.handleStringInput("Enter new company name. (Current name is " + selected.getCompany() + ")");
								selected.setCompany(newCName);;
								break;
							case 7:
								XMLWriter.editXMLFile(selected);
								break;
							default:
								break;
						}
					}
				}
			}
			else {
				System.out.println("There are no ships left!");
				break;
			}
		}
	}
	
	
	//XINYI METHODS
	//----------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	public void reset()
	{
		String[] containerType = { "refrigerated_special", "refrigerated", "heavy_special", "heavy", "basic_special", "basic" };
		super.reset(containerType);
	}
	
	
	//Print the cost payable for each Customer and its total
	public void printCustomerPayable(ArrayList<BasicObject> getCustomerList) 
	{
		int total = 0;
		
		System.out.println("---------------------------------------------------");
		System.out.println("Total Payable for Customer: ");
		System.out.println("---------------------------------------------------");
		System.out.printf("%-30s %-10s\n", "Customer Name", "Amount ($)");
		
		for (int i = 0; i < getCustomerList.size(); i++) 
		{
			total += getCustomerList.get(i).getBalance();
			System.out.printf("%-30s %-10s\n", getCustomerList.get(i).getName(), getCustomerList.get(i).getBalance());
		}
		
		System.out.println("---------------------------------------------------");
		System.out.printf("%-30s %-10s\n", "The total cost is", total);
		System.out.println("---------------------------------------------------");
	}
	
	
	//Print Customer Containers Type and Capacity
	public void printCustomerInfo(ArrayList<BasicObject> getCustomerList) 
	{
		String[] containerType = { "refrigerated_special", "refrigerated", "heavy_special", "heavy", "basic_special", "basic" };

		System.out.println("---------------------------------------------------");
		System.out.println("Customer Containers: ");
		System.out.println("---------------------------------------------------");

		for (int i = 0; i < getList().size(); i++) 
		{
			ArrayList<Integer> CustMaxCap = new ArrayList<Integer>();
			ArrayList<Integer> CustCurCap = new ArrayList<Integer>();

			for (int j = 0; j < containerType.length; j++) 
			{
				HashMap<String, BasicContainer> s = getCustomerList.get(i).getContainerList();

				if (s.containsKey(containerType[j])) 
				{
					CustMaxCap.add(s.get(containerType[j]).getMaxCapacity());
					CustCurCap.add(s.get(containerType[j]).getCurrentCapacity());
				} 
				else 
				{
					CustMaxCap.add(-1);
					CustCurCap.add(-1);
				}
			}

			SupportFunction sf = new SupportFunction();
			
			System.out.println("Customer: " + getCustomerList.get(i).getName());
			sf.printArrayLine(new String[] { "R(S)", "R", "H(S)", "H", "B(S)", "B" }, " ");
			sf.printArrayLine(CustMaxCap, "Max Cap");
			sf.printArrayLine(CustCurCap, "Cur Cap");
			System.out.println();
		}
	}
	
	//Store all the Customer Container By Type
	public HashMap<String, ArrayList<Integer>> storeCustomerContainerByType(ArrayList<BasicObject> getCustomerList)
	{
		String[] containerType = { "refrigerated_special", "refrigerated", "heavy_special", "heavy", "basic_special", "basic" };

		//Store all customer container according to its type for easy comparison
		HashMap<String, ArrayList<Integer>> custContainerMap = new HashMap<String, ArrayList<Integer>>();
		
		for (int i = 0; i < containerType.length; i++)
		{	
			ArrayList<Integer> containerCustomerList = new ArrayList<Integer>();
			
			for (int j = 0; j < getCustomerList.size(); j++)
			{
				Customer c = (Customer)getList().get(j);
				
				if (c.getContainerList().containsKey(containerType[i]))
				{
					containerCustomerList.add(j);
					custContainerMap.put(containerType[i], containerCustomerList);
				}
			}
		}
		//Print all the results
		//System.out.println("");
		//for (Map.Entry<String, ArrayList<Integer>> entry : custContainerMap.entrySet()) 
		//   System.out.println(entry.getKey() + " : " + entry.getValue());
		return custContainerMap;
	}
	
	
	//Print out all the Customer and its Containers and types (Non-readable for testing purpose) - Xinyi
	public void listAllCustomerContainer() 
	{
		// List out the Customer's Container (HashMap)
		System.out.println("--------------------------------------------------------------");
		System.out.println("List All Customer Container");
		System.out.println("--------------------------------------------------------------");
		
		for (int i = 0; i < getList().size(); i++) 
		{
			Customer c = (Customer)getList().get(i);
			System.out.println(c.getName());

			for (Map.Entry<String, BasicContainer> entry : c.getContainerList().entrySet())
				System.out.println(entry.getKey() + " :  " + entry.getValue());
		}
	}
	
	public HashMap<String, ArrayList<Integer>> storeCustomerContainerByType1(ArrayList<BasicObject> getCustomerList)
	{
		String[] containerType = { "basic_special", "refrigerated_special", "heavy_special", "basic", "refrigerated", "heavy" };

		//Store all customer container according to its type for easy comparison
		HashMap<String, ArrayList<Integer>> custContainerMap = new HashMap<String, ArrayList<Integer>>();
		
		for (int i = 0; i < containerType.length; i++)
		{	
			ArrayList<Integer> containerCustomerList = new ArrayList<Integer>();
			
			for (int j = 0; j < getCustomerList.size(); j++)
			{
				Customer c = (Customer)getList().get(j);
				if (c.getContainerList().containsKey(containerType[i]))
				{
						containerCustomerList.add(j);
						custContainerMap.put(containerType[i],containerCustomerList);
					
				}
			}
		}
		return custContainerMap;
	}

}
