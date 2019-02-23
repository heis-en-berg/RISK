package com.java.controller.startup;

import java.util.*;

import com.java.model.cards.CardsDeck;
import com.java.model.gamedata.GameData;
import com.java.model.map.Country;
import com.java.model.map.GameMap;
import com.java.model.player.Player;

public class StartUpPhase {

	public GameData gameData;
	
	public StartUpPhase(GameData gameData) {
		this.gameData = gameData;
	}
	
	public GameData start() {
		return null;
	}
	
	public Integer getNoOfPlayerFromUser() {
		return null;
	}
	
	public ArrayList<String> getPlayersNamesFromUser() {
		return null;
	}
	
	public ArrayList<Player> generatePlayers(ArrayList<String> playerNames){
		ArrayList<Player> newPlayers = new ArrayList<Player>();

		for(int i=0; i < playerNames.size(); i++){
			newPlayers.add(new Player(((Integer)i+1),playerNames.get(i)));
		}
		return newPlayers;
	}
	
	public void assignCountriesToPlayers() {
		// used to assign the countires to the players
		HashMap<Integer, HashSet<String>> conqueredContriesPerPlayer = new HashMap<Integer, HashSet<String>>();


		// used to obtian the country objects
		HashMap<String, Country> countryObject = gameData.gameMap.getCountryObjects();

		Random randomPlayerID = new Random();
		int numOfPlayersId;

		// start randomly assigning the countries evenly to the players
		String[] countiresToAssign = Arrays.copyOf(countryObject.keySet().toArray(), countryObject.size(),String[].class);

		// converting to arraylist for shuffing the elements (contries)
		ArrayList<String> countiresToAssignArrayList =  new ArrayList<String>(Arrays.asList(countiresToAssign));

		Collections.shuffle(countiresToAssignArrayList);

		// calculate the even number of contries to assign to each player
		int numToAssign = countiresToAssignArrayList.size()/gameData.getNoOfPlayers();

		for (int i = 0; i < gameData.getNoOfPlayers(); i++){
			conqueredContriesPerPlayer.put(i+1,new HashSet<String>()); // its just empty strings to fill up
		}

		// iterating and assigning country to a player
		for(String countryName : countryObject.keySet()){
			numOfPlayersId = randomPlayerID.nextInt(gameData.getNoOfPlayers()) + 1; // from first to last
			HashSet<String> countriesPlayer = conqueredContriesPerPlayer.get(numOfPlayersId);
			countriesPlayer.add(countryName);
		}

		gameData.gameMap.setConqueredCountriesPerPlayer(conqueredContriesPerPlayer);
	}
	
	public Integer initialArmyCalculation(Integer numPlayers) {
		Integer numberOfArmiesPerPlayer = new Integer(0);
		switch(numPlayers) {
			case 2:
				numberOfArmiesPerPlayer = 40;
				break;
			case 3:
				numberOfArmiesPerPlayer = 35;
				break;
			case 4:
				numberOfArmiesPerPlayer = 30;
				break;
			case 5:
				numberOfArmiesPerPlayer = 25;
				break;
			case 6:
				numberOfArmiesPerPlayer = 20;
				break;
		}
		return numberOfArmiesPerPlayer;
	}
	public CardsDeck generateCardsDeck() {
		return null;
	}
	
	public void placeArmies() {
		
	}
	
	public Integer getNumberOfArmiesToAllocate(Integer noOfPlayers) {
		return null;
	}
}
