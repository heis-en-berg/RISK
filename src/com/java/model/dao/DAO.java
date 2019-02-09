package com.java.model.dao;

import java.util.ArrayList;

import com.java.model.cards.CardsDeck;
import com.java.model.map.Continent;
import com.java.model.map.Country;
import com.java.model.map.GameMap;
import com.java.model.player.Player;

public class DAO {

	public GameMap gameMap;
	public Integer noOfPlayers;
	public ArrayList<Player> players;
	public CardsDeck cardsDeck; /* For build 2 */
	
	public static final Integer MIN_PLAYERS = 2;
	public static final Integer MAX_PLAYERS = 6;
	
	/* 
	 * Dummy Data Generator : To be used for development purpose only. 
	 */
	
	public DAO(){
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
			Player player = new Player("P" + p);
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
		this.gameMap.addAdjacentCountry("C1", "C2");
		this.gameMap.addAdjacentCountry("C2", "C1");
		this.gameMap.addAdjacentCountry("C2", "C3");
		this.gameMap.addAdjacentCountry("C2", "C6");
		this.gameMap.addAdjacentCountry("C3", "C2");
		this.gameMap.addAdjacentCountry("C3", "C5");
		this.gameMap.addAdjacentCountry("C4", "C5");
		this.gameMap.addAdjacentCountry("C4", "C6");
		this.gameMap.addAdjacentCountry("C5", "C4");
		this.gameMap.addAdjacentCountry("C5", "C6");
		this.gameMap.addAdjacentCountry("C5", "C3");
		this.gameMap.addAdjacentCountry("C6", "C5");
		this.gameMap.addAdjacentCountry("C6", "C4");
		this.gameMap.addAdjacentCountry("C6", "C2");
	}
	
	private void generateContinents() {
			Continent continent1 = new Continent("Continent1", 3);
			continent1.addCountry("C1");
			continent1.addCountry("C2");
			continent1.addCountry("C3");
			
			Continent continent2 = new Continent("Continent2", 4);
			continent2.addCountry("C4");
			continent2.addCountry("C5");
			continent2.addCountry("C6");
			
			this.gameMap.addContinent(continent1);
			this.gameMap.addContinent(continent2);
	}
	
	private void assignCountriesToPlayers() {
		this.players.get(0).addConqueredCountry("Continent1", "C1");
		this.players.get(0).addConqueredCountry("Continent1", "C3");
		this.players.get(0).addConqueredCountry("Continent2", "C5");
		this.players.get(1).addConqueredCountry("Continent1", "C2");
		this.players.get(1).addConqueredCountry("Continent2", "C4");
		this.players.get(1).addConqueredCountry("Continent2", "C6");
		
		this.gameMap.countries.get("C1").conquerorNameId = "P1";
		this.gameMap.countries.get("C2").conquerorNameId = "P2";
		this.gameMap.countries.get("C3").conquerorNameId = "P1";
		this.gameMap.countries.get("C4").conquerorNameId = "P2";
		this.gameMap.countries.get("C5").conquerorNameId = "P1";
		this.gameMap.countries.get("C6").conquerorNameId = "P2";
	}
	
	private void placeArmies() {
		this.gameMap.countries.get("C1").armyCount = 3;
		this.gameMap.countries.get("C2").armyCount = 1;
		this.gameMap.countries.get("C3").armyCount = 2;
		this.gameMap.countries.get("C4").armyCount = 1;
		this.gameMap.countries.get("C5").armyCount = 5;
		this.gameMap.countries.get("C6").armyCount = 6;
	}
	
}
