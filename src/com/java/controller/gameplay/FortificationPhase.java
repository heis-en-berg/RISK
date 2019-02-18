package com.java.controller.gameplay;

import java.util.ArrayList;
import java.util.HashMap;

public interface FortificationPhase {

	void startFortification();
	HashMap<String, ArrayList<Integer>> getPotentialFortificationScenarios();
	String choseCountryToFortifyfrom();
	String choseCountryToFortifyto();
	Integer getNoOfArmiesToMove();
	Boolean fortify(String fromCountryId, String toCountryId, Integer noOfArmies);
	
}
