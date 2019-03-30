package com.java.model.player;

import com.java.model.cards.Card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class HumanMode extends PlayerStrategy {

    public HumanMode() {
    }
    /**
     * The executeAttack() method encompasses the overall attack phase logic and flow
     * including both single and all-out mode. Attack phase ends when player either
     * no longer wants to attack, or cannot attack.
     */
    @Override
    public void executeReinforcement() {
        notifyView();

        ArrayList<Card> playerExchangeCards;
        playerExchangeCards = getValidCards();
        Integer totalReinforcementArmyCount = calculateTotalReinforcement(playerExchangeCards);

        ReinforcementPhaseState reinforcementPhase = new ReinforcementPhaseState();
        reinforcementPhase.setNumberOfArmiesReceived(totalReinforcementArmyCount);

        reinforcementPhaseState.add(reinforcementPhase);

        notifyView();
        placeArmy(totalReinforcementArmyCount);
    }
    @Override
    public void executeAttack() {
        System.out.println();
        System.out.println("**** Attack Phase Begins for player " + this.playerName + "..****\n");

        // implement an all-out mode
        boolean allOut = false;
        Boolean hasConnqueredAtleastOneCountry = false;

        while (gameOn) {

            AttackPhaseState attackPhase = new AttackPhaseState();
            attackPhase.setAttackingPlayer(this.playerName);
            attackPhaseState.add(attackPhase);
            notifyView();

            boolean wantToAttack = false;
            String playerDecision = "no";

            // First get confirmation from the player that attack is desired
            // unless player had specified "all out" mode

            System.out.println("\n Would you like to attack? (YES/NO)");
            if (input.hasNextLine()) {
                playerDecision = input.nextLine();
            }

            switch (playerDecision.toLowerCase()) {
                case "yes":
                case "yeah":
                case "y":
                case "sure":
                    wantToAttack = true;
                    break;
            }

            if (!wantToAttack) {
                System.out.println("\n" + this.playerName + "does not wish to attack..");
                attackPhaseState.clear();
                notifyView();
                break;
            }

            // Now fetch all attack possibilities for player
            System.out.println("\n" + "Fetching potential attack scenarios for " + this.playerName + "...\n");
            HashMap<String, ArrayList<String>> attackScenarios = getPotentialAttackScenarios();

            if (attackScenarios.isEmpty()) {
                System.out
                        .println("There are currently no attack opportunities for " + this.playerName + ".. Sorry!\n");
                break;
            }

            // show PlayerStrategy all the options they have
            showAllAttackScenarios(attackScenarios);
            String selectedSourceCountry = getCountryToAttackFrom(attackScenarios);
            attackPhase.setAttackingCountry(selectedSourceCountry);
            notifyView();

            String selectedDestinationCountry = getEnemyCountryToAttack(selectedSourceCountry, attackScenarios);
            attackPhase.setDefendingCountry(selectedDestinationCountry);
            notifyView();

            String defendingPlayer = gameData
                    .getPlayer(this.gameData.gameMap.getCountry(selectedDestinationCountry).getCountryConquerorID())
                    .getPlayerName();
            attackPhase.setDefendingPlayer(defendingPlayer);
            notifyView();

            // Check if attacking player wants to "go all out"
            if (!allOut) {
                System.out.println("\n Would you like to go all out? (YES/NO)");
                if (input.hasNextLine()) {
                    playerDecision = input.nextLine();
                }

                switch (playerDecision.toLowerCase()) {
                    case "yes":
                    case "yeah":
                    case "y":
                    case "sure":
                        allOut = true;
                        break;
                }
            }

            // Based on what mode the attack is set to happen in, these will be determined
            // differently
            Integer selectedAttackerDiceCount = 1;
            Integer selectedDefenderDiceCount = 1;

            // attack once
            if (!allOut) {
                // prompt attacker and defender for dice count preferences
                selectedAttackerDiceCount = getDesiredDiceCountFromPlayer(this.playerName, selectedSourceCountry,
                        "attack");
                attackPhase.setAttackerDiceCount(selectedAttackerDiceCount);
                notifyView();

                selectedDefenderDiceCount = getDesiredDiceCountFromPlayer(defendingPlayer, selectedDestinationCountry,
                        "defend");
                attackPhase.setDefenderDiceCount(selectedDefenderDiceCount);
                notifyView();

                rollDiceBattle(attackPhase);

                hasConnqueredAtleastOneCountry = fight(attackPhase) || hasConnqueredAtleastOneCountry;
            }

            // or keep attacking if all-out mode & player still can attack & player still
            // hasn't conquered target
            while (allOut && !attackPhase.getBattleOutcomeFlag()) {
                if (this.gameData.gameMap.getCountry(selectedSourceCountry).getCountryArmyCount() > 1) {
                    // dont prompt players for input, proceed with max allowed dice count for both
                    // players
                    selectedAttackerDiceCount = getActualMaxAllowedDiceCountForAction("attack", selectedSourceCountry,
                            3);
                    attackPhase.setAttackerDiceCount(selectedAttackerDiceCount);
                    selectedDefenderDiceCount = getActualMaxAllowedDiceCountForAction("defend",
                            selectedDestinationCountry, 2);
                    attackPhase.setDefenderDiceCount(selectedDefenderDiceCount);
                    rollDiceBattle(attackPhase);
                    hasConnqueredAtleastOneCountry = fight(attackPhase) || hasConnqueredAtleastOneCountry;
                } else {
                    break;
                }
            }
            allOut = false;

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

            System.out.println("\nEnter the country name to place armies: ");
            String countryNameByUser = input.nextLine();

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
                Integer numberOfArmiesToBePlacedByUser = Integer.parseInt(input.nextLine());

                if (numberOfArmiesToBePlacedByUser > reinforcementArmy) {
                    System.out.println("Input value '" + numberOfArmiesToBePlacedByUser
                            + "' should not be greater than the Total Reinforcement Army Count " + "("
                            + String.valueOf(reinforcementArmy) + ")\n\n");
                    continue;
                }

                if (!(numberOfArmiesToBePlacedByUser > 0)) {
                    System.out.println("Please input a value greater than 0.\n\n");
                    continue;
                }

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

    /**
     * Helper method to get and also validate the source country player wishes to
     * attack from.
     *
     * @param attackScenarios: the comprehensive list with all source and target
     *                         countries.
     * @return sourceCountry: a validated country option to attack from.
     */
    @Override
    public String getCountryToAttackFrom(HashMap<String, ArrayList<String>> attackScenarios) {
        String selectedSourceCountry = " ";

        do {
            System.out.println("Which one of your countries would you like to attack FROM?");
            if (input.hasNextLine()) {
                selectedSourceCountry = input.nextLine();
            }
        } while (!attackScenarios.containsKey(selectedSourceCountry));

        return selectedSourceCountry;
    }

    /**
     * Helper method to get and also validate the target country to be attacked.
     *
     * @param attackScenarios:       the comprehensive list with all source and target
     *                               countries.
     * @param selectedSourceCountry: a validated country option to attack from.
     * @return destinationCountry: a validated target country based on the source.
     */
    @Override
    public String getEnemyCountryToAttack(String selectedSourceCountry, HashMap<String, ArrayList<String>> attackScenarios) {
        {

            String selectedDestinationCountry = " ";

            do {
                System.out.println("\n Which enemy country would you like to attack from " + selectedSourceCountry + "?");
                if (input.hasNextLine()) {
                    selectedDestinationCountry = input.nextLine();
                }
            } while (!attackScenarios.get(selectedSourceCountry).contains(selectedDestinationCountry));

            return selectedDestinationCountry;
        }
    }
        /**
         * Helper method to get and also validate the attacker and defender's dice
         * selections. This is meant to be a generic functionality which tailors to both
         * actions.
         *
         * @param player: the player rolling the dice.
         * @param country: either the source country to attack from / the target country
         *        to be attacked
         * @param action: either attack/defense.
         * @return selectedDiceCount: a valid number of dice player can roll.
         *
         */
        @Override
        public Integer getDesiredDiceCountFromPlayer (String player, String country, String action){

            String selectedDiceCount = "1";

            // max # of dice a defender can roll is 2
            int defaultMaxDiceCountAllowedForAction = 2;

            // max # of dice an attacker can roll is 3
            if (action.equals("attack")) {
                defaultMaxDiceCountAllowedForAction = 3;
            }

            // but as countries are attacked, army counts changes and we need to dynamically
            // update thresholds
            int maxDiceCountAllowedForAction = getActualMaxAllowedDiceCountForAction(action, country,
                    defaultMaxDiceCountAllowedForAction);

            do {
                System.out.println("How many dice would " + player + " like to roll to " + action + "?" + "\t (up to "
                        + maxDiceCountAllowedForAction + " dice)\n");
                if (input.hasNextLine()) {
                    selectedDiceCount = input.nextLine();
                }
            } while (isNaN(selectedDiceCount) || Integer.parseInt(selectedDiceCount) < 1
                    || Integer.parseInt(selectedDiceCount) > maxDiceCountAllowedForAction);

            return Integer.parseInt(selectedDiceCount);
        }
    }
