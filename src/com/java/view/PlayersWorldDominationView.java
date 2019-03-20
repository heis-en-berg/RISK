package com.java.view;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import com.java.model.Observable;
import com.java.model.map.GameMap;
import com.java.model.player.Player;

public class PlayersWorldDominationView implements GameView {
	
	private File playersWorldDominationFile;
    private FileWriter editView;
    
    public PlayersWorldDominationView() {
    	
    	playersWorldDominationFile = new File("./PlayersWorldDominationView.txt");

        if(playersWorldDominationFile.exists()){
            playersWorldDominationFile.delete();
        }
        
        setUpFile();
    }
    
    private void setUpFile() {
    	try{
            playersWorldDominationFile.createNewFile();
            editView = new FileWriter(playersWorldDominationFile);
            editView.write(""); // to clear the view
            editView.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    	
    }
    
	@Override
	public void update(Observable observable) {
		
		if(observable instanceof GameMap) {
			try {
            	setUpFile();
            	
            	HashMap<String,Double> ownershipPercentage = ((GameMap) observable).getOwnershipPercentage();
                editView.write("\nPercentage of the map controlled by every player:\n");
                
                for(Entry<String, Double> entry : ownershipPercentage.entrySet()) {
                	editView.write("\nPlayer: " + entry.getKey() + "\t\t\t" + String.format("%.2f", entry.getValue()) + "%");
                }
                
                editView.close();
                
			} catch (IOException e) {
                e.printStackTrace();
            }
		}
		
	}

}
