package com.java.controller.map;

import java.util.HashMap;

import com.java.model.map.Continent;
import com.java.model.map.Country;
import com.java.model.map.GameMap;

public class MapValidator {

	GameMap map;

	public Boolean validateMap(GameMap map) {
		this.map = map;
		
		Boolean response = true;
		
		System.out.println();
		System.out.println("############### Starting Map Validation ###############");
		response = validateMapMetaData();
		response = validateMapContinents() && response;
		response = validateMapCountries() && response;
		System.out.println("############### Map Validation Finished ###############");
		
		if(!response) {
			System.out.println("Result: Invalid Map");
		} else {
			System.out.println("Result: Map is Valid");
		}
		return response;
	}

	private Boolean validateMapCountries() {
		
		Boolean response = true;
		
		HashMap<String, Country> countries = map.getAllCountries();

		if(countries.size() < 2) {
			System.out.println("ERROR: Less than 2 countries");
			response = false;
		}
		
		for(String countryName : countries.keySet()) {
			if(map.getAdjacentCountries(countryName).size() == 0) {
				System.out.println("ERROR: Country " + countryName + " doesn't have any adjacent country");
				response = false;
			}
		}
		
		return response;
	}

	private Boolean validateMapContinents() {
		
		Boolean response = true;
		HashMap<String, Continent> continenets = map.getAllContinents();
		
		if(continenets.size() < 1) {
			System.out.println("ERROR: No continents in the map");
			response = false;
		}
		
		for(String continentName : continenets.keySet()) {
			if(map.getContinentCountries(continentName).size() < 2) {
				System.out.println("ERROR: Continent " + continentName + " has less than 2 countries");
				response = false;
			}
		}
		return response;
	}

	private Boolean validateMapMetaData() {
		
		Boolean response = true;
		
		if(map.getMapAuthor().length() == 0) {
			System.out.println("ERROR: Map Author name missing");
			response = false;
		}
		
		return response;
	}
	
}
