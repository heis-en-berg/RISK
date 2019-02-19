package com.java.controller.map;

import java.util.Scanner;

import com.java.model.map.Continent;
import com.java.model.map.GameMap;

public class MapEditor {

	GameMap map;
	Scanner scanner;

	public MapEditor(GameMap map) {
		this.map = map;
		scanner = new Scanner(System.in);
	}

	public void editMap() {
		Integer userChoice = 0;
		do {
			userChoice = getEditMapUserChoice();
		} while(userChoice < 1 || userChoice > 9);
		
		switch (userChoice) {
			case 1: changeMapAuthor();
					break;
			case 2: addContinenToMap();
					break;
			case 3: removeContinentFromMap();
					break;
			case 4: addCountryToMap();
					break;
			case 5: removeCountryFromMap();
					break;
			case 6: addAdjacenecyBetweenCountries();
					break;
			case 7: removeAdjacenecyBetweenCountries();
					break;
			case 8: showMapContent();
					break;
			case 9: if(validateMap()) {
						return;
					}
					break;
		}
		
		editMap();
	}
	
	private Integer getEditMapUserChoice() {
		Integer userChoice = 0;
		System.out.println("\nEdit map: \n1. Change Author\n2. Add a Continent\n3. Remove a Continent\n"
				+ "4. Add a Country\n5. Remove a Country\n6. Add Adjacency\n7. Remove Adjacency\n"
				+ "8. Show Map Content\n9. Save and Exit");
		System.out.println("Enter choice: ");
		try {
			userChoice = Integer.parseInt(scanner.nextLine());
		} catch(NumberFormatException e) {
			System.out.println("Enter a valid input!!");
			userChoice = 0;
		}
		return userChoice;
	}

	private void showMapContent() {
		//TODO : Print with details and remove method from model
		map.printMap();
	}

	private void removeAdjacenecyBetweenCountries() {
		String countryName1 = null;
		String countryName2 = null;
		System.out.println("Please enter the names of the countries to be disconnected: ");
		System.out.println("Country : ");
		countryName1 = scanner.nextLine().trim();
		if(map.getCountry(countryName1) == null) {
			System.out.println(countryName1 + " doesn't exist in the map.");
			return;
		}
		System.out.println("Country : ");
		countryName2 = scanner.nextLine().trim();
		if(map.getCountry(countryName2) == null) {
			System.out.println(countryName2 + " doesn't exist in the map.");
			return;
		}
		if(!map.removeAdjacenyBetweenCountries(countryName1, countryName2)) {
			System.out.println("No edge extsts between the countries");
			return;
		}
		System.out.println("Edge removed successfully");
	}

	private void addAdjacenecyBetweenCountries() {
		String countryName1 = null;
		String countryName2 = null;
		System.out.println("Please enter the names of the countries to be connected: ");
		System.out.println("Country : ");
		countryName1 = scanner.nextLine().trim();
		if(map.getCountry(countryName1) == null) {
			System.out.println(countryName1 + " doesn't exist in the map.");
			return;
		}
		System.out.println("Country : ");
		countryName2 = scanner.nextLine().trim();
		if(map.getCountry(countryName2) == null) {
			System.out.println(countryName2 + " doesn't exist in the map.");
			return;
		}
		map.setAdjacentCountry(countryName1, countryName2);
		System.out.println("Edge added successfully");
	}

	private void removeCountryFromMap() {
		String countryName = null;
		System.out.print("Please Enter the name of the Country to be removed: ");
		countryName = scanner.nextLine().trim();
		if(map.getCountry(countryName) == null) {
			System.out.println(countryName + " doesn't exist in the map.");
			return;
		}
		map.removeCountry(countryName);
		System.out.println("Country removed successfully");
	}

	private void addCountryToMap() {
		System.out.print("Please Enter the name of the new Country: ");
		String newCountryName = scanner.nextLine().trim();
		if(map.getCountry(newCountryName) != null) {
			System.out.println("Country already exists");
			return;
		}
		String continentName = null;
		System.out.print("Please Enter the name of the Continent, that " + newCountryName + " belongs to: ");
		continentName = scanner.nextLine().trim();
		if(map.getContinent(continentName) == null) {
			System.out.println(continentName + " doesn't exist in the map.");
			return;
		}
		map.addCountry(newCountryName, continentName);
		System.out.println("Country added successfully");
	}

	private void removeContinentFromMap() {
		
		String continentName = null;
		System.out.print("Please Enter the name of the Continent to be removed: ");
		continentName = scanner.nextLine().trim();
		if(map.getContinent(continentName) == null) {
			System.out.println(continentName + " doesn't exist in the map.");
			return;
		}
		map.removeContinent(continentName);
		System.out.println("Continent removed successfully");
	}

	private void addContinenToMap() {
		System.out.print("Please Enter the name of the new Continent: ");
		String newContinentName = scanner.nextLine().trim();
		if(map.getContinent(newContinentName) != null) {
			System.out.println("Continent already exists");
			return;
		}
		Integer continentControlValue = -1;
		do {
			try {
				System.out.print("Please Enter the control value of the Continent: " + newContinentName + ":");
				continentControlValue = Integer.parseInt(scanner.nextLine().trim());
				if(continentControlValue < 0) {
					System.out.println("Invalid Input.");
				}
			} catch(NumberFormatException e) {
				System.out.println("Invalid Input.");
				continentControlValue = -1;
			}
		} while(continentControlValue < 0);
		map.addContinent(newContinentName, continentControlValue);
		System.out.println("Continent added successfully");
	}

	private void changeMapAuthor() {
		System.out.println("Current Map Author: " + map.getMapAuthor());
		System.out.print("Please Enter the name of the author: ");
		String mapAuthorName = scanner.nextLine().trim();
		map.setMapAuthor(mapAuthorName);
		System.out.println("Map Author updated to : " + map.getMapAuthor());
	}
	
	private boolean validateMap() {
		// TODO Auto-generated method stub
		return true;
	}

}
