package com.java.view;

import com.java.controller.map.MapLoader;
import com.java.controller.startup.StartUpPhase;
import com.java.model.gamedata.GameData;
import com.java.model.map.Country;
import com.java.model.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

/**
 * RiskGameDriver class is used as a view in which the user will interact with a given console.
 * It allows the risk game to start by loading the map selected by user from console.
 * Players then get to chose the number of players and the names to play, system provides an id based on that amount.
 * In roundrobin fashion each player will be assigned a country at random.
 * Each player will be able to place their armies on the assigned country based on the army total.
 *
 * @author Arnav Bhardwaj
 * @author Karan Dhingra
 * @author Ghalia Elkerdi
 * @author Sahil Singh Sodhi
 * @author Cristian Rodriguez 
 * @version 1.0.0
 */
public class RiskGameDriver {

	private GameData gameData;
	private StartUpPhase startUp;
	Scanner input = new Scanner(System.in);

    /**
     * Constructor will allow to load the map from user selection.
     * It would assign the parsed values from the map and store it in gameData object to use.
     */
	public RiskGameDriver() {
		gameData = new GameData();
		// using this will load the map
		MapLoader maploader = new MapLoader();
		gameData.gameMap = maploader.loadMap();
	}

    /**
     * Begins the console interface by initating the startup
     */
	public void startGame() {
		// Call the helpers here
		initiateStartUpPhase();
		initiateRoundRobin();
		ramdomAssignationOfCountries();
		initialArmyPlacement();
	}
	
	/**
     * The Start up phase intiates in this method by calling the helpers from StartUp phase. This method asks
     * for the names of the players and the number of players.
     */
	private void initiateStartUpPhase() {
		
		// Need to call the controller start uphase
		startUp = new StartUpPhase(gameData);
		int numOfPlayer = 0;
		String numOfPlayerStr = "";
		
		// Retrives the number of players provided by the user.
		do {

			System.out.println("\nNote: You can only have players between 2 to " + Math.min(GameData.MAX_PLAYERS, gameData.gameMap.getNumberOfCountries()));
			System.out.println("Enter the number of players: ");

			numOfPlayerStr = input.nextLine();

		} while (isNaN(numOfPlayerStr)
                || Integer.parseInt(numOfPlayerStr) < GameData.MIN_PLAYERS
				|| Integer.parseInt(numOfPlayerStr) > Math.min(GameData.MAX_PLAYERS, gameData.gameMap.getNumberOfCountries()));
		
		// Stores the number of players in game data.
		numOfPlayer = Integer.parseInt(numOfPlayerStr);
		gameData.setNoOfPlayers(numOfPlayer);
		
		// Array list to store the name of the players provided by the user.
		ArrayList<String> playerNames = new ArrayList<String>();
		
		// Asks the name of each player.
		for (int i = 0; i < numOfPlayer; i++) {
			
			String playerNameInput = "";
			
			while(playerNameInput == null || playerNameInput.length() == 0) {
				System.out.println("\nPlayer " + (i + 1));
				System.out.println("Enter your name: ");
				playerNameInput = input.nextLine().trim();
			}
			
			playerNames.add(playerNameInput.trim());
		}
		
		// Array list to store the name of the players provided by the user.
		startUp.generatePlayers(playerNames);
	}
	
	/**
     * Every country owned by a player start at least with one army at the beginning, then the player can choose where to put
     * the remaining armies.
     */
	private void initialArmyPlacement() {
		
		// Calculation of initial army
		Integer numberOfArmiesAvailablePerPlayer;
		System.out.println("Calculation of initial armies done....");

		// Gets all countries from game map.
		HashMap<String, Country> countryObjects = gameData.gameMap.getAllCountries();

		for (Player player : gameData.getPlayers()) {

			// Gets all countries from game map.
			Boolean firstTime = true;
			HashSet<String> countriesPerPlayer = gameData.gameMap.getConqueredCountriesPerPlayer(player.getPlayerID());
			String[] countriesPerPlayerArray = Arrays.copyOf(countriesPerPlayer.toArray(), countriesPerPlayer.size(),String[].class);
			numberOfArmiesAvailablePerPlayer = startUp.initialArmyCalculation(gameData.getNoOfPlayers());
			
			// Displays the number of armies per player.
			System.out.println(" ");
			System.out.println("Number of Armies per player: " + numberOfArmiesAvailablePerPlayer);

			while (numberOfArmiesAvailablePerPlayer > 0) {
				
				// Displays the lists of countries owned by a player.
				System.out.println("Player: " + player.getPlayerName() + " owns the following countries: ");

				// At the beggining every country has at least one army.
				for (int i = 0; i < countriesPerPlayerArray.length; i++) {

					String countryName = countriesPerPlayerArray[i];

					if (firstTime) {
						countryObjects.get(countryName).addArmy(1);
						numberOfArmiesAvailablePerPlayer--;
					}

					System.out.println("\t " + i + ": " + countryName + " has " + countryObjects.get(countryName).getCountryArmyCount() + " armies placed.");
				}

				firstTime = false;
				
				String chosedCountryByUserStr = "";
				
				// The user chooses a country and place the army
				do {
					System.out.println("\nYou have " + numberOfArmiesAvailablePerPlayer + " armies left.");
					System.out.println("Please pick the number associated with the country in order to place your armies: ");

					chosedCountryByUserStr = input.nextLine();

				} while (isNaN(chosedCountryByUserStr) 
						|| Integer.parseInt(chosedCountryByUserStr) > countriesPerPlayerArray.length - 1
						|| Integer.parseInt(chosedCountryByUserStr) < 0);
				
				int chosedCountryByUser = Integer.parseInt(chosedCountryByUserStr);
				String countryName = countriesPerPlayerArray[chosedCountryByUser];
				
				String numberOfArmiesByUserStr = "";
				
				// The user chooses how many army wants to put in a choosed country.
				do {

					System.out.println("Player: " + player.getPlayerName() + " How many armies do you want to place in " + countryName + "?");

					numberOfArmiesByUserStr = input.nextLine();

				} while (isNaN(numberOfArmiesByUserStr)
						|| Integer.parseInt(numberOfArmiesByUserStr) > numberOfArmiesAvailablePerPlayer
						|| Integer.parseInt(numberOfArmiesByUserStr) < 1);
				
				
				int numberOfArmiesByUser = Integer.parseInt(numberOfArmiesByUserStr);

				//input.close();
				
				// Adds the army to the country.
				Country selectedCountry = countryObjects.get(countryName);
				selectedCountry.addArmy(numberOfArmiesByUser);
				numberOfArmiesAvailablePerPlayer = numberOfArmiesAvailablePerPlayer - numberOfArmiesByUser;
			}
		}

	}
	
	/**
     * Calls the start up generate round robin helper to generate the order to play. Then displays the order of play.
     */
	private void initiateRoundRobin() {
		System.out.println(" ");
		System.out.println("The following list has the order of the players in round robin fashion: ");
		System.out.println("The results are based on the higher number of dice a player roled ");
		ArrayList<Player> results = startUp.generateRoundRobin();
		for(int i = 0; i < results.size(); i++) {
			System.out.println((i+1) + " " + results.get(i).getPlayerName());
		}
	}
	
	/**
     * Calls the assign countries to players from the controller.
     */
	private void ramdomAssignationOfCountries() {
		// Country Assignment begins
		startUp.assignCountriesToPlayers();
	}
	
	/**
     * Helper method to test if a given strin can be converted to a int.
     */
	private boolean isNaN(final String string) {
		try {
			Integer.parseInt(string);
		} catch (final Exception e) {
			System.out.println("Invalid Input");
			return true;
		}
		return false;
	}
	
	private void initiateRoundRobinBasedGamePlay() {
		// TODO Auto-generated method stub
	}

	private void startTurn() {
		// TODO Auto-generated method stub
	}
	

}
