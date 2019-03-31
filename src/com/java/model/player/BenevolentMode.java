package com.java.model.player;

import com.java.model.cards.ArmyType;
import com.java.model.cards.Card;
import com.java.model.map.Country;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class BenevolentMode extends PlayerStrategy {

    private Scanner input = new Scanner(System.in);
    public BenevolentMode(Integer playerID, String playerName) {

        super(playerID,playerName);

    }

    @Override
    public void executeReinforcement() {
        notifyView();

        Integer getReinforcementCountFromCards = getReinforcementCountFromValidCardsAI();
        Integer totalReinforcementArmyCount = getReinforcementCountFromCards + calculateReinforcementArmy();
        ReinforcementPhaseState reinforcementPhase = new ReinforcementPhaseState();
        reinforcementPhase.setNumberOfArmiesReceived(totalReinforcementArmyCount);

        reinforcementPhaseState.add(reinforcementPhase);

        notifyView();
        placeArmy(totalReinforcementArmyCount);

    }

    @Override
    public ArrayList<Card> getValidCards() {
        return null;
    }

    private int tradeCardsAI(ArrayList<Card> playerCardList){
        HashMap<Enum,Integer> playerDeck = new HashMap<>();
        Integer reinforcementAICount = 0;
        Integer infantryCount = 0;
        Integer cavalryCount = 0;
        Integer artilleryCount = 0;
        int minArmyType = 0;
        boolean isExchange = false;
        boolean isExtraTerritoryMatch = false;
        ArrayList<Card> toBeRemoved = new ArrayList<>();
        for(Enum armyType: ArmyType.values()){
            playerDeck.put(armyType,0);
        }
        for(Card card : playerCardList){
            if(card.getArmyType().ordinal() == 0){
                playerDeck.put(card.getArmyType(),++infantryCount);
            }
            if(card.getArmyType().ordinal() == 1){
                playerDeck.put(card.getArmyType(),++cavalryCount);
            }
            if(card.getArmyType().ordinal() == 2){
                playerDeck.put(card.getArmyType(),++artilleryCount);
            }
            minArmyType = Math.min(Math.min(infantryCount,cavalryCount),artilleryCount);
        }

        // Get the max possibilities of different cards
        while(minArmyType != 0){
            boolean isInfantry = false;
            boolean isCavalry = false;
            boolean isArtillery = false;
            for(Card card:playerCardList){
                if((card.getArmyType().equals(ArmyType.INFANTRY)) && (!isInfantry)){
                    if(card.getCountry().getCountryConquerorID() == playerID){
                        isExtraTerritoryMatch = true;
                    }
                    playerDeck.put(ArmyType.INFANTRY,--infantryCount);
                    toBeRemoved.add(card);
                    isInfantry = true;
                }
                if((card.getArmyType().equals(ArmyType.CAVALRY)) && (!isCavalry)){
                    if(card.getCountry().getCountryConquerorID() == playerID){
                        isExtraTerritoryMatch = true;
                    }
                    toBeRemoved.add(card);
                    playerDeck.put(ArmyType.CAVALRY,--cavalryCount);
                    isCavalry = true;
                }
                if((card.getArmyType().equals(ArmyType.ARTILLERY)) && (!isArtillery)){
                    if(card.getCountry().getCountryConquerorID() == playerID){
                        isExtraTerritoryMatch = true;
                    }
                    playerDeck.put(ArmyType.ARTILLERY,--artilleryCount);
                    toBeRemoved.add(card);
                    isArtillery = true;
                }
                if(isInfantry && isCavalry && isArtillery){
                    break;
                }
            }
            reinforcementAICount += getCardExchangeArmyCount();
            isExchange = true;
            minArmyType = Math.min(Math.min(infantryCount,cavalryCount),artilleryCount);
            for(Card card: toBeRemoved){
                playerCardList.remove(card);
                this.gameData.cardsDeck.setCard(card);
            }
            toBeRemoved.clear();
        }

        // Get the max possibilities of same cards
        for(Enum key :playerDeck.keySet()){
            while(playerDeck.get(key) > 2){
                reinforcementAICount += getCardExchangeArmyCount();
                int count = 0;
                for(Card card : playerCardList){
                    if((card.getArmyType().equals(key)) && (count < 3)){
                        if(card.getCountry().getCountryConquerorID() == playerID){
                            isExtraTerritoryMatch = true;
                        }
                        toBeRemoved.add(card);
                        count++;
                    }
                    if(count == 3){
                        for(Card removeCard: toBeRemoved){
                            playerCardList.remove(removeCard);
                            this.gameData.cardsDeck.setCard(removeCard);
                        }
                        toBeRemoved.clear();
                        break;
                    }
                }
                isExchange = true;
                playerDeck.put(key,playerDeck.get(key)-3);
            }
        }

        if(isExchange == true){
            setCardExchangeArmyCount();
        }
        if(isExtraTerritoryMatch == true){
            reinforcementAICount += 2;
        }
        return reinforcementAICount;
    }

    public Integer getReinforcementCountFromValidCardsAI() {

        ArrayList<Card> playerCardList = getPlayerCardList();

        Integer reinforcementArmyCardsCount = 0;

        if(playerCardList.size()>2){
            reinforcementArmyCardsCount = tradeCardsAI(playerCardList);
        }
        return reinforcementArmyCardsCount;
    }

    @Override
    public void placeArmy(Integer reinforcementArmy) {

        Integer currentPlayerID = playerID;
        HashSet<String> conqueredCountryByThisPlayer = gameData.gameMap.getConqueredCountriesPerPlayer(currentPlayerID);

        System.out.println();
        System.out.println("**** Reinforcement Phase Begins for player " + this.playerName + "..****\n");

        while (reinforcementArmy > 0) {
             String weakestCountry = "";
             Integer weakestArmyCount = Integer.MIN_VALUE;
             for(String country: conqueredCountryByThisPlayer){

                Integer currentCountryArmyCount = gameData.gameMap.getCountry(country).getCountryArmyCount();

                HashSet<String> adjacentCountries = gameData.gameMap.getAdjacentCountries(country);
                Integer enemyArmyCount = 0;
                for(String adjCountry : adjacentCountries){
                    if(gameData.gameMap.getCountry(adjCountry).getCountryConquerorID() != currentPlayerID){
                        enemyArmyCount += gameData.gameMap.getCountry(adjCountry).getCountryArmyCount();
                    }
                }
                 if(weakestArmyCount < (enemyArmyCount - currentCountryArmyCount)){
                     weakestArmyCount = (enemyArmyCount - currentCountryArmyCount);
                     weakestCountry = country;
                 }
            }
             gameData.gameMap.getCountry(weakestCountry).addArmy(1);
             reinforcementArmy -= 1;
        }
        System.out.println("\nReinforcement is done for player "+playerName+". Here is an overview. \n");
        for(String country: conqueredCountryByThisPlayer){
            System.out.println("Country: "+country+", Army Count: "+gameData.gameMap.getCountry(country).getCountryArmyCount());
        }
    }
    @Override
    public void executeAttack() {

        System.out.println("\n\n**** Attack Phase Begins for player " + this.playerName + "..****\n");
        System.out.println("Player "+playerName+" is Benevolent. The player never attacks");
    }

    @Override
    public void executeFortification() {
        System.out.println();
        System.out.println("**** Fortification Phase Begins for player " + this.playerName + "..****\n");

        FortificationPhaseState fortificationPhase = new FortificationPhaseState();
        fortificationPhaseState.add(fortificationPhase);
        notifyView();

        // First get confirmation from the player that fortification is desired.

        boolean doFortify = true;

        System.out.println("\n" + "I am a Cheater! , Going to double all my arimies where I dont own any neighbour countries " + this.playerName + "...\n");

        //get list of countries owned by player
        HashSet<String> countriesOwned = gameData.gameMap.getConqueredCountriesPerPlayer(playerID);

        HashSet<String> adacentCountries;

        boolean isCountryBelongsToMe = false;

        for(String entry : countriesOwned) {
            Country belongsToPlayer = new Country();
            // then check if thoes countries nebouires are nont owned by me
            adacentCountries = gameData.gameMap.getAdjacentCountries(entry);

            for(String adjEntry: adacentCountries){
                // check if the countries are not owned by me the player
                belongsToPlayer = gameData.gameMap.getCountry(adjEntry);

                if(belongsToPlayer.getCountryConquerorID() == playerID){
                    isCountryBelongsToMe = true;
                }

                if(isCountryBelongsToMe == true){
                    break;
                }

            }
            // when it is not in the list I own just double the army amount.
            if(isCountryBelongsToMe == false){
                gameData.gameMap.addArmyToCountry(belongsToPlayer.getCountryName(),belongsToPlayer.getCountryArmyCount() *2);

                fortificationPhase = new FortificationPhaseState();

                fortificationPhase.setFromCountry(belongsToPlayer.getCountryName());
                fortificationPhase.setToCountry(" I am Cheating, :) ");
                fortificationPhase.setNumberOfArmiesMoved(belongsToPlayer.getCountryArmyCount());

                fortificationPhaseState.add(fortificationPhase);

                notifyView();
            }

        }

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("\n****Fortification Phase Ends for player " + this.playerName + "..****\n");

        fortificationPhaseState.clear();
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

}

