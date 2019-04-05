package com.java.model.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

//import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

public class AggresiveMode extends PlayerStrategy {

    public AggresiveMode(Integer playerID, String playerName) {

        super(playerID,playerName);

    }

    // Reinforcement Phase Begins
    @Override
    public void executeReinforcement() {
        notifyView();

        Integer getReinforcementCountFromCards = getReinforcementCountFromValidCardsAI();
        Integer totalReinforcementArmyCount = getReinforcementCountFromCards + calculateReinforcementArmy();
        ReinforcementPhaseState reinforcementPhase = new ReinforcementPhaseState();
        reinforcementPhase.setNumberOfArmiesReceived(totalReinforcementArmyCount);

        reinforcementPhaseState.add(reinforcementPhase);

        notifyView();
        placeArmy(totalReinforcementArmyCount);

    }

    /**
     * Method to place armies.
     * @param reinforcementArmy the number of armies to be reinforced
     */
    @Override
    public void placeArmy(Integer reinforcementArmy) {

        Integer currentPlayerID = playerID;
        HashSet<String> conqueredCountryByThisPlayer = gameData.gameMap.getConqueredCountriesPerPlayer(currentPlayerID);

        System.out.println();
        System.out.println("**** Reinforcement Phase Begins for player " + this.playerName + "..****\n");

        String strongestCountry = "";
        Integer strongestCountryArmyCount = Integer.MIN_VALUE;
        Integer currentCountryArmyCount = 0;
        for(String country: conqueredCountryByThisPlayer){
            currentCountryArmyCount = gameData.gameMap.getCountry(country).getCountryArmyCount();
            if(strongestCountryArmyCount < currentCountryArmyCount){
                strongestCountry = country;
                strongestCountryArmyCount = currentCountryArmyCount;
            }
        }
        gameData.gameMap.getCountry(strongestCountry).addArmy(reinforcementArmy);
        System.out.println("\nReinforcement is done for player "+playerName+". Here is an overview. \n");
        for(String country: conqueredCountryByThisPlayer){
            System.out.println("Country: "+country+", Army Count: "+gameData.gameMap.getCountry(country).getCountryArmyCount());
        }
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
