package com.java.view;

import com.java.model.Observable;
import com.java.model.player.AttackPhaseState;
import com.java.model.player.FortificationPhaseState;
import com.java.model.player.Player;
import com.java.model.player.ReinforcementPhaseState;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class PhaseView implements GameView {
	
    private File phaseFile;
    private FileWriter editView;

    public PhaseView(){
       // phaseFile = new File("./src/com/java/view/PhaseView.txt");
    	phaseFile = new File("./PhaseView.txt");

        if(phaseFile.exists()){
            phaseFile.delete();
        }
        
        setUpFile();
    }
    
    private void setUpFile() {
    	try{
            phaseFile.createNewFile();
            editView = new FileWriter(phaseFile);
            editView.write(""); // to clear the view
            editView.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void update(Observable observable) {

        if(observable instanceof Player){
            
            ArrayList<ReinforcementPhaseState> reinforcementList = ((Player) observable).getReinforcementPhaseState();
            ArrayList<AttackPhaseState> attackList = ((Player) observable).getAttackPhaseState();
            ArrayList<FortificationPhaseState> fortificationList = ((Player) observable).getFortificationPhaseState();

            // reinforcement PHase view
            if(!reinforcementList.isEmpty()) {
                try {
                	setUpFile();
                    editView.write("\nCurrent Phase: Reinforcement Phase");
                    editView.write("\nCurrent Player: " + ((Player) observable).getPlayerName());
                    editView.write("\nActions:" );

                    editView.flush();
                    // iterate over reinforcementPhase and write to the file results
                    for (ReinforcementPhaseState eachReinforcement : reinforcementList) {
                    	Integer numberOfArmies = eachReinforcement.getNumberOfArmiesReceived();
                    	if(numberOfArmies > 0) {
                    		editView.write("\n  Number of Armies Received: " + numberOfArmies);
                            editView.flush();
                    	}
                    }
                    
                    for (ReinforcementPhaseState eachReinforcement : reinforcementList) {
                    	
                    	String country = eachReinforcement.getToCountry();
                    	Integer numberOfArmiesPlaced = eachReinforcement.getNumberOfArmiesPlaced();
                    	
                    	if(country != null) {
                    		editView.write("\n  ***********");
                            editView.write("\n  To Country: " + country);
                            editView.write("\n  Number of Armies Placed: " + numberOfArmiesPlaced );
                            editView.flush();
                    	}
                    }
                    editView.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (!attackList.isEmpty()) {
            	try {
           		 setUpFile();
                    editView.write("\nCurrent Phase: Attack Phase");
                    editView.write("\nCurrent Player: " + ((Player) observable).getPlayerName());
                    editView.write("\nActions:" );

                    editView.flush();
                    
                    // iterate over AttackPhaseState and write to the file results
                    for (AttackPhaseState eachAttack : attackList) {
                    	
                    	String attackingPlayer = ((Player) observable).getPlayerName();
                    	String defendingPlayer = eachAttack.getDefendingPlayer();

                    	String attackingCountry = eachAttack.getAttackingCountry();
                    	String defendingCountry = eachAttack.getDefendingCountry();

                    	Integer attackerDiceCount = eachAttack.getAttackerDiceCount();
                    	Integer defenderDiceCount = eachAttack.getDefenderDiceCount();
                    	ArrayList<Integer> attackerDiceRollResults = eachAttack.getAttackerDiceRollResults();
                    	ArrayList<Integer> defenderDiceRollResults = eachAttack.getDefenderDiceRollResults();
                    	
                    	if(defendingPlayer != null) {
                    		
                    		editView.write("\n  ***********");
                            editView.write("\n  Attacking player: " + attackingPlayer);
                            editView.write("\n  Defending player: " + defendingPlayer);
                            editView.write("\n  Attacking country: " + attackingCountry);
                            editView.write("\n  Defending country: " + defendingCountry);
                            editView.write("\n  Attacker dice count: " + attackerDiceCount);
                            editView.write("\n  Defending dice count: " + defenderDiceCount);
                            editView.write("\n  Attacker dice roll results: " + attackerDiceRollResults.toString());
                            editView.write("\n  Defender dice roll results: " + defenderDiceRollResults.toString());
                            editView.flush();
                        }
                    }
                    
                    editView.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }	
            	
            } else if (!fortificationList.isEmpty()) {
            	 try {
            		 setUpFile();
                     editView.write("\nCurrent Phase: Fortification Phase");
                     editView.write("\nCurrent Player: " + ((Player) observable).getPlayerName());
                     editView.write("\nActions:" );

                     editView.flush();
                     
                     // iterate over FortificationPhase and write to the file results
                     for (FortificationPhaseState eachFortification : fortificationList) {
                    	 
                    	 String fromCountry = eachFortification.getFromCountry();
                    	 String toCountry = eachFortification.getToCountry();
                    	 Integer numberOfArmiesMoved = eachFortification.getNumberOfArmiesMoved();
                    	 
                    	 if(fromCountry != null || toCountry != null) {
                    		 editView.write("\n  ***********");
                             editView.write("\n  From Country: " + fromCountry);
                             editView.write("\n  To Country: " + toCountry);
                             editView.write("\n  Number of armies moved: " + numberOfArmiesMoved);
                             editView.flush();
                    	 }
                     }
                     
                     editView.close();

                 } catch (IOException e) {
                     e.printStackTrace();
                 }	
            }
        }
    }
}
