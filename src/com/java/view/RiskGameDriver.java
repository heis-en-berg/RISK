package com.java.view;

import com.java.controller.map.MapCreator;
import com.java.model.gamedata.GameData;

public class RiskGameDriver {

	public GameData gameData;

	public RiskGameDriver() {
		gameData = new GameData();
		MapCreator mapCreator = new MapCreator();
		gameData.gameMap = mapCreator.loadMap();
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
