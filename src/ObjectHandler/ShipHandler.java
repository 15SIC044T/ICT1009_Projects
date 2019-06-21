package ObjectHandler;

//Import Package
import Containers.*;
import xml.XMLWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ShipHandler extends BasicHandler 
{
	public void addNewObject(String shipName, String ownerName, String company, int valueInBillion, int id) {
		Ship ship = new Ship(shipName, ownerName, company, valueInBillion, id, true);
		addObject(ship);
	}

	public void displayMenu() {
		for (int i = 0; i < getList().size(); i++)
			System.out.println(i + 1 + ") " + getObjWithIndex(i).getName());
		System.out.println("0) Back");
	}

	public void addMenu() {
		int newID = createUniqueID();

		String shipName = SupportFunction.handleStringInput("Please enter ship's name: ");
		String ownerName = SupportFunction.handleStringInput("Please enter owner's name: ");
		String companyName = SupportFunction.handleStringInput("Please enter company's name: ");
		int valueInBillion = SupportFunction.handleIntInput("Please enter the ship's value: ");

		addNewObject(shipName, ownerName, companyName, valueInBillion, newID);
		System.out.println("A new ship has been added with the following information: ");
		
		BasicObject newShip = (Ship)getObjWithIndex(getList().size() - 1);
		newShip.printInfo();

		String filePath = getDirectory() + "\\ship" + newID + ".xml";
		newShip.setFilePath(filePath);
		XMLWriter.addNewShipXML(newShip);
	}

	public void deleteMenu() {
		int numOfShips = getList().size();
		if (numOfShips > 0) {
			System.out.println("Please choose which ship to delete.");
			displayMenu();
			int deleteOpt = SupportFunction.checkInputNum(numOfShips);
			if (deleteOpt == 0)
				return;
			BasicObject deletedShip = getObjWithIndex(deleteOpt - 1);
			System.out.println("The following ship has been deleted.");
			deletedShip.printInfo();

			XMLWriter.deleteXMLFile(deletedShip.getFilePath());

			deleteObjWithIndex(deleteOpt - 1);
		} else
			System.out.println("There are no ships left!");
	}

	public void updateMenu() {
		while (true) {
			int numOfShips = getList().size();
			if (numOfShips > 0) {
				System.out.println("Please choose which ship to edit.");
				displayMenu();
				int shipChoice = SupportFunction.checkInputNum(numOfShips);
				if (shipChoice == 0)
					break;

				// Once ship is selected
				while (true) {
					Ship selectedShip = (Ship)getList().get(shipChoice - 1);
					HashMap<String, BasicContainer> containerList = selectedShip.getContainerList();
					selectedShip.printInfo();

					System.out.println("Select an option.");
					System.out.println("1) Update total capacity.");
					System.out.println("2) Add a new container type.");
					System.out.println("3) Delete a container type.");
					System.out.println("4) Edit container information.");
					System.out.println("5) Modify ship's name.");
					System.out.println("6) Modify owner's name.");
					System.out.println("7) Modify company's name.");
					System.out.println("8) Modify ship's value in billions.");
					System.out.println("9) Save ship information to XML.");
					System.out.println("0) Back");
					int opt = SupportFunction.checkInputNum(9);
					if (opt == 0)
						break;
					else {
						switch (opt) {
						case 1:
							int newCapacity = SupportFunction
									.handleIntInput("Enter new capacity: (Previous total capacity is: "
											+ selectedShip.getShipMaxCapacity() + ")");
							selectedShip.modifyShipCapacity(newCapacity);
							break;
							
						case 2:
							// Once filter out which types are already
							// available,
							ArrayList<String> unusedTypes = selectedShip.findMissingContainerTypes();
							int length = unusedTypes.size();
							if (length == 0) 
							{
								System.out.println("This ship can carry all possible types!");
								break;
							} 
							else 
							{
								String[] unusedTypesArr = unusedTypes.toArray(new String[unusedTypes.size()]);
								SupportFunction.displayArrayAsMenu(unusedTypesArr,
										"These are the types of containers you can add:");

								int choice = SupportFunction.checkInputNum(unusedTypesArr.length);
								if (choice == 0)
									break;

								String chosenType = unusedTypesArr[choice - 1];

								int input = SupportFunction.handleIntInput("Enter max load for the type " + chosenType);
								selectedShip.addContainer(chosenType,
										SupportFunction.createShipContainerObject(chosenType, input));

								input = SupportFunction
										.handleIntInput("Enter price per container for the type " + chosenType);
								selectedShip.modifyContainerPrice(chosenType, input);
							}
							break;
							
						case 3:
							int numOfKeys = containerList.keySet().size();
							if (numOfKeys == 0) {
								System.out.println("There are no container types left!");
								break;
							} else {
								String[] types = selectedShip.findCurrentContainerTypes();
								SupportFunction.displayArrayAsMenu(types, "Please select a container type to delete:");

								int choice = SupportFunction.checkInputNum(types.length);
								if (choice == 0)
									break;
								else {
									if (types[choice-1].equals("special"))
										selectedShip.setSpecial(false);
									
									selectedShip.deleteContainer(types[choice-1]);
								}
							}
							break;
						case 4:
							int keySize = containerList.keySet().size();
							if (keySize == 0) 
							{
								System.out.println("There are no container types left!");
								break;
							} 
							else 
							{
								String[] types = SupportFunction.filterTypeList(containerList);
								SupportFunction.displayArrayAsMenu(types, "Please select a container type to update:");

								int choice = SupportFunction.checkInputNum(types.length);
								if (choice == 0)
									break;

								BasicContainer currType = containerList.get(types[choice - 1]);

								int input = SupportFunction.handleIntInput("Enter new max capacity for "
										+ types[choice - 1] + " type. (Current max capacity is: "
										+ currType.getMaxCapacity() + ")");
								currType.setMaxCapacity(input);

								input = SupportFunction
										.handleIntInput("Enter new price per container for " + types[choice - 1]
											+ " type. (Current price is: " + currType.getPricePerContainer() + ")");
								currType.setPricePerContainer(input);
							}
							break;
							
						case 5:
							String newName = SupportFunction.handleStringInput(
									"Enter new ship name. (Current name is " + selectedShip.getName() + ")");
							selectedShip.setName(newName);
							break;
							
						case 6:
							String newOName = SupportFunction.handleStringInput(
									"Enter new owner name. (Current name is " + selectedShip.getOwnerName() + ")");
							selectedShip.setOwnerName(newOName);
							break;
							
						case 7:
							String newCName = SupportFunction.handleStringInput(
									"Enter new company name. (Current name is " + selectedShip.getCompany() + ")");
							selectedShip.setCompany(newCName);
							break;
							
						case 8:
							int newVal = SupportFunction.handleIntInput("Enter new ship value. (Current value is "
									+ selectedShip.getValueInBillion() + ")");
							selectedShip.setValueInBillion(newVal);
							;
							break;
						case 9:
							XMLWriter.editXMLFile(selectedShip);
							break;
						default:
							break;
						}
					}
				}
			} 
			else 
			{
				System.out.println("There are no ships left!");
				break;
			}
		}
	}
	
	// XINYI METHODS
	// -------------------------------------------------------------------------------------------------------------------------------------------

	public void reset() 
	{
		String[] shipListContainerType = { "basic", "heavy", "refrigerated", "special", "all" };
		super.reset(shipListContainerType);
	}


	// Print the Selected the Ship (Max Capacity, Current Capacity, and its
	// respective Container types)
	public void printShipCapacity(ArrayList<BasicObject> shipList, int selected) {
		String[] shipListContainerType = { "basic", "heavy", "refrigerated", "special", "all" };

		ArrayList<Integer> ShipMaxCap = new ArrayList<Integer>();
		ArrayList<Integer> ShipCurCap = new ArrayList<Integer>();

		for (int j = 0; j < shipListContainerType.length; j++) {
			HashMap<String, BasicContainer> s = shipList.get(selected).getContainerList();

			if (s.containsKey(shipListContainerType[j])) {
				ShipMaxCap.add(s.get(shipListContainerType[j]).getMaxCapacity());
				ShipCurCap.add(s.get(shipListContainerType[j]).getCurrentCapacity());
			} else {
				ShipMaxCap.add(-1);
				ShipCurCap.add(-1);
			}
		}

		SupportFunction sf = new SupportFunction();
		Ship s = (Ship)shipList.get(selected);

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("Owner Name: " + s.getOwnerName());
		System.out.println("Ship Name: " + s.getName());
		System.out.println("Ship Status: " + (s.getStatus() ? "Full" : "Not Full"));
		System.out.println("Handle Special: " + (s.getSpecial() ? "Yes" : "No"));
		System.out.println("-----------------------------------------------------------------------------------");
		sf.printArrayLine(shipListContainerType, " ");
		sf.printArrayLine(ShipMaxCap, "Max Cap");
		sf.printArrayLine(ShipCurCap, "Cur Cap");
		System.out.println();
	}

	// Print the All the Ship (Max Capacity, Current Capacity, and its
	// respective Container types)
	public void printShipCapacity(ArrayList<BasicObject> shipList) {
		System.out.println("---------------------------------------------------");
		System.out.println("Ship capacity ");
		System.out.println("---------------------------------------------------");

		String[] shipListContainerType = { "basic", "heavy", "refrigerated", "special", "all" };
		for (int i = 0; i < shipList.size(); i++) {
			ArrayList<Integer> ShipMaxCap = new ArrayList<Integer>();
			ArrayList<Integer> ShipCurCap = new ArrayList<Integer>();

			for (int j = 0; j < shipListContainerType.length; j++) {
				HashMap<String, BasicContainer> s = shipList.get(i).getContainerList();

				if (s.containsKey(shipListContainerType[j])) {
					ShipMaxCap.add(s.get(shipListContainerType[j]).getMaxCapacity());
					ShipCurCap.add(s.get(shipListContainerType[j]).getCurrentCapacity());
				} else {
					ShipMaxCap.add(-1);
					ShipCurCap.add(-1);
				}
			}

			SupportFunction sf = new SupportFunction();
			
			Ship s = (Ship)shipList.get(i);
			System.out.println("-----------------------------------------------------------------------------------");
			System.out.println("Owner Name: " + s.getOwnerName());
			System.out.println("Ship Name: " + s.getName());
			System.out.println("Ship Status: " + (s.getStatus() ? "Full" : "Not Full"));
			System.out.println("Handle Special: " + (s.getSpecial() ? "Yes" : "No"));
			System.out.println("-----------------------------------------------------------------------------------");
			sf.printArrayLine(shipListContainerType, " ");
			sf.printArrayLine(ShipMaxCap, "Max Cap");
			sf.printArrayLine(ShipCurCap, "Cur Cap");
			System.out.println();
		}
	}

	// Store all ship container according to its type for easy comparison
	public HashMap<String, ArrayList<Integer>> storeShipContainerByType(ArrayList<BasicObject> shipList) 
	{
		String[] shipContainerType = { "refrigerated", "heavy", "basic" };
		HashMap<String, ArrayList<Integer>> shipContainerMap = new HashMap<String, ArrayList<Integer>>();

		for (int i = 0; i < shipContainerType.length; i++) 
		{
			// initialize an array for each, to group the same Ship according to its type
			ArrayList<Integer> containerShipList = new ArrayList<Integer>();
			ArrayList<Integer> containerShipListSpecial = new ArrayList<Integer>();

			for (int j = 0; j < shipList.size(); j++) 
			{
				Ship s = (Ship)shipList.get(j);

				if (s.getContainerList().containsKey(shipContainerType[i])) 
				{
					// Add it into either "refrigerated", "heavy", "basic"
					containerShipList.add(j);
					shipContainerMap.put(shipContainerType[i], containerShipList);

					// If container contains special property, extend the containerType with "_special"
					if (s.getSpecial()) {
						containerShipListSpecial.add(j);
						shipContainerMap.put(shipContainerType[i] + "_special", containerShipListSpecial);
					}
					// In the end, the HashMap for shipContainerMap type consist: (6)
					// "refrigerated_special", "refrigerated", "heavy_special", "heavy", "basic_special", "basic"
				}
			}
		}
		// Print all the results
		// System.out.println("");
		// for (Map.Entry<String, ArrayList<Integer>> entry :
		// shipContainerMap.entrySet())
		// System.out.println(entry.getKey() + " : " + entry.getValue());

		return shipContainerMap;
	}

	// Sort the HashMap (shipContainerMap)'s ArrayList so that the SHIP cheapest
	// container is at the front of the array
	public void sortShipContainerByPrice(HashMap<String, ArrayList<Integer>> shipContainerMap, ArrayList<BasicObject> getShipList) 
	{
		String[] containerType = { "refrigerated_special", "refrigerated", "heavy_special", "heavy", "basic_special",
				"basic" };

		for (int i = 0; i < containerType.length; i++) 
		{
			// Check if the HashMap type contains "_special" if so, trim it
			String str = containerType[i].contains("_special")
					? containerType[i].substring(0, containerType[i].indexOf('_')) : containerType[i];

			if (shipContainerMap.containsKey(containerType[i])) 
			{
				ArrayList<Integer> shipList = shipContainerMap.get(containerType[i]);

				// Sort the ArrayList according to its PricePerContainer
				// (Ascending Order)
				Collections.sort(shipList, ((Integer s1, Integer s2) -> getShipList.get(s1).getContainerList().get(str).getPricePerContainer()
										- getShipList.get(s2).getContainerList().get(str).getPricePerContainer()));
				// commented original
				// Collections.reverse(shipList);

				// Print all the results
				// System.out.println("\n" + containerType[i] + "--" +
				// shipList.size());
				// for (int j = 0; j < shipList.size(); j++)
				// System.out.println(shipList.get(j) + " : " +
				// info.getShipList().get(shipList.get(j)).getContainerList().get(str).getPricePerContainer());
			}
		}
	}

	// Print Out all the Ship and its Containers and types (Non-readable for testing purpose) - Xinyi
	public void listAllShipContainer() 
	{
		// List out the Ship's Container (HashMap)
		System.out.println("--------------------------------------------------------------");
		System.out.println("List All Ship Container");
		System.out.println("--------------------------------------------------------------");
		for (int i = 0; i < getList().size(); i++) {
			Ship s = (Ship)getList().get(i);
			System.out.println(s.getName() + ", Special : " + s.getSpecial());

			for (Map.Entry<String, BasicContainer> entry : s.getContainerList().entrySet())
				System.out.println(entry.getKey() + " :  " + entry.getValue());
		}
	}

	public void sortShipContainerMax(HashMap<String, ArrayList<Integer>> shipContainerMap, ArrayList<BasicObject> getShipList) 
	{
		String[] containerType = { "special", "basic", "refrigerated", "heavy" };

		for (int i = 0; i < containerType.length; i++) 
		{
			String str = containerType[i];
			if (shipContainerMap.containsKey(str)) 
			{
				ArrayList<Integer> shipList = shipContainerMap.get(containerType[i]);

				// Sort the ArrayList according to its capacity
				Collections.sort(shipList, ((Integer s1, Integer s2) -> 
				getShipList.get(s2).getContainerList().get(str).getMaxCapacity()
				- getShipList.get(s1).getContainerList().get(str).getMaxCapacity()));

			}

		}
	}

	public HashMap<String, ArrayList<Integer>> storeShipContainer(ArrayList<BasicObject> shipList) 
	{
		String[] shipContainerType = { "basic", "refrigerated", "heavy" };
		HashMap<String, ArrayList<Integer>> shipContainerMap = new HashMap<String, ArrayList<Integer>>();

		for (int i = 0; i < shipContainerType.length; i++) 
		{
			// initialize an array for each, to group the same Ship according to
			// its type
			ArrayList<Integer> containerShipList = new ArrayList<Integer>();
			ArrayList<Integer> containerShipListSpecial = new ArrayList<Integer>();

			for (int j = 0; j < shipList.size(); j++) 
			{
				Ship s = (Ship)shipList.get(j);

				if (s.getContainerList().containsKey(shipContainerType[i])) 
				{
					containerShipList.add(j);
					shipContainerMap.put(shipContainerType[i], containerShipList);

					// If container contains special property, extend the
					// containerType with "_special"
					if (s.getSpecial()) {
						containerShipListSpecial.add(j);
						shipContainerMap.put(shipContainerType[i] + "_special", containerShipListSpecial);
					}
				}
			}
		}
		// Print all the results
		// System.out.println("");
		// for (Map.Entry<String, ArrayList<Integer>> entry :
		// shipContainerMap.entrySet())
		// System.out.println(entry.getKey() + " : " + entry.getValue());

		return shipContainerMap;
	}
	
	public void getShipListFull(ArrayList<BasicObject> shipList) 
	{
		System.out.println("--------------------------------------------------------------");
		System.out.println("List All Ship Not Full");
		System.out.println("--------------------------------------------------------------");
		for (int j = 0; j < shipList.size(); j++) 
		{
			Ship s1 = (Ship)shipList.get(j);
			if(s1.getStatus() == false)
			{
				System.out.println("-----------------------------------------------------------------------------------");
				System.out.println("Owner Name: " + s1.getOwnerName());
				System.out.println("Ship Name: " + s1.getName());
				System.out.println("-----------------------------------------------------------------------------------");
			}
		}
	}

}
