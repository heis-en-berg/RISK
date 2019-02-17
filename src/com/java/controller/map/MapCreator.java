package com.java.controller.map;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.java.model.map.Continent;
import com.java.model.map.Country;
import com.java.model.map.GameMap;


/**
 * 
 * @author Karan Dhingra
 *
 */
public class MapCreator {

	GameMap map = new GameMap();
	Scanner scanner;
	public static final String DEFAULT_MAP_FILE_PATH = "./map/default.map";
	
	public MapCreator() {
		scanner = new Scanner(System.in);
	}

	public GameMap loadMap() {

		String defaultMapChoice = "";

		while (!defaultMapChoice.equalsIgnoreCase("y") && !defaultMapChoice.equalsIgnoreCase("n")) {
			defaultMapChoice = getChoiceToUseDeafaultMap();
		}

		if (defaultMapChoice.equalsIgnoreCase("y")) {
			while (!loadMapFromFile(DEFAULT_MAP_FILE_PATH)) {

			}
		}
		return map;
	}

	private String getChoiceToUseDeafaultMap() {
		System.out.println("Would you like to load the default map? (y/n)");
		String choice = scanner.nextLine();
		if (!choice.equalsIgnoreCase("y") && !choice.equalsIgnoreCase("n")) {
			System.out.println("Invalid input");
		}
		return choice;
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
					Continent continent = new Continent(splitString[0], Integer.parseInt(splitString[1]));
					map.addContinent(continent);
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
				Country country = new Country(currentTerritory, currentTerritoryContinent);
				map.addCountry(country);
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
