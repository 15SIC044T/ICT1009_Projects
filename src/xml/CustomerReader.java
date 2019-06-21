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

public class CustomerReader implements XMLReader {

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

	// This method load all the XML files to ArrayList (Container)
	public boolean loadXML(String containerDirectory, ArrayList<BasicObject> customerList) {
		File conDirectL = new File(containerDirectory);
		String[] conChosen = conDirectL.list();
		// System.out.println(conChosen);
		for (int i = 0; i < conChosen.length; i++) {
			if (conChosen[i].endsWith(".XML")||conChosen[i].endsWith(".xml")||conChosen[i].endsWith(".Xml")) {
				System.out.println(conChosen[i]);
				try {
					File userFile = new File(containerDirectory + '/' + conChosen[i]);
					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(userFile);
					NodeList custList = doc.getElementsByTagName("customer");
					NodeList custConList = doc.getElementsByTagName("container");

					Customer customer = null;
					BasicContainer containerInfo = null;

					String custName, company;
					int ID, age;

					for (int sFile = 0; sFile < custList.getLength(); sFile++) {
						Node nNode = custList.item(sFile);
						System.out.println("\n Current Element chosen: " + nNode.getNodeName());

						if (nNode.getNodeType() == Node.ELEMENT_NODE) {
							Element eElement = (Element) nNode;
							System.out.println("Customer's ID: " + (int)Double.parseDouble(eElement.getAttribute("ID")));
							System.out.println("Customer's Name: "
									+ eElement.getElementsByTagName("name").item(0).getTextContent());
							System.out.println(
									"Customer's age: " + (int)Double.parseDouble(eElement.getElementsByTagName("age").item(0).getTextContent()));
							System.out.println("Customer's company:"
									+ eElement.getElementsByTagName("company").item(0).getTextContent());

							ID = (int)Double.parseDouble(eElement.getAttribute("ID"));
							custName = eElement.getElementsByTagName("name").item(0).getTextContent();
							company = eElement.getElementsByTagName("company").item(0).getTextContent();
							age = (int)Double.parseDouble(eElement.getElementsByTagName("age").item(0).getTextContent().trim());

							customer = new Customer(ID, custName, company, age, containerDirectory + "\\" + conChosen[i]);
						}
						customerList.add(customer);
					}

					String special_prop = null, type = null;
					int num = 0;
					for (int sFile = 0; sFile < custConList.getLength(); sFile++) {
						Node nNode = custConList.item(sFile);

						System.out.println("\n Current Element chosen: " + nNode.getNodeName());

						if (nNode.getNodeType() == Node.ELEMENT_NODE) {
							Element eElement = (Element) nNode;
							System.out.println("Container's type: " + eElement.getAttribute("type_ID"));
							type = eElement.getAttribute("type_ID");
							if (eElement.getAttribute("type_ID").indexOf("basic") > -1) {
								System.out.println("Number of basic conatiners: "
										+ (int)Double.parseDouble(
												eElement.getElementsByTagName("number").item(0).getTextContent().trim()));
								num = (int)Double.parseDouble(
										eElement.getElementsByTagName("number").item(0).getTextContent().trim());

								if (eElement.getAttribute("type_ID").indexOf("special") > -1) {
									System.out.println("The special properties are: " + eElement
											.getElementsByTagName("special_property").item(0).getTextContent().trim());
									special_prop = eElement.getElementsByTagName("special_property").item(0)
											.getTextContent().trim();

									containerInfo = new BasicContainer(num, special_prop);
								} else
									containerInfo = new BasicContainer(num);
							} else if (eElement.getAttribute("type_ID").indexOf("heavy") > -1) {
								System.out.println("Number of heavy containers: "
										+ (int)Double.parseDouble(
												eElement.getElementsByTagName("number").item(0).getTextContent().trim()));
								num = (int)Double.parseDouble(
										eElement.getElementsByTagName("number").item(0).getTextContent().trim());

								if (eElement.getAttribute("type_ID").indexOf("special") > -1) {
									System.out.println("The special properties are: " + eElement
											.getElementsByTagName("special_property").item(0).getTextContent().trim());
									special_prop = eElement.getElementsByTagName("special_property").item(0)
											.getTextContent().trim();

									containerInfo = new HeavyContainer(num, special_prop);
								} else
									containerInfo = new HeavyContainer(num);
							} else if ((eElement.getAttribute("type_ID").indexOf("refrigerated")) > -1) {
								System.out.println("Number of refrigerated containers: "
										+ (int)Double.parseDouble(
												eElement.getElementsByTagName("number").item(0).getTextContent().trim()));
								num =(int)Double.parseDouble(
										eElement.getElementsByTagName("number").item(0).getTextContent().trim());

								if (eElement.getAttribute("type_ID").indexOf("special") > -1) {
									System.out.println("The special properties are: " + eElement
											.getElementsByTagName("special_property").item(0).getTextContent().trim());
									special_prop = eElement.getElementsByTagName("special_property").item(0)
											.getTextContent().trim();

									containerInfo = new RefrigeratedContainer(num, special_prop);
								} else
									containerInfo = new RefrigeratedContainer(num);
							}
						}
						// containerList.add(containerInfo); //Additional Stuff
						customer.addContainer(type, containerInfo);
					}
				} catch (Exception e) {
					System.out.println("Error found!Wrong folder selected!PLease choose 'containers' folder!");
					return false;
				}
			}
		}
		return true;
	}
}
