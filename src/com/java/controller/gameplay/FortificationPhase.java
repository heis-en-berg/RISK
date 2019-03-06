package com.java.controller.gameplay;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * FortificationPhase to build a comprehensive map of all the possible fortification paths and to allow one
 * fortification move per turn for a player.
 *  * @author Arnav Bhardwaj
 *  * @author Karan Dhingra
 *  * @author Ghalia Elkerdi
 *  * @author Sahil Singh Sodhi
 *  * @author Cristian Rodriguez
 *  * @version 1.0.0
 */
public interface FortificationPhase {

	HashMap<String, ArrayList<String>> getPotentialFortificationScenarios();
	void buildFortificationPath(HashMap<String, ArrayList<String>> fortificationScenarios, String rootCountry);
	
}
