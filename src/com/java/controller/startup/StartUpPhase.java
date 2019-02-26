package com.java.controller.startup;

import java.util.*;

import com.java.controller.dice.Dice;
import com.java.model.cards.CardsDeck;
import com.java.model.gamedata.GameData;
import com.java.model.map.Country;
import com.java.model.map.GameMap;
import com.java.model.player.Player;

public class StartUpPhase {

	public GameData gameData;
	private static int playerId = 1;
	
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
			newPlayers.add(new Player(playerId,playerNames.get(i)));
			playerId++;
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
		int numOfCountriesPerPlayerToAssign = countiresToAssignArrayList.size()/gameData.getNoOfPlayers();
		
		int numOfCountriesToAssign = countiresToAssignArrayList.size();
		int numberOfPlayers = gameData.getNoOfPlayers();
		int index = 0;
		// this index keeps track of the current index of array countries
		int indexKeeper = 0;
		// iterating the players Ids to give them the countries
		for (int i = 1; i < gameData.getNoOfPlayers() + 1; i++){
			HashSet<String> countriesOwnedByAPlayer = new HashSet<String>();
			int numberOfCountriesAssigned = 0;
			for(int j = indexKeeper; j < indexKeeper + numOfCountriesPerPlayerToAssign; j++) {
				countriesOwnedByAPlayer.add(countiresToAssignArrayList.get(j));
				numberOfCountriesAssigned++;
			}
			// we change the number of countries to give to the player to be fair with each.
			indexKeeper = indexKeeper + numberOfCountriesAssigned;
			conqueredContriesPerPlayer.put(i,countriesOwnedByAPlayer);
			index++;
			numOfCountriesToAssign = numOfCountriesToAssign - numberOfCountriesAssigned;
			numberOfPlayers--;
			if(numberOfPlayers != 0) {
				numOfCountriesPerPlayerToAssign = numOfCountriesToAssign/numberOfPlayers;
			}
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

	public ArrayList<String> generateRoundRobin(){

		//iterate over players and set the order of play
		ArrayList<Player> temp = new ArrayList<Player>();
		ArrayList<Player> tiePlayers = gameData.getPlayers();
		ArrayList<String> results = new ArrayList<String>();
		
		Dice dice = new Dice();
		int aux = tiePlayers.size();

		// this will resolve ties based on the die rolled
		while(!tiePlayers.isEmpty()) {
			results.add(" ");

			// assign the player object to the roll die interger ammount
			for(int k = 0; k < aux; k++){
				tiePlayers.get(k).setOrderOfPlay(dice.rollDice());
				results.add("Player " + tiePlayers.get(k).getPlayerName() + " rolled the dice: " + tiePlayers.get(k).getOrderOfPlay());
			}

            // when the player roll dice the tie has to be resolved by calling custom sort from 0 till
			tiePlayers = sort(tiePlayers, 0, aux);

            // to check if the first two players do not have the same number rolled add to temp array
			if(tiePlayers.size() > 1 && tiePlayers.get(0).getOrderOfPlay() != tiePlayers.get(1).getOrderOfPlay()) {
				temp.add(tiePlayers.get(0));
				tiePlayers.remove(0);
			}
			
			if(tiePlayers.size() == 1) {
				temp.add(tiePlayers.get(0));
				tiePlayers.remove(0);
			}


			Boolean flag = false;

			for(int i = 0; i < tiePlayers.size() - 1; i++) {
				if(tiePlayers.get(0).getOrderOfPlay() == tiePlayers.get(i+1).getOrderOfPlay()) {
					aux = i + 1 + 1;
					flag = true;

				} else {
					if(!flag) {
						aux = 0;
					}
					break;
				}
			}
		}
		
		results.add(" ");
		results.add("The following list has the order of the players in round robin fashion: ");
		
		for(int i = 0; i < temp.size(); i++) {
			results.add((i+1) + " " + temp.get(i).getPlayerName());
			temp.get(i).setOrderOfPlay(i);
		}
		gameData.setPlayers(temp);
		return results;
	}


	//custom sorting that allows to pick the start and stop index passed from user.
	private ArrayList<Player> sort(ArrayList<Player> array, int from, int to) {

		for(int i = from; i < to; i++) {
			for(int j = from; j < to; j++) {
				if(array.get(i).getOrderOfPlay() > array.get(j).getOrderOfPlay() ) {
					Player temp = array.get(i);
					array.set(i, array.get(j));
					array.set(j, temp);
				}
			}
		}
		return array;
	}

	public void placeArmies() {
		
	}
	
	public Integer getNumberOfArmiesToAllocate(Integer noOfPlayers) {
		return null;
	}
}
