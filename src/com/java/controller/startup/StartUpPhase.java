package com.java.controller.startup;

import java.util.ArrayList;

import com.java.model.cards.CardsDeck;
import com.java.model.dao.DAO;
import com.java.model.map.GameMap;
import com.java.model.player.Player;

public class StartUpPhase {

	public DAO dao;
	
	public StartUpPhase() {
		dao = new DAO();
	}
	
	public DAO start() {
		return null;
	}
	
	private GameMap loadMap() {
		return null;
	}
	
	private Integer getNoOfPlayerFromUser() {
		return null;
	}
	
	private ArrayList<String> getPlayersNamesFromUser() {
		return null;
	}
	
	private ArrayList<Player> generatePlayers(ArrayList<String> playerNames){
		return null;
	}
	
	private void assignCountriesToPlayers() {
		
	}
	
	private CardsDeck generateCardsDeck() {
		return null;
	}
	
	private void placeArmies() {
		
	}
	
	private Integer getNumberOfArmiesToAllocate(Integer noOfPlayers) {
		return null;
	}
}
