package com.java.model.player;

import com.java.model.cards.Card;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AggresiveMode extends PlayerStrategy {

    public AggresiveMode(Integer playerID, String playerName) {

        super(playerID,playerName);

    }

    @Override
    public void executeReinforcement() {

    }

    @Override
    public ArrayList<Card> getValidCards() {
        return null;
    }

    @Override
    public void placeArmy(Integer reinforcementArmy) {

    }

    @Override
    public void executeAttack() {

    }

    @Override
    public String getCountryToAttackFrom(HashMap<String, ArrayList<String>> attackScenarios) {
        return null;
    }

    @Override
    public String getEnemyCountryToAttack(String selectedSourceCountry, HashMap<String, ArrayList<String>> attackScenarios) {
        return null;
    }

    @Override
    public Integer getDesiredDiceCountFromPlayer(String player, String country, String action) {
        return null;
    }

    @Override
    public Integer getNumberofArmiesAttackerWantsToMove(String selectedSourceCountry) {
        return null;
    }

    @Override
    public void executeFortification() {
    	
    	System.out.println();
        System.out.println("**** Fortification Phase Begins for player " + this.playerName + "..****\n");

        System.out.println("\n" + "Fetching potential fortification scenarios for " + this.playerName + "...\n");

        HashMap<String, ArrayList<String>> potentialFortificationScenarios = getPotentialFortificationScenarios();
        
        if (potentialFortificationScenarios == null) {
            System.out.println(
                    "There are currently no fortification opportunities for " + this.playerName + ".. Sorry!\n");
            System.out.println("\n****Fortification Phase Ends for player " + this.playerName + "..****\n");
            return;
        }

        // if the list is empty or only contains a single scenario means we don't have anything to check/do 
        // that's because the hashmap of scenarios is keyed on source country to fortify from
        // and if there's only 1 scenario in total, means other countries only have 1 army on the ground and cant help
        if (potentialFortificationScenarios.isEmpty() || potentialFortificationScenarios.size() == 1) {
            System.out.println(
                    "There are currently no 'aggressive' fortification opportunities for " + this.playerName + ".. Sorry!\n");
            System.out.println("\n****Fortification Phase Ends for player " + this.playerName + "..****\n");
            return;
        }
        
        String strongestCountry = getStrongestCountryConqueredByPlayer(potentialFortificationScenarios);
        String secondStrongestCountry = getSecondStrongestCountryConqueredByPlayer(potentialFortificationScenarios,strongestCountry);


        Integer maxNoOfArmiesToMove = gameData.gameMap.getCountry(secondStrongestCountry).getCountryArmyCount() - 1;

        gameData.gameMap.getCountry(secondStrongestCountry).deductArmy(maxNoOfArmiesToMove);
        gameData.gameMap.getCountry(strongestCountry).addArmy(maxNoOfArmiesToMove);
        HashSet<String> conqueredCountryByThisPlayer = gameData.gameMap.getConqueredCountriesPerPlayer(playerID);
        System.out.println("Moved "+maxNoOfArmiesToMove+" armies from "+secondStrongestCountry+" to "+strongestCountry);
        System.out.println("\nAn overview after Fortification.\n");
        for(String country: conqueredCountryByThisPlayer){
            System.out.println("Country: "+country+", Army Count: "+gameData.gameMap.getCountry(country).getCountryArmyCount());
        }

    }
    
    public String getStrongestCountryConqueredByPlayer(HashMap<String, ArrayList<String>> potentialScenarios) {
		
    	String strongestCountry = null;
    	Integer maxArmyCountEncountered = 1;
        
        // find the strongest country conquered by the player, knowing it would be contained as a key 
        for (String country : potentialScenarios.keySet()){
        	Integer currentCountryArmyCount = gameData.gameMap.getCountry(country).getCountryArmyCount();
        	if (currentCountryArmyCount > maxArmyCountEncountered) {
        		strongestCountry = country;
        		maxArmyCountEncountered = currentCountryArmyCount;
        	}        
        }
        
    	return strongestCountry;
    }
    
    public String getSecondStrongestCountryConqueredByPlayer(HashMap<String, ArrayList<String>> potentialScenarios, String strongestCountry) {
		
    	String secondStrongestCountry = null;
    	Integer maxArmyCountEncountered = 1;
        
        // find second strongest country conquered by the player based on value passed in as strongest 
    	for(String country: potentialScenarios.get(strongestCountry)) {
        	Integer currentCountryArmyCount = gameData.gameMap.getCountry(country).getCountryArmyCount();
        	// only countries with 2 or more armies on the ground qualify as suppliers
        	if (currentCountryArmyCount > maxArmyCountEncountered) {
        		secondStrongestCountry = country;
        		maxArmyCountEncountered = currentCountryArmyCount;
        	}
        }
        
    	return secondStrongestCountry;
    }
    
    
    
}
