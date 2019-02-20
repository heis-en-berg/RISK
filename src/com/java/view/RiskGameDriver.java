package com.java.view;

import com.java.controller.map.MapLoader;
import com.java.controller.startup.StartUpPhase;
import com.java.model.gamedata.GameData;
import com.java.model.player.Player;

import java.util.ArrayList;
import java.util.Scanner;

public class RiskGameDriver {

	private GameData gameData;
	private StartUpPhase startUp;


	public RiskGameDriver() {
		gameData = new GameData(); // needs this to load the map
		MapLoader maploader = new MapLoader(); // using this will load the map
		gameData.gameMap = maploader.loadMap();
		gameData.generateDummyData();
	}

	public void startGame() {
		// Call the helpers here
		initiateStartUpPhase();
	}
	
	private void initiateStartUpPhase() {
		// need to call the controller start uphase
		startUp = new StartUpPhase(gameData);
		Scanner input = new Scanner(System.in);
		int numOfPlayer =0;

		do {
			System.out.println("Note: You can only have players between 2 to 6");
			System.out.println("Enter the number of players: ");

			numOfPlayer = input.nextInt();

		} while((numOfPlayer < GameData.MIN_PLAYERS && numOfPlayer > GameData.MAX_PLAYERS));

		input.nextLine(); // move cursor

		// set values in the game data
		gameData.setNoOfPlayers(numOfPlayer);

		ArrayList<String> playerNames = new ArrayList<String>();

		for(int i=0; i<numOfPlayer; i++ ){
			System.out.println("Player " + (i+1) + " Enter your name: ");
			playerNames.add(input.next());
		}

		// array list of players returned when generated from the controller
		ArrayList<Player> players = startUp.generatePlayers(playerNames);

		// assign to game data the playerss
		gameData.setPlayers(players);

		// Country Assignment begins
		startUp.assignCountriesToPlayers();



	}

	private void initiateRoundRobinBasedGamePlay() {
		// TODO Auto-generated method stub
	}
	
	private void startTurn() {
		// TODO Auto-generated method stub
	}
	
}
