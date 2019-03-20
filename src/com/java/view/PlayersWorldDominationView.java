package com.java.view;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.java.model.Observable;

public class PlayersWorldDominationView implements GameView {
	
	private File playersWorldDominationFile;
    private FileWriter editView;
    
    public PlayersWorldDominationView() {
    	
    	playersWorldDominationFile = new File("./PhaseView.txt");

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
	public void update(Observable o) {
		// TODO Auto-generated method stub
		
	}

}
