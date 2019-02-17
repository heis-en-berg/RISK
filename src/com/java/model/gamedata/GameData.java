package com.java.model.gamedata;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import com.java.model.cards.CardsDeck;
import com.java.model.map.Continent;
import com.java.model.map.Country;
import com.java.model.map.GameMap;
import com.java.model.player.Player;

public class GameData {

	public GameMap gameMap;
	public Integer noOfPlayers;
	public ArrayList<Player> players;
	public CardsDeck cardsDeck; /* For build 2 */
	
	public static final Integer MIN_PLAYERS = 2;
	public static final Integer MAX_PLAYERS = 6;
	
	/* 
	 * Dummy Data Generator : To be used for development purpose only. 
	 */
	
	private AtomicInteger playerIdAtomicInteger = new AtomicInteger(0);
	
	public GameData(){
		gameMap = new GameMap();
		noOfPlayers = 2;
		players  = new ArrayList<>();
		generateDummyData();
	}
	
	private void generateDummyData(){
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
		generateCountries();
		setAdjacentCountries();
		generateContinents();
		assignCountriesToPlayers();
		placeArmies();
	}
	
	private void generateCountries() {
		Country country1 = new Country("C1", "Continent1");
		Country country2 = new Country("C2", "Continent1");
		Country country3 = new Country("C3", "Continent1");
		Country country4 = new Country("C4", "Continent2");
		Country country5 = new Country("C5", "Continent2");
		Country country6 = new Country("C6", "Continent2");
		this.gameMap.addCountry(country1);
		this.gameMap.addCountry(country2);
		this.gameMap.addCountry(country3);
		this.gameMap.addCountry(country4);
		this.gameMap.addCountry(country5);
		this.gameMap.addCountry(country6);
	}
	
	private void setAdjacentCountries() {
		this.gameMap.setAdjacentCountry("C1", "C2");
		this.gameMap.setAdjacentCountry("C2", "C1");
		this.gameMap.setAdjacentCountry("C2", "C3");
		this.gameMap.setAdjacentCountry("C2", "C6");
		this.gameMap.setAdjacentCountry("C3", "C2");
		this.gameMap.setAdjacentCountry("C3", "C5");
		this.gameMap.setAdjacentCountry("C4", "C5");
		this.gameMap.setAdjacentCountry("C4", "C6");
		this.gameMap.setAdjacentCountry("C5", "C4");
		this.gameMap.setAdjacentCountry("C5", "C6");
		this.gameMap.setAdjacentCountry("C5", "C3");
		this.gameMap.setAdjacentCountry("C6", "C5");
		this.gameMap.setAdjacentCountry("C6", "C4");
		this.gameMap.setAdjacentCountry("C6", "C2");
	}
	
	private void generateContinents() {
			Continent continent1 = new Continent("Continent1", 3);
			
			Continent continent2 = new Continent("Continent2", 4);
			
			this.gameMap.addContinent(continent1);
			this.gameMap.addContinent(continent2);
	}
	
	private void assignCountriesToPlayers() {
		this.gameMap.setCountryConquerer("C1", 1);
		this.gameMap.setCountryConquerer("C2", 2);
		this.gameMap.setCountryConquerer("C3", 1);
		this.gameMap.setCountryConquerer("C4", 2);
		this.gameMap.setCountryConquerer("C5", 1);
		this.gameMap.setCountryConquerer("C6", 2);
	}
	
	private void placeArmies() {
		this.gameMap.getCountry("C1").addArmy(3);
		this.gameMap.getCountry("C2").addArmy(1);
		this.gameMap.getCountry("C3").addArmy(2);
		this.gameMap.getCountry("C4").addArmy(1);
		this.gameMap.getCountry("C5").addArmy(5);
		this.gameMap.getCountry("C6").addArmy(6);
	}
	
}
