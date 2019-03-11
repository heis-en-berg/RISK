package com.java.view;

import com.java.controller.gameplay.Turn;
import com.java.controller.map.MapLoader;
import com.java.controller.startup.StartUpPhase;
import com.java.model.gamedata.GameData;
import com.java.model.map.Country;
import com.java.model.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Arrays;


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
		startTurn();
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
		startUp.generateCardsDeck();
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

				// At the begining every country has at least one army.
				for (int i = 0; i < countriesPerPlayerArray.length; i++) {

					String countryName = countriesPerPlayerArray[i];

					if (firstTime) {
						countryObjects.get(countryName).addArmy(1);
						numberOfArmiesAvailablePerPlayer--;
					}

					System.out.println("\t " + i + ": " + countryName + " has " + countryObjects.get(countryName).getCountryArmyCount() + " armies placed.");
				}

				firstTime = false;

				String chosedCountryByUser = "";
				do {
					System.out.println("\nYou have " + numberOfArmiesAvailablePerPlayer + " armies left.");
					System.out.println("Please pick the number associated with the country in order to place your armies: ");
					chosedCountryByUser = input.nextLine();
					if(isNaN(chosedCountryByUser) || Integer.parseInt(chosedCountryByUser) < 0 
							|| Integer.parseInt(chosedCountryByUser) >= countriesPerPlayerArray.length) {
						System.out.println("Invalid Input!!");
					}
				} while(isNaN(chosedCountryByUser) || Integer.parseInt(chosedCountryByUser) < 0 
						|| Integer.parseInt(chosedCountryByUser) >= countriesPerPlayerArray.length);
				
				String countryName = countriesPerPlayerArray[Integer.parseInt(chosedCountryByUser)];

				String numberOfArmiesByUser = "";
				
				do {
					System.out.println("Player: " + player.getPlayerName() + " How many armies do you want to place in "
							+ countryName + "?");
					numberOfArmiesByUser = input.nextLine();

					if(isNaN(numberOfArmiesByUser) || Integer.parseInt(numberOfArmiesByUser) < 0 
							|| Integer.parseInt(numberOfArmiesByUser) > numberOfArmiesAvailablePerPlayer) {
						System.out.println("Invalid input!!");
					}

				} while(isNaN(numberOfArmiesByUser) || Integer.parseInt(numberOfArmiesByUser) < 0
						|| Integer.parseInt(numberOfArmiesByUser) > numberOfArmiesAvailablePerPlayer);

				Country selectedCountry = countryObjects.get(countryName);
				selectedCountry.addArmy(Integer.parseInt(numberOfArmiesByUser));
				numberOfArmiesAvailablePerPlayer -= Integer.parseInt(numberOfArmiesByUser);
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
     * @param stringInput determines if the string typed by user is an integer
     * @return the evaluation of true if it is an integer or false otherwise
     */
	private boolean isNaN(final String stringInput) {
		try {
			Integer.parseInt(stringInput);

		} catch (final Exception e) {
			return true;
		}
		return false;
	}

	/**
	 * There will be another round if the number of players is greater than one.
	 */
	private void startTurn() {

		Turn turn;
		ArrayList<Player> playerList = this.gameData.getPlayers();
		Player currentPlayer;
		int reset_turn = 0;
		int player;

		//There will be another round if the number of players is greater than one.
		while(playerList.size() != 1) {
			for (player = reset_turn; player < playerList.size(); ) {
				currentPlayer = playerList.get(player);
				// Exclude turn for the player with no countries. The player has been defeated.
				if (this.gameData.gameMap.getConqueredCountriesPerPlayer(player) != null) {
					System.out.println("\n***** Turn Begins for player "+currentPlayer.getPlayerName() +" *****\n");
					turn = new Turn(currentPlayer, this.gameData);
					turn.startTurn();
					System.out.println("\n***** Turn Ends for player "+currentPlayer.getPlayerName() +" *****\n");
					player++;
				} else {
					this.gameData.removePlayers(currentPlayer);
					playerList = this.gameData.getPlayers();
				}
			}
			// Reset turn for player one.
			player = reset_turn;
		}

		//The one player left in the playerList has conquered the whole map.
		System.out.println("Congratulations! "+playerList.get(0).getPlayerName() + " wins the game.");
	}
}
