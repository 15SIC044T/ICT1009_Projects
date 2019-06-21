package xml;

import ObjectHandler.*;
import Containers.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

import javax.swing.JFileChooser;

import java.io.File;       
import java.util.HashMap;

public class XMLWriter{
	public XMLWriter(){}
	
	public static String saveDialog(){
		JFileChooser choose;
		choose = new JFileChooser();
		String defaultDir = ".";
		
		choose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		choose.setCurrentDirectory(new java.io.File(defaultDir));
		int returnValue = choose.showOpenDialog(choose);

		if (returnValue == JFileChooser.APPROVE_OPTION)
			return choose.getSelectedFile().getAbsolutePath();
		else
			return "";
	
	}
	
	public static void saveXMLFile(){
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("shipscheduling");
			doc.appendChild(rootElement);
			
			// ship elements
			Element ship = doc.createElement("ship");
			rootElement.appendChild(ship);

			// set attribute to ship element
			//Attr attr = doc.createAttribute("id");
			//attr.setValue("1");
			//ship.setAttributeNode(attr);
			// shorten way
			// staff.setAttribute("id", "1");

			// ship name
			String var_shipname = "Titanic";
			Attr shipName = doc.createAttribute("name");
			shipName.setValue(var_shipname);
			ship.setAttributeNode(shipName);

			//  owner elements
			Element ownerName = doc.createElement("owner");
			ownerName.appendChild(doc.createTextNode("James Cameron"));
			ship.appendChild(ownerName);

			// value in billion elements
			Element valueIB = doc.createElement("value_in_billion");
			valueIB.appendChild(doc.createTextNode("2"));
			ship.appendChild(valueIB);

			//company elements
			Element company = doc.createElement("company");
			company.appendChild(doc.createTextNode("Paramount"));
			ship.appendChild(company);
			
			
			for(int j =0; j< 5; j++){
				Element container = doc.createElement("container");
				rootElement.appendChild(container);
				
				Attr containerType = doc.createAttribute("type");
				containerType.setValue("all");
				container.setAttributeNode(containerType);
				
				Element containerMaxLoad = doc.createElement("max_load");
				containerMaxLoad.appendChild(doc.createTextNode("300"));
				container.appendChild(containerMaxLoad);
			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("C:\\Users\\Wee\\workspace\\JavaProject\\example data\\file.xml"));

			//Output to console for testing
			//StreamResult result = new StreamResult(System.out);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "6");
			transformer.transform(source, result);
			
			System.out.println("File saved!");

		  } catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		  } catch (TransformerException tfe) {
			tfe.printStackTrace();
		  }
	}
	
	public static void deleteXMLFile(String filePath){
		String fp = filePath;
		try{
    		File file = new File(fp);
        	
    		if(!file.delete()) {
    			System.out.println("Delete operation is failed.");
    		}
    	   
    	} catch(Exception e){
    		e.printStackTrace();	
    	}
	}
	
	public static void addNewShipXML(BasicObject obj) {
		try {
			Ship s = (Ship) obj;
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("shipinformation");
			doc.appendChild(rootElement);
			
			// ship elements
			Element ship = doc.createElement("ship");
			rootElement.appendChild(ship);
			
			// ship name;
			Attr shipName = doc.createAttribute("name");
			shipName.setValue(s.getName());
			ship.setAttributeNode(shipName);

			//  owner elements
			Element ownerName = doc.createElement("owner");
			ownerName.appendChild(doc.createTextNode(s.getOwnerName()));
			ship.appendChild(ownerName);

			// value in billion elements
			Element valueIB = doc.createElement("value_in_billion");
			valueIB.appendChild(doc.createTextNode(String.valueOf(s.getValueInBillion())));
			ship.appendChild(valueIB);

			//company elements
			Element company = doc.createElement("company");
			company.appendChild(doc.createTextNode(String.valueOf(s.getCompany())));
			ship.appendChild(company);
			
			createContainers(doc, rootElement, s);
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "6");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(s.getFilePath()));
			transformer.transform(source, result);
		

		  } catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		  } catch (TransformerException tfe) {
			tfe.printStackTrace();
		  }
	}
	
	public static void addNewCustomerXML(BasicObject obj) {
		try {
			Customer c = (Customer) obj;
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("containerRequirement");
			doc.appendChild(rootElement);
			
			// ship elements
			Element firstChild = doc.createElement("customer");
			rootElement.appendChild(firstChild);
			
			// ship name;
			Attr id = doc.createAttribute("ID");
			id.setValue(String.valueOf(c.getID()));
			firstChild.setAttributeNode(id);

			//  owner elements
			Element e1 = doc.createElement("name");
			e1.appendChild(doc.createTextNode(c.getName()));
			firstChild.appendChild(e1);

			// value in billion elements
			Element e2 = doc.createElement("age");
			e2.appendChild(doc.createTextNode(String.valueOf(c.getAge())));
			firstChild.appendChild(e2);

			//company elements
			Element e3 = doc.createElement("company");
			e3.appendChild(doc.createTextNode(String.valueOf(c.getCompany())));
			firstChild.appendChild(e3);
			
			createContainers(doc, rootElement, c);
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "6");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(c.getFilePath()));
			transformer.transform(source, result);
		

		  } catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		  } catch (TransformerException tfe) {
			tfe.printStackTrace();
		  }
	}
	
	public static void createElement(Document doc, Element parent, String eName, String value) {
		Element newElement = doc.createElement(eName);
		newElement.appendChild(doc.createTextNode(value));
		parent.appendChild(newElement);
	}
	
	// For customers
	public static void createContainers(Document doc, Element firstChild, Customer c) {
		HashMap<String, BasicContainer> cList = c.getContainerList();
		String[] sortingOrder = {"basic", "basic_special", "heavy", "heavy_special", "refrigerated"};
		for (int j = 0; j < sortingOrder.length; j++) {
			if (cList.keySet().contains(sortingOrder[j])) {
				Element container = doc.createElement("container");
				firstChild.appendChild(container);
				
				Attr containerType = doc.createAttribute("type_ID");
				containerType.setValue(sortingOrder[j]);
				container.setAttributeNode(containerType);
				
				int maxCapacity = c.getContainerMaxCapacity(sortingOrder[j]);
				Element containerMaxLoad = doc.createElement("number");
				containerMaxLoad.appendChild(doc.createTextNode(String.valueOf(maxCapacity)));
				container.appendChild(containerMaxLoad);
				
				String specialProperty = c.getContainerProperty(sortingOrder[j]);
				if (specialProperty != null) {
					Element property = doc.createElement("special_property");
					property.appendChild(doc.createTextNode(specialProperty));
					container.appendChild(property);
				}
			}
		}
	}
	
	// For ships
	public static void createContainers(Document doc, Element firstChild, BasicObject s) {
		HashMap<String, BasicContainer> cList = s.getContainerList();
		String[] sortingOrder = {"all", "basic", "heavy", "special", "refrigerated"};
		for (int j = 0; j < sortingOrder.length; j++) {
			if (cList.keySet().contains(sortingOrder[j])) {
				Element container = doc.createElement("container");
				firstChild.appendChild(container);
				
				Attr containerType = doc.createAttribute("type");
				containerType.setValue(sortingOrder[j]);
				container.setAttributeNode(containerType);
				
				int maxCapacity = s.getContainerMaxCapacity(sortingOrder[j]);
				Element containerMaxLoad = doc.createElement("max_load");
				containerMaxLoad.appendChild(doc.createTextNode(String.valueOf(maxCapacity)));
				container.appendChild(containerMaxLoad);
				
				int containerPrice = s.getContainerPrice(sortingOrder[j]);
				if (containerPrice != 0) {
					Element priceElement = doc.createElement("price_per_container");
					priceElement.appendChild(doc.createTextNode(String.valueOf(containerPrice)));
					container.appendChild(priceElement);
				}
			}
		}
	}
	
	public static void deleteContainers(Node firstChild){
		NodeList containerNodes = firstChild.getChildNodes();
		for (int count = 0; count < containerNodes.getLength(); count++){
	         Node node = containerNodes.item(count);
	         if("container".equals(node.getNodeName()))
	        	 firstChild.removeChild(node);
         }
	}
	
	public static void editXMLFile(Customer s) {
		deleteXMLFile(s.getFilePath());
		addNewCustomerXML(s);
	}
	
	public static void editXMLFile(Ship s) {
		deleteXMLFile(s.getFilePath());
		addNewShipXML(s);
	}
}

		
