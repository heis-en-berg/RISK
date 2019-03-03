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
	private Scanner input;

	public Turn(Player player, GameData gameData) {

		this.gameData = gameData;
		this.player = player;
		this.currentPlayerID = player.getPlayerID();
		dice = new Dice(); //To be used in attack phase.
		input = new Scanner(System.in);
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

		Integer currentPlayerID = player.getPlayerID();
		HashSet<String> countriesOwned = this.gameData.gameMap.getConqueredCountriesPerPlayer(currentPlayerID);

		this.clearConsole();
		System.out.println("Reinforcement Phase Begins..\n");
		try {
			sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		clearConsole();

		while (reinforcementArmy > 0){

			System.out.print("Total Reinforcement Army Count Remaining -> ["+ String.valueOf(reinforcementArmy)+"]\n");

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
				System.out.println("'"+countryNameByUser+"' is an invalid country name. Please verify the country name from the list.\n\n");
				continue;
			}
			/*Check for a valid country name, but the country belonging to a different player.*/
			if(this.gameData.gameMap.getCountry(countryNameByUser).getCountryConquerorID() != currentPlayerID){
				clearConsole();
				System.out.println("'"+countryNameByUser + "' does not belong to you yet!!. Please verify your countries owned from the list below.\n\n");
				continue;
			}

			/*Information about armies and placement of armies*/
			System.out.println("Enter the number of armies to be placed, Remaining Army ("+reinforcementArmy+") :");
			try {
				Integer numberOfArmiesToBePlacedByUser = Integer.parseInt(input.nextLine());
				if (numberOfArmiesToBePlacedByUser > reinforcementArmy) {
					clearConsole();
					System.out.println("Input value '"+numberOfArmiesToBePlacedByUser+ "' should not be greater than the Total Reinforcement Army Count "+"("+String.valueOf(reinforcementArmy)+")\n\n");
					continue;
				}
				if(!(numberOfArmiesToBePlacedByUser > 0)){
					clearConsole();
					System.out.println("Please input a value greater than 0.\n\n");
					continue;
				}
				clearConsole();
				System.out.println("Successful...Country chosen " + countryNameByUser + " ,Number of armies placed: " + numberOfArmiesToBePlacedByUser + "\n\n");
				this.gameData.gameMap.getCountry(countryNameByUser).addArmy(numberOfArmiesToBePlacedByUser);
				reinforcementArmy -= numberOfArmiesToBePlacedByUser;
			}catch (NumberFormatException ex){
				this.clearConsole();
				System.out.println(ex.getMessage()+", please enter numeric values only!\n\n");
				continue;
			}
		}
		/*End of reinforcement phase, Print the final overview.*/
		clearConsole();
		System.out.println("Reinforcement Phase is now complete. Here's an overview: \n\n");
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
		// TODO Auto-generated method stub
	}

	@Override
	public HashMap<String, ArrayList<String>> getPotentialFortificationScenarios() {

		// TODO Auto-generated method stub
		return null;

	}
}
