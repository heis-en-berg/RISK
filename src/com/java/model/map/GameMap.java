package com.java.model.map;

import java.util.HashMap;
import java.util.HashSet;

public class GameMap implements Cloneable{

	public static final Integer DEFAULT_NUMBER_OF_COUNTRIES = 42;
	
	private HashMap<String, Country> countryObjects;
	private HashMap<String, Continent> continentObjects;
	private HashMap<String, HashSet<String>> adjacentCountries;
	private HashMap<String, HashSet<String>> continentCountries;
	private HashMap<Integer, HashSet<String>> conqueredCountriesPerPlayer;
	private HashMap<Integer, HashSet<String>> conqueredContinentsPerPlayer;
	private String mapAuthor;

	public String warn;


	public GameMap() {
		mapAuthor = "";
		warn = "";
		countryObjects = new HashMap<>();
		adjacentCountries = new HashMap<>();
		continentObjects = new HashMap<>();
		continentCountries = new HashMap<>();
		conqueredCountriesPerPlayer = new HashMap<>();
		conqueredContinentsPerPlayer = new HashMap<>();
	}
	
	@Override
    public GameMap clone() {
		GameMap gameMap = null;
		try {
			gameMap = (GameMap) super.clone();
			gameMap.countryObjects = new HashMap<>(countryObjects);
			gameMap.continentObjects = new HashMap<>(continentObjects);
			
			HashMap<String, HashSet<String>> newAdjacentCountriesObject = new HashMap<>();
			for(String countryName : this.adjacentCountries.keySet()) {
				newAdjacentCountriesObject.put(countryName, new HashSet<>(adjacentCountries.get(countryName)));
			}
			gameMap.adjacentCountries = newAdjacentCountriesObject;
			
			HashMap<String, HashSet<String>> newContinentCountries = new HashMap<>();
			for(String continentName : this.continentCountries.keySet()) {
				newContinentCountries.put(continentName, new HashSet<>(continentCountries.get(continentName)));
			}
			gameMap.continentCountries = newContinentCountries;
			
		} catch (CloneNotSupportedException e) {
			System.out.println("Gamemap Cloning error");
		}
 
        return gameMap;        // return deep copy
    }

	public void addCountry(String countryName, String countryContinentName) {
		Country country = new Country(countryName, countryContinentName);
		this.countryObjects.put(country.getCountryName(), country);
		if(!this.continentCountries.containsKey(country.getCountryContinentName())) {
			this.continentCountries.put(country.getCountryContinentName(), new HashSet<>());
		}
		this.continentCountries.get(country.getCountryContinentName()).add(country.getCountryName());
	}

	/**
	 * Removes country from the map. Only to be used by MapEditor, before Startup Phase.
	 * @param countryName name of the country to be removed
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
		this.countryObjects.remove(countryName);
	}
	
	public Integer getNumberOfCountries() {
		return countryObjects.size();
	}
	
	public HashMap<String, Country> getAllCountries(){
		return this.countryObjects;
	}

	public Country getCountry(String countryName) {
		if (!this.countryObjects.containsKey(countryName)) {
			return null;
		}
		return this.countryObjects.get(countryName);
	}
	
	public void addContinent(String continentName, Integer controlValue) {
		Continent continent = new Continent(continentName, controlValue);
		this.continentObjects.put(continent.getContinentName(), continent);
	}

	public void removeContinent(String continentName) {
		this.continentObjects.remove(continentName);
		Object[] continentCountriesSet = getContinentCountries(continentName).toArray();
		for(Object continentCountry : continentCountriesSet) {
			removeCountry(continentCountry.toString());
		}
		continentCountries.remove(continentName);
	}
	
	public HashMap<String, Continent> getAllContinents(){
		return this.continentObjects;
	}

	public Continent getContinent(String continentName) {
		if(!this.continentObjects.containsKey(continentName)) {
			return null;
		}
		return this.continentObjects.get(continentName);
	}

	public void setAdjacentCountry(String countryName, String adjacentCountryName) {
		setAdjacency(countryName, adjacentCountryName);
		setAdjacency(adjacentCountryName, countryName);
	}
	
	private void setAdjacency(String fromCountry, String toCountry) {
		if (!this.adjacentCountries.containsKey(fromCountry)) {
			this.adjacentCountries.put(fromCountry, new HashSet<>());
		}
		this.adjacentCountries.get(fromCountry).add(toCountry);
	}

	public HashSet<String> getAdjacentCountries(String countryName) {
		if(!this.adjacentCountries.containsKey(countryName)) {
			return new HashSet<>();
		}
		return this.adjacentCountries.get(countryName);
	}
	
	public HashMap<String, HashSet<String>> getAdjacentCountriesObject() {
		return adjacentCountries;
	}

	public Boolean removeAdjacenyBetweenCountries(String countryName, String adjacentCountryName) {
		return (removeAdjacency(countryName, adjacentCountryName) && removeAdjacency(adjacentCountryName, countryName));
	}

	private Boolean removeAdjacency(String countryName, String adjacentCountryName) {
		if (this.adjacentCountries.containsKey(countryName)
				&& this.adjacentCountries.get(countryName).contains(adjacentCountryName)) {
			this.adjacentCountries.get(countryName).remove(adjacentCountryName);
			return true;
		}
		return false;
	}

	public HashSet<String> getContinentCountries(String continentName){
		if(!this.continentCountries.containsKey(continentName)) {
			return new HashSet<String>();
		}
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
	
	/*
	 * Just for development purpose
	 */
	public void printMap() {
		System.out.println("Author: " +  getMapAuthor());

		System.out.println("[[Countries]]");
		for(String countryName: countryObjects.keySet()) {
			System.out.println(countryName);
			System.out.println("Adjacent Countries::" + getAdjacentCountries(countryName).toString());
		}
		
		System.out.println("[[Continent]]");
		for(String continentName: continentObjects.keySet()) {
			System.out.println(continentName + " :: " + getContinentCountries(continentName).toString());
		}
	}

	public HashMap<Integer, HashSet<String>> getConqueredCountriesPerPlayerObject() {
		return conqueredCountriesPerPlayer;
	}

	// SET THE WHOLE OBJECT NOT ADDING ELEMENTS using gettes and setters to acess private data 

	public void setConqueredContinentsPerPlayer(HashMap<Integer, HashSet<String>> conqueredContinentsPerPlayer) {
		this.conqueredContinentsPerPlayer = conqueredContinentsPerPlayer;
	}

	public void setCountryObjects(HashMap<String, Country> countryObjects) {
		this.countryObjects = countryObjects;
	}

	public HashMap<String, Continent> getContinentObjects() {
		return continentObjects;
	}

	public void setContinentObjects(HashMap<String, Continent> continentObjects) {
		this.continentObjects = continentObjects;
	}

	public HashMap<String, HashSet<String>> getAdjacentCountries() {
		return adjacentCountries;
	}

	public void setAdjacentCountries(HashMap<String, HashSet<String>> adjacentCountries) {
		this.adjacentCountries = adjacentCountries;
	}

	public HashMap<String, HashSet<String>> getContinentCountries() {
		return continentCountries;
	}

	public void setContinentCountries(HashMap<String, HashSet<String>> continentCountries) {
		this.continentCountries = continentCountries;
	}

	public HashMap<Integer, HashSet<String>> getConqueredContinentsPerPlayer() {
		return conqueredContinentsPerPlayer;
	}

}
