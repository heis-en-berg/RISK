package com.java.controller.gameplay;

/**
 * ReinforcementPhase allows the player to position the armies in the player's owned countries and
 * to count the reinforcement army based on the number of territories and continents owned.
 */
public interface ReinforcementPhase {

	void startReinforcement();
	Integer calculateReinforcementArmy();
	void placeArmy(Integer reinforcementArmy);
	
}
