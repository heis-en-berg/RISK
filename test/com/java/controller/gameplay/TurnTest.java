package com.java.controller.gameplay;

import com.java.model.gamedata.GameData;
import com.java.model.map.GameMap;
import com.java.model.player.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;


public class TurnTest {

    private GameData gameData;
    private Turn turn, turn2;
    private Player playerOne,playerTwo;
    private ArrayList<Player> players;

    @Before
    public void setUp() {

        // Load the game with a dummy map and data.
        gameData = new GameData();
        gameData.gameMap = new GameMap();

        // Test data with two players, two continents and six countries.
        players = new ArrayList<>();
        playerOne = new Player(1, "P1");
        playerTwo = new Player(2, "P2");
        players.add(playerOne);
        players.add(playerTwo);

        gameData.gameMap.addContinent("Continent1", 5);
        gameData.gameMap.addContinent("Continent2", 6);

        gameData.gameMap.addCountry("C1", "Continent1");
        gameData.gameMap.addCountry("C2", "Continent1");
        gameData.gameMap.addCountry("C3", "Continent1");
        gameData.gameMap.addCountry("C4", "Continent2");
        gameData.gameMap.addCountry("C5", "Continent2");
        gameData.gameMap.addCountry("C6", "Continent2");

        gameData.gameMap.setCountryConquerer("C1", 1);
        gameData.gameMap.setCountryConquerer("C2", 1);
        gameData.gameMap.setCountryConquerer("C3", 1);
        gameData.gameMap.setCountryConquerer("C4", 1);
        gameData.gameMap.setCountryConquerer("C5", 1);
        gameData.gameMap.setCountryConquerer("C6", 1);

        gameData.gameMap.setContinentConquerer("Continent1", 1);
        gameData.gameMap.setContinentConquerer("Continent2", 1);

        gameData.gameMap.getCountry("C1").addArmy(3);
        gameData.gameMap.getCountry("C2").addArmy(1);
        gameData.gameMap.getCountry("C3").addArmy(2);
        gameData.gameMap.getCountry("C4").addArmy(1);
        gameData.gameMap.getCountry("C5").addArmy(5);
        gameData.gameMap.getCountry("C6").addArmy(6);
    }

    /**
     * Calculate total reinforcement army with no conquered continents.
     */
    @Test
    public void testCalculateReinforcementArmyNoConqueredContinent() {

        turn = new Turn(playerTwo, gameData);
        int actual_value = turn.calculateReinforcementArmy();
        int expected_value = 3;
        assertEquals(expected_value, actual_value);
    }

    /**
     * Calculate total reinforcement army with conquered continents having control values.
     */
    @Test
    public void testCalculateReinforcementArmyWithConqueredContinent() {

        turn2 = new Turn(playerOne,gameData);
        int actual_value = turn2.calculateReinforcementArmy();
        int expected_value = 14;
        assertEquals(expected_value,actual_value);
    }
}
