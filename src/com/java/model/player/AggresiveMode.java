package com.java.model.player;

import com.java.model.cards.Card;

import java.util.ArrayList;
import java.util.HashMap;

public class AggresiveMode extends PlayerStrategy {

    public AggresiveMode(Integer playerID, String playerName) {

        super(playerID,playerName);

    }

    @Override
    public void executeReinforcement() {

    }

    @Override
    public ArrayList<Card> getValidCards() {
        return null;
    }

    @Override
    public void placeArmy(Integer reinforcementArmy) {

    }

    @Override
    public void executeAttack() {

    }

    @Override
    public String getCountryToAttackFrom(HashMap<String, ArrayList<String>> attackScenarios) {
        return null;
    }

    @Override
    public String getEnemyCountryToAttack(String selectedSourceCountry, HashMap<String, ArrayList<String>> attackScenarios) {
        return null;
    }

    @Override
    public Integer getDesiredDiceCountFromPlayer(String player, String country, String action) {
        return null;
    }

    @Override
    public Integer getNumberofArmiesAttackerWantsToMove(String selectedSourceCountry) {
        return null;
    }

    @Override
    public void executeFortification() {

    }
}
