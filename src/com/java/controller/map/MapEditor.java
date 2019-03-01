package com.java.controller.map;

import java.util.Scanner;

import com.java.model.map.GameMap;

public class MapEditor {

	GameMap originalMap;
	GameMap editedMap;
	Scanner scanner;
	MapValidator mapValidator;

	public MapEditor(GameMap map) {
		this.originalMap = map;
		this.editedMap = originalMap.clone();
		scanner = new Scanner(System.in);
		mapValidator = new MapValidator();
	}

	public GameMap editMap() {
		Integer userChoice = 0;
		do {
			userChoice = getEditMapUserChoice();
		} while(userChoice < 1 || userChoice > 11);
		
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
			case 9: mapValidator.validateMap(editedMap);
					break;
			case 10: editedMap = originalMap.clone();
					 System.out.println("Changes discarded");
					 break;
			case 11: if(mapValidator.validateMap(editedMap)) {
						return editedMap;
					 }
					 break;
		}
		
		return editMap();
	}
	
	private Integer getEditMapUserChoice() {
		System.out.println("\nEdit map: \n1. Change Author\n2. Add a Continent\n3. Remove a Continent\n"
				+ "4. Add a Country\n5. Remove a Country\n6. Add Adjacency\n7. Remove Adjacency\n"
				+ "8. Show Map Content\n9. Validate Map\n10. Discard changes\n11. Save and Exit");
		System.out.print("Enter choice: ");
		
		String userChoiceStr = scanner.nextLine();
		if(isNaN(userChoiceStr) || Integer.parseInt(userChoiceStr) < 1 || Integer.parseInt(userChoiceStr) > 11) {
			System.out.println("Invalid input!!");
			userChoiceStr = "0";
		}
		return Integer.parseInt(userChoiceStr);
	}

	protected void showMapContent() {
		//TODO : Print with details and remove method from model
		editedMap.printMap();
	}

	protected void removeAdjacenecyBetweenCountries() {
		String countryName1 = null;
		String countryName2 = null;
		System.out.println("Please enter the names of the countries to be disconnected: ");
		System.out.print("Country : ");
		countryName1 = scanner.nextLine().trim();
		if(editedMap.getCountry(countryName1) == null) {
			System.out.println(countryName1 + " doesn't exist in the map.");
			return;
		}
		System.out.print("Country : ");
		countryName2 = scanner.nextLine().trim();
		if(editedMap.getCountry(countryName2) == null) {
			System.out.println(countryName2 + " doesn't exist in the map.");
			return;
		}
		if(!editedMap.removeAdjacenyBetweenCountries(countryName1, countryName2)) {
			System.out.println("No edge extsts between the countries");
			return;
		}
		System.out.println("Edge removed successfully");
	}

	protected void addAdjacenecyBetweenCountries() {
		String countryName1 = null;
		String countryName2 = null;
		System.out.println("Please enter the names of the countries to be connected: ");
		System.out.print("Country : ");
		countryName1 = scanner.nextLine().trim();
		if(editedMap.getCountry(countryName1) == null) {
			System.out.println(countryName1 + " doesn't exist in the map.");
			return;
		}
		System.out.print("Country : ");
		countryName2 = scanner.nextLine().trim();
		if(editedMap.getCountry(countryName2) == null) {
			System.out.println(countryName2 + " doesn't exist in the map.");
			return;
		}
		editedMap.setAdjacentCountry(countryName1, countryName2);
		System.out.println("Edge added successfully");
	}

	protected void removeCountryFromMap() {
		String countryName = null;
		System.out.print("Please Enter the name of the Country to be removed: ");
		countryName = scanner.nextLine().trim();
		if(editedMap.getCountry(countryName) == null) {
			System.out.println(countryName + " doesn't exist in the map.");
			return;
		}
		editedMap.removeCountry(countryName);
		System.out.println("Country removed successfully");
	}

	protected void addCountryToMap() {
		System.out.print("Please Enter the name of the new Country: ");
		String newCountryName = scanner.nextLine().trim();
		if(editedMap.getCountry(newCountryName) != null) {
			System.out.println("Country already exists");
			return;
		}
		String continentName = null;
		System.out.print("Please Enter the name of the Continent, that " + newCountryName + " belongs to: ");
		continentName = scanner.nextLine().trim();
		if(editedMap.getContinent(continentName) == null) {
			System.out.println(continentName + " doesn't exist in the map.");
			return;
		}
		editedMap.addCountry(newCountryName, continentName);
		System.out.println("Country added successfully");
	}

	protected void removeContinentFromMap() {
		
		String continentName = null;
		System.out.print("Please Enter the name of the Continent to be removed: ");
		continentName = scanner.nextLine().trim();
		if(editedMap.getContinent(continentName) == null) {
			System.out.println(continentName + " doesn't exist in the map.");
			return;
		}
		editedMap.removeContinent(continentName);
		System.out.println("Continent removed successfully");
	}

	protected void addContinenToMap() {
		System.out.print("Please Enter the name of the new Continent: ");
		String newContinentName = scanner.nextLine().trim();
		if(editedMap.getContinent(newContinentName) != null) {
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
		editedMap.addContinent(newContinentName, continentControlValue);
		System.out.println("Continent added successfully");
	}

	protected void changeMapAuthor() {
		System.out.println("Current Map Author: " + editedMap.getMapAuthor());
		System.out.print("Please Enter the name of the author: ");
		String mapAuthorName = scanner.nextLine().trim();
		if(mapAuthorName.isEmpty() || mapAuthorName.length() == 0) {
			System.out.println("Invalid Name");
			return;
		}
		editedMap.setMapAuthor(mapAuthorName);
		System.out.println("Map Author updated to : " + editedMap.getMapAuthor());
	}
	
	protected boolean isNaN(final String string) {
		try {
			Integer.parseInt(string);
		} catch (final Exception e) {
			return true;
		}
		return false;
	}

}
