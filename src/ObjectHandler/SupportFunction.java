package ObjectHandler;
//Import Package
import Containers.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;
import java.util.*;


public class SupportFunction {

	// Print array ezpz
	public void printArrayLine(String[] al, String str) 
	{
		System.out.printf("%-10s", str);
		for (int i = 0; i < al.length; i++)
			System.out.printf("%-15s", al[i]);
		System.out.println("");
	}

	public void printArrayLine(ArrayList<Integer> al) 
	{
		for (int i = 0; i < al.size(); i++)
			System.out.printf("%-15s", (al.get(i) > -1 ? al.get(i) : "-"));
		System.out.println("");
	}

	public void printArrayLine(ArrayList<Integer> al, String str) 
	{
		System.out.printf("%-10s", str);
		for (int i = 0; i < al.size(); i++)
			System.out.printf("%-15s", (al.get(i) > -1 ? al.get(i) : "-"));
		System.out.println("");
	}
	
	// Takes an array and displays it as a menu with numbers 0 to length of array.
	public static void displayArrayAsMenu(String[] myArray, String intro) {
		System.out.println(intro);
		for (int i = 0; i < myArray.length; i++) 
			System.out.println(i+1 + ") " + myArray[i]);
		System.out.println("0) Back");
	}
	
	// Function to print out information and ask for integer input.
	public static int handleIntInput(String info) {
		SupportFunction sf = new SupportFunction();
		System.out.println(info);
		int input = sf.checkInputNum();
		return input;
	}
	
	// Function to print out information and ask for string input.
	public static String handleStringInput(String info) {
		SupportFunction sf = new SupportFunction();
		System.out.println(info);
		String input = sf.checkString();
		return input;
	}
	
	// Return a array of container types without the "all" type.
	public static String[] filterTypeList(HashMap<String, BasicContainer> map) {
		Set<String> typeSet = new HashSet<String>(map.keySet());
		typeSet.remove(new String("all"));
		String[] types = typeSet.toArray(new String[typeSet.size()]);
		return types;
	}
	
	// Adds leading 0s to numbers.
	public static int formatID(int id) {
		String newString = String.format("%03d", id);
		int formattedID = Integer.parseInt(newString);
		return formattedID;
	}
	
	// This method check the input integer (With >= 0)
	public int checkInputNum() 
	{
		while (true) 
		{
			try {
				Scanner input = new Scanner(System.in);
				int option = input.nextInt();

				if (option >= 0)
					return option;
				else
					System.out.print("Invalid option! Please enter option: ");

			} catch (InputMismatchException e) {
				System.out.print("Illegal option! Please enter option: ");
			}
		}
	}

	// This method check the input integer (With 0 to Max Option)
	public static int checkInputNum(int minOpt, int maxOpt) 
	{
		while (true) 
		{
			try 
			{
				Scanner input = new Scanner(System.in);
				int option = input.nextInt();

				if (option >= minOpt && option <= maxOpt)
					return option;
				else
					System.out.print("Invalid option! Please enter option: ");

			} catch (InputMismatchException e) 
			{
				System.out.print("Illegal option! Please enter option: ");
			}
		}
	}
	
	// Check input number with default value of 0
	public static int checkInputNum(int maxOpt) 
	{
		return checkInputNum(0, maxOpt);
	}

	
	// This method check for string
	public String checkString() 
	{
		while (true) 
		{
			Scanner input = new Scanner(System.in);
			String string = input.nextLine();
			if (!(string.matches("^[a-zA-Z\\s+]*")))
				System.out.print("Invalid string, please enter again: ");
			else
				return string;
		}
	}
		
	public static int getRandomDigitNumber(int minimum, int maximum) 
	{
		Random rand = new Random();
		int randomNum = minimum + rand.nextInt((maximum - minimum) + 1);
		return randomNum;
	}
	
	public static String getRandomSpecialProperty() 
	{
		String[] special = {"toxic", "explosive"};
		int rand = getRandomDigitNumber(1,3);
		if (rand < 3)
			return special[rand-1];
		else
			return "toxic,explosive";
	} 
	
	public static BasicContainer createShipContainerObject(String key, int maxLoad) 
	{
		switch (key) 
		{
		case "all":
			return new AllContainer(maxLoad);
		case "heavy":
			return new HeavyContainer(maxLoad);
		case "refrigerated":
			return new RefrigeratedContainer(maxLoad);
		case "special":
			return new SpecialContainer(maxLoad);
		default:
			return new BasicContainer(maxLoad);
		}
	}
	
	public static BasicContainer createCustomerContainerObject(String key, int maxLoad) 
	{
		String property;
		switch (key) 
		{
		case "all":
			return new AllContainer(maxLoad);
		case "basic":
			return new BasicContainer(maxLoad);
		case "basic_special":
			property = getRandomSpecialProperty();
			return new BasicContainer(maxLoad, property);
		case "heavy":
			return new HeavyContainer(maxLoad);
		case "heavy_special":
			property = getRandomSpecialProperty();
			return new HeavyContainer(maxLoad, property);
		case "refrigerated":
			return new RefrigeratedContainer(maxLoad);
		case "refrigerated_special":
			property = getRandomSpecialProperty();
			return new RefrigeratedContainer(maxLoad, property);
		case "special":
			return new SpecialContainer(maxLoad);
		default:
			return new BasicContainer(maxLoad);
		}
	}
}
