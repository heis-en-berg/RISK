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

//        for (int i = 0; i < 10; i++) {
//			Card card = gameData.cardsDeck.getCard();
//			addToPlayerCardList(card);
//		}
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
        HashSet<String> countriesOwned = this.gameData.gameMap.getConqueredCountriesPerPlayer(currentPlayerID);

        System.out.println();
        System.out.println("**** Reinforcement Phase Begins for player " + this.playerName + "..****\n");

        while (reinforcementArmy > 0) {

            System.out.print(playerName + "'s Total Reinforcement Army Count Remaining -> ["
                    + String.valueOf(reinforcementArmy) + "]\n");

            /* Information about the countries owned by the player and enemy countries. */
            for (String countries : countriesOwned) {

                System.out.println("\nCountry owned by " + playerName + "-> " + countries + " & Army Count: "
                        + this.gameData.gameMap.getCountry(countries).getCountryArmyCount());

                HashSet<String> adjCountries = this.gameData.gameMap.getAdjacentCountries(countries);

                if (adjCountries.isEmpty()) {
                    System.out.println("No neighboring enemy country for country " + countries);
                }

                for (String enemyCountries : adjCountries) {
                    if (this.gameData.gameMap.getCountry(enemyCountries).getCountryConquerorID() != currentPlayerID) {
                        System.out.println("Neighboring Enemy country name: " + enemyCountries + " & Army Count: "
                                + this.gameData.gameMap.getCountry(enemyCountries).getCountryArmyCount());
                    }
                }
            }


            // Instead of asking for an input we are assigning the first country owned by cheater.
            String countryNameByUser = "";

            for (String countries : countriesOwned) {
                countryNameByUser = countries;
                break;
            }

            System.out.println("\nEnter the country name to place armies: " + countryNameByUser);

            /* Check for an invalid country name. */
            if (this.gameData.gameMap.getCountry(countryNameByUser) == null) {
                System.out.println("'" + countryNameByUser
                        + "' is an invalid country name. Please verify the country name from the list.\n\n");
                continue;
            }

            /*
             * Check for a valid country name, but the country belonging to a different
             * player.
             */
            if (this.gameData.gameMap.getCountry(countryNameByUser).getCountryConquerorID() != currentPlayerID) {
                System.out.println("'" + countryNameByUser
                        + "' does not belong to you yet!!. Please verify your countries owned from the list below.\n\n");
                continue;
            }

            /* Information about armies and placement of armies */
            System.out.println("Enter the number of armies to be placed, Remaining Army (" + reinforcementArmy + ") :");

            try {
                // We assign all the assigned armies to the first country in the set.
                Integer numberOfArmiesToBePlacedByUser = reinforcementArmy;

                System.out.println("Successful...Country chosen " + countryNameByUser + " ,Number of armies placed: "
                        + numberOfArmiesToBePlacedByUser + "\n\n");

                this.gameData.gameMap.addArmyToCountry(countryNameByUser, numberOfArmiesToBePlacedByUser);
                reinforcementArmy -= numberOfArmiesToBePlacedByUser;

                ReinforcementPhaseState reinforcementPhase = new ReinforcementPhaseState();
                reinforcementPhase.setToCountry(countryNameByUser);
                reinforcementPhase.setNumberOfArmiesPlaced(numberOfArmiesToBePlacedByUser);
                reinforcementPhaseState.add(reinforcementPhase);
                notifyView();

            } catch (NumberFormatException ex) {
                System.out.println(ex.getMessage() + ", please enter numeric values only!\n\n");
            }
        }
        /* End of reinforcement phase, Print the final overview. */
        System.out.println("Reinforcement Phase is now complete. Here's an overview: \n\n");
        for (String countries : countriesOwned) {
            System.out.println("Country owned by you: " + countries + " ,Army Count: "
                    + this.gameData.gameMap.getCountry(countries).getCountryArmyCount());
        }
        reinforcementPhaseState.clear();
        System.out.println("\n**** Reinforcement Phase Ends for player " + this.playerName + "..****\n");
    }

    @Override
    public void executeAttack() {

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

