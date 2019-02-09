package com.java.model.map;

import java.util.ArrayList;

public class Continent {

	public String continentNameId;
	public Integer controlValue;
	public ArrayList<String> countries;
	
	public Continent(String continentName, Integer controlValue) {
		this.continentNameId = continentName;
		this.controlValue = controlValue;
		countries = new ArrayList<>();
	}
	
	public void addCountry(String countryName) {
		countries.add(countryName);
	}
	
	public void removeCountry(String countryName) {
		countries.remove(countryName);
	}
	
	public void changeContinentName(String continentName) {
		this.continentNameId = continentName;
	}
	
	public void changeControlValue(Integer controlValue) {
		this.controlValue = controlValue;
	}
	
}
