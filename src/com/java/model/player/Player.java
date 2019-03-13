package com.java.model.player;


import com.java.model.cards.Card;
import com.java.model.gamedata.GameData;

import java.util.ArrayList;
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
public class Player {

	private Integer playerID;
	private String playerName;
	private Integer orderOfPlay;
	private ArrayList<Card> cardList;
	private static int cardExchangeArmyCount = 5;

	private Scanner input;
	private GameData gameData;

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

	}

	//-------------------TODO 1:  reinforcment actions: Starts here--------------------------------------
	public void startReinforcement() {

		ArrayList<Card> playerExchangeCards;
		playerExchangeCards = getValidCards();
		Integer totalReinforcementArmyCount = calculateTotalReinforcement(playerExchangeCards);
		placeArmy(totalReinforcementArmyCount);
	}
	//-------------------TODO 1:  reinforcment actions: Ends here--------------------


	/**
	 * The startTurn() method organizes the flow of the game by ordering phase-execution.
	 */
	public void startTurn() {
		input = new Scanner(System.in);
		startReinforcement();
		// startAttack(); For build 2.
		//fortify();
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


}
