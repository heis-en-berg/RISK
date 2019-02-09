package com.java.controller.gameplay;

import java.util.ArrayList;
import java.util.HashMap;

public interface FortificationPhase {

	void startFortification();
	HashMap<Integer, ArrayList<Integer>> getPotentialFortificationScenarios();
	Integer choseCountryToFortifyfrom();
	Integer getNoOfArmiesToMove();
	Boolean fortify(Integer fromCountryId, Integer toCountryId, Integer noOfArmies);
	
}
