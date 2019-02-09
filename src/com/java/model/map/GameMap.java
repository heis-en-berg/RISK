package com.java.model.map;

import java.util.ArrayList;
import java.util.HashMap;

public class GameMap {

	public HashMap<String, Country> countries;
	public HashMap<String, ArrayList<String>> adjacentCountries;
	public HashMap<String, Continent> continents;
	
	public GameMap() {
		countries = new HashMap<>();
		adjacentCountries = new HashMap<>();
		continents = new HashMap<>();
	}
	
	public void addCountry(Country country) {
		this.countries.put(country.countryNameId, country);
	}
	
	public void removeCountry(String countryNameId) {
		this.continents.remove(countryNameId);
	}
	
	public void addContinent(Continent continent) {
		this.continents.put(continent.continentNameId, continent);
	}
	
	public void removeContinent(String continentNameId) {
		this.continents.remove(continentNameId);
	}
	
	public void addAdjacentCountry(String countryNameId, String adjacentCountryNameId) {
		if(!this.adjacentCountries.containsKey(countryNameId)) {
			this.adjacentCountries.put(countryNameId, new ArrayList<>());
		}
		this.adjacentCountries.get(countryNameId).add(adjacentCountryNameId);
	}
	
	public Country getCountry(String countryNameId) {
		return this.countries.get(countryNameId);
	}
	
	public ArrayList<String> getAdjacentCountries(String countryNameId) {
		return this.adjacentCountries.get(countryNameId);
	}
	
	public Continent getContinent(String continentNameId) {
		return this.continents.get(continentNameId);
	}
	
}
