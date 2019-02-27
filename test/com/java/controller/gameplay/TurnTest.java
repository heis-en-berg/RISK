package com.java.controller.gameplay;

import com.java.model.gamedata.GameData;
import com.java.model.map.GameMap;
import com.java.model.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TurnTest {

    private GameData gameData;
    private   Player player;
    private Turn turn;
    private ArrayList<Player> players;

    @Before
    public void setUp(){

        gameData = new GameData();
        players = new ArrayList<>();
        player = new Player(1,"P1");
        this.gameData.gameMap = new GameMap();
        players.add(player);
        this.gameData.setPlayers(players);
        this.gameData.gameMap.addContinent("Continent1",5);
        this.gameData.gameMap.addContinent("Continent2",6);
        this.gameData.gameMap.addCountry("C1","Continent1");
        this.gameData.gameMap.addCountry("C2", "Continent1");
        this.gameData.gameMap.addCountry("C3", "Continent1");
        this.gameData.gameMap.addCountry("C4", "Continent2");
        this.gameData.gameMap.addCountry("C5", "Continent2");
        this.gameData.gameMap.addCountry("C6", "Continent2");
        this.gameData.gameMap.setCountryConquerer("C1", 1);
        this.gameData.gameMap.setCountryConquerer("C2", 2);
        this.gameData.gameMap.setCountryConquerer("C3", 1);
        this.gameData.gameMap.setCountryConquerer("C4", 2);
        this.gameData.gameMap.setCountryConquerer("C5", 1);
        this.gameData.gameMap.setCountryConquerer("C6", 2);
        this.gameData.gameMap.setContinentConquerer("Continent1",1);
        this.gameData.gameMap.setContinentConquerer("Continent2",1);
        this.gameData.gameMap.getCountry("C1").addArmy(3);
        this.gameData.gameMap.getCountry("C2").addArmy(1);
        this.gameData.gameMap.getCountry("C3").addArmy(2);
        this.gameData.gameMap.getCountry("C4").addArmy(1);
        this.gameData.gameMap.getCountry("C5").addArmy(5);
        this.gameData.gameMap.getCountry("C6").addArmy(6);
    }
    @Test
    public void calculateReinforcementArmy() {

        turn = new Turn(player,gameData);
        int actual_value = turn.calculateReinforcementArmy();
        int expected_value = 14;
        assertEquals(expected_value,actual_value);

    }
}