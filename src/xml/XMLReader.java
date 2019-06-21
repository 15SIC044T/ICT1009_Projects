package xml;

import ObjectHandler.BasicObject;
import java.util.ArrayList;

import javax.swing.JFileChooser;

public interface XMLReader
{
	// XMLReader class is responsible for reading from XML files and storing the data into ArrayLists.
	public String checkFileDirectory(JFileChooser choose);

	public boolean loadXML(String shipDirectory, ArrayList<BasicObject> arrayList);
		
}
