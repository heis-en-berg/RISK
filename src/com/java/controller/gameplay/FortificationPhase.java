package com.java.controller.gameplay;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * FortificationPhase to build a comprehensive map of all the possible fortification paths and to allow one
 * fortification move per turn for a player.
 */
public interface FortificationPhase {

	HashMap<String, ArrayList<String>> getPotentialFortificationScenarios();
	void buildFortificationPath(HashMap<String, ArrayList<String>> fortificationScenarios, String rootCountry);
	
}
