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
            }


        }


    }
}
