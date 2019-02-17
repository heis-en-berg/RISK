package com.java.view;

import com.java.controller.map.MapLoader;
import com.java.model.gamedata.GameData;

public class RiskGameDriver {

	public GameData gameData;

	public RiskGameDriver() {
		gameData = new GameData();
		MapLoader maploader = new MapLoader();
		gameData.gameMap = maploader.loadMap();
		gameData.generateDummyData();
	}

	public void startGame() {
		// TODO Auto-generated method stub
	}
	
	private void initiateStartUpPhase() {
		// TODO Auto-generated method stub
	}
	
	private void initiateRoundRobinBasedGamePlay() {
		// TODO Auto-generated method stub
	}
	
	private void startTurn() {
		// TODO Auto-generated method stub
	}
	
}
