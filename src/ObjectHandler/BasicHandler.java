package ObjectHandler;

import java.util.ArrayList;
import java.util.HashMap;

import Containers.BasicContainer;

public abstract class BasicHandler {
	
	private ArrayList<BasicObject> list = new ArrayList<BasicObject>();
	
	private String directory = "";
	
	public ArrayList<BasicObject> getList() { return list;}
	
	public BasicObject getObjWithIndex(int index) { return getList().get(index); }
	public void deleteObjWithIndex(int index) { getList().remove(index); }
	
	public void addObject(BasicObject o) { getList().add(o); }
	public void clearShips() { getList().clear(); }
	
	public void setDirectory(String dir) { directory = dir; }
	public String getDirectory() { return directory; }
	
	public abstract void displayMenu();
	
	public abstract void addMenu();
	public abstract void deleteMenu();
	public abstract void updateMenu();
	
	public ArrayList<Integer> getListOfIDs() 
	{
		ArrayList<Integer> usedNumbers = new ArrayList<Integer>();
		for (int index = 0; index < getList().size(); index++) {
			BasicObject currObj = getList().get(index);
			usedNumbers.add(currObj.getID());
		}
		return usedNumbers;
	}
	
	public int createUniqueID() 
	{
		ArrayList<Integer> IDs = getListOfIDs();
		int i = 1;
		for (i = 1; i < IDs.size() + 1; i++) {
			if (!IDs.contains(i)) { // Find the gap in the number sequence, eg:
									// 1, 3 will return 2
				return i;
			} else if (i == IDs.size()) // If no gaps, return sequence last
										// index + 1
				return i + 1;
		}
		return i;
	}
	
	public void reset(String[] typeArr) 
	{
		for (int i = 0; i < typeArr.length; i++) {
			for (int j = 0; j < getList().size(); j++) {
				// Set the status back to not full
				if ( getList().get(j) instanceof Ship) {
					Ship s = (Ship)getList().get(j);
					s.setStatus(false);
				}
				getList().get(j).setBalance(0);
				
				HashMap<String, BasicContainer> sMap = getList().get(j).getContainerList();

				if (sMap.containsKey(typeArr[i])) {
					// Set all the current capacity to max capacity
					sMap.get(typeArr[i]).setCurrentCapacity(sMap.get(typeArr[i]).getMaxCapacity());
				}
			}
		}
	}
}
