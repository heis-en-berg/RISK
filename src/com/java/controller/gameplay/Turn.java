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
	private Dice dice = new Dice(); //To be used in attack phase.
	private static final int MINIMUM_REINFORCEMENT_ARMY_NUMBER = 3;
	private static final int REINFORCEMENT_DIVISION_FACTOR = 3;

	public Turn(GameData new_gameData, Player new_player) {

		this.gameData = new_gameData;
		this.player = new_player;
	}
	/*
	 * Fortification Phase
	 * @see com.java.controller.gameplay.FortificationPhase
	 */

	@Override
	public Integer calculateReinforcementArmy() {

		Integer totalReinforecementArmyCount;
		Integer totalCountriesOwnedByPlayer = 0;
		Integer currentPlayerID = player.getPlayerID();
		Integer totalControlValue=0;


		/*Count the total number of countries owned by the player */
		HashSet<String> conqueredCountriesPerPlayer= this.gameData.gameMap.getConqueredCountriesPerPlayer(currentPlayerID);
		totalCountriesOwnedByPlayer = conqueredCountriesPerPlayer.size();

		/*
		 * totalReinforecementArmyCount will be the maximum of totalCountriesOwnedByPlayer/REINFORCEMENT_DIVISION_FACTOR
		 * and MINIMUM_REINFORCEMENT_ARMY_NUMBER.
		 */
		totalReinforecementArmyCount = (totalCountriesOwnedByPlayer / REINFORCEMENT_DIVISION_FACTOR) > MINIMUM_REINFORCEMENT_ARMY_NUMBER ?
				totalCountriesOwnedByPlayer / REINFORCEMENT_DIVISION_FACTOR : MINIMUM_REINFORCEMENT_ARMY_NUMBER;

		/*Count the total number of continents owned by the player and retrieve the continent's control value. */

		HashSet<String> conqueredContinentsPerPlayer= this.gameData.gameMap.getConqueredContinentsPerPlayer(1);
		for(String continent: conqueredContinentsPerPlayer){
			Integer controlValue = this.gameData.gameMap.getContinent(continent).getContinentControlValue();
			totalControlValue = totalControlValue + controlValue;
		}
		totalReinforecementArmyCount = totalReinforecementArmyCount + totalControlValue;

		return totalReinforecementArmyCount;
	}

	@Override
	public void placeArmy(Integer reinforcementArmy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startReinforcement() {



	}

	@Override
	public void startFortification() {
		// TODO Auto-generated method stub

	}

	@Override
	public HashMap<Integer, ArrayList<Integer>> getPotentialFortificationScenarios() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer choseCountryToFortifyfrom() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getNoOfArmiesToMove() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean fortify(Integer fromCountryId, Integer toCountryId, Integer noOfArmies) {
		// TODO Auto-generated method stub
		return null;
	}
}

	/*
	 * Reinforcement Phase
	 * @see com.java.controller.gameplay.ReinforcementPhase
	 */

