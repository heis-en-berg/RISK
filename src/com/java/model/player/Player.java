package com.java.model.player;

import java.util.ArrayList;
import java.util.HashMap;

import com.java.model.cards.Card;

public class Player {

	public String playerNameId;
	public ArrayList<Card> cards;
	public HashMap<String, ArrayList<String>> conqueredCountries;
	
	public Player(String playerName) {
		this.playerNameId = playerName;
		cards = new ArrayList<>();
		conqueredCountries = new HashMap<>();
	}
	
	public void addConqueredCountry(String continentNameId, String countryNameId) {
		if(!this.conqueredCountries.containsKey(continentNameId)) {
			this.conqueredCountries.put(continentNameId, new ArrayList<>());
		}
		this.conqueredCountries.get(continentNameId).add(countryNameId);
	}
	
}
