package com.java.model.player;

import java.util.ArrayList;

public class AttackPhaseState {
	public String attackingCountry = null;
	public String defendingCountry = null;
	public Integer attackerDiceCount = null;
	public Integer defenderDiceCount = null;
	public ArrayList<Integer> attackerDiceRollResults = null;
	public ArrayList<Integer> defenderDiceRollResults = null;
	public Integer attackerLostArmyCount = null;
	public Integer defenderLostArmyCount = null;
	public Boolean battleOutcomeFlag = null; // true if attacker conquers the territory else false;
}
