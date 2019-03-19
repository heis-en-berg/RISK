package com.java.model.player;



import com.java.controller.dice.Dice;
import com.java.model.Observable;
import com.java.model.cards.Card;
import com.java.model.gamedata.GameData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

/**
 * This class models the player, it holds the id, the name, and the order to play
 * for the round robin.
 * 
 * @author Arnav Bhardwaj
 * @author Karan Dhingra
 * @author Ghalia Elkerdi
 * @author Sahil Singh Sodhi
 * @author Cristian Rodriguez 
 * @version 1.0.0
 * */
public class Player extends Observable {

	private Integer playerID;
	private String playerName;
	private Integer orderOfPlay;
	private ArrayList<Card> cardList;
	private static int cardExchangeArmyCount = 5;

	private Scanner input;
	private GameData gameData;
	private Dice playerDice = new Dice();
	
	private ArrayList<AttackPhaseState> attackPhaseState;
	private ArrayList<ReinforcementPhaseState> reinforcementPhaseState;
	private ArrayList<FortificationPhaseState> fortificationPhaseState;

	private static final int MINIMUM_REINFORCEMENT_ARMY_NUMBER = 3;
	private static final int REINFORCEMENT_DIVISION_FACTOR = 3;

	/**
	 * Creates a player by giving the id and the name
	 * 
	 * @param playerID the player id.
	 * @param playerName the player name.
	 * */
	public Player(Integer playerID, String playerName) {
		this.playerID = playerID;
		this.playerName = playerName;
		this.cardList = new ArrayList<Card>();
		attackPhaseState = new ArrayList<>();
		reinforcementPhaseState = new ArrayList<>();
		fortificationPhaseState = new ArrayList<>();
	}

	//------------------- reinforcment actions: Starts here--------------------------------------
	public void startReinforcement() {

		ArrayList<Card> playerExchangeCards;
		playerExchangeCards = getValidCards();
		Integer totalReinforcementArmyCount = calculateTotalReinforcement(playerExchangeCards);
		ReinforcementPhaseState reinforcementPhase = new ReinforcementPhaseState();
		reinforcementPhase.setNumberOfArmiesReceived(totalReinforcementArmyCount);
		reinforcementPhaseState.add(reinforcementPhase);
		notifyView();
		placeArmy(totalReinforcementArmyCount);
	}
	//------------------- reinforcment actions: Ends here--------------------


	/**
	 * The startTurn() method organizes the flow of the game by ordering phase-execution.
	 */
	public void startTurn() {
		input = new Scanner(System.in);
		startReinforcement();
		startAttack(); 
		fortify();
	}
	

	private ArrayList<Card> getValidCards(){
		ArrayList<Card> playerCardList = getPlayerCardList();
		ArrayList<Card> playerExchangeCards = new ArrayList<>();
		boolean can_exchange = false;

		for (int i = 0; i < 5; i++) {
			Card card = gameData.cardsDeck.getCard();
			addToPlayerCardList(card);
		}

		System.out.println("*** Cards in hand ***");
		this.showCards();

		System.out.println("Do you wish to exchange cards ? (yes/no)");
		String userInput = input.nextLine();

		while (!((userInput.toLowerCase().equals("yes")) || (userInput.toLowerCase().equals("no")))) {
			System.out.println("Please input either yes or no.");
			userInput = input.nextLine();
		}

		if ((userInput.equals("yes")) || (userInput.equals("no") && playerCardList.size() > 4)) {
			if(userInput.equals("no") && (playerCardList.size() > 4)){
				System.out.println("You must exchange cards. You have 5 cards in your hand.");
			}
			this.showCards();
			int count = 0;
			Integer cardNumber;

			while(!can_exchange) {
				playerExchangeCards = new ArrayList<Card>();
				System.out.println("Please enter three card numbers from the list of the same or different army types.");
				for (int i = 0; i < 3; i++) {
					cardNumber = input.nextInt();
					while (cardNumber > playerCardList.size()) {
						System.out.println("Please input correct number from list");
						cardNumber = input.nextInt();
					}
					playerExchangeCards.add(playerCardList.get(cardNumber));
				}

				can_exchange = isValidExchange(playerExchangeCards);
			}
		}
		return playerExchangeCards;
	}

	/**
	 * placeArmy method allows the player to position the armies in the player's owned countries.
	 * @param reinforcementArmy total reinforcement army count to be placed by the current player.
	 */
	private void placeArmy(Integer reinforcementArmy) {

		Integer currentPlayerID = playerID;
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
				
				ReinforcementPhaseState reinforcementPhase = new ReinforcementPhaseState();
				reinforcementPhase.setToCountry(countryNameByUser);
				reinforcementPhase.setNumberOfArmiesPlaced(numberOfArmiesToBePlacedByUser);
				reinforcementPhaseState.add(reinforcementPhase);
				notifyView();

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
		reinforcementPhaseState.clear();
		System.out.println("\n**** Reinforcement Phase Ends for player "+ this.playerName +"..****\n");
	}
	private void showCards(){

		ArrayList<Card> playerCardList = getPlayerCardList();
		ArrayList<String> playerCountryList = new ArrayList<String>();

		int cardsCount = 0;

		for (Card cards : playerCardList) {
			System.out.println(cardsCount + ". " + cards.getCountry().getCountryName() + " " + cards.getArmyType());
			playerCountryList.add(cards.getCountry().getCountryName());
			cardsCount++;
		}
	}


	private int calculateTotalReinforcement(ArrayList<Card> playerExchangeCards){
		int totalReinforcementArmyCount = 0;
		totalReinforcementArmyCount += (reinforcementArmyCountFromCards(playerExchangeCards) + calculateReinforcementArmy());

		return totalReinforcementArmyCount;
	}


	/**
	 * Function to count the reinforcement army based on the number of territories and continents owned.
	 * @return total reinforcement army count
	 */
	public Integer calculateReinforcementArmy() {

		Integer totalReinforcementArmyCount = 0;
		Integer totalCountriesOwnedByPlayer;
		Integer currentPlayerID = playerID;
		ArrayList<Card> playerCardList = getPlayerCardList();
		ArrayList<Card> playerExchangeCards;
		ArrayList<String> playerCountryList = new ArrayList<String>();
		boolean can_exchange = false;

		/*
		 * Count the total number of continents owned by the player and retrieve the
		 * continent's control value.
		 */
		HashSet<String> conqueredContinentsPerPlayer = this.gameData.gameMap
				.getConqueredContinentsPerPlayer(currentPlayerID);

		for (String continent : conqueredContinentsPerPlayer) {
			Integer controlValue = this.gameData.gameMap.getContinent(continent).getContinentControlValue();
			totalReinforcementArmyCount += controlValue;
		}

		/*
		 * Count the total number of countries owned by the player and provide a minimum
		 * of three armies.
		 */
		totalCountriesOwnedByPlayer = this.gameData.gameMap.getConqueredCountriesPerPlayer(currentPlayerID).size();
		totalReinforcementArmyCount += totalCountriesOwnedByPlayer
				/ REINFORCEMENT_DIVISION_FACTOR > MINIMUM_REINFORCEMENT_ARMY_NUMBER
				? totalCountriesOwnedByPlayer / REINFORCEMENT_DIVISION_FACTOR
				: MINIMUM_REINFORCEMENT_ARMY_NUMBER;
		return totalReinforcementArmyCount;
	}

	private int reinforcementArmyCountFromCards(ArrayList<Card> playerExchangeCards){

		int countReinforcementFromCardExchange = 0;

		boolean can_exchange = isValidExchange(playerExchangeCards);
		boolean extraTerritoryMatchArmy = isExtraTerritoryMatchArmy(playerExchangeCards);

		if(can_exchange == true){
			if(extraTerritoryMatchArmy == true){
				countReinforcementFromCardExchange += 2;
			}
			countReinforcementFromCardExchange += Player.getCardExchangeArmyCount();
			Player.setCardExchangeArmyCount();
		}

		return countReinforcementFromCardExchange;
	}


	private boolean isExtraTerritoryMatchArmy(ArrayList<Card> playerExchangeCards){
		boolean extraTerritoryMatchArmy = false;

		for(Card card : playerExchangeCards){
			if (playerID == card.getCountry().getCountryConquerorID()) {
				extraTerritoryMatchArmy = true;
				break;
			}
		}
		return extraTerritoryMatchArmy;
	}


	// Helper to check the cards exchange
	private boolean isValidExchange(ArrayList<Card> playerExchangeCards){
		boolean condition_same = ((playerExchangeCards.get(0).getArmyType().equals(playerExchangeCards.get(1).getArmyType())) &&
				playerExchangeCards.get(0).getArmyType().equals(playerExchangeCards.get(2).getArmyType()));
		boolean condition_different = (!(playerExchangeCards.get(0).getArmyType().equals(playerExchangeCards.get(1).getArmyType()))) &&
				(!(playerExchangeCards.get(0).getArmyType().equals(playerExchangeCards.get(2).getArmyType()))) &&
				(!(playerExchangeCards.get(1).getArmyType().equals(playerExchangeCards.get(2).getArmyType())));
		return (condition_same || condition_different);
	}
	
	/**
	 * The startAttack() method encompasses the attack phase logic and flow.
	 */
	
	// TO-DO in next refactor round: create an "endAttack" method which displays the appropriate messages and handles notifications
	public void startAttack() {
		
		System.out.println();
		System.out.println("**** Attack Phase Begins for player "+ this.playerName +"..****\n");	
		
		boolean gameOn =  true;
		
		while (gameOn) {
			
			// First get confirmation from the player that attack is desired.
			boolean wantToAttack = false;
			String playerDecision = "no";
			System.out.println("\n Would you like to attack? (YES/NO)");
			if (input.hasNextLine()) {
				playerDecision = input.nextLine();
			}
	
			switch (playerDecision.toLowerCase()) {
				case "yes":
					wantToAttack = true;
					break;
				case "yeah":
					wantToAttack = true;
					break;
				case "y":
					wantToAttack = true;
					break;
				case "sure":
					wantToAttack = true;
					break;
			}
	
			if (!wantToAttack) {
				gameOn = false;
				System.out.println("\n" + this.playerName + "is peaceful and does not wish to attack anyone. Ending attack phase..");
				System.out.println("\n****Attack Phase Ends for player "+ this.playerName +"..****\n");
				return;
			} else {
				System.out.println("\n" + "Fetching potential attack scenarios for " + this.playerName + "...\n");
			}
			
			// no need to recursively loop through the neighbors of the countries conquered by the player
			// we merely require the immediate neighbors
			// this is used in all the helper functions so initialize & populate it here and pass it along whenever need be
			HashSet<String> poolOfPotentialSourceCountries = new HashSet<String>();
			poolOfPotentialSourceCountries = this.gameData.gameMap.getConqueredCountriesPerPlayer(playerID);
			
			HashSet<String> poolOfPotentialDestinationCountries = new HashSet<String>();
			HashSet<String> destinationCountriesWithMultiSharedBorders = new HashSet<String>();
	
			
			// Now fetch all possibilities for player 
			HashMap<String,ArrayList<String>> attackScenarios = new HashMap<String,ArrayList<String>>();
			
			// Keep track for dice counts 
			HashMap<String,Integer> maxAttackArmyCountPossiblePerSrcCountry = new HashMap<String,Integer>();
			HashMap<String,Integer> maxDefenseArmyCountPossiblePerDestCountry = new HashMap<String,Integer>();
			
			for (String potentialSourceCountry : poolOfPotentialSourceCountries) {
				// eliminate countries with only a single army count
				if (this.gameData.gameMap.getCountry(potentialSourceCountry).getCountryArmyCount() < 2) {
					continue;
				}
				HashSet<String> adjacentCountries = new HashSet<String>();
				adjacentCountries = this.gameData.gameMap.getAdjacentCountries(potentialSourceCountry);
				for (String adjacentCountry : adjacentCountries) {
					// ensure adjacent country not owned by same player 
					if (this.gameData.gameMap.getCountry(adjacentCountry).getCountryConquerorID() != playerID){
						if(poolOfPotentialDestinationCountries.contains(adjacentCountry)) {
							// this enemy country can be attacked from multiple fronts
							if(!destinationCountriesWithMultiSharedBorders.contains(adjacentCountry)) {
								destinationCountriesWithMultiSharedBorders.add(adjacentCountry);
							} 
						} else {
							poolOfPotentialDestinationCountries.add(adjacentCountry);
						}
						attackScenarios.putIfAbsent(potentialSourceCountry, new ArrayList<String>());
						attackScenarios.get(potentialSourceCountry).add(adjacentCountry);		
					}
				}
			}
	
			if (attackScenarios.isEmpty()) {
				gameOn = false;
				System.out.println("There are currently no attack opportunities for " + this.playerName + ".. Sorry!\n");
				System.out.println("\n****Attack Phase Ends for player "+ this.playerName +"..****\n");
				return;
			}
		
			// Print all the options out for the player to see and choose from
			for (String keySourceCountry : attackScenarios.keySet()) {
				int maxAttackArmyCount = this.gameData.gameMap.getCountry(keySourceCountry).getCountryArmyCount() - 1 ;	
				if(maxAttackArmyCount >= 3) {
					maxAttackArmyCountPossiblePerSrcCountry.put(keySourceCountry, 3);
				} else if(maxAttackArmyCount >= 2) {
					maxAttackArmyCountPossiblePerSrcCountry.put(keySourceCountry, 2);
				} else {
					maxAttackArmyCountPossiblePerSrcCountry.put(keySourceCountry, 1);
				}		
				for (String correspondingDestinationCountry : attackScenarios.get(keySourceCountry)) {
					int defenderArmyCount = this.gameData.gameMap.getCountry(correspondingDestinationCountry).getCountryArmyCount();
					maxDefenseArmyCountPossiblePerDestCountry.putIfAbsent(correspondingDestinationCountry, defenderArmyCount);
					System.out.println("\n" + keySourceCountry + "\t -> \t" + correspondingDestinationCountry
							+ "\t **defended by " + defenderArmyCount  + " of " 
							+ gameData.getPlayer(this.gameData.gameMap.getCountry(correspondingDestinationCountry).getCountryConquerorID()).getPlayerName() + "'s armies**"
							+ "\t (attack with up to " + maxAttackArmyCountPossiblePerSrcCountry.get(keySourceCountry) + " armies)");
				}
			}
			
			String  selectedSourceCountry = null; 
			//String  selectedDefenderPlayer = null; 
			String  selectedDestinationCountry = null;
			String  selectedAttackerDiceCount = "1";
			String  selectedDefenderDiceCount = "1";
			Integer maxAllowedAttackerDiceCount = 1;
			Integer maxAllowedDefenderDiceCount = 1;
			
			// prompt Player for course of action to take
			boolean canAttack = false;
			
			while (!canAttack) {
				
				System.out.println("\n Which country would you like to attack?");
				if (input.hasNextLine()) {
					selectedDestinationCountry = input.nextLine();
				}
				
				if(!poolOfPotentialDestinationCountries.contains(selectedDestinationCountry)) {
					System.out.println("\n PLEASE ENTER A VALID ENEMY COUNTRY FROM THE OPTIONS ABOVE!");
					continue;
				}
				
				// if multiple fronts to choose from
				if(destinationCountriesWithMultiSharedBorders.contains(selectedDestinationCountry)) {
					System.out.println("Which country would you like to attack " + selectedDestinationCountry + " from?");
					if (input.hasNextLine()) {
						selectedSourceCountry = input.nextLine();
						// validate the actual pair
						if(!attackScenarios.get(selectedSourceCountry).contains(selectedDestinationCountry)) {
							System.out.println("\n PLEASE ENTER A VALID COUNTRY FROM THE OPTIONS ABOVE!");
							continue;
						}
					}		
				} else {
					// lookup and enforce single-option source country based on chosen destination
					for (String keySourceCountry : attackScenarios.keySet()) {
						for (String correspondingDestinationCountry : attackScenarios.get(keySourceCountry)) {
							if(correspondingDestinationCountry.equals(selectedDestinationCountry)) {
								selectedSourceCountry = keySourceCountry;
							}
						}
					}
					
				}
				
				maxAllowedAttackerDiceCount = maxAttackArmyCountPossiblePerSrcCountry.get(selectedSourceCountry);
		
				do {
					System.out.println("How many dice would you like to roll to attack " + selectedDestinationCountry 
							+ "\t (up to " + maxAllowedAttackerDiceCount + " dice)\n");
					if (input.hasNextLine()) {
						selectedAttackerDiceCount = input.nextLine();
					}	
				} while (isNaN(selectedAttackerDiceCount) || Integer.parseInt(selectedAttackerDiceCount) < 0 || Integer
						.parseInt(selectedAttackerDiceCount) > maxAllowedAttackerDiceCount );
							
				// if we've made it this far, means the criteria has been met and we should record state
				canAttack = true;	
			}
			
			// Now notify the defender and get their input on defense head-count
			System.out.println("\n HEADS-UP " 
								+ gameData.getPlayer(this.gameData.gameMap.getCountry(selectedDestinationCountry).getCountryConquerorID()).getPlayerName()  
								+ " YOU ARE UNDER ATTACK!");
			
			if(maxDefenseArmyCountPossiblePerDestCountry.get(selectedDestinationCountry) >= 2) {
				maxAllowedDefenderDiceCount = maxDefenseArmyCountPossiblePerDestCountry.get(selectedDestinationCountry);
				do {
					System.out.println("How many dice would you like to roll to defend " + selectedDestinationCountry 
							+ "\t (up to " + maxAllowedDefenderDiceCount + " dice)\n");
					if (input.hasNextLine()) {
						selectedDefenderDiceCount = input.nextLine();
					}	
				} while (isNaN(selectedDefenderDiceCount) || Integer.parseInt(selectedDefenderDiceCount) < 0 || Integer
						.parseInt(selectedDefenderDiceCount) > maxAllowedDefenderDiceCount );
			}
			
			System.out.println("\n ROLLING DICE \n");
			
			int[] attackerDiceRolls = new int[Integer.parseInt(selectedAttackerDiceCount)];
			int[] defenderDiceRolls = new int[Integer.parseInt(selectedDefenderDiceCount)];
	
			for (int a=0; a < Integer.parseInt(selectedAttackerDiceCount) ; a++) {
				attackerDiceRolls[a]= playerDice.rollDice();
				System.out.println("\n Attacker rolls: " + attackerDiceRolls[a] + "\n");
			}
			Arrays.sort(attackerDiceRolls);
			
			for (int d=0; d < Integer.parseInt(selectedDefenderDiceCount) ; d++) {
				defenderDiceRolls[d]= playerDice.rollDice();
				System.out.println("\n Defender rolls: " + defenderDiceRolls[d] + "\n");
			}
			Arrays.sort(defenderDiceRolls);
			
			for (int d=0; d < Integer.parseInt(selectedDefenderDiceCount) ; d++) {
				if(defenderDiceRolls[d] >= attackerDiceRolls[d]) {
					System.out.println("\n Attacker loses 1 army count\n");
					this.gameData.gameMap.getCountry(selectedSourceCountry).deductArmy(1);
				} else {
					this.gameData.gameMap.getCountry(selectedDestinationCountry).deductArmy(1);
					System.out.println("\n Defender loses 1 army count\n");
				}
				if(this.gameData.gameMap.getCountry(selectedDestinationCountry).getCountryArmyCount() == 0) {
					// declare new winner 
					this.gameData.gameMap.getCountry(selectedDestinationCountry).setConquerorID(playerID);
					this.gameData.gameMap.getCountry(selectedDestinationCountry).setArmyCount(Integer.parseInt(selectedAttackerDiceCount));
					System.out.println("\n" + this.playerName + " has conquered " + selectedDestinationCountry + " !");
					break;
				}
			}
			
			System.out.println("Army count for " + selectedSourceCountry + " is now: "
					+ this.gameData.gameMap.getCountry(selectedSourceCountry).getCountryArmyCount());
			System.out.println("Army count for " + selectedDestinationCountry + " is now: "
					+ this.gameData.gameMap.getCountry(selectedDestinationCountry).getCountryArmyCount());
		}
				
		System.out.println("\n****Attack Phase Ends for player "+ this.playerName +"..****\n");		
	}
		
	
	/**
	 * Method to guide the player through various fortification options when applicable.
	 */
	public void fortify() {

		System.out.println();
		System.out.println("**** Fortification Phase Begins for player "+ this.playerName +"..****\n");
		
		FortificationPhaseState  fortificationPhase = new FortificationPhaseState();
		fortificationPhaseState.add(fortificationPhase);
		notifyView();

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
		case "yeah":
			doFortify = true;
			break;
		case "y":
			doFortify = true;
			break;
		case "sure":
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
		
		fortificationPhase = new FortificationPhaseState();
		fortificationPhase.setFromCountry(fromCountry);
		fortificationPhase.setToCountry(toCountry);
		fortificationPhase.setNumberOfArmiesMoved(Integer.parseInt(noOfArmiesToMove));
		
		fortificationPhaseState.add(fortificationPhase);
		notifyView();
		
		fortificationPhaseState.clear();
		
		System.out.println("\n****Fortification Phase Ends for player "+ this.playerName +"..****\n");
	}
	
	/**
	 * Helper method to build a comprehensive map of all the possible fortification paths (for both immediate and extended neighbors)
	 * It has all the necessary checks and validation to ensure the path includes only countries owned by the given current player in the turn.
	 * Moreover, ensures that candidates suggested as "source countries" to move armies from satisfy the minimal requirements of army presence on the ground.
	 *
	 * @return Hashmap of all potential fortification scenarios for player.
	 */
	public HashMap<String, ArrayList<String>> getPotentialFortificationScenarios() {

		// Draft/prelim structure which contains only directly adjacent countries owned by the player
		HashMap<String, ArrayList<String>> prelimFortificationScenarios = new HashMap<String, ArrayList<String>>();
		// What will be returned: includes full (extended) paths of countries owned by the player
		HashMap<String, ArrayList<String>> allFortificationScenarios = new HashMap<String, ArrayList<String>>();

		// Step 1: get the comprehensive list of all countries currently conquered by the player
		HashSet<String> poolOfPotentialCountries = new HashSet<String>();
		poolOfPotentialCountries = this.gameData.gameMap.getConqueredCountriesPerPlayer(playerID);

		// Step 2: draw preliminary paths - irrespective of army counts & extended neighbors
		for (String potentialSourceCountry : poolOfPotentialCountries) {
			buildFortificationPath(prelimFortificationScenarios, potentialSourceCountry);
		}

		if (prelimFortificationScenarios.isEmpty()) {
			return null;
		}

		// Before we return the set, we have to slightly manipulate it 
		// to ensure the only keys featured are valid source countries which meet the min requirements (at least 2)
		for (String keySourceCountry : prelimFortificationScenarios.keySet()) {
			if (this.gameData.gameMap.getCountry(keySourceCountry).getCountryArmyCount() < 2) {
				continue;
			}
			for (String correspondingDestinationCountry : prelimFortificationScenarios.get(keySourceCountry)) {	
				allFortificationScenarios.putIfAbsent(keySourceCountry, new ArrayList<String>());
				allFortificationScenarios.get(keySourceCountry).add(correspondingDestinationCountry);
			}
		}
		
		// in case all countries conquered by player have 1 army count
		if (allFortificationScenarios.isEmpty()) {
			return null;
		}

		return allFortificationScenarios;
	}
	
	/**
	 * Small helper method to ensure countries in scope are recursively traversed and included in the "path"
	 * This is NOT the final and full picture for fortification scenarios, it is merely a stepping stone.
	 * The main getPotentialFortificationScenarios method eliminates the invalid source candidates based on army counts
	 *
	 * @param fortificationScenarios a HashStructure to be populated
	 * @param rootCountry country to be checked for adjacency
	 */
	
	public void buildFortificationPath(HashMap<String, ArrayList<String>> fortificationScenarios, String rootCountry) {
		ArrayList<String> longestConqueredPathFromRoot = new ArrayList<String>();
		longestConqueredPathFromRoot.add(rootCountry);
		traverseNeighbouringCountries(longestConqueredPathFromRoot, rootCountry);
		fortificationScenarios.put(rootCountry, longestConqueredPathFromRoot);	
	}
	
	/**
	 * Small helper method to recursively traverse all countries owned by player
	 * and build a steady path starting from the root country passed in.
	 * 
	 *
	 * @param longestConqueredPathFromRoot to be drawn based on adjacency and country ownership
	 * @param rootCountry country to serve as root of search
	 */
	public void traverseNeighbouringCountries(ArrayList<String> longestConqueredPathFromRoot, String rootCountry) {
		
		HashSet<String> adjacentCountries = new HashSet<String>();
		adjacentCountries = this.gameData.gameMap.getAdjacentCountries(rootCountry);
		for (String adjacentCountry : adjacentCountries) {
			// ensure adjacent country also owned by same player - otherwise no path to/through it
			if (this.gameData.gameMap.getCountry(adjacentCountry).getCountryConquerorID() == playerID 
					&& ! longestConqueredPathFromRoot.contains(adjacentCountry)) 
			{
				longestConqueredPathFromRoot.add(adjacentCountry);
				traverseNeighbouringCountries(longestConqueredPathFromRoot, adjacentCountry);
			}
		}
	}
	
	/**
	 * Getter of the player name.
	 * 
	 * @return the player name.
	 * */
	public String getPlayerName() {
		return this.playerName;
	}

	
	/**
	 * Getter of the player id.
	 * 
	 * @return the player id.
	 * */
	public Integer getPlayerID() {
		return this.playerID;
	}
	
	/**
	 * Getter of the order to play.
	 * 
	 * @return the order of playe.
	 * */
	public Integer getOrderOfPlay() {
		return orderOfPlay;
	}
	
	/**
	 * Setter of the order of play.
	 * 
	 * @param orderOfPlay order of play.
	 * */
	public void setOrderOfPlay(Integer orderOfPlay) {
		this.orderOfPlay = orderOfPlay;
	}

	public static void setCardExchangeArmyCount() {
		Player.cardExchangeArmyCount += 5;
	}

	public static int getCardExchangeArmyCount() {
		return cardExchangeArmyCount;
	}


	public ArrayList<Card> getPlayerCardList() {
		return cardList;
	}

	public void addToPlayerCardList(Card card) {
		this.cardList.add(card);
	}


	// acts as previous turn constructor to allow acess to the gamedata's data
	public void setGameData(GameData gamedata){
		this.gameData = gamedata;
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


	public ArrayList<AttackPhaseState> getAttackPhaseState() {
		return attackPhaseState;
	}

	public ArrayList<ReinforcementPhaseState> getReinforcementPhaseState() {
		return reinforcementPhaseState;
	}

	public ArrayList<FortificationPhaseState> getFortificationPhaseState() {
		return fortificationPhaseState;
	}


}
