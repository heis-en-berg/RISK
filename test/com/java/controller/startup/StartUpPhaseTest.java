package com.java.controller.startup;

import com.java.controller.map.MapLoader;
import com.java.model.gamedata.GameData;
import com.java.model.map.GameMap;
import com.java.model.player.Player;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class StartUpPhaseTest {

    static GameData gameData;
    static StartUpPhase startUp;

    @BeforeAll
    public static void beforeEverything(){
        // now map
        gameData = new GameData();
        MapLoader maploader = new MapLoader();

        startUp = new StartUpPhase(gameData);
        gameData.setNoOfPlayers(6);
        gameData.gameMap = new GameMap();

        // add test data
        gameData.gameMap.addContinent("Continent1",5);
        gameData.gameMap.addContinent("Continent2",6);
        gameData.gameMap.addCountry("C1","Continent1");
        gameData.gameMap.addCountry("C2", "Continent1");
        gameData.gameMap.addCountry("C3", "Continent1");
        gameData.gameMap.addCountry("C4", "Continent2");
        gameData.gameMap.addCountry("C5", "Continent2");
        gameData.gameMap.addCountry("C6", "Continent2");
        gameData.gameMap.setCountryConquerer("C1", 1);
        gameData.gameMap.setCountryConquerer("C2", 2);
        gameData.gameMap.setCountryConquerer("C3", 1);
        gameData.gameMap.setCountryConquerer("C4", 2);
        gameData.gameMap.setCountryConquerer("C5", 1);
        gameData.gameMap.setCountryConquerer("C6", 2);
        gameData.gameMap.setContinentConquerer("Continent1",1);
        gameData.gameMap.setContinentConquerer("Continent2",1);
        gameData.gameMap.getCountry("C1").addArmy(3);
        gameData.gameMap.getCountry("C2").addArmy(1);
        gameData.gameMap.getCountry("C3").addArmy(2);
        gameData.gameMap.getCountry("C4").addArmy(1);
        gameData.gameMap.getCountry("C5").addArmy(5);
        gameData.gameMap.getCountry("C6").addArmy(6);
    }

    @Test
    void generatePlayers() {
        // create players
        ArrayList<Player> expectedPlayer = new ArrayList<Player>();
        expectedPlayer.add(new Player(1, "Karan"));
        expectedPlayer.add(new Player(2, "Arnav"));
        expectedPlayer.add(new Player(3, "Sahil"));
        expectedPlayer.add(new Player(4, "Cristian"));
        expectedPlayer.add(new Player(5, "Ghalia"));
        expectedPlayer.add(new Player(6, "Professor"));

        ArrayList<String> names = new ArrayList<String>();

        for (int i = 0; i<names.size(); i++){
            names.add(expectedPlayer.get(i).getPlayerName());
        }


        gameData.setPlayers(expectedPlayer);

        ArrayList<Player> actualPlayers = gameData.getPlayers();

        // check if the ids and names are same
        for (int i = 0; i<actualPlayers.size(); i++) {
            assertEquals(expectedPlayer.get(i).getPlayerID(), actualPlayers.get(i).getPlayerID());
            assertEquals(expectedPlayer.get(i).getPlayerName(), actualPlayers.get(i).getPlayerName());

        }
    }

    @Test
    void assignCountriesToPlayers() {
    }

    @Test
    void initialArmyCalculation() {
    }

    @Test
    void generateRoundRobin() {
    }
}