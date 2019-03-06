package com.java.controller.gameplay;

/**
 * ReinforcementPhase allows the player to position the armies in the player's owned countries and
 * to count the reinforcement army based on the number of territories and continents owned.
 * @author Arnav Bhardwaj
 * @author Karan Dhingra
 * @author Ghalia Elkerdi
 * @author Sahil Singh Sodhi
 * @author Cristian Rodriguez
 * @version 1.0.0
 */
public interface ReinforcementPhase {

	void startReinforcement();
	Integer calculateReinforcementArmy();
	void placeArmy(Integer reinforcementArmy);
	
}
