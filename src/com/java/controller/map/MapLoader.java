package com.java.controller.map;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.java.model.map.GameMap;

/**
 * 
 * @author Karan Dhingra
 *
 */
public class MapLoader {

	GameMap map = new GameMap();
	Scanner scanner;
	public static final String DEFAULT_MAP_FILE_PATH = "./map/default.map";

	public MapLoader() {
		scanner = new Scanner(System.in);
	}

	public GameMap loadMap() {

		Integer defaultMapChoice = null;

		while (defaultMapChoice == null) {
			defaultMapChoice = getChoiceToUseDeafaultMap();
		}

		if (defaultMapChoice == 1) {
			loadMapFromFile(DEFAULT_MAP_FILE_PATH);
			getChoiceToContinueOrEditMap();
		} else if (defaultMapChoice == 2) {
			String userMapFilePath = null;

			do {
				userMapFilePath = getAndValidateUserMapFilePath();
			} while (userMapFilePath == null);

			while (!loadMapFromFile(userMapFilePath)) {
				System.out.println("Error in file content");
				getAndValidateUserMapFilePath();
			}
			System.out.println("Map Loaded successfully");
			getChoiceToContinueOrEditMap();
		} else {
			MapCreator mapCreator = new MapCreator(map);
			mapCreator.createMap();
		}
		
		scanner.close();
		return map;
	}

	private void getChoiceToContinueOrEditMap() {
		Integer choice = null;
		do {
			System.out.println();
			System.out.println("Options");
			System.out.println("1. Edit map");
			System.out.println("2. Continue");
			System.out.println();
			System.out.print("Enter choice: ");
			choice = scanner.nextInt();
			if (!(choice == 1 || choice == 2)) {
				System.out.println("Invalid Input");
			}
		} while (!(choice == 1 || choice == 2));

		if (choice == 1) {
			MapEditor mapEditor = new MapEditor(map);
			mapEditor.editMap();
		}
	}

	private Integer getChoiceToUseDeafaultMap() {
		System.out.println("Options");
		System.out.println("1. Load default map");
		System.out.println("2. Load your own map");
		System.out.println("3. Create map");
		System.out.println();
		System.out.print("Enter choice: ");
		Integer choice = scanner.nextInt();

		if (!(choice >= 1 && choice <= 3)) {
			System.out.println("Invalid input");
			return null;
		}
		return choice;
	}

	private String getAndValidateUserMapFilePath() {
		System.out.println();
		System.out.print("Enter File Path: ");
		String filePath = null;
		while ((filePath = scanner.nextLine()).isEmpty()) {
			if (!filePath.equals("")) {
				break;
			}
		}
		if (!new File(filePath).exists()) {
			System.out.println("Ooooops, File not found. Try Again.");
			return null;
		}
		return filePath;
	}

	private Boolean loadMapFromFile(String mapFilePath) {

		Boolean response = true;
		BufferedReader mapFileBufferedReader = null;

		try {
			mapFileBufferedReader = new BufferedReader(new FileReader(mapFilePath));
		} catch (FileNotFoundException e1) {
			System.out.println("File not Found");
			return false;
		}

		try {
			response = readMapMetaData(mapFileBufferedReader);
			if (response) {
				response = readandLoadContinents(mapFileBufferedReader);
			}
			if (response) {
				response = readandLoadCountries(mapFileBufferedReader);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}

	private Boolean readMapMetaData(BufferedReader mapFileBufferedReader) throws IOException {
		Boolean response = true;
		String[] splitString;
		String currentLine = "";

		if ((currentLine = mapFileBufferedReader.readLine()) != null) {
			currentLine = currentLine.trim();

			if (!currentLine.equals("[Map]")) {
				response = false;
			} else {

				if ((currentLine = mapFileBufferedReader.readLine()) != null) {
					splitString = currentLine.split("=");
					map.setMapAuthor(splitString[1]);
				} else {
					response = false;
				}

				if ((currentLine = mapFileBufferedReader.readLine()) != null) {
					splitString = currentLine.split("=");
					map.warn = splitString[1];
				} else {
					response = false;
				}
			}
		}
		return response;
	}

	private Boolean readandLoadContinents(BufferedReader mapFileBufferedReader) throws IOException {

		Boolean response = true;
		String[] splitString;
		String currentLine = "";

		if ((currentLine = mapFileBufferedReader.readLine()) != null) {
			String tag = currentLine.trim();
			if (!tag.equals("[Continents]")) {
				response = false;
			} else {
				while ((currentLine = mapFileBufferedReader.readLine()) != null
						&& !currentLine.equals("[Territories]")) {
					splitString = currentLine.split("=");
					map.addContinent(splitString[0], Integer.parseInt(splitString[1]));
				}
			}
		}

		return response;
	}

	private Boolean readandLoadCountries(BufferedReader mapFileBufferedReader)
			throws NumberFormatException, IOException {

		Boolean response = true;
		String[] splitString;
		String currentLine = "";
		ArrayList<String> territoryTextData = new ArrayList<>();

		if ((currentLine = mapFileBufferedReader.readLine()) != null) {

			while (currentLine != null) {
				territoryTextData.add(currentLine);
				splitString = currentLine.split(",");
				String currentTerritory = splitString[0];
				String currentTerritoryContinent = splitString[1];
				map.addCountry(currentTerritory, currentTerritoryContinent);
				currentLine = mapFileBufferedReader.readLine();
			}

			for (int i = 0; i < territoryTextData.size(); i++) {
				splitString = territoryTextData.get(i).split(",");
				for (int j = 2; j < splitString.length; j++) {
					String adjacentCountry = splitString[j];
					map.setAdjacentCountry(splitString[0], adjacentCountry);
				}
			}

		} else {
			response = false;
		}

		return response;
	}

}
