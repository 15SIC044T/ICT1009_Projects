//import package
import xml.*;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

//Basic Java Import
import java.util.*;

//File Directory Selection, Importation
import javax.swing.JFileChooser;

import ObjectHandler.*;

//Main Program starts here ---------------------------------------------------------------
public class Main 
{
	public static void main(String[] args) throws IOException 
	{
		// Initialize XML Object
		XMLReader xmlShipReader = new ShipReader();
		XMLReader xmlContainerReader = new CustomerReader();

		// This is here because I try to bring the JFileChoose to show on top of window, thus I pre-load this.
		JFileChooser choose = new JFileChooser();

		// Initialize Object
		ShipHandler sh = new ShipHandler();
		CustomerHandler ch = new CustomerHandler();
		SchedulerHandler sch = new SchedulerHandler();

		// Initialize Variables
		int option = -1, fileOpt = -1;
		String shipDirectory = "", containerDirectory = "";
		boolean login = false, loadShip = false;

		// Select the directories of the Ship and Container Folder
		while (true) 
		{
			displayFileMenu(shipDirectory, containerDirectory);
			fileOpt = SupportFunction.checkInputNum(0, 3);

			switch (fileOpt) 
			{
			case 0: // Exit
				System.out.println("System now exit!");
				break;

			case 1: // Get Ship Information
				// Get the ship Directory
				shipDirectory = xmlShipReader.checkFileDirectory(choose);
				
				// Load all the ship information to the arrayList
				if (shipDirectory != "")
					xmlShipReader.loadXML(shipDirectory, sh.getList());
				else
					System.out.println("You did not select directory!");

				System.out.println(loadShip);
				break;

			case 2: // Get Container Information
				// Get the container directory
				containerDirectory = xmlContainerReader.checkFileDirectory(choose);

				// Load all the container information to the arrayList
				if (containerDirectory != "")
					xmlContainerReader.loadXML(containerDirectory, ch.getList());
				else
					System.out.println("You did not select directory!");
				break;

			case 3: // Login to the system
				// Make sure the directories contains appropriate XML file to login to system

				if (shipDirectory == "" && containerDirectory == "")
					System.out.println("Please make sure Ships and Container Folder is selected!");
				else
					login = true;
				break;

			default:
				System.out.println("Error, no such option exists,please input again");
			}

			if (fileOpt == 0 || login == true)
				break;
		}

		if (fileOpt == 3) 
		{
			sh.setDirectory(shipDirectory);
			ch.setDirectory(containerDirectory);
			
			// Start to select Menu after importing XML files
			while (true) 
			{
				displayMenu();
				option = SupportFunction.checkInputNum(0, 8);

				// Run Menu Case
				switch (option) 
				{
				case 0: // Exit
					System.out.println("System now exit!");
					break;

				case 1: // 1) Schedule containers to ships by minimum number of ships
					
					//Reset the current Capacity
					sh.reset();
					ch.reset();
					sch.resetScheduler(sch.getSchedulerList());
					HashMap<String, ArrayList<Integer>> shipContainerMap1 = sh.storeShipContainer(sh.getList());

					//// Store the Ship Container by maxShipLoad
					sh.sortShipContainerMax(shipContainerMap1, sh.getList());
					//
					// Store all the Customer Container by Type
					HashMap<String, ArrayList<Integer>> custContainerMap1 = ch.storeCustomerContainerByType1(ch.getList());

					sch.scheduleByMin(shipContainerMap1, custContainerMap1, sh.getList(), ch.getList());

					sch.printShipScheduler(sh.getList(), ch.getList(), sch.getSchedulerList());

					// Print the Ship Capacity
					sh.printShipCapacity(sh.getList());

					// Print all the remaining capacity of customer containers
					ch.printCustomerInfo(ch.getList());

					// Print the total cost payable by the customer
					ch.printCustomerPayable(ch.getList());

					//sh.resetShip(sh.getShipList());
					ch.reset();
					//sch.resetScheduler(sch.getSchedulerList());
					break;

				case 2: // 2) Schedule containers to ships by minimum total
						// price
					System.out.println("2) Schedule containers to ships by minimum total price");

					System.out.println("Greedy + round robin Algorithm");
					// Greedy + Round Robin Algorithms
					
					// Reset the Ship
					sh.reset();
					ch.reset();
					sch.resetScheduler(sch.getSchedulerList());

					// Store all the Ship Container by Type
					HashMap<String, ArrayList<Integer>> shipContainerMapa = sh.storeShipContainerByType(sh.getList());

					// Store the Ship Container by Price
					sh.sortShipContainerByPrice(shipContainerMapa, sh.getList());

					// Store all the Customer Container by Type
					HashMap<String, ArrayList<Integer>> custContainerMapa = ch.storeCustomerContainerByType(ch.getList());

					// Schedule Ship
					sch.scheduleByPriceRR(shipContainerMapa, custContainerMapa, sh.getList(), ch.getList());

					// Print the Ship Scheduler Information, consisting the
					// Ship remaining capacity & customer container loaded
					// to ship respectively
					sch.printShipScheduler(sh.getList(), ch.getList(), sch.getSchedulerList());

					// Print the Ship Capacity
					// sh.printShipCapacity(sh.getShipList());

					// Print all the remaining capacity of customer containers
					ch.printCustomerInfo(ch.getList());

					// Print the total cost payable by the customer
					ch.printCustomerPayable(ch.getList());

					// Reset the Ship
					// sh.resetShip(sh.getShipList());
					// ch.resetCustomer(ch.getList());
					// sch.resetScheduler(sch.getSchedulerList());
				

					break;

				case 3: // 3) Schedule containers to ships with optimum solution
					System.out.println("2) Greedy Algorithm");
					// Greedy Algorithms
					
					// Reset the Ship
					sh.reset();
					ch.reset();
					sch.resetScheduler(sch.getSchedulerList());

					// Store all the Ship Container by Type
					HashMap<String, ArrayList<Integer>> shipContainerMap = sh.storeShipContainerByType(sh.getList());

					// Store the Ship Container by Price
					sh.sortShipContainerByPrice(shipContainerMap, sh.getList());

					// info.listAllCustomerContainer();

					// Store all the Customer Container by Type
					HashMap<String, ArrayList<Integer>> custContainerMap = ch.storeCustomerContainerByType(ch.getList());

					// Schedule Ship
					sch.scheduleByPrice(shipContainerMap, custContainerMap, sh.getList(), ch.getList());

					// Print the Ship Scheduler Information, consisting the
					// Ship remaining capacity & customer container loaded
					// to ship respectively
					sch.printShipScheduler(sh.getList(), ch.getList(), sch.getSchedulerList());

					// Print the Ship Capacity
					// sh.printShipCapacity(sh.getShipList());

					// Print all the remaining capacity of customer
					// containers
					ch.printCustomerInfo(ch.getList());

					// Print the total cost payable by the customer
					ch.printCustomerPayable(ch.getList());

					// Reset the Ship
					//sh.resetShip(sh.getShipList());
					//ch.resetCustomer(ch.getList());
					//sch.resetScheduler(sch.getSchedulerList());
					break;

				case 4: // 4) List all ships that are not full
					ArrayList<BasicObject> shipContainerMap2 = sh.getList();
					sh.getShipListFull(shipContainerMap2);
					break;

				case 5: // 5) Update container information
					while (true) 
					{
						System.out.println("Would you like to add, delete or update customer information?");
						System.out.println("1) Add a new customer.");
						System.out.println("2) Delete a customer.");
						System.out.println("3) Update customer details.");
						System.out.println("0) Back");
						int updateOption = SupportFunction.checkInputNum(3);
						if (updateOption == 0)
							break;
						switch (updateOption) 
						{
						case 1:
							ch.addMenu();
							break;
						case 2:
							ch.deleteMenu();
							break;
						default:
							ch.updateMenu();
							break;
						}
					}
					break;

				case 6: // 6) Update ship information
					while (true) 
					{
						System.out.println("Would you like to add, delete or update ship information?");
						System.out.println("1) Add ship.");
						System.out.println("2) Delete ship.");
						System.out.println("3) Update ship.");
						System.out.println("0) Back");
						System.out.print("Please enter option: ");
						int updateOption = SupportFunction.checkInputNum(3);
						if (updateOption == 0)
							break;
						switch (updateOption) 
						{
						case 1:
							sh.addMenu();
							break;
						case 2:
							sh.deleteMenu();
							break;
						default:
							sh.updateMenu();
							break;
						}
					}
					break;
					
				case 8:
					// Print the Ship Capacity
					sh.printShipCapacity(sh.getList());

					// Print all the remaining capacity of customer containers
					ch.printCustomerInfo(ch.getList());
					break;

				case 7: // 7) Save scheduling plan to disk
					FileOutputStream fop = null;
					File file;
					PrintStream console = System.out; // declare purpose to get out from reading to txt file
					while (true)
					{
						displaySaveSchedulingMenu();
						int saveFileOption = SupportFunction.checkInputNum(4);
						
						if (saveFileOption == 0) 
							break;				
						
						switch (saveFileOption)
						{
						case 1: 
							try
							{
								String filePath = XMLWriter.saveDialog();
								file = new File(filePath + "\\MinShip_Scheduling.txt");
								if (file.exists() == true)
								{
									System.out.println("\n\n\tFile existed! Overwriting...\n");
								}
								fop = new FileOutputStream(file);
								sh.reset();
								ch.reset();
								sch.resetScheduler(sch.getSchedulerList());
								HashMap<String, ArrayList<Integer>> shipContainerMap11 = sh.storeShipContainer(sh.getList());
								sh.sortShipContainerMax(shipContainerMap11, sh.getList());
								HashMap<String, ArrayList<Integer>> custContainerMap11 = ch.storeCustomerContainerByType1(ch.getList());
								sch.scheduleByMin(shipContainerMap11, custContainerMap11, sh.getList(), ch.getList());
								
								PrintStream out = new PrintStream(fop);
								System.setOut(out);
								System.out.println("----------------minimum number of ships schedule----------------\n");
								sch.printShipScheduler(sh.getList(), ch.getList(), sch.getSchedulerList());
								sh.printShipCapacity(sh.getList());
								ch.printCustomerInfo(ch.getList());
								ch.printCustomerPayable(ch.getList());
								ch.reset();
								out.close();
								System.setOut(console);
								System.out.println("\n\n\t(min_ship) Saved Successfully !\n");
								System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
							}
							catch(IOException e)
							{
								System.out.print("Not saving file?\n");
							}
							break;
							
							
						case 2: 
							try
							{
								String filePath = XMLWriter.saveDialog();
								file = new File(filePath+"\\MinCost_Scheduling(Greedy).txt");
								if (file.exists() == true)
								{
									System.out.println("\n\n\tFile existed! Overwriting...");
								}

								fop = new FileOutputStream(file);
								sh.reset();
								ch.reset();
								sch.resetScheduler(sch.getSchedulerList());
								HashMap<String, ArrayList<Integer>> shipContainerMap22 = sh.storeShipContainerByType(sh.getList());
								sh.sortShipContainerByPrice(shipContainerMap22, sh.getList());
								HashMap<String, ArrayList<Integer>> custContainerMap22 = ch.storeCustomerContainerByType(ch.getList());

								PrintStream out = new PrintStream(fop);
								System.setOut(out);
								System.out.println("--------minimum total price of ships schedule - Greedy Algorithm--------");
								sch.scheduleByPrice(shipContainerMap22, custContainerMap22, sh.getList(), ch.getList());
								sch.printShipScheduler(sh.getList(), ch.getList(), sch.getSchedulerList());
								ch.printCustomerInfo(ch.getList());
								ch.printCustomerPayable(ch.getList());
								out.close();
								System.setOut(console);
								System.out.println("\n\n\t(min_cost_greedy) Saved Successfully !\n");
								System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));

							}catch(IOException e)
							{
								System.out.print("Not saving file?\n");
							}
							break;
							
							
						case 3: 
							try
							{
								String filePath = XMLWriter.saveDialog();
								file = new File(filePath+"\\MinCost_Scheduling(Greedy+RR).txt");
								if (file.exists() == true){
									System.out.println("\n\n\tFile existed! Overwriting...");
							}
							fop = new FileOutputStream(file);
							sh.reset();
							ch.reset();
							sch.resetScheduler(sch.getSchedulerList());
							HashMap<String, ArrayList<Integer>> shipContainerMapaa = sh.storeShipContainerByType(sh.getList());
							sh.sortShipContainerByPrice(shipContainerMapaa, sh.getList());
							HashMap<String, ArrayList<Integer>> custContainerMapaa = ch.storeCustomerContainerByType(ch.getList());
							
							PrintStream out = new PrintStream(fop);
							System.setOut(out);
							System.out.println("--------minimum total price of ships schedule - Greedy Algorithm + Round Robin--------");
							sch.scheduleByPriceRR(shipContainerMapaa, custContainerMapaa, sh.getList(),ch.getList());
							sch.printShipScheduler(sh.getList(), ch.getList(), sch.getSchedulerList());
							ch.printCustomerInfo(ch.getList());
							ch.printCustomerPayable(ch.getList());
							out.close();
							System.setOut(console);
							System.out.println("\n\n\t(min_cost_greedy_RR) Saved Successfully!\n");
							System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
							
							}catch(IOException e){
								System.out.print("Not saving file?\n");
								
							}
							break;
						case 4:
							try{
								String filePath = XMLWriter.saveDialog();
								file = new File(filePath+"\\OptimumSol_Scheduling.txt");
								if (file.exists() == true){
									System.out.println("\n\n\tFile existed! Overwriting...");
							}
							fop = new FileOutputStream(file);
							HashMap<String, ArrayList<Integer>> shipContainerMap4 = sh.storeShipContainerByType(sh.getList());
							sch.getSchedulerList();
							
							PrintStream out = new PrintStream(fop);
							System.setOut(out);
							System.out.println("----------------optimum solution of ship schedule----------------");
							sch.printShipScheduler(sh.getList(), ch.getList(), sch.getSchedulerList());
							out.close();
							System.setOut(console);
							System.out.println("\n\n\t(optimum_sol) Saved Successfully!\n");
							System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
							}catch(IOException e)
							{
								System.out.print("Not saving file?\n");
								
							}
							break;
						}
					}
					break;

				default:
					System.out.println("Error, no such option exists,please input again");
				}
				System.out.println("\n");

				// Break out of the system if 0 is selected
				if (option == 0)
					break;
			}
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------
	// Display File Menu
	public static void displayFileMenu(String shipDirectory, String containerDirectory) 
	{
		System.out.println("\nWelcome!");
		System.out.println("Choose folder to access: ");
		System.out.println("1) Ship Folder " + (shipDirectory == "" ? "" : "(" + shipDirectory + ")"));
		System.out.println("2) Container Folder " + (containerDirectory == "" ? "" : "(" + containerDirectory + ")"));
		System.out.println("3) Login to the system!");
		System.out.println("0) Exit");
		System.out.print("Please enter option: ");
	}

	// Display Menu
	public static void displayMenu() 
	{
		System.out.println("\n( -------------------------- M E N U -------------------------- )");
		System.out.println("1) Schedule containers to ships by minimum number of ships");
		System.out.println("2) Schedule containers to ships by minimum total price");
		System.out.println("3) Schedule containers to ships with optimum solution");
		System.out.println("4) List all ships that are not full");
		System.out.println("5) Update customer information");
		System.out.println("6) Update ship information");
		System.out.println("7) Save scheduling plan to disk");
		System.out.println("8) View Ship and Customer Container");
		System.out.println("0) Exit");
		System.out.print("Please enter option: ");
	}
	
	// Display Save Scheduling Menu
	public static void displaySaveSchedulingMenu() 
	{
		System.out.println("\n( ------------------- SAVE SCHEDULING ------------------- )");
		System.out.println("1) minimum number of ships schedule");
		System.out.println("2) minimum total price of ships schedule - Greedy Algorithm");
		System.out.println("3) minimum total price of ships schedule - Greedy Algorithm + Round Robin");
		System.out.println("4) optimum solution of ship schedule");
		System.out.println("0) Back");
		System.out.print("Please enter option and choose a folder to save file: ");
	}
}