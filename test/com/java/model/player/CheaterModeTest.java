
package com.java.model.player;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.BeforeClass;
import org.junit.Test;
import com.java.model.cards.Card;
import com.java.model.cards.CardsDeck;
import com.java.model.gamedata.GameData;
import com.java.model.map.Country;
import com.java.model.map.GameMap;

public class CheaterModeTest {
        private static GameData gameData;
        private static Player playerOne,playerTwo;
        private static ArrayList<Player> players;

        @BeforeClass
        static public void setUp() {
            // Load the game with a dummy map and dummy data.
            gameData = new GameData();
            gameData.gameMap = new GameMap();
            // Test data with two players, two continents and six countries.
            players = new ArrayList<Player>();
            playerOne   = new Player();
            playerOne.setStrategyType(new HumanMode(1, "P1"));
            playerTwo   = new Player();
            playerTwo.setStrategyType(new CheaterMode(2, "P2"));

            players.add(playerOne);
            players.add(playerTwo);

            gameData.gameMap.setupPlayerNames(players);

            gameData.gameMap.addContinent("Continent1", 5);
            gameData.gameMap.addContinent("Continent2", 6);
            gameData.gameMap.addCountry("C1", "Continent1");
            gameData.gameMap.addCountry("C2", "Continent1");
            gameData.gameMap.addCountry("C3", "Continent1");
            gameData.gameMap.addCountry("C4", "Continent2");
            gameData.gameMap.addCountry("C5", "Continent2");
            gameData.gameMap.addCountry("C6", "Continent2");

            // set adjacency as it's a pillar requirements for fortification functionality
            gameData.gameMap.setAdjacentCountry("C1", "C3");
            gameData.gameMap.setAdjacentCountry("C1", "C4");
            gameData.gameMap.setAdjacentCountry("C2", "C4");
            gameData.gameMap.setAdjacentCountry("C3", "C5");
            gameData.gameMap.setAdjacentCountry("C4", "C5");
            gameData.gameMap.setAdjacentCountry("C4", "C6");
            gameData.gameMap.setCountryConquerer("C1", 1);
            gameData.gameMap.setCountryConquerer("C2", 1);
            gameData.gameMap.setCountryConquerer("C3", 1);
            gameData.gameMap.setCountryConquerer("C4", 1);
            gameData.gameMap.setCountryConquerer("C5", 2);
            gameData.gameMap.setCountryConquerer("C6", 2);

            gameData.gameMap.addArmyToCountry("C1", 1);
            gameData.gameMap.addArmyToCountry("C2", 3);
            gameData.gameMap.addArmyToCountry("C3", 5);
            gameData.gameMap.addArmyToCountry("C4", 1);
            gameData.gameMap.addArmyToCountry("C5", 7);
            gameData.gameMap.addArmyToCountry("C6", 9);

            playerOne.getStrategyType().setGameData(gameData);
            playerTwo.getStrategyType().setGameData(gameData);

        }

        /**
         * Tests the received number of armies from fortify.
         */
        @Test
        public void testCalculateFortificationPaths() {

            ArrayList<Country> countryList = new ArrayList<Country>();

            HashMap<String, Integer> armiesPerPlayer = gameData.gameMap.getNumberOfArmiesPerPlayer();

            Integer numberOfInitialArmies = armiesPerPlayer.get("2");

            countryList.add(gameData.gameMap.getCountry("C1"));
            countryList.add(gameData.gameMap.getCountry("C2"));
            countryList.add(gameData.gameMap.getCountry("C3"));
            gameData.cardsDeck = new CardsDeck(countryList);
            for (int cardCount = 0; cardCount < 3; cardCount++) {
                Card card = gameData.cardsDeck.getCard();
                playerTwo.getStrategyType().addToPlayerCardList(card);
            }

            ArrayList<Card> playerExchangeCards = playerTwo.getStrategyType().getPlayerCardList();

            int numberOfLegalArmies = playerTwo.getStrategyType().calculateTotalReinforcement(playerExchangeCards);

            playerTwo.getStrategyType().executeReinforcement();

            armiesPerPlayer = gameData.gameMap.getNumberOfArmiesPerPlayer();

            Integer numberOfArmiesAfterReinforcement = armiesPerPlayer.get("2");

            Integer actualValue = numberOfArmiesAfterReinforcement - numberOfInitialArmies;

            Integer expectedValue = 2 * (numberOfLegalArmies - 5);

            assertEquals(actualValue, expectedValue);

        }

    @Test
    public void executeAttack() {
    }

    @Test
    public void executeFortification() {
    }
}