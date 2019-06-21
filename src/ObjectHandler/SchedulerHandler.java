package ObjectHandler;

//Import Package
import Containers.*;

import java.util.*;

public class SchedulerHandler {
	private ArrayList<Scheduler> schedulerList = new ArrayList<Scheduler>();

	public void initHandler(ArrayList<Scheduler> s) {
		schedulerList = new ArrayList<Scheduler>(s); // Creates a copy of the
														// original lists so
														// editing lists here
														// won't modify the
														// originals.
	}

	public ArrayList<Scheduler> compareScheduler(ArrayList<Scheduler> schedulerList) {

		return schedulerList;
	}

	public ArrayList<Scheduler> getSchedulerList() {
		return schedulerList;
	}

	// XINYI METHODS
	// -------------------------------------------------------------------------------------------------------------------------------------------

	public void resetScheduler(ArrayList<Scheduler> schedulerList) {
		schedulerList.clear();
	}

	// Print the Ship Scheduler and its capacity + Customer loaded amount
	public void printShipScheduler(ArrayList<BasicObject> shipList, ArrayList<BasicObject> customerList,
			ArrayList<Scheduler> getSchedulerList) {
		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("Ship Scheduling ");

		for (int i = 0; i < shipList.size(); i++) {
			ShipHandler sh = new ShipHandler();
			sh.printShipCapacity(shipList, i);

			System.out.printf("%-20s %-30s %-10s\n", "Customer Name", "Container Type", "Loaded");
			for (int k = 0; k < getSchedulerList.size(); k++) {
				Scheduler sch = getSchedulerList.get(k);
				if (sch.ship == i) {
					System.out.printf("%-20s %-30s %-10s\n", customerList.get(sch.getCustomer()).getName(),
							sch.getContainerType(), sch.getLoadCapacity());
				}
			}
			System.out.println("\n");
		}
	}
	

	// Schedule the Customer Container to Ship
	public void scheduleByPrice(HashMap<String, ArrayList<Integer>> shipContainerMap,
			HashMap<String, ArrayList<Integer>> custContainerMap, ArrayList<BasicObject> getShipList,
			ArrayList<BasicObject> getCustomerList) {
		String[] containerType = { "refrigerated_special", "refrigerated", "heavy_special", "heavy", "basic_special",
				"basic" };

		// Compare value from (Refrigerated_Special to Basic)
		for (int i = 0; i < containerType.length; i++) {
			if (custContainerMap.containsKey(containerType[i])) {
				// Take the lowest balance of the customer and load its
				// container to the ship
				ArrayList<Integer> customerList = custContainerMap.get(containerType[i]);

				// Sort the ArrayList according to its balance (Ascending Order)
				Collections.sort(customerList, ((Integer c1, Integer c2) -> getCustomerList.get(c1).getBalance()
						- getCustomerList.get(c2).getBalance()));
				// uncommented original
				Collections.reverse(customerList);

				// If Ship container type is the same as Customer
				if (shipContainerMap.containsKey(containerType[i])) {
					// Get the ArrayList from the HashMap (Cust & Ship
					// respectively)
					// ArrayList<Integer> custList =
					// custContainerMap.get(containerType[i]);
					ArrayList<Integer> shipList = shipContainerMap.get(containerType[i]);

					for (int j = 0; j < customerList.size(); j++) {
						for (int k = 0; k < shipList.size(); k++) {
							
							Ship s = (Ship)getShipList.get(shipList.get(k));
							
							// Trim the containerType for Ship
							String cTypeTrim = containerType[i].contains("_special")
									? containerType[i].substring(0, containerType[i].indexOf('_')) : containerType[i];

							// Container Map of a customer
							HashMap<String, BasicContainer> cMap = getCustomerList.get(customerList.get(j))
									.getContainerList();
							HashMap<String, BasicContainer> sMap = getShipList.get(shipList.get(k)).getContainerList();

							// check if remainingCapacity is > 0 for customer
							// and ship && within ship "all" capacity
							if (cMap.get(containerType[i]).getCurrentCapacity() > 0
									&& sMap.get(cTypeTrim).getCurrentCapacity() > 0
									&& sMap.get("all").getCurrentCapacity() > 0
									&& (containerType[i].contains("_special")
											? sMap.get("special").getCurrentCapacity() > 0 : true)) {
								// get the min Capacity of the ship (either
								// special / original)
								int minCap = containerType[i].contains("_special")
										? Math.min(
												Math.min(sMap.get(cTypeTrim).getCurrentCapacity(),
														sMap.get("special").getCurrentCapacity()),
												sMap.get("all").getCurrentCapacity())
										: Math.min(sMap.get(cTypeTrim).getCurrentCapacity(),
												sMap.get("all").getCurrentCapacity());
								// (Load Customer to ship) get the remaining
								// container from the customer after minus from
								// ship
								int currCap = minCap - cMap.get(containerType[i]).getCurrentCapacity();

								// System.out.println("minCap: " + minCap);
								// System.out.println("currCap: " + currCap);

								// Positive currCap value (Ship has more
								// container than customer)
								if (currCap >= 0) {
									// Make changes to the Ship information
									if (containerType[i].contains("_special"))
										sMap.get("special")
												.setCurrentCapacity(sMap.get("special").calculateRemainingValue(
														cMap.get(containerType[i]).getCurrentCapacity()));
									sMap.get(cTypeTrim).setCurrentCapacity(sMap.get(cTypeTrim)
											.calculateRemainingValue(cMap.get(containerType[i]).getCurrentCapacity()));

									sMap.get("all").setCurrentCapacity(sMap.get("all")
											.calculateRemainingValue(cMap.get(containerType[i]).getCurrentCapacity()));
									if (sMap.get("all").getCurrentCapacity() == 0)
										s.setStatus(true);

									// Store the ship and customer to the
									// scheduler
									Scheduler schedule = new Scheduler(shipList.get(k), customerList.get(j),
											cMap.get(containerType[i]).getCurrentCapacity(), containerType[i]);
									getSchedulerList().add(schedule);

									// Make changes to the customer information
									getCustomerList.get(customerList.get(j)).addBalance(sMap.get(cTypeTrim)
											.calculatePrice(cMap.get(containerType[i]).getCurrentCapacity()));
									// info.getCustomerList().get(customerList.get(j)).addBalance(sMap.get(cTypeTrim).calculatePrice(cMap.get(containerType[i]).getMaxCapacity()));
									cMap.get(containerType[i]).setCurrentCapacity(0);
								} else // Negative currCap value (Customer has
										// more container than ship)
								{
									// Make changes to the Ship Information
									if (containerType[i].contains("_special"))
										sMap.get("special").setCurrentCapacity(
												sMap.get("special").calculateRemainingValue(minCap));
									sMap.get(cTypeTrim)
											.setCurrentCapacity(sMap.get(cTypeTrim).calculateRemainingValue(minCap));
									sMap.get("all").setCurrentCapacity(sMap.get("all").calculateRemainingValue(minCap));
									if (sMap.get("all").getCurrentCapacity() == 0)
										s.setStatus(true);

									// Store the ship and customer to the
									// scheduler
									Scheduler schedule = new Scheduler(shipList.get(k), customerList.get(j), minCap,
											containerType[i]);
									getSchedulerList().add(schedule);

									// Make changes to the customer information
									getCustomerList.get(customerList.get(j))
											.addBalance(sMap.get(cTypeTrim).calculatePrice(minCap));
									cMap.get(containerType[i]).setCurrentCapacity(
											cMap.get(containerType[i]).calculateRemainingValue(minCap));
								}
							}

							// System.out.println("(END)Container Type: " +
							// containerType[i]);
							// System.out.println(customerList.get(j) + ",
							// Customer: " +
							// info.getCustomerList().get(customerList.get(j)).getName());
							// System.out.println("Container (C, M): " +
							// cMap.get(containerType[i]).getCurrentCapacity() +
							// " , " +
							// cMap.get(containerType[i]).getMaxCapacity());

						}
					}
				}
			}
		}
	}

	// Schedule the Customer Container to Ship
	public void scheduleByPriceRR(HashMap<String, ArrayList<Integer>> shipContainerMap,
			HashMap<String, ArrayList<Integer>> custContainerMap, ArrayList<BasicObject> getShipList,
			ArrayList<BasicObject> getCustomerList) {

		String[] containerType = { "refrigerated_special", "refrigerated", "heavy_special", "heavy", "basic_special",
				"basic" };
				// String[] containerType = { "refrigerated_special",
				// "refrigerated", "heavy_special"};

		// Compare value from (Refrigerated_Special to Basic)
		for (int i = 0; i < containerType.length; i++) {
			if (custContainerMap.containsKey(containerType[i])) {
				// Take the lowest balance of the customer and load its
				// container to the ship
				ArrayList<Integer> customerList = custContainerMap.get(containerType[i]);

				// If Ship container type is the same as Customer
				if (shipContainerMap.containsKey(containerType[i])) {
					// Get the ArrayList from the HashMap (Cust & Ship
					// respectively)
					// ArrayList<Integer> custList =
					// custContainerMap.get(containerType[i]);
					ArrayList<Integer> shipList = shipContainerMap.get(containerType[i]);

					int customerListSize = customerList.size(), shipListSize = shipList.size();

					int equalDivide = 0, remainder = 0;

					for (int k = 0; k < shipList.size(); k++) {
						Ship s = (Ship) getShipList.get(shipList.get(k));
						
						// Sort the ArrayList according to its balance
						// (Ascending Order)
						Collections.sort(customerList, ((Integer c1, Integer c2) -> getCustomerList.get(c1).getBalance() - getCustomerList.get(c2).getBalance()));
						// uncommented original
						Collections.reverse(customerList);

						// Trim the containerType for Ship
						String cTypeTrim = containerType[i].contains("_special")
								? containerType[i].substring(0, containerType[i].indexOf('_')) : containerType[i];

						HashMap<String, BasicContainer> sMap = getShipList.get(shipList.get(k)).getContainerList();

						if (customerListSize > 0) {
							while (true) {
								int minCap = containerType[i].contains("_special")
										? Math.min(
												Math.min(sMap.get(cTypeTrim).getCurrentCapacity(),
														sMap.get("special").getCurrentCapacity()),
												sMap.get("all").getCurrentCapacity())
										: Math.min(sMap.get(cTypeTrim).getCurrentCapacity(),
												sMap.get("all").getCurrentCapacity());

								equalDivide = minCap / customerListSize;
								remainder = minCap % customerListSize;

								for (int j = 0; j < customerList.size(); j++) {
									
									Customer c = (Customer)getCustomerList.get(customerList.get(j));
									// Container Map of a customer
									HashMap<String, BasicContainer> cMap = getCustomerList.get(customerList.get(j))
											.getContainerList();

									if (cMap.get(containerType[i]).getCurrentCapacity() > 0
											&& sMap.get(cTypeTrim).getCurrentCapacity() > 0
											&& sMap.get("all").getCurrentCapacity() > 0
											&& (containerType[i].contains("_special")
													? sMap.get("special").getCurrentCapacity() > 0 : true)) {
										int currCap = equalDivide - cMap.get(containerType[i]).getCurrentCapacity();

										// Positive currCap value (Ship has more
										// container than customer)
										if ((minCap <= customerListSize) ? false : currCap >= 0) {
											// Make changes to the Ship
											// information
											if (containerType[i].contains("_special"))
												sMap.get("special")
														.setCurrentCapacity(sMap.get("special").calculateRemainingValue(
																cMap.get(containerType[i]).getCurrentCapacity()));
											sMap.get(cTypeTrim)
													.setCurrentCapacity(sMap.get(cTypeTrim).calculateRemainingValue(
															cMap.get(containerType[i]).getCurrentCapacity()));

											sMap.get("all").setCurrentCapacity(sMap.get("all").calculateRemainingValue(
													cMap.get(containerType[i]).getCurrentCapacity()));
											if (sMap.get("all").getCurrentCapacity() == 0)
												s.setStatus(true);

											// Store the ship and customer to
											// the scheduler
											Scheduler schedule = new Scheduler(shipList.get(k), customerList.get(j),
													cMap.get(containerType[i]).getCurrentCapacity(), containerType[i]);
											getSchedulerList().add(schedule);

											// Make changes to the customer
											// information
											c.addBalance(sMap.get(cTypeTrim)
													.calculatePrice(cMap.get(containerType[i]).getCurrentCapacity()));
											// info.getCustomerList().get(customerList.get(j)).addBalance(sMap.get(cTypeTrim).calculatePrice(cMap.get(containerType[i]).getMaxCapacity()));
											cMap.get(containerType[i]).setCurrentCapacity(0);

											remainder = +currCap;
											customerListSize -= 1;
										} else // Negative currCap value
												// (Customer has more container
												// than ship)
										{
											int specialCase = 0;
											if (minCap <= customerListSize)
												specialCase = remainder;

											if (containerType[i].contains("_special"))
												sMap.get("special")
														.setCurrentCapacity(sMap.get("special").calculateRemainingValue(
																specialCase > 0 ? specialCase : equalDivide));
											sMap.get(cTypeTrim)
													.setCurrentCapacity(sMap.get(cTypeTrim).calculateRemainingValue(
															specialCase > 0 ? specialCase : equalDivide));
											sMap.get("all").setCurrentCapacity(sMap.get("all").calculateRemainingValue(
													specialCase > 0 ? specialCase : equalDivide));
											if (sMap.get("all").getCurrentCapacity() == 0)
												s.setStatus(true);

											// Store the ship and customer to
											// the scheduler
											Scheduler schedule = new Scheduler(shipList.get(k), customerList.get(j),
													(specialCase > 0 ? specialCase : equalDivide), containerType[i]);
											getSchedulerList().add(schedule);

											// Make changes to the customer
											// information
											c.addBalance(sMap.get(cTypeTrim)
													.calculatePrice(specialCase > 0 ? specialCase : equalDivide));
											cMap.get(containerType[i]).setCurrentCapacity(
													cMap.get(containerType[i]).calculateRemainingValue(
															specialCase > 0 ? specialCase : equalDivide));

											shipListSize -= 1;
										}
									}
								}

								if (customerListSize == 0 || (shipListSize == 0 && remainder == 0)
										|| (equalDivide == 0 && remainder == 0))
									break;
								else
									equalDivide = remainder;
							}
						}
					}
				}
			}
		}
	}

	public void scheduleByMin(HashMap<String, ArrayList<Integer>> shipContainerMap1,
			HashMap<String, ArrayList<Integer>> custContainerMap1, ArrayList<BasicObject> getShipList,
			ArrayList<BasicObject> getCustomerList) {
		String[] containerType = { "basic_special", "refrigerated_special", "heavy_special", "basic", "refrigerated",
				"heavy" };
		
		// Compare value from (Refrigerated_Special to Basic)
		for (int i = 0; i < containerType.length; i++) {
			if (custContainerMap1.containsKey(containerType[i])) {

				ArrayList<Integer> customerList = custContainerMap1.get(containerType[i]);

				if (shipContainerMap1.containsKey(containerType[i])) {

					ArrayList<Integer> shipList = shipContainerMap1.get(containerType[i]);

					for (int j = 0; j < customerList.size(); j++) {
						for (int k = 0; k < shipList.size(); k++) {

							// Trim the containerType for Ship
							String cTypeTrim = containerType[i].contains("_special")
									? containerType[i].substring(0, containerType[i].indexOf('_')) : containerType[i];

							// Container Map of a customer
							HashMap<String, BasicContainer> cMap = getCustomerList.get(customerList.get(j)).getContainerList();
							HashMap<String, BasicContainer> sMap = getShipList.get(shipList.get(k)).getContainerList();
							
							Ship s = (Ship)getShipList.get(shipList.get(k));
							Customer c = (Customer)getCustomerList.get(customerList.get(j));
							
							if (cMap.get(containerType[i]).getCurrentCapacity() > 0
									&& sMap.get(cTypeTrim).getCurrentCapacity() > 0
									&& sMap.get("all").getCurrentCapacity() > 0
									&& (containerType[i].contains("_special")
									? sMap.get("special").getCurrentCapacity() > 0 : true)) 
							{
								if (containerType[i].contains("_special")) 
								{
									int minCap = Math.min(Math.min(sMap.get(cTypeTrim).getCurrentCapacity(),
														sMap.get("special").getCurrentCapacity()),
														sMap.get("all").getCurrentCapacity());
									
									int currCap = minCap - cMap.get(containerType[i]).getCurrentCapacity();
									if (currCap >= 0) 
									{
										// Make changes to the Ship information
										if (containerType[i].contains("_special")) 
										{
											sMap.get("special")
											.setCurrentCapacity(sMap.get("special").calculateRemainingValue(
											cMap.get(containerType[i]).getCurrentCapacity()));
											sMap.get(cTypeTrim).setCurrentCapacity(sMap.get(cTypeTrim).calculateRemainingValue(
											cMap.get(containerType[i]).getCurrentCapacity()));

											sMap.get("all").setCurrentCapacity(sMap.get("all").calculateRemainingValue(
											cMap.get(containerType[i]).getCurrentCapacity()));
										}
										
										if (sMap.get("all").getCurrentCapacity() == 0) 
											s.setStatus(true);
										

										// Store the ship and customer to the
										// scheduler
										Scheduler scheduler = new Scheduler(shipList.get(k), customerList.get(j),
										cMap.get(containerType[i]).getCurrentCapacity(), containerType[i]);
										
										getSchedulerList().add(scheduler);

										// Make changes to the customer
										// information
										c.addBalance(sMap.get(cTypeTrim)
										.calculatePrice(cMap.get(containerType[i]).getCurrentCapacity()));
										// info.getCustomerList().get(customerList.get(j)).addBalance(sMap.get(cTypeTrim).calculatePrice(cMap.get(containerType[i]).getMaxCapacity()));
										cMap.get(containerType[i]).setCurrentCapacity(0);
									} 
									else // Negative currCap value (Customer has more container than ship)
									{
										// Make changes to the Ship Information
										if (containerType[i].contains("_special"))
										{
											sMap.get("special").setCurrentCapacity(
											sMap.get("special").calculateRemainingValue(minCap));
											sMap.get(cTypeTrim).setCurrentCapacity(
											sMap.get(cTypeTrim).calculateRemainingValue(minCap));
											sMap.get("all").setCurrentCapacity(sMap.get("all").calculateRemainingValue(minCap));
										}
										if (sMap.get("all").getCurrentCapacity() == 0) {
											s.setStatus(true);
										}

										// Store the ship and customer to the
										// scheduler
										Scheduler scheduler = new Scheduler(shipList.get(k), customerList.get(j),
																			minCap, containerType[i]);
																			getSchedulerList().add(scheduler);

										// Make changes to the customer information
										c.addBalance(sMap.get(cTypeTrim).calculatePrice(minCap));
										cMap.get(containerType[i]).setCurrentCapacity(cMap.get(containerType[i]).calculateRemainingValue(minCap));
									}
								}
								// cust container no _special
								else {
									int minCap = Math.min(sMap.get(containerType[i]).getCurrentCapacity(),
											sMap.get("all").getCurrentCapacity());
									int currCap = minCap - cMap.get(containerType[i]).getCurrentCapacity();
									// Positive currCap value (Ship has more
									// container than customer)
									if (currCap >= 0) {
										// Make changes to the Ship information
										sMap.get(containerType[i])
												.setCurrentCapacity(sMap.get(containerType[i]).calculateRemainingValue(
														cMap.get(containerType[i]).getCurrentCapacity()));

										sMap.get("all").setCurrentCapacity(sMap.get("all").calculateRemainingValue(
												cMap.get(containerType[i]).getCurrentCapacity()));
										if (sMap.get("all").getCurrentCapacity() == 0) 
											s.setStatus(true);

										// Store the ship and customer to the
										// scheduler
										Scheduler scheduler = new Scheduler(shipList.get(k), customerList.get(j),
										cMap.get(containerType[i]).getCurrentCapacity(), containerType[i]);
										
										getSchedulerList().add(scheduler);
										
										c.addBalance(sMap.get(cTypeTrim)
										.calculatePrice(cMap.get(containerType[i]).getCurrentCapacity()));
										
										cMap.get(containerType[i]).setCurrentCapacity(0);
										
									} 
									else // Negative currCap value (Customer has more container than ship)
									{
										// Make changes to the Ship Information
										sMap.get("all").setCurrentCapacity(sMap.get("all").calculateRemainingValue(minCap));
										if (sMap.get("all").getCurrentCapacity() == 0) 
											s.setStatus(true);

										// Store the ship and customer to the
										// scheduler
										Scheduler scheduler = new Scheduler(shipList.get(k), customerList.get(j),
												cMap.get(containerType[i]).getCurrentCapacity(), containerType[i]);
										getSchedulerList().add(scheduler);

										// Make changes to the customer
										// information
										c.addBalance(sMap.get(cTypeTrim).calculatePrice(minCap));
										cMap.get(containerType[i]).setCurrentCapacity(
										cMap.get(containerType[i]).calculateRemainingValue(minCap));
									}
								}
							}

						}
					}
				}
			}
		}
	}
	
	public void compareScheduler(HashMap<String, ArrayList<Integer>> shipContainerMap1,
			HashMap<String, ArrayList<Integer>> custContainerMap1, ArrayList<BasicObject> getShipList,
			ArrayList<BasicObject> getCustomerList) {
		
	}
	
}
