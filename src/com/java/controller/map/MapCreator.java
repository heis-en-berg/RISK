package com.java.controller.map;

import com.java.model.map.GameMap;

public class MapCreator extends MapEditor {

	public MapCreator(GameMap map) {
		super(map);
	}

	public GameMap createMap() {
		
		Integer userChoice = 0;
		do {
			userChoice = getCreateMapUserChoice();
		} while(userChoice < 1 || userChoice > 11);
		
		switch (userChoice) {
			case 1: addMapAuthor();
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
			 		 return null;
			case 11: if(mapValidator.validateMap(editedMap)) {
						return editedMap;
					 }
					 break;
		}
		
		return createMap();
	}

	private Integer getCreateMapUserChoice() {
		Integer userChoice = 0;
		System.out.println("\nCreate a new Map: \n1. Add Author\n2. Add a Continent\n3. Remove a Continent\n"
				+ "4. Add a Country\n5. Remove a Country\n6. Add Adjacency\n7. Remove Adjacency\n"
				+ "8. Show Map Content\n9. Validate Map\n10. Discard changes and Go back\n11. Save and Exit");
		System.out.print("Enter choice: ");
		try {
			userChoice = Integer.parseInt(scanner.nextLine());
		} catch(NumberFormatException e) {
			System.out.println("Invalid input!!");
			userChoice = 0;
		}
		return userChoice;
	}
	
	private void addMapAuthor() {
		System.out.print("Please Enter the name of the author: ");
		String mapAuthorName = scanner.nextLine().trim();
		if(mapAuthorName.isEmpty() || mapAuthorName.length() == 0) {
			System.out.println("Invalid Name");
			return;
		}
		editedMap.setMapAuthor(mapAuthorName);
		System.out.println("Author: " + mapAuthorName);
	}
	
	
}
