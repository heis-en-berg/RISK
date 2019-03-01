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
 * @author Cristian Rodriguez
 * @version 1.0.0
 */
public class RiskGameDriver {

	private GameData gameData;
	private StartUpPhase startUp;

    /**
     * Constructor will allow to load the map from user selection.
     * It would assign the parsed values from the map and store it in gameData object to use.
     */
	public RiskGameDriver() {
		gameData = new GameData();
		MapLoader maploader = new MapLoader(); // using this will load the map
		gameData.gameMap = maploader.loadMap();
	}

    /**
     * begins the console interface by initating the startup
     */
	public void startGame() {
		// Call the helpers here
		initiateStartUpPhase();
	}

  
	private void initiateStartUpPhase() {
		// need to call the controller start uphase
		startUp = new StartUpPhase(gameData);
		Scanner input = new Scanner(System.in);
		int numOfPlayer = 0;
		String numOfPlayerStr = "";

		do {

			System.out.println("\nNote: You can only have players between 2 to " + gameData.gameMap.getNumberOfCountries());
			System.out.println("Enter the number of players: ");

			numOfPlayerStr = input.nextLine();

		} while (isNaN(numOfPlayerStr)
                || Integer.parseInt(numOfPlayerStr) < GameData.MIN_PLAYERS
				|| Integer.parseInt(numOfPlayerStr) > Math.min(GameData.MAX_PLAYERS, gameData.gameMap.getNumberOfCountries()));

		numOfPlayer = Integer.parseInt(numOfPlayerStr);

		// set values in the game data
		gameData.setNoOfPlayers(numOfPlayer);

		ArrayList<String> playerNames = new ArrayList<String>();

		for (int i = 0; i < numOfPlayer; i++) {
			
			String playerNameInput = "";
			
			while(playerNameInput == null || playerNameInput.length() == 0) {
				System.out.println("\nPlayer " + (i + 1));
				System.out.println("Enter your name: ");
				playerNameInput = input.nextLine().trim();
			}
			
			playerNames.add(playerNameInput.trim());
		}
		
		startUp.generatePlayers(playerNames);

		// now start the roundrobin order
		System.out.println(" ");
		initiateRoundRobin();
		
		// Country Assignment begins
		startUp.assignCountriesToPlayers();

		// Calculation of initial army
		System.out.println("Calculation of initial armies done....");

		HashMap<String, Country> countryObjects = gameData.gameMap.getAllCountries();

		for (Player player : gameData.getPlayers()) {

			Boolean firstTime = true;
			HashSet<String> countriesPerPlayer = gameData.gameMap.getConqueredCountriesPerPlayer(player.getPlayerID());

			String[] countriesPerPlayerArray = Arrays.copyOf(countriesPerPlayer.toArray(), countriesPerPlayer.size(),
					String[].class);
			Integer numberOfArmiesAvailablePerPlayer = startUp.initialArmyCalculation(gameData.getNoOfPlayers());

			System.out.println(" ");
			System.out.println("Number of Army per player: " + numberOfArmiesAvailablePerPlayer);

			while (numberOfArmiesAvailablePerPlayer > 0) {

				System.out.println("Player: " + player.getPlayerName() + " owns the following countries: ");

				for (int i = 0; i < countriesPerPlayerArray.length; i++) {
					String countryName = countriesPerPlayerArray[i];

					if (firstTime) {
						countryObjects.get(countryName).addArmy(1);
						numberOfArmiesAvailablePerPlayer--;
					}
					System.out.println("\t " + i + ": " + countryName + " has "
							+ countryObjects.get(countryName).getCountryArmyCount() + " armies placed.");
				}

				firstTime = false;
				System.out.println("You have " + numberOfArmiesAvailablePerPlayer + " armies left.");

				System.out.println("Please pick the number associated with the country in order to place your armies: ");

				//TODO : ERROR check
				int chosedCountryByUser = input.nextInt();
				String countryName = countriesPerPlayerArray[chosedCountryByUser];

				System.out.println("Player: " + player.getPlayerName() + " How many armies do you want to place in "
						+ countryName + "?");

				//TODO : ERROR check
				int numberOfArmiesByUser = input.nextInt();

				input.close();

				Country selectedCountry = countryObjects.get(countryName);
				selectedCountry.addArmy(numberOfArmiesByUser);
				numberOfArmiesAvailablePerPlayer = numberOfArmiesAvailablePerPlayer - numberOfArmiesByUser;
			}
		}

	}

	private void initiateRoundRobin() {
		ArrayList<String> results = startUp.generateRoundRobin();
		for (int i = 0; i < results.size(); i++) {
			System.out.println(results.get(i));
		}
	}

	private void initiateRoundRobinBasedGamePlay() {
		// TODO Auto-generated method stub
	}

	private void startTurn() {
		// TODO Auto-generated method stub
	}

	private boolean isNaN(final String string) {
		try {
			Integer.parseInt(string);
		} catch (final Exception e) {
			System.out.println("Invalid Input");
			return true;
		}
		return false;
	}

}
