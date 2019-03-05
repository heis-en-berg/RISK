package com.java.controller.gameplay;

import com.java.model.gamedata.GameData;
import com.java.model.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import static java.lang.Thread.sleep;

/**
 * The Turn class is used as a controller which entirely manages the rundown of a given player's turn in the game.
 * For the scope of version 1.0.0 it includes the fully implemented functionality for both the reinforcement and fortification phases.
 * During reinforcement, players get allocated a minimum number of armies which they position in any self-owned country of their choice.
 * Subsequently, fortification allows players to relocate their existing armies on the ground and move them between countries they own,
 * provided a continuous path exists between the source and destination.
 *
 * @author Arnav Bhardwaj
 * @author Karan Dhingra
 * @author Ghalia Elkerdi
 * @author Sahil Singh Sodhi
 * @author Cristian Rodriguez 
 * @version 1.0.0
 */

public class Turn implements ReinforcementPhase, AttackPhase, FortificationPhase {

	public GameData gameData;
	public Player player;
	public Integer currentPlayerID;
	public String playerName;
	private Scanner input;
	private static final int MINIMUM_REINFORCEMENT_ARMY_NUMBER = 3;
	private static final int REINFORCEMENT_DIVISION_FACTOR = 3;

 
	/**
     * Turn Constructor will allow the initialization of core data shared across both phases.
     */
	
	public Turn(Player player, GameData gameData) {

		this.gameData = gameData;
		this.player = player;
		this.currentPlayerID = player.getPlayerID();
		input = new Scanner(System.in);
		this.playerName = player.getPlayerName();
	}
	/**
     * The startTurn() method organizes the flow of the game by ordering phase-execution.
     */

	public void startTurn() {
		startReinforcement();
		// startAttack(); For build 2.
		fortify();
	}

	/*
	 * Reinforcement Phase
	 *
	 * @see com.java.controller.gameplay.ReinforcementPhase
	 */
	@Override
	public void startReinforcement() {

		Integer totalReinforcementArmyCount = calculateReinforcementArmy();
		placeArmy(totalReinforcementArmyCount);
	}

	@Override
	public void placeArmy(Integer reinforcementArmy) {

		Integer currentPlayerID = player.getPlayerID();
		HashSet<String> countriesOwned = this.gameData.gameMap.getConqueredCountriesPerPlayer(currentPlayerID);

		System.out.println();
		System.out.println("**** Reinforcement Phase Begins for player "+ this.playerName +"..****\n");

		while (reinforcementArmy > 0) {

			System.out.print(playerName+"'s Total Reinforcement Army Count Remaining -> [" + String.valueOf(reinforcementArmy) + "]\n");

			/* Information about the countries owned by the player and enemy countries. */
			for (String countries : countriesOwned) {
				System.out.println("\nCountry owned by "+ playerName+ "-> " + countries + " & Army Count: "
						+ this.gameData.gameMap.getCountry(countries).getCountryArmyCount());
				HashSet<String> adjCountries = this.gameData.gameMap.getAdjacentCountries(countries);
				if (adjCountries.isEmpty()) {
					System.out.println("No neighboring enemy country for country " + countries);
				}
				for (String enemyCountries : adjCountries) {
					if (this.gameData.gameMap.getCountry(enemyCountries).getCountryConquerorID() != currentPlayerID) {
						System.out.println("Neighboring Enemy country name: " + enemyCountries + " & Army Count: "
								+ this.gameData.gameMap.getCountry(enemyCountries).getCountryArmyCount());
					}
				}
			}
			System.out.println("\nEnter the country name to place armies: ");
			String countryNameByUser = input.nextLine();
			/* Check for an invalid country name. */
			if (this.gameData.gameMap.getCountry(countryNameByUser) == null) {
				System.out.println("'" + countryNameByUser
						+ "' is an invalid country name. Please verify the country name from the list.\n\n");
				continue;
			}
			/*
			 * Check for a valid country name, but the country belonging to a different
			 * player.
			 */
			if (this.gameData.gameMap.getCountry(countryNameByUser).getCountryConquerorID() != currentPlayerID) {
				System.out.println("'" + countryNameByUser
						+ "' does not belong to you yet!!. Please verify your countries owned from the list below.\n\n");
				continue;
			}

			/* Information about armies and placement of armies */
			System.out.println("Enter the number of armies to be placed, Remaining Army (" + reinforcementArmy + ") :");
			try {
				Integer numberOfArmiesToBePlacedByUser = Integer.parseInt(input.nextLine());
				if (numberOfArmiesToBePlacedByUser > reinforcementArmy) {
					System.out.println("Input value '" + numberOfArmiesToBePlacedByUser
							+ "' should not be greater than the Total Reinforcement Army Count " + "("
							+ String.valueOf(reinforcementArmy) + ")\n\n");
					continue;
				}
				if (!(numberOfArmiesToBePlacedByUser > 0)) {
					System.out.println("Please input a value greater than 0.\n\n");
					continue;
				}
				System.out.println("Successful...Country chosen " + countryNameByUser + " ,Number of armies placed: "
						+ numberOfArmiesToBePlacedByUser + "\n\n");
				this.gameData.gameMap.getCountry(countryNameByUser).addArmy(numberOfArmiesToBePlacedByUser);
				reinforcementArmy -= numberOfArmiesToBePlacedByUser;
			} catch (NumberFormatException ex) {
				System.out.println(ex.getMessage() + ", please enter numeric values only!\n\n");
				continue;
			}
		}
		/* End of reinforcement phase, Print the final overview. */
		System.out.println("Reinforcement Phase is now complete. Here's an overview: \n\n");
		for (String countries : countriesOwned) {
			System.out.println("Country owned by you: " + countries + " ,Army Count: "
					+ this.gameData.gameMap.getCountry(countries).getCountryArmyCount());
		}
		System.out.println("\n****Reinforcement Phase Ends for player "+ this.playerName +"..****\n");
	}

	@Override
	public Integer calculateReinforcementArmy() {

		Integer totalReinforecementArmyCount = 0;
		Integer totalCountriesOwnedByPlayer;
		Integer currentPlayerID = player.getPlayerID();

		/*
		 * Count the total number of continents owned by the player and retrieve the
		 * continent's control value.
		 */
		HashSet<String> conqueredContinentsPerPlayer = this.gameData.gameMap
				.getConqueredContinentsPerPlayer(currentPlayerID);

		for (String continent : conqueredContinentsPerPlayer) {
			Integer controlValue = this.gameData.gameMap.getContinent(continent).getContinentControlValue();
			totalReinforecementArmyCount += controlValue;
		}

		/*
		 * Count the total number of countries owned by the player and provide a minimum
		 * of three armies.
		 */
		totalCountriesOwnedByPlayer = this.gameData.gameMap.getConqueredCountriesPerPlayer(currentPlayerID).size();
		totalReinforecementArmyCount += totalCountriesOwnedByPlayer
				/ REINFORCEMENT_DIVISION_FACTOR > MINIMUM_REINFORCEMENT_ARMY_NUMBER
				? totalCountriesOwnedByPlayer / REINFORCEMENT_DIVISION_FACTOR
				: MINIMUM_REINFORCEMENT_ARMY_NUMBER;

		return totalReinforecementArmyCount;
	}

/**
	 * Method to guide the player through various fortification options when applicable.
	 * 
	 * @param none
	 * @return void
	 */
	public void fortify() {

		System.out.println();
		System.out.println("**** Fortification Phase Begins for player "+ this.playerName +"..****\n");		

		// First get confirmation from the player that fortification is desired.

		boolean doFortify = false;
		String playerDecision = "no";
		System.out.println("Would you like to fortify? (YES/NO)");
		if (input.hasNextLine()) {
			playerDecision = input.nextLine();
		}

		switch (playerDecision.toLowerCase()) {
		case "yes":
			doFortify = true;
			break;
		}

		if (!doFortify) {
			System.out.println(this.playerName + " does not wish to fortify. Ending turn..");
			System.out.println("\n****Fortification Phase Ends for player "+ this.playerName +"..****\n");
			return;
		} else {
			System.out.println("\n" + "Fetching potential fortification scenarios for " + this.playerName + "...\n");
		}

		// Now fetch all possibilities for player (this could get long as the game progresses and more land is acquired)

		HashMap<String, ArrayList<String>> fortificationScenarios = getPotentialFortificationScenarios();

		if (fortificationScenarios == null) {
			System.out.println("There are currently no fortification opportunities for " + this.playerName + ".. Sorry!\n");
			System.out.println("\n****Fortification Phase Ends for player "+ this.playerName +"..****\n");
			return;
		}

		if (fortificationScenarios.isEmpty()) {
			System.out.println("There are currently no fortification opportunities for " + this.playerName + ".. Sorry!\n");
			System.out.println("\n****Fortification Phase Ends for player "+ this.playerName +"..****\n");
			return;
		}

		// This structure will accelerate and organize the army count process/validation
		HashMap<String, Integer> armiesPerPotentialFortificationSourceCountry = new HashMap<String, Integer>();

		// Print all the options out for the player to see and choose from
		for (String keySourceCountry : fortificationScenarios.keySet()) {
			armiesPerPotentialFortificationSourceCountry.put(keySourceCountry,
					this.gameData.gameMap.getCountry(keySourceCountry).getCountryArmyCount());
			// the range is one less because of the minimum requirement of having at least 1 army on the ground at all times.
			int possibleNumOfArmyRange = armiesPerPotentialFortificationSourceCountry.get(keySourceCountry) - 1;
			for (String correspondingDestinationCountry : fortificationScenarios.get(keySourceCountry)) {
				if (!correspondingDestinationCountry.equalsIgnoreCase(keySourceCountry)) {
					System.out.println("\n" + keySourceCountry + "\t -> \t" + correspondingDestinationCountry
							+ "\t (up to " + possibleNumOfArmyRange + " armies)");
				}
			}
		}

		// Recycle variable
		// clear the decision variable holder between choices
		playerDecision = "";

		// while selection doesn't match any of the offered options, prompt user
		while (!fortificationScenarios.containsKey(playerDecision)) {
			System.out.println("\n" + "Please choose one of the suggested countries to move armies FROM: ");
			playerDecision = input.nextLine();
		}
		String fromCountry = playerDecision;

		// while number of armies to be moved is not coherent, prompt user
		// 0 is a valid selection
		String noOfArmiesToMove = "-1";
		do {
			System.out.println("\n" + "How many armies would you like to move from " + fromCountry + "?");
			noOfArmiesToMove = input.nextLine();
		} while (isNaN(noOfArmiesToMove) || Integer.parseInt(noOfArmiesToMove) < 0 || Integer
				.parseInt(noOfArmiesToMove) >= armiesPerPotentialFortificationSourceCountry.get(fromCountry));

		playerDecision = "";

		// check that the {from - to} combination specifically makes sense as a valid
		// path
		while (!fortificationScenarios.get(fromCountry).contains(playerDecision)) {
			System.out.println(
					"\n" + "Please choose one of the valid countries to move armies INTO (knowing that you've chosen to move them from country "
							+ fromCountry + "): ");
			playerDecision = input.nextLine();
		}
		String toCountry = playerDecision;
		
		// At this stage all that's left to do really is adjust the army counts in the
		// respective countries to reflect they player's fortification move
		this.gameData.gameMap.getCountry(fromCountry).deductArmy(Integer.parseInt(noOfArmiesToMove));
		this.gameData.gameMap.getCountry(toCountry).addArmy(Integer.parseInt(noOfArmiesToMove));

		System.out.println("\nFortification Successful for "+ this.playerName +". Here is a summary of the new status-quo:\n");

		System.out.println("Army count for " + fromCountry + " is now: "
				+ this.gameData.gameMap.getCountry(fromCountry).getCountryArmyCount());
		System.out.println("Army count for " + toCountry + " is now: "
				+ this.gameData.gameMap.getCountry(toCountry).getCountryArmyCount());

		System.out.println("\n****Fortification Phase Ends for player "+ this.playerName +"..****\n");
	}

/**
	 * Helper method to build a comprehensive map of all the possible fortification paths (for both immediate and extended neighbors)
	 * It has all the necessary checks and validation to ensure the path includes only countries owned by the given current player in the turn.
	 * Moreover, ensures that candidates suggested as "source countries" to move armies from satisfy the minimal requirements of army presence on the ground.
	 * 
	 * @param none
	 * @return Hashmap of all potential fortification scenarios for player.
	 */

	@Override
	public HashMap<String, ArrayList<String>> getPotentialFortificationScenarios() {

		// Draft/prelim structure which contains only directly adjacent countries owned by the player
		HashMap<String, ArrayList<String>> prelimFortificationScenarios = new HashMap<String, ArrayList<String>>();
		// What will be returned: includes full (extended) paths of countries owned by the player
		HashMap<String, ArrayList<String>> allFortificationScenarios = new HashMap<String, ArrayList<String>>();

		// Step 1: get the comprehensive list of all countries currently conquered by the player
		HashSet<String> poolOfPotentialCountries = new HashSet<String>();
		poolOfPotentialCountries = this.gameData.gameMap.getConqueredCountriesPerPlayer(currentPlayerID);

		// Step 2: limit the scope by eliminating some of the countries as options to
		// fortify *from*.
		// This is enforced by the known minimum requirement of at least 1 army on the
		// ground at all times.
		// Given that the "from" and "to" matter => key the hashmap of scenarios on
		// "froms" and append all potential "to's" as lists of values for a given "from
		// key"

		for (String potentialSourceCountry : poolOfPotentialCountries) {
			if (this.gameData.gameMap.getCountry(potentialSourceCountry).getCountryArmyCount() > 1) {
				// Step 3: buildFortificationPath is the main iterative logic which will "draw"
				// the preliminary short paths among direct neighboring countries
				// and populate the fortificationScenarios as necessary
				buildFortificationPath(prelimFortificationScenarios, potentialSourceCountry);
			}
		}

		if (prelimFortificationScenarios.isEmpty()) {
			return null;
		}

		// Before we return the set, we have to slightly manipulate it while copying to
		// the final structure
		// This is to basically draw "full paths" and make potentially longer
		// connections beyond just "immediate neighbors"
		// This follows the principle of "what's yours is also mine" among a given key
		// and its values
		for (String keySourceCountry : prelimFortificationScenarios.keySet()) {
			for (String correspondingDestinationCountry : prelimFortificationScenarios.get(keySourceCountry)) {	
				allFortificationScenarios.putIfAbsent(keySourceCountry, new ArrayList<String>());
				allFortificationScenarios.get(keySourceCountry).add(correspondingDestinationCountry);
				if (!prelimFortificationScenarios.containsKey(correspondingDestinationCountry)) {
					continue;
				}
				allFortificationScenarios.get(keySourceCountry)
						.addAll(prelimFortificationScenarios.get(correspondingDestinationCountry));
			}
		}

		return allFortificationScenarios;
	}
	
	/**
	 * Small helper method to preliminarily build the short paths among potential immediate adjacent countries.
	 * This is NOT the final and full picture for fortification scenarios, it is merely a stepping stone.
	 * 
	 * @param HashMapStructure to be populated, and a country to be checked for adjacency
	 * @return void
	 */
	@Override
	public void buildFortificationPath(HashMap<String, ArrayList<String>> fortificationScenarios, String rootCountry) {

		HashSet<String> adjacentCountries = new HashSet<String>();
		adjacentCountries = this.gameData.gameMap.getAdjacentCountries(rootCountry);
		for (String adjacentCountry : adjacentCountries) {
			// need to ensure the adjacent country is also owned by that very same player -
			// otherwise there's no path
			if (this.gameData.gameMap.getCountry(adjacentCountry).getCountryConquerorID() == currentPlayerID) {
				fortificationScenarios.putIfAbsent(rootCountry, new ArrayList<String>());
				fortificationScenarios.get(rootCountry).add(adjacentCountry);
			}
		}
	}

	/**
	 * Helper method to test if a given strin can be converted to a int.
	 * 
	 * @param stringInput determines if the string typed by user is an integer
	 * @return the evaluation of true if it is an integer or false otherwise
	 */
	private boolean isNaN(final String stringInput) {
		try {
			Integer.parseInt(stringInput);

		} catch (final Exception e) {

			System.out.println("Invalid Input");
			return true;
		}
		return false;
	}

}
