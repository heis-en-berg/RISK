package com.java.model.map;

import com.java.model.player.Player;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class GameMapTest {


    @BeforeClass
    public static void beforeEverything() {

    }

    @Test
    public void calculateOwnershipPercentage() {
        GameMap gameMap = new GameMap();

        gameMap = new GameMap();

        Player player1 = new Player(1, "Player1");
        Player player2 = new Player(2, "Player2");

        ArrayList<Player> players = new ArrayList<Player>();
        players.add(player1);
        players.add(player2);

        gameMap.setupPlayerNames(players);

        gameMap.setMapAuthor("karan");
        gameMap.addContinent("Continent1", 10);
        gameMap.addContinent("Continent2", 20);
        gameMap.addCountry("Country1", "Continent1");
        gameMap.addCountry("Country2", "Continent1");
        gameMap.addCountry("Country3", "Continent2");
        gameMap.addCountry("Country4", "Continent2");
        gameMap.setAdjacentCountry("Country1", "Country2");
        gameMap.setAdjacentCountry("Country3", "Country4");


        gameMap.setCountryConquerer("Country1", 1);
        gameMap.setCountryConquerer("Country2", 1);
        gameMap.setCountryConquerer("Country3", 1);
        gameMap.setCountryConquerer("Country4", 2);

        gameMap.calculateOwnershipPercentage();

        HashMap<String,Double> ownershipPercentage = gameMap.getOwnershipPercentage();

        double percentagePlayer1 = ownershipPercentage.get("1");
        double percentagePlayer2 = ownershipPercentage.get("2");
        double expected_percentagePlayer1 = 75.0;
        double expected_percentagePlayer2 = 25.0;
        assertEquals(expected_percentagePlayer1,percentagePlayer1,0);
        assertEquals(expected_percentagePlayer2,percentagePlayer2,0);

    }
}