package com.java.controller.startup;

import com.java.model.gamedata.GameData;
import com.java.model.map.GameMap;
import com.java.model.player.Player;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * StartUpPhaseTest class tests the important aspects of start up phase, for
 * instance: player generation, random country assigment to players, the number of initial army based on 
 * number of players and generate round robin.
 * 
 * @author Arnav Bhardwaj
 * @author Cristian Rodriguez
 * @version 1.0
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

        startUp = new StartUpPhase(gameData);
        gameData.setNoOfPlayers(6);
        gameData.gameMap = new GameMap();
        
        // create players
        results = new ArrayList<Player>();
        expectedPlayer = new ArrayList<Player>();
        expectedPlayer.add(new Player(1, "Karan"));
        expectedPlayer.add(new Player(2, "Arnav"));
        expectedPlayer.add(new Player(3, "Sahil"));
        expectedPlayer.add(new Player(4, "Cristian"));
        expectedPlayer.add(new Player(5, "Ghalia"));
        expectedPlayer.add(new Player(6, "Professor"));

        ArrayList<String> names = new ArrayList<String>();

        for (int i = 0; i<gameData.getNoOfPlayers(); i++){
            names.add(expectedPlayer.get(i).getPlayerName());
        }
        
        gameData.setPlayers(expectedPlayer);

        //results = startUp.generateRoundRobin();

        // Add test data
        gameData.gameMap.addCountry("C1","Continent1");
        gameData.gameMap.addCountry("C2", "Continent1");
        gameData.gameMap.addCountry("C3", "Continent1");
        gameData.gameMap.addCountry("C4", "Continent2");
        gameData.gameMap.addCountry("C5", "Continent2");
        gameData.gameMap.addCountry("C6", "Continent2");
        
    }
    
    /**
     * Test the player generation by testing if the ids are given in a right way.
     * */
    @Test
    public void generatePlayers() {
    
        ArrayList<Player> actualPlayers = gameData.getPlayers();
        
        // check if the ids and names are same
        for (int i = 0; i < actualPlayers.size(); i++) {
            assertEquals(expectedPlayer.get(i).getPlayerID(), actualPlayers.get(i).getPlayerID());
            assertEquals(expectedPlayer.get(i).getPlayerName(), actualPlayers.get(i).getPlayerName());

        }
        
    }
    
    /**
     * Random assignation is tested by comparing if one country belongs to only one player.
     * */
    @Test
    public void assignCountriesToPlayers() {
    	
        startUp.assignCountriesToPlayers();
        ArrayList<Player> players = gameData.getPlayers();

        // if one player id's country matches any of the other players meaning countries are assigned wrongly
        HashSet<String> countriesOwnedByPlayer0 =  gameData.gameMap.getConqueredCountriesPerPlayer(1);

        for(String country : countriesOwnedByPlayer0){
            for(Player player: players){
                if(player.getPlayerID() == 1){
                    continue;
                }
                HashSet<String> countriesOwned =  gameData.gameMap.getConqueredCountriesPerPlayer(player.getPlayerID());
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
    	Double factor = (double) (gameData.gameMap.getNumberOfCountries()/42);
    	Integer result;
    	Integer expected;
    	
    	for(int i = 0; i < expectedValues.length; i++) {
    		result = startUp.initialArmyCalculation(data[i]);
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
    		expected = (int)results.get(i).getOrderOfPlay();
    		actual = i + 1;
    		assertEquals(expected, actual);
    	}
    	
    }
}