package com.java.controller.gameplay;

import com.java.controller.dice.Dice;
import com.java.model.gamedata.GameData;
import com.java.model.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Turn implements ReinforcementPhase, AttackPhase, FortificationPhase{

	public GameData gameData;
	public Player player;
	public Integer currentPlayerID;
	private Dice dice;
	private static final int MINIMUM_REINFORCEMENT_ARMY_NUMBER = 3;
	private static final int REINFORCEMENT_DIVISION_FACTOR = 3;

	/*Colors for console output. */
	private static final String ANSI_RESET = "\u001B[0m";
	private static final String ANSI_RED = "\u001B[31m";
	private static final String ANSI_BOLD = "\033[0;1m";

	public Turn(Player player, GameData gameData) {

		this.gameData = gameData;
		this.player = player;
		this.currentPlayerID = player.getPlayerID();
		dice = new Dice(); //To be used in attack phase.
	}

	public void startTurn(){
		startReinforcement();
		//startAttack();      For build 2.
		fortify();
	}

	public void clearConsole(){
		for (int line = 0; line < 50; ++line)
			System.out.println();
	}

	public void highlightStatementInRed(String str){
		System.out.print(ANSI_RED + str + ANSI_RESET);
	}

	public void boldStatement(String str){
		System.out.print(ANSI_BOLD + str + ANSI_RESET);
	}

	/*
	 * Reinforcement Phase
	 * @see com.java.controller.gameplay.ReinforcementPhase
	 */
	@Override
	public void startReinforcement() {

		Integer totalReinforcementArmyCount = calculateReinforcementArmy();
		placeArmy(totalReinforcementArmyCount);
	}

	@Override
	public Integer calculateReinforcementArmy() {

		Integer totalReinforecementArmyCount = 0;
		Integer totalCountriesOwnedByPlayer;
		

		/*Count the total number of continents owned by the player and retrieve the continent's control value. */
		HashSet<String> conqueredContinentsPerPlayer= this.gameData.gameMap.getConqueredContinentsPerPlayer(currentPlayerID);

		for(String continent: conqueredContinentsPerPlayer){
			Integer controlValue = this.gameData.gameMap.getContinent(continent).getContinentControlValue();
			totalReinforecementArmyCount += controlValue;
		}

		/*Count the total number of countries owned by the player and provide a minimum of three armies. */
		totalCountriesOwnedByPlayer= this.gameData.gameMap.getConqueredCountriesPerPlayer(currentPlayerID).size();
		totalReinforecementArmyCount += totalCountriesOwnedByPlayer/REINFORCEMENT_DIVISION_FACTOR > MINIMUM_REINFORCEMENT_ARMY_NUMBER ?
				totalCountriesOwnedByPlayer/REINFORCEMENT_DIVISION_FACTOR:MINIMUM_REINFORCEMENT_ARMY_NUMBER;

		return totalReinforecementArmyCount;
	}

	@Override
	public void placeArmy(Integer reinforcementArmy) {

		Scanner input = new Scanner(System.in);
		Integer currentPlayerID = player.getPlayerID();
		HashSet<String> countriesOwned = this.gameData.gameMap.getConqueredCountriesPerPlayer(currentPlayerID);

		this.clearConsole();
		highlightStatementInRed("Reinforcement Phase Begins...\n");
		try {
			sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.clearConsole();

		while (reinforcementArmy > 0){
			System.out.print("Total Reinforcement Army Count Remaining -> ");
			this.boldStatement("["+String.valueOf(reinforcementArmy)+"]\n");

			/*Information about the countries owned by the player and enemy countries. */
			for(String countries: countriesOwned) {
				System.out.println("\nCountry owned by you: "+countries + " ,Army Count: " + this.gameData.gameMap.getCountry(countries).getCountryArmyCount());
				HashSet<String> adjCountries = this.gameData.gameMap.getAdjacentCountries(countries);
				if(adjCountries.isEmpty()){
					System.out.println("No neighboring enemy country for country "+countries);
				}
				for(String enemyCountries: adjCountries) {
					if(this.gameData.gameMap.getCountry(enemyCountries).getCountryConquerorID() != currentPlayerID) {
						System.out.println("Neighboring Enemy country name: " +enemyCountries+ " ,Army Count: " + this.gameData.gameMap.getCountry(enemyCountries).getCountryArmyCount());
					}
				}
			}
			System.out.println("\nEnter the country name to place armies: ");
			String countryNameByUser = input.nextLine();
			/*Check for an invalid country name.*/
			if(this.gameData.gameMap.getCountry(countryNameByUser) == null) {
				this.clearConsole();
				this.highlightStatementInRed("'"+countryNameByUser+"' is an invalid country name. Please verify the country name from the list.\n\n");
				continue;
			}
			/*Check for a valid country name, but the country belonging to a different player.*/
			if(this.gameData.gameMap.getCountry(countryNameByUser).getCountryConquerorID() != currentPlayerID){
				this.clearConsole();
				this.highlightStatementInRed("'"+countryNameByUser + "' does not belong to you yet!!. Please verify your countries owned from the list below.\n\n");
				continue;
			}

			/*Information about armies and placement of armies*/
			System.out.println("Enter the number of armies to be placed, Remaining Army ("+reinforcementArmy+") :");
			try {
				Integer numberOfArmiesToBePlacedByUser = Integer.parseInt(input.nextLine());
				if (numberOfArmiesToBePlacedByUser > reinforcementArmy) {
					this.clearConsole();
					this.highlightStatementInRed("Input value '"+numberOfArmiesToBePlacedByUser+ "' should not be greater than the Total Reinforcement Army Count ");
					this.boldStatement("("+String.valueOf(reinforcementArmy)+")\n\n");
					continue;
				}
				if(!(numberOfArmiesToBePlacedByUser > 0)){
					this.clearConsole();
					this.highlightStatementInRed("Please input a value greater than 0.\n\n");
					continue;
				}
				this.clearConsole();
				this.highlightStatementInRed("Successful...Country chosen " + countryNameByUser + " ,Number of armies placed: " + numberOfArmiesToBePlacedByUser + "\n\n");
				this.gameData.gameMap.getCountry(countryNameByUser).addArmy(numberOfArmiesToBePlacedByUser);
				reinforcementArmy -= numberOfArmiesToBePlacedByUser;
			}catch (NumberFormatException ex){
				this.clearConsole();
				this.highlightStatementInRed(ex.getMessage()+", please enter numeric values only!\n\n");
				continue;
			}
		}
		input.close();
		/*End of reinforcement phase, Print the final overview.*/
		this.clearConsole();
		this.highlightStatementInRed("Reinforcement Phase is now complete. Here's an overview: \n\n");
		for(String countries: countriesOwned) {
			System.out.println("Country owned by you: "+countries + " ,Army Count: " + this.gameData.gameMap.getCountry(countries).getCountryArmyCount());
		}
	}

	/*
	 * Fortification Phase
	 * @see com.java.controller.gameplay.FortificationPhase
	 */
	@Override
	public void fortify() {
		
		boolean doFortify = false;
		Scanner input = new Scanner(System.in);  
		System.out.println("Would you like to fortify? (YES/NO)");
		String playerDecision = scanner.nextLine();
		//input.close();
		
		switch(playerDecision.toLowerCase()){
        case "yes":
        	doFortify = true;
            break;
        }
		
		if(!doFortify) {
			System.out.println("Player does not wish to fortify. Ending turn..");
			return;
		} else {
			System.out.println("Fetching potential fortification scenarios for player..");
		}
		
		HashMap<String, ArrayList<String>> fortificationScenarios = getPotentialFortificationScenarios();
		
		if(fortificationScenarios != null) {
			/*for (TypeKey name: example.keySet()){
	            String key =name.toString();
	            String value = example.get(name).toString();  
	            System.out.println(key + " " + value);  
			}*/ 
		} else {
			System.out.println("There are currently no fortification opportunities for current player.. Sorry!");
			return;
		} 
		
		// while selection doesn't match int options printed
		System.out.println("Choose scenario by number");
		//playerDecision = scanner.nextLine();
		
		Integer noOfArmies = getNoOfArmiesToMove();
		String fromCountryName = chooseCountryToFortifyfrom();
		String toCountryName = chooseCountryToFortifyto();
		//placeArmy above should be refactored to accommodate both reinforcement & fortification phases 
		

	}

	@Override
	public HashMap<String, ArrayList<String>> getPotentialFortificationScenarios() {

		HashMap<String, ArrayList<String>> fortificationScenarios  = new HashMap<String, ArrayList<String>>();
		HashSet<String> poolOfPotentialCountries = new HashSet<String>();
		HashSet<String> adjacentCountries = new HashSet<String>();
		
		// Step 1: get the comprehensive list of all countries currently conquered by player
		poolOfPotentialCountries = this.gameData.gameMap.getConqueredCountriesPerPlayer(currentPlayerID);
		
		// Step 2: limit the scope by eliminating some of the countries as options to fortify *from*.
		// This is enforced by the known minimum requirement of at least 1 army on the ground at all times.
    	// Given that the "from" and "to" matter => we will key our hash of scenarios on "froms" and append all potential "to's" as values for a from key
		
		for (String potentialCountry : poolOfPotentialCountries){
		    if(this.gameData.gameMap.getCountry(potentialCountry).getCountryArmyCount() > 1 ) {
		    	// once we ensure a country has more than 1 army, it becomes a potential key
		    	// ****
		    	// Step 3: now try to build the paths by carefully checking neighbors and whether or not they're in scope
		    	adjacentCountries = getAdjacentCountries(potentialCountry);
		    	for (String adjacentCountry : adjacentCountries){
		    		// need to ensure the adjacent country is also owned by that very same player - otherwise there's no path
		    		if(poolOfPotentialCountries.contains(adjacentCountry)) {
		    			fortificationScenarios.putIfAbsent(potentialCountry, new ArrayList<String>());
		    			fortificationScenarios.get(potentialCountry).add(adjacentCountry);
		    		}
		    	}
		    }
		}
		
		if(fortificationScenarios.isEmpty()) {
			return null;
		} 
		
		return fortificationScenarios;
	}

	@Override
	public String chooseCountryToFortifyfrom() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String chooseCountryToFortifyto() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getNoOfArmiesToMove() {
		// TODO Auto-generated method stub
		return null;
	}

}
