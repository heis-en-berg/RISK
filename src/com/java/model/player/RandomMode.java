package com.java.model.player;

import com.java.model.cards.ArmyType;
import com.java.model.cards.Card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;


public class RandomMode extends PlayerStrategy {

	private static Scanner input = new Scanner(System.in);;
	Random random;

	public RandomMode(Integer playerID, String playerName) {
		super(playerID, playerName);
		random = new Random();
	}

	/**
	 * The executeAttack() method encompasses the overall attack phase logic and
	 * flow including both single and all-out mode. Attack phase ends when player
	 * either no longer wants to attack, or cannot attack.
	 */
	@Override
	public void executeReinforcement() {
		notifyView();

		Integer getReinforcementCountFromCards = getReinforcementCountFromValidCardsAI();
		Integer totalReinforcementArmyCount = getReinforcementCountFromCards + calculateReinforcementArmy();
		ReinforcementPhaseState reinforcementPhase = new ReinforcementPhaseState();
		reinforcementPhase.setNumberOfArmiesReceived(totalReinforcementArmyCount);

		reinforcementPhaseState.add(reinforcementPhase);

		notifyView();
		placeArmy(totalReinforcementArmyCount);
	}

	/**
	 * Gets valid cards to trade.
	 *
	 * @return array of valid cards.
	 */

	@Override
	public ArrayList<Card> getValidCards() {
		return null;
	}

	private int tradeCardsAI(ArrayList<Card> playerCardList) {
		HashMap<Enum, Integer> playerDeck = new HashMap<>();
		Integer reinforcementAICount = 0;
		Integer infantryCount = 0;
		Integer cavalryCount = 0;
		Integer artilleryCount = 0;
		int minArmyType = 0;
		boolean isExtraTerritoryMatch = false;
		ArrayList<Card> toBeRemoved = new ArrayList<>();
		for (Enum armyType : ArmyType.values()) {
			playerDeck.put(armyType, 0);
		}
		for (Card card : playerCardList) {
			if (card.getArmyType().ordinal() == 0) {
				playerDeck.put(card.getArmyType(), ++infantryCount);
			}
			if (card.getArmyType().ordinal() == 1) {
				playerDeck.put(card.getArmyType(), ++cavalryCount);
			}
			if (card.getArmyType().ordinal() == 2) {
				playerDeck.put(card.getArmyType(), ++artilleryCount);
			}
			minArmyType = Math.min(Math.min(infantryCount, cavalryCount), artilleryCount);
		}

		// Get the max possibilities of different cards
		while (minArmyType != 0) {
			boolean isInfantry = false;
			boolean isCavalry = false;
			boolean isArtillery = false;
			for (Card card : playerCardList) {
				if ((card.getArmyType().equals(ArmyType.INFANTRY)) && (!isInfantry)) {
					if (card.getCountry().getCountryConquerorID() == playerID) {
						isExtraTerritoryMatch = true;
					}
					playerDeck.put(ArmyType.INFANTRY, --infantryCount);
					toBeRemoved.add(card);
					isInfantry = true;
				}
				if ((card.getArmyType().equals(ArmyType.CAVALRY)) && (!isCavalry)) {
					if (card.getCountry().getCountryConquerorID() == playerID) {
						isExtraTerritoryMatch = true;
					}
					toBeRemoved.add(card);
					playerDeck.put(ArmyType.CAVALRY, --cavalryCount);
					isCavalry = true;
				}
				if ((card.getArmyType().equals(ArmyType.ARTILLERY)) && (!isArtillery)) {
					if (card.getCountry().getCountryConquerorID() == playerID) {
						isExtraTerritoryMatch = true;
					}
					playerDeck.put(ArmyType.ARTILLERY, --artilleryCount);
					toBeRemoved.add(card);
					isArtillery = true;
				}
				if (isInfantry && isCavalry && isArtillery) {
					break;
				}
			}
			reinforcementAICount += getCardExchangeArmyCount();
			setCardExchangeArmyCount();

			minArmyType = Math.min(Math.min(infantryCount, cavalryCount), artilleryCount);
			for (Card card : toBeRemoved) {
				playerCardList.remove(card);
				this.gameData.cardsDeck.setCard(card);
			}
			toBeRemoved.clear();
		}

		// Get the max possibilities of same cards
		for (Enum key : playerDeck.keySet()) {
			while (playerDeck.get(key) > 2) {
				reinforcementAICount += getCardExchangeArmyCount();
				setCardExchangeArmyCount();
				int count = 0;
				for (Card card : playerCardList) {
					if ((card.getArmyType().equals(key)) && (count < 3)) {
						if (card.getCountry().getCountryConquerorID() == playerID) {
							isExtraTerritoryMatch = true;
						}
						toBeRemoved.add(card);
						count++;
					}
					if (count == 3) {
						for (Card removeCard : toBeRemoved) {
							playerCardList.remove(removeCard);
							this.gameData.cardsDeck.setCard(removeCard);
						}
						toBeRemoved.clear();
						break;
					}
				}
				playerDeck.put(key, playerDeck.get(key) - 3);
			}
		}

		if (isExtraTerritoryMatch == true) {
			reinforcementAICount += 2;
		}
		return reinforcementAICount;

	}

	public Integer getReinforcementCountFromValidCardsAI() {

		ArrayList<Card> playerCardList = getPlayerCardList();

		Integer reinforcementArmyCardsCount = 0;

		if (playerCardList.size() > 2) {
			reinforcementArmyCardsCount = tradeCardsAI(playerCardList);
		}
		return reinforcementArmyCardsCount;
	}

	/**
	 * PlaceArmy method allows the player to position the armies in the player's
	 * owned countries.
	 *
	 * @param reinforcementArmy total reinforcement army count to be placed by the
	 *                          current player.
	 */
	@Override
	public void placeArmy(Integer reinforcementArmy) {

		Integer currentPlayerID = playerID;
		HashSet<String> countriesOwned = this.gameData.gameMap.getConqueredCountriesPerPlayer(currentPlayerID);

		ArrayList<String> countriesOwnedList = new ArrayList<>(countriesOwned);

		System.out.println();
		System.out.println("**** Reinforcement Phase Begins for player " + this.playerName + "..****\n");

		while (reinforcementArmy > 0) {

			System.out.print(playerName + "'s Total Reinforcement Army Count Remaining -> ["
					+ String.valueOf(reinforcementArmy) + "]\n");

			Integer randomReinforcementCount = 0;
			Integer randomCountryIndex = random.nextInt(countriesOwnedList.size());

			String randomCountry = countriesOwnedList.get(randomCountryIndex);

			while (randomReinforcementCount == 0) {
				randomReinforcementCount = random.nextInt(reinforcementArmy + 1);
			}

			System.out.println("Successful...Country chosen " + randomCountry + " ,Number of armies placed: "
					+ randomReinforcementCount + "\n\n");

			this.gameData.gameMap.addArmyToCountry(randomCountry, randomReinforcementCount);
			reinforcementArmy -= randomReinforcementCount;

			ReinforcementPhaseState reinforcementPhase = new ReinforcementPhaseState();
			reinforcementPhase.setToCountry(randomCountry);
			reinforcementPhase.setNumberOfArmiesPlaced(randomReinforcementCount);
			reinforcementPhaseState.add(reinforcementPhase);
			notifyView();
		}
		/* End of reinforcement phase, Print the final overview. */
		System.out.println("Reinforcement Phase is now complete. Here's an overview: \n\n");
		for (String countries : countriesOwned) {
			System.out.println("Country owned by you: " + countries + " ,Army Count: "
					+ this.gameData.gameMap.getCountry(countries).getCountryArmyCount());
		}

		reinforcementPhaseState.clear();
		System.out.println("\n**** Reinforcement Phase Ends for player " + this.playerName + "..****\n");
	}

	@Override
	public void executeAttack() {
		System.out.println();
		System.out.println("**** Attack Phase Begins for player " + this.playerName + "..****\n");

		// implement an all-out mode
		Boolean hasConnqueredAtleastOneCountry = false;

		AttackPhaseState attackPhase = new AttackPhaseState();
		attackPhase.setAttackingPlayer(this.playerName);
		attackPhaseState.add(attackPhase);
		notifyView();

		// Now fetch all attack possibilities for player
		System.out.println("\n" + "Fetching potential attack scenarios for " + this.playerName + "...\n");
		HashMap<String, ArrayList<String>> attackScenarios = getPotentialAttackScenarios();

		if (attackScenarios.isEmpty()) {
			System.out.println("There are currently no attack opportunities for " + this.playerName + ".. Sorry!\n");
			return;
		}

		// show PlayerStrategy all the options they have
		showAllAttackScenarios(attackScenarios);
		String selectedSourceCountry = getCountryToAttackFrom(attackScenarios);
		attackPhase.setAttackingCountry(selectedSourceCountry);
		notifyView();

		String selectedDestinationCountry = getEnemyCountryToAttack(selectedSourceCountry, attackScenarios);
		attackPhase.setDefendingCountry(selectedDestinationCountry);
		notifyView();

		String defendingPlayer = gameData
				.getPlayer(this.gameData.gameMap.getCountry(selectedDestinationCountry).getCountryConquerorID())
				.getStrategyType().getPlayerName();
		attackPhase.setDefendingPlayer(defendingPlayer);
		notifyView();

		while (gameOn) {

			attackScenarios = getPotentialAttackScenarios();

			if (attackScenarios.isEmpty()) {
				System.out
						.println("There are currently no attack opportunities for " + this.playerName + ".. Sorry!\n");
				break;
			}

			if (!attackScenarios.containsKey(selectedSourceCountry)
					|| !attackScenarios.get(selectedSourceCountry).contains(selectedDestinationCountry)) {
				System.out.println("Attack from " + selectedSourceCountry + " to " + selectedDestinationCountry
						+ " not possible!!!");
				break;
			}

			System.out.println("\nAttacker decided to attack " + selectedDestinationCountry + " from "
					+ selectedSourceCountry + "!!!");

			Integer selectedAttackerDiceCount = 1;
			Integer selectedDefenderDiceCount = 1;

			selectedAttackerDiceCount = getDesiredDiceCountFromPlayer(this.playerName, selectedSourceCountry, "attack");
			attackPhase.setAttackerDiceCount(selectedAttackerDiceCount);
			notifyView();

			selectedDefenderDiceCount = getDesiredDiceCountFromPlayer(defendingPlayer, selectedDestinationCountry,
					"defend");
			attackPhase.setDefenderDiceCount(selectedDefenderDiceCount);
			notifyView();

			rollDiceBattle(attackPhase);

			hasConnqueredAtleastOneCountry = fight(attackPhase) || hasConnqueredAtleastOneCountry;

			Integer botsDecisionToAttack = random.nextInt(2);
			if (botsDecisionToAttack == 0) {
				gameOn = false;
			}

			checkIfPlayerHasConqueredTheWorld();

		}

		if (hasConnqueredAtleastOneCountry) {
			Card card = gameData.cardsDeck.getCard();
			this.cardList.add(card);
			System.out.println("PlayerStrategy received 1 card => Army Type: " + card.getArmyType() + ", Country: "
					+ card.getCountry().getCountryName());
			System.out.println("Total cards : " + this.cardList.size());
		}

		endAttack();
	}

	/**
	 * Helper method to get and also validate the source country player wishes to
	 * attack from.
	 *
	 * @param attackScenarios: the comprehensive list with all source and target
	 *        countries.
	 * @return sourceCountry: a validated country option to attack from.
	 */
	@Override
	public String getCountryToAttackFrom(HashMap<String, ArrayList<String>> attackScenarios) {

		ArrayList<String> countryList = new ArrayList<String>(attackScenarios.keySet());

		Integer randomCountryIndex = random.nextInt(countryList.size());

		return countryList.get(randomCountryIndex);
	}

	/**
	 * Helper method to get and also validate the target country to be attacked.
	 *
	 * @param attackScenarios: the comprehensive list with all source and target
	 *        countries.
	 * @param selectedSourceCountry: a validated country option to attack from.
	 * @return destinationCountry: a validated target country based on the source.
	 */
	@Override
	public String getEnemyCountryToAttack(String selectedSourceCountry,
			HashMap<String, ArrayList<String>> attackScenarios) {
		{

			ArrayList<String> enemyCountries = attackScenarios.get(selectedSourceCountry);

			Integer randomEnemyCountryIndex = random.nextInt(enemyCountries.size());

			return enemyCountries.get(randomEnemyCountryIndex);
		}
	}

	/**
	 * Helper method to get and also validate the attacker and defender's dice
	 * selections. This is meant to be a generic functionality which tailors to both
	 * actions.
	 *
	 * @param player: the player rolling the dice.
	 * @param country: either the source country to attack from / the target country
	 *        to be attacked
	 * @param action: either attack/defense.
	 * @return selectedDiceCount: a valid number of dice player can roll.
	 */
	@Override
	public Integer getDesiredDiceCountFromPlayer(String player, String country, String action) {

		String selectedDiceCount = "0";

		// max # of dice a defender can roll is 2
		int defaultMaxDiceCountAllowedForAction = 2;

		// max # of dice an attacker can roll is 3
		if (action.equals("attack")) {
			defaultMaxDiceCountAllowedForAction = 3;
		}

		// but as countries are attacked, army counts changes and we need to dynamically
		// update thresholds
		int maxDiceCountAllowedForAction = getActualMaxAllowedDiceCountForAction(action, country,
				defaultMaxDiceCountAllowedForAction);

		if (this.gameData.getPlayer(this.gameData.gameMap.getCountry(country).getCountryConquerorID())
				.getStrategyType() instanceof HumanMode) {

			do {
				System.out.println("How many dice would " + player + " like to roll to " + action + "?" + "\t (up to "
						+ maxDiceCountAllowedForAction + " dice)\n");
				if (input.hasNextLine()) {
					selectedDiceCount = input.nextLine();
				}
			} while (isNaN(selectedDiceCount) || Integer.parseInt(selectedDiceCount) < 1
					|| Integer.parseInt(selectedDiceCount) > maxDiceCountAllowedForAction);

		} else {

			while (selectedDiceCount.equals("0")) {
				selectedDiceCount = Integer.toString(random.nextInt(maxDiceCountAllowedForAction + 1));
			}

		}

		return Integer.parseInt(selectedDiceCount);
	}

	/**
	 * Obtain the number of armies that the attcker choses to move to the conqured
	 * country
	 *
	 * @param selectedSourceCountry pass the value they decide to move to
	 * @return value to move the amount of army
	 */
	@Override
	public Integer getNumberofArmiesAttackerWantsToMove(String selectedSourceCountry) {

		Integer selectedArmyCount = 0;
		int maxArmyCountAllowed = this.gameData.gameMap.getCountry(selectedSourceCountry).getCountryArmyCount() - 1;

		while (selectedArmyCount == 0) {
			selectedArmyCount = random.nextInt(maxArmyCountAllowed + 1);
		}

		return selectedArmyCount;
	}

	@Override
	public void executeFortification() {

		System.out.println();
		System.out.println("**** Fortification Phase Begins for player " + this.playerName + "..****\n");

		FortificationPhaseState fortificationPhase = new FortificationPhaseState();
		fortificationPhaseState.add(fortificationPhase);
		notifyView();

		System.out.println("\n" + "Fetching potential fortification scenarios for " + this.playerName + "...\n");

		// Now fetch all possibilities for player (this could get long as the game
		// progresses and more land is acquired)

		HashMap<String, ArrayList<String>> fortificationScenarios = getPotentialFortificationScenarios();

		if (fortificationScenarios == null || fortificationScenarios.isEmpty()) {
			System.out.println(
					"There are currently no fortification opportunities for " + this.playerName + ".. Sorry!\n");
			System.out.println("\n****Fortification Phase Ends for player " + this.playerName + "..****\n");
			return;
		}

		// This structure will accelerate and organize the army count process/validation
		HashMap<String, Integer> armiesPerPotentialFortificationSourceCountry = new HashMap<String, Integer>();

		// Print all the options out for the player to see and choose from
		for (String keySourceCountry : fortificationScenarios.keySet()) {
			armiesPerPotentialFortificationSourceCountry.put(keySourceCountry,
					this.gameData.gameMap.getCountry(keySourceCountry).getCountryArmyCount());
			// the range is one less because of the minimum requirement of having at least 1
			// army on the ground at all times.
			int possibleNumOfArmyRange = armiesPerPotentialFortificationSourceCountry.get(keySourceCountry) - 1;
			for (String correspondingDestinationCountry : fortificationScenarios.get(keySourceCountry)) {
				if (!correspondingDestinationCountry.equalsIgnoreCase(keySourceCountry)) {
					System.out.println("\n" + keySourceCountry + "\t -> \t" + correspondingDestinationCountry
							+ "\t (up to " + possibleNumOfArmyRange + " armies)");
				}
			}
		}

		ArrayList<String> sourceCountries = new ArrayList<String>(fortificationScenarios.keySet());
		String randomSourceCountry = sourceCountries.get(random.nextInt(sourceCountries.size()));
		fortificationPhase.setFromCountry(randomSourceCountry);
		notifyView();

		ArrayList<String> correspondingDestinationCountries = fortificationScenarios.get(randomSourceCountry);
		String randomCorrespondingDestinationCountry = correspondingDestinationCountries
				.get(random.nextInt(correspondingDestinationCountries.size()));
		fortificationPhase.setToCountry(randomCorrespondingDestinationCountry);
		notifyView();

		Integer possibleNumOfArmyRange = armiesPerPotentialFortificationSourceCountry.get(randomSourceCountry) - 1;
		Integer randomNumberOfArmiesToMove = random.nextInt(possibleNumOfArmyRange + 1);
		fortificationPhase.setNumberOfArmiesMoved(randomNumberOfArmiesToMove);
		notifyView();

		System.out.println("\nPlayer decided to move " + randomNumberOfArmiesToMove + " armies from "
				+ randomSourceCountry + " to " + randomCorrespondingDestinationCountry);

		this.gameData.gameMap.deductArmyToCountry(randomSourceCountry, randomNumberOfArmiesToMove);
		this.gameData.gameMap.addArmyToCountry(randomCorrespondingDestinationCountry, randomNumberOfArmiesToMove);

		System.out.println(
				"\nFortification Successful for " + this.playerName + ". Here is a summary of the new status-quo:\n");

		System.out.println("Army count for " + randomSourceCountry + " is now: "
				+ this.gameData.gameMap.getCountry(randomSourceCountry).getCountryArmyCount());
		System.out.println("Army count for " + randomCorrespondingDestinationCountry + " is now: "
				+ this.gameData.gameMap.getCountry(randomCorrespondingDestinationCountry).getCountryArmyCount());

		fortificationPhaseState.clear();

		System.out.println("\n****Fortification Phase Ends for player " + this.playerName + "..****\n");
	}
}
