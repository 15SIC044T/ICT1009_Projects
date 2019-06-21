package xml;
//Import Package
import Containers.*;
import ObjectHandler.*;

//File Directory Selection, Importation
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class ShipReader implements XMLReader {
	// This method check for file directory selection
	public String checkFileDirectory(JFileChooser choose) {
		choose = new JFileChooser();
		String defaultDir = ".";
		String xml = "<?xml ...";
		xml = xml.replaceFirst("^([\\W]+)<", "<");
		FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("xml files (*.xml)", "XML","xml","Xml");
		choose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		choose.setCurrentDirectory(new java.io.File(defaultDir));
		choose.setFileFilter(xmlfilter);

		// shipDirect = getDir("Select for ship's directory", shipDirect);
		int returnValue = choose.showOpenDialog(choose);

		// System.out.println(returnValue);

		if (returnValue == JFileChooser.APPROVE_OPTION)
			return choose.getSelectedFile().getAbsolutePath();
		else
			return "";
	}

	public boolean loadXML(String shipDirectory, ArrayList<BasicObject> ownerList) {
		File shipDirectL = new File(shipDirectory);
		String[] conChosen = shipDirectL.list();
		// System.out.println(conChosen);
		for (int i = 0; i < conChosen.length; i++) {
			if (conChosen[i].endsWith(".XML")||conChosen[i].endsWith(".xml")||conChosen[i].endsWith(".Xml")) {
				System.out.println(conChosen[i]);
				try {
					File userFile = new File(shipDirectory + '/' + conChosen[i]);
					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(userFile);
					NodeList nList = doc.getElementsByTagName("ship");
					NodeList cList = doc.getElementsByTagName("container");

					Ship owner = null;
					BasicContainer shipContainer = null;
					
				
					for (int sFile = 0; sFile < nList.getLength(); sFile++) {
						Node nNode = nList.item(sFile);

						System.out.println("\nCurrent Element chosen: " + nNode.getNodeName());
						if (nNode.getNodeType() == Node.ELEMENT_NODE) {
							Element eElement = (Element) nNode;
							System.out.println("Name of ship: " + eElement.getAttribute("name"));
							System.out.println("Owner of ship: "
									+ eElement.getElementsByTagName("owner").item(0).getTextContent());
							System.out.println("Ship worth in billions: "
									+(int)Double.parseDouble(eElement.getElementsByTagName("value_in_billion").item(0).getTextContent()));
							System.out.println("Ship's company:"
									+ eElement.getElementsByTagName("company").item(0).getTextContent());

							owner = new Ship(eElement.getAttribute("name"),
									eElement.getElementsByTagName("owner").item(0).getTextContent(),
									eElement.getElementsByTagName("company").item(0).getTextContent(),
									(int)Double.parseDouble(eElement.getElementsByTagName("value_in_billion").item(0).getTextContent().trim()),
									i+1, shipDirectory + "\\" + conChosen[i]);
						}
						ownerList.add(owner);
					}

					String name = null;
					int maxLoad;
					int price = 0;

					for (int sFile = 0; sFile < cList.getLength(); sFile++) {
						price = 0;
						Node nNode = cList.item(sFile);

						System.out.println("\n Current Element chosen: " + nNode.getNodeName());
						if (nNode.getNodeType() == Node.ELEMENT_NODE) {
							Element eElement = (Element) nNode;
							System.out
									.println("Name of ship's container: " + (eElement.getAttribute("type").length() > 0
											? eElement.getAttribute("type") : eElement.getAttribute("type_ID")));

							name = (eElement.getAttribute("type").length() > 0 ? eElement.getAttribute("type")
									: eElement.getAttribute("type_ID"));

							if (eElement.getAttribute("type").equals("all")) {
								System.out.println("Ship's container max load is: "
										+ (int)Double.parseDouble(
												eElement.getElementsByTagName("max_load").item(0).getTextContent()));
								maxLoad = (int)Double.parseDouble(
										eElement.getElementsByTagName("max_load").item(0).getTextContent().trim());

								shipContainer = new AllContainer(maxLoad);
							} 
							else if (eElement.getAttribute("type").equals("basic")) //Because whatever all maxload is, basic will always equal to all
							{
								System.out.println("Ship's container max load is: "
										+ (int)Double.parseDouble(
												eElement.getElementsByTagName("max_load").item(0).getTextContent()));
								System.out.println("Ship's container price is: " +(int)Double.parseDouble(eElement.getElementsByTagName("price_per_container").item(0)
										.getTextContent()));
								maxLoad = (int)Double.parseDouble(
										eElement.getElementsByTagName("max_load").item(0).getTextContent().trim());
								price = (int)Double.parseDouble(eElement.getElementsByTagName("price_per_container").item(0)
										.getTextContent().trim());

								shipContainer = new BasicContainer(maxLoad, price);
							} 
							else if (eElement.getAttribute("type").equals("heavy")) 
							{
								System.out.println("Ship's container max load is: "
										+ (int)Double.parseDouble(
												eElement.getElementsByTagName("max_load").item(0).getTextContent()));
								System.out.println("Ship's container price is: " +(int)Double.parseDouble(eElement.getElementsByTagName("price_per_container").item(0)
										.getTextContent()));
								maxLoad = (int)Double.parseDouble(
										eElement.getElementsByTagName("max_load").item(0).getTextContent().trim());
								price = (int)Double.parseDouble(eElement.getElementsByTagName("price_per_container").item(0)
										.getTextContent().trim());


								shipContainer = new HeavyContainer(maxLoad, price);
							} 
							else if (eElement.getAttribute("type").equals("special")
									|| eElement.getAttribute("type_ID").equals("special")) 
							{
								System.out.println("Ship's container max load is: "
										+ (int)Double.parseDouble(
												eElement.getElementsByTagName("max_load").item(0).getTextContent()));
								maxLoad = (int)Double.parseDouble(
										eElement.getElementsByTagName("max_load").item(0).getTextContent().trim());

								shipContainer = new SpecialContainer(maxLoad);
								Ship o = (Ship) owner;
								o.setSpecial(true);
							} 
							else if (eElement.getAttribute("type").equals("refrigerated")) 
							{
								System.out.println("Ship's container max load is: "
										+ (int)Double.parseDouble(
												eElement.getElementsByTagName("max_load").item(0).getTextContent()));
								maxLoad = (int)Double.parseDouble(
										eElement.getElementsByTagName("max_load").item(0).getTextContent().trim());

								NodeList check = eElement.getElementsByTagName("price_per_container");
								if (check.getLength() > 0) {
									System.out.println("Ship's container price is: " +(int)Double.parseDouble(eElement.getElementsByTagName("price_per_container").item(0)
											.getTextContent()));
									price = (int)Double.parseDouble(eElement.getElementsByTagName("price_per_container")
											.item(0).getTextContent().trim());

									shipContainer = new RefrigeratedContainer(maxLoad, price);
								} else
									shipContainer = new RefrigeratedContainer(maxLoad);
							}
						}
						// containerList.add(shipContainer); //Additional stuff
						owner.addContainer(name, shipContainer);
					}

				} catch (Exception e) {
					System.out.println("Error found!Wrong folder selected!PLease choose 'ships' folder!");
					return false;
				}
			}
		}
		return true;
	}

}
