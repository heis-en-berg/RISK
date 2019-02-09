package com.java.controller.gameplay;

import java.util.ArrayList;
import java.util.HashMap;

import com.java.controller.dice.Dice;

public class Turn implements ReinforcementPhase, AttackPhase, FortificationPhase{

	private Dice dice = new Dice();
	
	/*
	 * Fortification Phase
	 * @see com.java.controller.gameplay.FortificationPhase
	 */
	
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

	/*
	 * Reinforcement Phase
	 * @see com.java.controller.gameplay.ReinforcementPhase
	 */
	
	@Override
	public void startReinforcement() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Integer calculateReinforcementArmy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void placeArmy(Integer reinforcementArmy) {
		// TODO Auto-generated method stub
		
	}

}
