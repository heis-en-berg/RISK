package com.java.controller.map;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import com.java.model.map.Continent;
import com.java.model.map.Country;
import com.java.model.map.GameMap;

/**
 * @author Karan Dhingra
 * TODO: validate connectivity
 */
public class MapLoader {

	GameMap map = new GameMap();
	Scanner scanner;
	public static final String DEFAULT_MAP_FILE_PATH = "./map/finalMap.map";

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
		} else if (defaultMapChoice == 2) {
			String userMapFilePath = null;

			do {
				userMapFilePath = getAndValidateUserMapFilePath();
			} while (userMapFilePath == null);

			while (!loadMapFromFile(userMapFilePath)) {
				System.out.println("Error in file content");
				getChoiceToUseDeafaultMap();
			}
			System.out.println("Map Loaded successfully");
		} else {
			MapCreator mapCreator = new MapCreator(map);
			map = mapCreator.createMap();
			if(map == null) {
				map = new GameMap();
				return loadMap();
			}
			
		}
		Integer editOrContinueChoice = 0;
		
		do {
			editOrContinueChoice = getChoiceToContinueOrEditMap();
			if (editOrContinueChoice == 1) {
				MapEditor mapEditor = new MapEditor(map);
				map = mapEditor.editMap();
			}
		} while(editOrContinueChoice != 2);
		
		saveMapAsTextFile();
		
		return map;
	}

	private Integer getChoiceToContinueOrEditMap() {
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

		return choice;
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
	
	private String nextLine(BufferedReader mapFileBufferedReader) throws IOException {
		String currentLine = null;
		
		do {
			currentLine = mapFileBufferedReader.readLine();
		} while(currentLine != null && currentLine.length() == 0);
		
		if(currentLine != null) {
			currentLine = currentLine.trim();
		}
		
		return currentLine;
	}

	private Boolean loadMapFromFile(String mapFilePath) {

		Boolean response = true;
		BufferedReader mapFileBufferedReader = null;

		MapValidator mapValidator = new MapValidator();
		try {
			if(!mapValidator.validateMapTextFile(mapFilePath)) {
				return false;
			}
		} catch (IOException e2) {
			System.out.println(e2.getMessage());
			return false;
		}
		
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
			if(!mapValidator.validateMap(map)) {
				return false;
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

		if ((currentLine = nextLine(mapFileBufferedReader)) != null) {
			currentLine = currentLine.trim();

			if (!currentLine.equals("[Map]")) {
				response = false;
			} else {

				if ((currentLine = nextLine(mapFileBufferedReader)) != null) {
					splitString = currentLine.split("=");
					map.setMapAuthor(splitString[1]);
				} else {
					response = false;
				}

				if ((currentLine = nextLine(mapFileBufferedReader)) != null) {
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

		if ((currentLine = nextLine(mapFileBufferedReader)) != null) {
			String tag = currentLine.trim();
			if (!tag.equals("[Continents]")) {
				response = false;
			} else {
				while ((currentLine = nextLine(mapFileBufferedReader)) != null
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

		if ((currentLine = nextLine(mapFileBufferedReader)) != null) {

			while (currentLine != null) {
				territoryTextData.add(currentLine);
				splitString = currentLine.split(",");
				String currentTerritory = splitString[0];
				String currentTerritoryContinent = splitString[1];
				map.addCountry(currentTerritory, currentTerritoryContinent);
				currentLine = nextLine(mapFileBufferedReader);
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
	
	private void saveMapAsTextFile() {
		File file = new File("./map/finalMap.map");
		HashMap<String, Continent> continents;
		HashMap<String, Country> countries;
		
		if(file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write("[Map]\n");
			writer.write("author=" + map.getMapAuthor() + "\n");
			writer.write("warn=" + map.warn + "\n");
			
			writer.write("[Continents]\n");
			continents = map.getAllContinents();
			for(String continentName : continents.keySet()) {
				writer.write(continentName + "=" + continents.get(continentName).getContinentControlValue() + "\n");
			}
			
			writer.write("[Territories]\n");
			countries = map.getAllCountries();
			for(String countryName : countries.keySet()) {
				writer.write(countryName + "," + map.getCountry(countryName).getCountryContinentName());
				HashSet<String> neighbours = map.getAdjacentCountries(countryName);
				for(String neighbour : neighbours) {
					writer.write("," + neighbour);
				}
				writer.write("\n");
			}
			
			writer.flush();
			writer.close();
			
		} catch (IOException e) {
			System.out.println("ERROR: Failure in map file creation");
		}
	}

}
