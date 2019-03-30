package com.java.controller.startup;

import com.java.model.gamedata.GameData;
import com.java.model.map.GameMap;
import com.java.model.player.HumanMode;
import com.java.model.player.Player;
import com.java.model.player.PlayerStrategy;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * StartUpPhaseTest class tests the important aspects of start up phase, for
 * instance: player generation, random country assigment to players, the number of initial army based on 
 * number of players and generate round robin.
 * 
 * @author Arnav Bhardwaj
 * @author Cristian Rodriguez
 * @version 2.0.0
 */
public class StartUpPhaseTest {
	/**
	 * Holds the game data.
	 * */
    static GameData gameData;
    
    /**
     * Start up phase object to be tested.
     * */
    static StartUpPhase startUp;
    
    /**
     * Expected players to be tested.
     * */
    static ArrayList<Player> expectedPlayer;
    
    /**
     * Holds the results of round robin in order to be tested.
     * */
    static ArrayList<Player> results;
    
    /**
     * Game data and a dummy map is created in order to test the methods which use this data.
     * */
    @BeforeClass
    public static void beforeEverything() {
        gameData = new GameData();
        gameData.setNoOfPlayers(6);
        gameData.gameMap = new GameMap();
        
        // create players
        results = new ArrayList<Player>();
        expectedPlayer = new ArrayList<Player>();
        Player player = new Player();
        player.setStrategyType(new HumanMode(1, "Karan"));
        expectedPlayer.add(player);
        player = new Player();
        player.setStrategyType(new HumanMode(2, "Arnav"));
        expectedPlayer.add(player);
        player = new Player();
        player.setStrategyType(new HumanMode(3, "Sahil"));
        expectedPlayer.add(player);
        player = new Player();
        player.setStrategyType(new HumanMode(4, "Cristian"));
        expectedPlayer.add(player);
        player = new Player();
        player.setStrategyType(new HumanMode(5, "Ghalia"));
        expectedPlayer.add(player);
        player = new Player();
        player.setStrategyType(new HumanMode(6, "Professor"));
        expectedPlayer.add(player);

        ArrayList<String> names = new ArrayList<String>();

        for (int i = 0; i<gameData.getNoOfPlayers(); i++){
            names.add(expectedPlayer.get(i).getStrategyType().getPlayerName());
        }
        
        gameData.setPlayers(expectedPlayer);

        //results = startUp.generateRoundRobin();

        // Add test data
        for(int i = 0 ; i < 42; i++) {
        	gameData.gameMap.addCountry("C" + i,"Continent1");
        }
        
        startUp = new StartUpPhase(gameData);
        
    }
    
    /**
     * Test the player generation by testing if the ids are given in a right way.
     * */
    @Test
    public void generatePlayers() {
    
        ArrayList<Player> actualPlayers = gameData.getPlayers();
        
        // check if the ids and names are same
        for (int i = 0; i < actualPlayers.size(); i++) {
            assertEquals(expectedPlayer.get(i).getStrategyType().getPlayerID(), actualPlayers.get(i).getStrategyType().getPlayerID());
            assertEquals(expectedPlayer.get(i).getStrategyType().getPlayerName(), actualPlayers.get(i).getStrategyType().getPlayerName());

        }
        
    }
    
    /**
     * Random assignation is tested by comparing if one country belongs to only one player.
     * */
    @Test
    public void assignCountriesToPlayers() {
    	
    	ArrayList<Player> players = gameData.getPlayers();
    	gameData.gameMap.setupPlayerNames(players);
    	startUp.assignCountriesToPlayers();

        // if one player id's country matches any of the other players meaning countries are assigned wrongly
        HashSet<String> countriesOwnedByPlayer0 =  gameData.gameMap.getConqueredCountriesPerPlayer(1);

        for(String country : countriesOwnedByPlayer0){
            for(Player player: players){
                if(player.getStrategyType().getPlayerID() == 1){
                    continue;
                }
                HashSet<String> countriesOwned =  gameData.gameMap.getConqueredCountriesPerPlayer(player.getStrategyType().getPlayerID());
                for (String countryNotExpected : countriesOwned){
                    assertNotEquals(country, countryNotExpected);
                }
            }
        }
        
    }
    
    /**
     * The initial number of army is given by a factor based on the number of countries. 
     * */
    @Test
    public void initialArmyCalculation() {
    	
    	Integer[] expectedValues = {40, 35, 30, 25, 20};
    	Integer[] data = {2, 3, 4, 5, 6};
    	Double factor =  ((double)gameData.gameMap.getNumberOfCountries()/42);
    	Integer result;
    	Integer expected;
    	
    	for(int i = 0; i < expectedValues.length; i++) {
    		result = startUp.initialArmyCalculation(data[i]);
    		expected = (int) (expectedValues[i] * factor);
    		assertEquals(expected, result);
    	}
    	
    }
    
    /**
     * The initial number of army is given by a factor based on the number of countries. 
     * */
    @Test
    public void initialArmyCalculationForMoreThan84Countries() {
    	
    	GameData localGameMap = new GameData();
    	
        localGameMap.setNoOfPlayers(6);
        
        localGameMap.gameMap = new GameMap();
        
        // create players
        results = new ArrayList<Player>();
        expectedPlayer = new ArrayList<Player>();
        Player player = new Player();
        player.setStrategyType(new HumanMode(1, "Karan"));
        expectedPlayer.add(player);
        player = new Player();
        player.setStrategyType(new HumanMode(2, "Arnav"));
        expectedPlayer.add(player);
        player = new Player();
        player.setStrategyType(new HumanMode(3, "Sahil"));
        expectedPlayer.add(player);
        player = new Player();
        player.setStrategyType(new HumanMode(4, "Cristian"));
        expectedPlayer.add(player);
        player = new Player();
        player.setStrategyType(new HumanMode(5, "Ghalia"));
        expectedPlayer.add(player);
        player = new Player();
        player.setStrategyType(new HumanMode(6, "Professor"));
        expectedPlayer.add(player);

        ArrayList<String> names = new ArrayList<String>();

        for (int i = 0; i<localGameMap.getNoOfPlayers(); i++){
            names.add(expectedPlayer.get(i).getStrategyType().getPlayerName());
        }
        
        localGameMap.setPlayers(expectedPlayer);

        //results = startUp.generateRoundRobin();

        // Add test data
        for(int i = 0 ; i < 84; i++) {
        	localGameMap.gameMap.addCountry("C" + i,"Continent1");
        }
        
        StartUpPhase startUpLocal = new StartUpPhase(localGameMap);
    	
    	
    	Integer[] expectedValues = {40, 35, 30, 25, 20};
    	
    	Integer[] data = {2, 3, 4, 5, 6};
    	
    	Double factor =  ((double)localGameMap.gameMap.getNumberOfCountries()/42);
    	
    	Integer result;
    	Integer expected;
    	
    	for(int i = 0; i < expectedValues.length; i++) {
    		result = startUpLocal.initialArmyCalculation(data[i]);
    		expected = (int) (expectedValues[i] * factor);
    		assertEquals(expected, result);
    	}
    	
    }
    
    /**
     * The initial number of army is given by a factor based on the number of countries. 
     * */
    @Test
    public void initialArmyCalculationForMoreThan126Countries() {
    	
    	GameData localGameMap = new GameData();
    	
        localGameMap.setNoOfPlayers(6);
        
        localGameMap.gameMap = new GameMap();
        
        // create players
        // create players
        results = new ArrayList<Player>();
        expectedPlayer = new ArrayList<Player>();
        Player player = new Player();
        player.setStrategyType(new HumanMode(1, "Karan"));
        expectedPlayer.add(player);
        player = new Player();
        player.setStrategyType(new HumanMode(2, "Arnav"));
        expectedPlayer.add(player);
        player = new Player();
        player.setStrategyType(new HumanMode(3, "Sahil"));
        expectedPlayer.add(player);
        player = new Player();
        player.setStrategyType(new HumanMode(4, "Cristian"));
        expectedPlayer.add(player);
        player = new Player();
        player.setStrategyType(new HumanMode(5, "Ghalia"));
        expectedPlayer.add(player);
        player = new Player();
        player.setStrategyType(new HumanMode(6, "Professor"));
        expectedPlayer.add(player);

        ArrayList<String> names = new ArrayList<String>();

        for (int i = 0; i<localGameMap.getNoOfPlayers(); i++){
            names.add(expectedPlayer.get(i).getStrategyType().getPlayerName());
        }
        
        localGameMap.setPlayers(expectedPlayer);

        //results = startUp.generateRoundRobin();

        // Add test data
        for(int i = 0 ; i < 126; i++) {
        	localGameMap.gameMap.addCountry("C" + i,"Continent1");
        }
        
        StartUpPhase startUpLocal = new StartUpPhase(localGameMap);
    	
    	
    	Integer[] expectedValues = {40, 35, 30, 25, 20};
    	
    	Integer[] data = {2, 3, 4, 5, 6};
    	
    	Double factor =  ((double)localGameMap.gameMap.getNumberOfCountries()/42);
    	
    	Integer result;
    	Integer expected;
    	
    	for(int i = 0; i < expectedValues.length; i++) {
    		result = startUpLocal.initialArmyCalculation(data[i]);
    		expected = (int) (expectedValues[i] * factor);
    		assertEquals(expected, result);
    	}
    	
    }
    
    /**
     * Round robin generation is tested by comparing the order of play of each player.
     * */
    @Test
    public void generateRoundRobin() {
    	
    	int expected;
    	int actual;
    	for(int i = 0; i < results.size(); i++) {
    		expected = (int)results.get(i).getStrategyType().getOrderOfPlay();
    		actual = i + 1;
    		assertEquals(expected, actual);
    	}
    	
    }
}