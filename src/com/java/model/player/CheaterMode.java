package com.java.model.player;

import com.java.model.cards.Card;
import com.java.model.map.Country;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CheaterMode extends PlayerStrategy{

    public CheaterMode(Integer playerID, String playerName) {

        super(playerID,playerName);

    }
    @Override
    public void executeReinforcement() {
    	notifyView();

        ArrayList<Card> playerExchangeCards;
        playerExchangeCards = getValidCards();
        Integer totalReinforcementArmyCount =  2 *calculateTotalReinforcement(playerExchangeCards);

        ReinforcementPhaseState reinforcementPhase = new ReinforcementPhaseState();
        reinforcementPhase.setNumberOfArmiesReceived(totalReinforcementArmyCount);

        reinforcementPhaseState.add(reinforcementPhase);

        notifyView();
        placeArmy(totalReinforcementArmyCount);

    }

    @Override
    public ArrayList<Card> getValidCards() {
        return new ArrayList<Card>();
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
        System.out.println();
        System.out.println("**** Attack Phase Begins for player " + this.playerName + "..****\n");

        Boolean hasConnqueredAtleastOneCountry = true;

        while (gameOn) {

            AttackPhaseState attackPhaseState = new AttackPhaseState();
            attackPhaseState.setAttackingPlayer(this.playerName);
            this.attackPhaseState.add(attackPhaseState);
            notifyView();


            // Now fetch all attack possibilities for player
            System.out.println("\n" + "Fetching potential attack scenarios for " + this.playerName + "...\n");

            // K = my source country , V = list of other countries that i dont own ( as nebiours)
            HashMap<String, ArrayList<String>> attackScenarios = getPotentialAttackScenarios();

            if (attackScenarios.isEmpty()) {
                System.out
                        .println("There are currently no attack opportunities for " + this.playerName + ".. Sorry!\n");
                break;
            }

            // show PlayerStrategy all the options they have
            showAllAttackScenarios(attackScenarios);
            System.out.println("\nCheater is getting all countries");
            // TODO Now chose the country to attack from and in a loop conqure all the nebiours
            for(Map.Entry<String,ArrayList<String>> currCountry : attackScenarios.entrySet()){

                // get the arraylist here for that player at "i"
                ArrayList<String> attackingCountries = currCountry.getValue();

                // now iterate over each of the attacking countries
                for(String eachattackingCountry : attackingCountries){

                    // get the country object
                    Country countryObject = gameData.gameMap.getCountry(eachattackingCountry);

                    String defendingPlayerName = gameData
                            .getPlayer(countryObject.getCountryConquerorID()).getStrategyType().getPlayerName();

                    attackPhaseState.setDefendingPlayer(defendingPlayerName);
                    this.notifyView();

                    attackPhaseState.setAttackingCountry(countryObject.getCountryName());
                    this.notifyView();

                    // change the country conqurer to the current player's lit of countries owned
                    Integer oldConquererPlayerId = countryObject.getCountryConquerorID();
                    Integer newConquererPlayerId = this.playerID;
                    
                    this.gameData.gameMap.updateCountryConquerer(eachattackingCountry, oldConquererPlayerId, newConquererPlayerId);
                    System.out.println("\nCheater " + getPlayerName() + " is conquering " + eachattackingCountry + "  from " + defendingPlayerName);
                }
            }

            checkIfPlayerHasConqueredTheWorld();

        }

        if (hasConnqueredAtleastOneCountry) {
            Card card = gameData.cardsDeck.getCard();
            this.cardList.add(card);
            System.out.println("PlayerStrategy received 1 card => Army Type: " + card.getArmyType() + ", Country: " + card.getCountry().getCountryName());
            System.out.println("Total cards : " + this.cardList.size());
        }

        endAttack();
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
        return null; // not needed since each country will be automatically be attacked from
    }

    @Override
    public String getEnemyCountryToAttack(String selectedSourceCountry, HashMap<String, ArrayList<String>> attackScenarios) {
        return null; // Not needed
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
