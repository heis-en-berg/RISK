package com.java.controller.gameplay;

import com.java.controller.dice.Dice;
import com.java.model.gamedata.GameData;
import com.java.model.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Turn implements ReinforcementPhase, AttackPhase, FortificationPhase{

	public GameData gameData;
	public Player player;
	private Dice dice;
	private static final int MINIMUM_REINFORCEMENT_ARMY_NUMBER = 3;
	private static final int REINFORCEMENT_DIVISION_FACTOR = 3;

	public Turn(Player player, GameData gameData) {

		this.gameData = gameData;
		this.player = player;
		dice = new Dice(); //To be used in attack phase.
	}

	public void startTurn(){
		startReinforcement();
		//startAttack();      For build 2.
		startFortification();
	}

	/*
	 * Reinforcement Phase
	 * @see com.java.controller.gameplay.ReinforcementPhase
	 */
	@Override
	public void startReinforcement() {

		Integer totalReinforecementArmyCount = calculateReinforcementArmy();
		placeArmy(totalReinforecementArmyCount);
	}

	@Override
	public Integer calculateReinforcementArmy() {

		Integer totalReinforecementArmyCount = 0;
		Integer totalCountriesOwnedByPlayer;
		Integer currentPlayerID = player.getPlayerID();

		/*Count the total number of continents owned by the player and retrieve the continent's control value. */

		HashSet<String> conqueredContinentsPerPlayer= this.gameData.gameMap.getConqueredContinentsPerPlayer(currentPlayerID);
		for(String continent: conqueredContinentsPerPlayer){
			Integer controlValue = this.gameData.gameMap.getContinent(continent).getContinentControlValue();
			totalReinforecementArmyCount += controlValue;
		}

		/*Count the total number of countries owned by the player */
		totalCountriesOwnedByPlayer= this.gameData.gameMap.getConqueredCountriesPerPlayer(currentPlayerID).size();
		totalReinforecementArmyCount += totalCountriesOwnedByPlayer/REINFORCEMENT_DIVISION_FACTOR;
		/*
		 * totalReinforecementArmyCount will be the maximum of totalCountriesOwnedByPlayer/REINFORCEMENT_DIVISION_FACTOR
		 * and MINIMUM_REINFORCEMENT_ARMY_NUMBER.
		 */
		totalReinforecementArmyCount = Math.max(totalReinforecementArmyCount, MINIMUM_REINFORCEMENT_ARMY_NUMBER);
		System.out.println(totalReinforecementArmyCount);
		return totalReinforecementArmyCount;
	}

	@Override
	public void placeArmy(Integer reinforcementArmy) {
		// TODO Auto-generated method stub

	}

	/*
	 * Fortification Phase
	 * @see com.java.controller.gameplay.FortificationPhase
	 */
	@Override
	public void startFortification() {
		getPotentialFortificationScenarios();
		Integer noOfArmies = getNoOfArmiesToMove();
		String fromCountryName = choseCountryToFortifyfrom();
		String toCountryName = choseCountryToFortifyto();
		fortify(fromCountryName,toCountryName,noOfArmies);
		// TODO Auto-generated method stub

	}

	@Override
	public HashMap<String, ArrayList<Integer>> getPotentialFortificationScenarios() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String choseCountryToFortifyfrom() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String choseCountryToFortifyto() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getNoOfArmiesToMove() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean fortify(String fromCountryName, String toCountryName, Integer noOfArmies) {
		// TODO Auto-generated method stub
		return null;
	}
}



