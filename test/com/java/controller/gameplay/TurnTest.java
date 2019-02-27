package com.java.controller.gameplay;

import com.java.model.gamedata.GameData;
import com.java.model.map.GameMap;
import com.java.model.player.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TurnTest {

    private GameData gameData;
    private   Player player;
    private Turn turn;

    @Before
    public void setUp(){

        gameData = new GameData();
        player = new Player(1,"Sahil");
        this.gameData.gameMap = new GameMap();
        this.gameData.players.add(player);
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
        int cal = turn.calculateReinforcementArmy();
        assertEquals(14,cal);

    }
}