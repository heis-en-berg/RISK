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

public class RiskGameDriver {

	private GameData gameData;
	private StartUpPhase startUp;

	public RiskGameDriver() {
		gameData = new GameData();
		MapLoader maploader = new MapLoader(); // using this will load the map
		gameData.gameMap = maploader.loadMap();
	}

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

		} while (isNaN(numOfPlayerStr) || Integer.parseInt(numOfPlayerStr) < GameData.MIN_PLAYERS
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

		// TODO Initial army placement start put this in another method
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

				String chosedCountryByUser = "";
				do {
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
		startTurn();
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

		ArrayList<Player> playerList = this.gameData.getPlayers();
		Player currentPlayer;
		for(int player = 0 ; player < playerList.size();){
			currentPlayer = this.gameData.getPlayers().get(player);
			/* Exclude turn for the player with no countries. The player has been defeated.*/
			if(this.gameData.gameMap.getConqueredCountriesPerPlayer(player) != null) {
				Turn turn = new Turn(currentPlayer, this.gameData);
				turn.startTurn();
				player++;
			}
			else{
				playerList.remove(currentPlayer);
			}
		}
	}

	private boolean isNaN(final String string) {
		try {
			Integer.parseInt(string);
		} catch (final Exception e) {
			return true;
		}
		return false;
	}

}
