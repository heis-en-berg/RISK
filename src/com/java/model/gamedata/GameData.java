package com.java.model.gamedata;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import com.java.model.cards.CardsDeck;
import com.java.model.map.GameMap;
import com.java.model.player.Player;

public class GameData {

	public GameMap gameMap;
	private Integer noOfPlayers;
	private ArrayList<Player> players;

	public CardsDeck cardsDeck; /* For build 2 */

	public static final Integer MIN_PLAYERS = 2;
	public static final Integer MAX_PLAYERS = 6;
	
	public ArrayList<Player> getPlayers() {
		return this.players;
	}
	
	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public Integer getNoOfPlayers() {
		return noOfPlayers;
	}

	public void setNoOfPlayers(Integer noOfPlayers) {
		this.noOfPlayers = noOfPlayers;
	}

	/*
	 * Dummy Data Generator : To be used for development purpose only.
	 */

	private AtomicInteger playerIdAtomicInteger = new AtomicInteger(1);

	public GameData(){
		noOfPlayers = 2;
		players  = new ArrayList<>();
	}

	public void generateDummyData(){
		generatePlayers();
		generateGameMap();
	}

	private void generatePlayers(){
		for(int p = 1; p <= this.noOfPlayers; p++) {
			Player player = new Player(playerIdAtomicInteger.getAndIncrement(), "P" + playerIdAtomicInteger.get());
			this.players.add(player);
		}
	}

	private void generateGameMap() {
		assignCountriesToPlayers();
		//assignContinentsToPlayers();
		placeArmies();
	}

	private void assignCountriesToPlayers() {
		this.gameMap.setCountryConquerer("C1", 1);
		this.gameMap.setCountryConquerer("C2", 2);
		this.gameMap.setCountryConquerer("C3", 1);
		this.gameMap.setCountryConquerer("C4", 2);
		this.gameMap.setCountryConquerer("C5", 1);
		this.gameMap.setCountryConquerer("C6", 2);
	}

//	private void assignContinentsToPlayers(){
//		this.gameMap.setContinentConquerer("Continent1",1);
//		this.gameMap.setContinentConquerer("Continent2",1);
//	}

	private void placeArmies() {
		this.gameMap.getCountry("C1").addArmy(3);
		this.gameMap.getCountry("C2").addArmy(1);
		this.gameMap.getCountry("C3").addArmy(2);
		this.gameMap.getCountry("C4").addArmy(1);
		this.gameMap.getCountry("C5").addArmy(5);
		this.gameMap.getCountry("C6").addArmy(6);
	}

}
