package com.java.model.player;

import com.java.model.cards.ArmyType;
import com.java.model.cards.Card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class BenevolentMode extends PlayerStrategy {

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
            setCardExchangeArmyCount();

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
                setCardExchangeArmyCount();
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
                playerDeck.put(key,playerDeck.get(key)-3);
            }
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

        System.out.println("\n" + "Fetching potential fortification scenarios for " + this.playerName + "...\n");

        HashMap<String,ArrayList<String>> reversedPotentialFortificationScenarios = reversedPotentialFortificationScenarios();
        HashMap<String, ArrayList<String>> potentialFortificationScenarios = getPotentialFortificationScenarios();

        for (String key : potentialFortificationScenarios.keySet()){
            for(String country: potentialFortificationScenarios.get(key)) {
                reversedPotentialFortificationScenarios.putIfAbsent(country, new ArrayList<>());
                reversedPotentialFortificationScenarios.get(country).add(key);
            }
        }
        HashSet<String> potentialCountriesToBeFortified = new HashSet<>();
        for(String country: potentialFortificationScenarios.keySet()){
            ArrayList<String> countries = potentialFortificationScenarios.get(country);
            for (String cntry : countries) {
                potentialCountriesToBeFortified.add(cntry);
            }
        }

        String weakestCountry = "";
        Integer weakerCountryArmyCount = 0;
        Integer currentPlayerID = playerID;

            for(String country : potentialCountriesToBeFortified){
                Integer currentCountryArmyCount = gameData.gameMap.getCountry(country).getCountryArmyCount();

                HashSet<String> adjacentCountries = gameData.gameMap.getAdjacentCountries(country);
                Integer enemyArmyCount = 0;
                for(String adjCountry : adjacentCountries){
                    if(gameData.gameMap.getCountry(adjCountry).getCountryConquerorID() != currentPlayerID){
                        enemyArmyCount += gameData.gameMap.getCountry(adjCountry).getCountryArmyCount();
                    }
                }
                if(weakerCountryArmyCount < (enemyArmyCount - currentCountryArmyCount)){
                    weakerCountryArmyCount = (enemyArmyCount - currentCountryArmyCount);
                    weakestCountry = country;
                }
            }

        ArrayList<String> potentialArmySuppliers = reversedPotentialFortificationScenarios.get(weakestCountry);
        Integer maximumArmy = 0;
        Integer currentArmyCount;
        String armySupplier = "";
        for(String country: potentialArmySuppliers){
            currentArmyCount = ((gameData.gameMap.getCountry(country).getCountryArmyCount())-1);
            if(currentArmyCount > maximumArmy){
                maximumArmy = currentArmyCount;
                armySupplier = country;
            }
        }
        gameData.gameMap.getCountry(armySupplier).deductArmy(maximumArmy);
        gameData.gameMap.getCountry(weakestCountry).addArmy(maximumArmy);
        HashSet<String> conqueredCountryByThisPlayer = gameData.gameMap.getConqueredCountriesPerPlayer(currentPlayerID);
        System.out.println("Moved "+maximumArmy+" armies from "+armySupplier+" to "+weakestCountry);
        System.out.println("\nAn overview after Fortification.\n");
        for(String country: conqueredCountryByThisPlayer){
            System.out.println("Country: "+country+", Army Count: "+gameData.gameMap.getCountry(country).getCountryArmyCount());
        }
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
