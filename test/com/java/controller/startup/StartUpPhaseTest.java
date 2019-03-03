package com.java.controller.startup;

import com.java.controller.map.MapLoader;
import com.java.model.gamedata.GameData;
import com.java.model.map.Country;
import com.java.model.map.GameMap;
import com.java.model.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class StartUpPhaseTest {

    static GameData gameData;
    static StartUpPhase startUp;
    static ArrayList<Player> expectedPlayer;

    @Before
    public void beforeEverything(){
        // now map
        gameData = new GameData();
        MapLoader maploader = new MapLoader();

        startUp = new StartUpPhase(gameData);
        gameData.setNoOfPlayers(6);
        gameData.gameMap = new GameMap();
        // create players
        expectedPlayer = new ArrayList<Player>();
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

        // Add test data
        gameData.gameMap.addCountry("C1","Continent1");
        gameData.gameMap.addCountry("C2", "Continent1");
        gameData.gameMap.addCountry("C3", "Continent1");
        gameData.gameMap.addCountry("C4", "Continent2");
        gameData.gameMap.addCountry("C5", "Continent2");
        gameData.gameMap.addCountry("C6", "Continent2");
    }

    @Test
    public void generatePlayers() {

        ArrayList<Player> actualPlayers = gameData.getPlayers();

        // check if the ids and names are same
        for (int i = 0; i<actualPlayers.size(); i++) {
            assertEquals(expectedPlayer.get(i).getPlayerID(), actualPlayers.get(i).getPlayerID());
            assertEquals(expectedPlayer.get(i).getPlayerName(), actualPlayers.get(i).getPlayerName());

        }
    }

    @Test
    public void assignCountriesToPlayers() {
        startUp.assignCountriesToPlayers();
        ArrayList<Player> players = gameData.getPlayers();

        // Used to obtain the country objects
        HashMap<String, Country> countryObject = gameData.gameMap.getAllCountries();

        // if one player id's country matches any of the other players meaning coutnries are assigned wrongly
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

    @Test
    public void initialArmyCalculation() {
    }

    @Test
    public void generateRoundRobin() {
    }
}