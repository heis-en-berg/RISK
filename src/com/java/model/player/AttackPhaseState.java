package com.java.model.player;

import java.util.ArrayList;

public class AttackPhaseState {
	private String attackingPlayer = null;
	private String defendingPlayer = null;
	private String attackingCountry = null;
	private String defendingCountry = null;
	private Integer attackerDiceCount = null;
	private Integer defenderDiceCount = null;
	private ArrayList<Integer> attackerDiceRollResults = null;
	private ArrayList<Integer> defenderDiceRollResults = null;
	private Integer attackerLostArmyCount = null;
	private Integer defenderLostArmyCount = null;
	private Boolean battleOutcomeFlag = null; // true if attacker conquers the territory else false;

	public AttackPhaseState() {
	}

	public String getAttackingPlayer() {
		return attackingPlayer;
	}

	public void setAttackingPlayer(String attackingPlayer) {
		this.attackingPlayer = attackingPlayer;
	}

	public String getDefendingPlayer() {
		return defendingPlayer;
	}

	public void setDefendingPlayer(String defendingPlayer) {
		this.defendingPlayer = defendingPlayer;
	}

	public String getAttackingCountry() {
		return attackingCountry;
	}

	public void setAttackingCountry(String attackingCountry) {
		this.attackingCountry = attackingCountry;
	}

	public String getDefendingCountry() {
		return defendingCountry;
	}

	public void setDefendingCountry(String defendingCountry) {
		this.defendingCountry = defendingCountry;
	}

	public Integer getAttackerDiceCount() {
		return attackerDiceCount;
	}

	public void setAttackerDiceCount(Integer attackerDiceCount) {
		this.attackerDiceCount = attackerDiceCount;
	}

	public Integer getDefenderDiceCount() {
		return defenderDiceCount;
	}

	public void setDefenderDiceCount(Integer defenderDiceCount) {
		this.defenderDiceCount = defenderDiceCount;
	}

	public ArrayList<Integer> getAttackerDiceRollResults() {
		return attackerDiceRollResults;
	}

	public void setAttackerDiceRollResults(ArrayList<Integer> attackerDiceRollResults) {
		this.attackerDiceRollResults = attackerDiceRollResults;
	}

	public ArrayList<Integer> getDefenderDiceRollResults() {
		return defenderDiceRollResults;
	}

	public void setDefenderDiceRollResults(ArrayList<Integer> defenderDiceRollResults) {
		this.defenderDiceRollResults = defenderDiceRollResults;
	}

	public Integer getAttackerLostArmyCount() {
		return attackerLostArmyCount;
	}

	public void setAttackerLostArmyCount(Integer attackerLostArmyCount) {
		this.attackerLostArmyCount = attackerLostArmyCount;
	}

	public Integer getDefenderLostArmyCount() {
		return defenderLostArmyCount;
	}

	public void setDefenderLostArmyCount(Integer defenderLostArmyCount) {
		this.defenderLostArmyCount = defenderLostArmyCount;
	}

	public Boolean getBattleOutcomeFlag() {
		return battleOutcomeFlag;
	}

	public void setBattleOutcomeFlag(Boolean battleOutcomeFlag) {
		this.battleOutcomeFlag = battleOutcomeFlag;
	}
}
