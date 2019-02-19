package com.java.controller.startup;

import java.util.ArrayList;

import com.java.model.cards.CardsDeck;
import com.java.model.gamedata.GameData;
import com.java.model.map.GameMap;
import com.java.model.player.Player;

public class StartUpPhase {

	public GameData gameData;
	
	public StartUpPhase(GameData gameDate) {
		this.gameData = new GameData();
	}
	
	public GameData start() {
		return null;
	}
	
	public Integer getNoOfPlayerFromUser() {
		return null;
	}
	
	public ArrayList<String> getPlayersNamesFromUser() {
		return null;
	}
	
	public ArrayList<Player> generatePlayers(ArrayList<String> playerNames){
		ArrayList<Player> newPlayers = new ArrayList<Player>();

		for(int i=0; i < playerNames.size(); i++){
			newPlayers.add(new Player(((Integer)i+1),playerNames.get(i)));
		}
		return newPlayers;
	}
	
	public void assignCountriesToPlayers() {
		
	}
	
	public CardsDeck generateCardsDeck() {
		return null;
	}
	
	public void placeArmies() {
		
	}
	
	public Integer getNumberOfArmiesToAllocate(Integer noOfPlayers) {
		return null;
	}
}
