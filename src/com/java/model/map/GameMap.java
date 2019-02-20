package com.java.model.map;

import java.util.HashMap;
import java.util.HashSet;

public class GameMap {

	private HashMap<String, Country> countryObjects;
	private HashMap<String, Continent> continentObjects;
	private HashMap<String, HashSet<String>> adjacentCountries;
	private HashMap<String, HashSet<String>> continentCountries;
	private HashMap<Integer, HashSet<String>> conqueredCountriesPerPlayer;
	private HashMap<Integer, HashSet<String>> conqueredContinentsPerPlayer;
	private String mapAuthor;

	public String warn;


	public GameMap() {
		countryObjects = new HashMap<>();
		adjacentCountries = new HashMap<>();
		continentObjects = new HashMap<>();
		continentCountries = new HashMap<>();
		conqueredCountriesPerPlayer = new HashMap<>();
		conqueredContinentsPerPlayer = new HashMap<>();
	}

	public void addCountry(Country country) {
		this.countryObjects.put(country.getCountryName(), country);
		if(!this.continentCountries.containsKey(country.getCountryContinentName())) {
			this.continentCountries.put(country.getCountryContinentName(), new HashSet<>());
		}
		this.continentCountries.get(country.getCountryContinentName()).add(country.getCountryName());
	}

	/**
	 * Removes country from the map. Only to be used by MapEditor, before Startup Phase.
	 * @param countryName
	 */
	public void removeCountry(String countryName) {
		Country country = this.countryObjects.get(countryName);

		/* Removes country name from adjacentCountries */
		this.adjacentCountries.remove(countryName);
		for(String fromCountryKey : adjacentCountries.keySet()) {
			if(this.adjacentCountries.get(fromCountryKey).contains(countryName)) {
				this.adjacentCountries.get(fromCountryKey).remove(countryName);
			}
		}

		/* Removes country name from continentCountries */
		this.continentCountries.get(country.getCountryContinentName()).remove(countryName);

		/* Removes country object from countryObjects */
		this.continentObjects.remove(countryName);
	}

	public Country getCountry(String countryName) {
		if (!this.countryObjects.containsKey(countryName)) {
			return null;
		}
		return this.countryObjects.get(countryName);
	}

	public void addContinent(Continent continent) {
		this.continentObjects.put(continent.getContinentName(), continent);
	}

	public void removeContinent(String continentName) {
		this.continentObjects.remove(continentName);
	}

	public Continent getContinent(String continentName) {
		return this.continentObjects.get(continentName);
	}

	public void setAdjacentCountry(String countryName, String adjacentCountryName) {
		if (!this.adjacentCountries.containsKey(countryName)) {
			this.adjacentCountries.put(countryName, new HashSet<>());
		}
		this.adjacentCountries.get(countryName).add(adjacentCountryName);
	}

	public HashSet<String> getAdjacentCountries(String countryName) {
		return this.adjacentCountries.get(countryName);
	}

	public void removeAdjacenyBetweenCountries(String countryName, String adjacentCountryName) {
		removeAdjacency(countryName, adjacentCountryName);
		removeAdjacency(adjacentCountryName, countryName);
	}

	private void removeAdjacency(String countryName, String adjacentCountryName) {
		if (this.adjacentCountries.containsKey(countryName)
				&& this.adjacentCountries.get(countryName).contains(adjacentCountryName)) {
			this.adjacentCountries.get(countryName).remove(adjacentCountryName);
		}
	}

	public HashSet<String> getContinentCountries(String continentName){
		return this.continentCountries.get(continentName);
	}

	public void setCountryConquerer(String countryName, Integer playerId) {
		if(!this.conqueredCountriesPerPlayer.containsKey(playerId)) {
			this.conqueredCountriesPerPlayer.put(playerId, new HashSet<>());
		}
		this.conqueredCountriesPerPlayer.get(playerId).add(countryName);

		Country country= this.getCountry(countryName);
		country.setConquerorID(playerId);
	}

	public void setContinentConquerer(String continentName, Integer playerId) {
		if(!this.conqueredContinentsPerPlayer.containsKey(playerId)) {
			this.conqueredContinentsPerPlayer.put(playerId, new HashSet<>());
		}
		this.conqueredContinentsPerPlayer.get(playerId).add(continentName);
		Continent continent = this.getContinent(continentName);
		continent.setContinentConquerorID(playerId);
	}

	public  HashSet<String> getConqueredCountriesPerPlayer(Integer playerId){
		if(!this.conqueredCountriesPerPlayer.containsKey(playerId)) {
			return new HashSet<>();
		}
		return this.conqueredCountriesPerPlayer.get(playerId);
	}

	public  HashSet<String> getConqueredContinentsPerPlayer(Integer playerId){
		if(!this.conqueredContinentsPerPlayer.containsKey(playerId)) {
			return new HashSet<>();
		}
		return this.conqueredContinentsPerPlayer.get(playerId);
	}

	public void updateCountryConquerer(String countryName, Integer oldConquererPlayerId, Integer newConquererPlayerId) {
		if(this.conqueredCountriesPerPlayer.get(oldConquererPlayerId).contains(countryName)) {
			this.conqueredCountriesPerPlayer.get(oldConquererPlayerId).remove(countryName);
		}
		this.conqueredCountriesPerPlayer.get(newConquererPlayerId).add(countryName);

		Country country= this.getCountry(countryName);
		country.setConquerorID(newConquererPlayerId);
	}

	public void setMapAuthor(String mapAuthor) {
		this.mapAuthor = mapAuthor;
	}

	public String getMapAuthor() {
		return this.mapAuthor;
	}

	public HashMap<String, Country> getCountryObjects() {
		return countryObjects;
	}

	public HashMap<Integer, HashSet<String>> getConqueredCountriesPerPlayer() {
		return conqueredCountriesPerPlayer;
	}

	// SET THE WHOLE OBJECT NOT ADDING ELEMENTS

	public void setConqueredCountriesPerPlayer(HashMap<Integer, HashSet<String>> conqueredCountriesPerPlayer) {
		this.conqueredCountriesPerPlayer = conqueredCountriesPerPlayer;
	}

	public void setConqueredContinentsPerPlayer(HashMap<Integer, HashSet<String>> conqueredContinentsPerPlayer) {
		this.conqueredContinentsPerPlayer = conqueredContinentsPerPlayer;
	}
}
