package com.java.controller.gameplay;

import com.java.model.gamedata.GameData;
import com.java.model.map.GameMap;
import com.java.model.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TurnTest {

//    public static int getBoundIntegerFromUser(IntegerAsker asker) {
//        int input = asker.ask("\nEnter the country name to place armies: ");
//        while (input < 1 || input > 10)
//            input = asker.ask("Wrong number, try again.");
//        return input;
//    }

//    public static class IntegerAsker {
//        private final Scanner scanner;
//        private final PrintStream out;
//
//        public IntegerAsker(InputStream in, PrintStream out) {
//            scanner = new Scanner(in);
//            this.out = out;
//        }
//
//        public int ask(String message) {
//            out.println(message);
//            return scanner.nextInt();
//        }
//    }

    private GameData gameData;
    private Turn turn;
    private Player playerOne,playerTwo;
    private ArrayList<Player> players;

    @Before
    public void setUp() {

        gameData = new GameData();
        gameData.gameMap = new GameMap();

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

        turn = new Turn(playerOne, gameData);

    }

    @Test
    public void calculateReinforcementArmy() {

        int actual_value = turn.calculateReinforcementArmy();
        int expected_value = 14;
        assertEquals(expected_value, actual_value);

    }

    @Test
    public void placeArmy() {
//        turn.placeArmy(14);
//        IntegerAsker asker = mock(IntegerAsker.class);
//        when(asker.ask("\nEnter the country name to place armies: ")).thenReturn("C1");
//        when(asker.ask("Wrong number, try again.")).thenReturn(3);
//
//        getBoundIntegerFromUser(asker);
//
//        verify(asker).ask("Wrong number, try again.");

    }
}
