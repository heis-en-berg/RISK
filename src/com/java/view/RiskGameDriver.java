package com.java.view;

import com.java.controller.dice.Dice;
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

public class RiskGameDriver {

	private GameData gameData;
	private StartUpPhase startUp;


	public RiskGameDriver() {
		gameData = new GameData(); // needs this to load the map
		MapLoader maploader = new MapLoader(); // using this will load the map
		gameData.gameMap = maploader.loadMap();
		//gameData.generateDummyData();
	}

	public void startGame() {
		// Call the helpers here
		initiateStartUpPhase();
	}

	private void initiateStartUpPhase() {
		// need to call the controller start uphase
		startUp = new StartUpPhase(gameData);
		Scanner input = new Scanner(System.in);
		int numOfPlayer =0;

		do {
			System.out.println(" ");
			System.out.println("Note: You can only have players between 2 to 6");
			System.out.println("Enter the number of players: ");

			numOfPlayer = input.nextInt();

		} while((numOfPlayer < GameData.MIN_PLAYERS && numOfPlayer > GameData.MAX_PLAYERS));

		input.nextLine(); // move cursor

		// set values in the game data
		gameData.setNoOfPlayers(numOfPlayer);

		ArrayList<String> playerNames = new ArrayList<String>();
		
		System.out.println(" ");
		
		for(int i=0; i<numOfPlayer; i++ ) {
			System.out.println("Player " + (i+1));
			System.out.println("Enter your name: ");
			playerNames.add(input.next());
		}


		// array list of players returned when generated from the controller
		ArrayList<Player> players = startUp.generatePlayers(playerNames);

		// assign to game data the playerss
		gameData.setPlayers(players);

		// Country Assignment begins
		startUp.assignCountriesToPlayers();
		
		// now start the roundrobin order
		System.out.println(" ");
		initiateRoundRobin();


		// List players and countries for each
		System.out.println(" ");
		System.out.println("List of players with owned countries");
		HashMap<Integer, HashSet<String>> conqueredCountriesPerPlayer = gameData.gameMap.getConqueredCountriesPerPlayer();


		// Calculation of initial army
		System.out.println("Calculation of initial armies done....");
		
		// TODO Initial army placement start put this in another method
		HashMap<String, Country> countryObjects = gameData.gameMap.getCountryObjects();
		
		for(Player player : startUp.gameData.getPlayers()) {

			Boolean firstTime = true;
			HashSet<String> countriesPerPlayer = conqueredCountriesPerPlayer.get(player.getPlayerID());
			
			String[] countriesPerPlayerArray = Arrays.copyOf(countriesPerPlayer.toArray(), countriesPerPlayer.size() ,String[].class);
			Integer numberOfArmiesAvailablePerPlayer =  startUp.initialArmyCalculation(gameData.getNoOfPlayers());

			System.out.println(" ");
			System.out.println("Number of Countries per player: " + numberOfArmiesAvailablePerPlayer);

			while(numberOfArmiesAvailablePerPlayer > 0) {

				System.out.println("Player: " + player.getPlayerName() + " owns the following countries: ");

				for(int i = 0; i < countriesPerPlayerArray.length; i++) {
					String countryName = countriesPerPlayerArray[i];

					if(firstTime) {
						countryObjects.get(countryName).addArmy(1);
						numberOfArmiesAvailablePerPlayer--;
					}
					System.out.println("\t " + i + ": " + countryName + " has " + countryObjects.get(countryName).getCountryArmyCount() + " armies placed.");
				}

				firstTime = false;
				System.out.println("You have " + numberOfArmiesAvailablePerPlayer + " armies left.");
				
				System.out.println("Please pick the number associated with the country in order to place your armies: ");
				int chosedCountryByUser = input.nextInt();
				String countryName = countriesPerPlayerArray[chosedCountryByUser];
				
				
				System.out.println("Player: " + player.getPlayerName() + " How many armies do you want to put in " + countryName + "?");
				int numberOfArmiesByUser = input.nextInt();
				
				Country selectedCountry = countryObjects.get(countryName);
				selectedCountry.addArmy(numberOfArmiesByUser);
				numberOfArmiesAvailablePerPlayer = numberOfArmiesAvailablePerPlayer - numberOfArmiesByUser;
			}
		}

	}

	private void initiateRoundRobin(){
		ArrayList<String> results = startUp.generateRoundRobin();
		for(int i = 0; i < results.size(); i ++) {
			System.out.println(results.get(i));
		}
	}

	private void initiateRoundRobinBasedGamePlay() {
		// TODO Auto-generated method stub
	}

	private void startTurn() {
		// TODO Auto-generated method stub
	}

}
