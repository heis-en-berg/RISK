package com.java.model.map;

public class Country {

	public String countryNameId;
	public String continentNameId;
	public String conquerorNameId;
	public Integer armyCount;
	
	public Country(String countryName, String continentNameId) {
		this.countryNameId = countryName;
		this.continentNameId = continentNameId;
		this.armyCount = 0;
	}
	
	public void changeCountryName(String countryName) {
		this.countryNameId = countryName;
	}
	
	public Integer addArmy(Integer armyCount) {
		this.armyCount += armyCount;
		return this.armyCount;
	}
	
	public Integer deductArmy(Integer armyCount) {
		this.armyCount -= armyCount;
		return this.armyCount;
	}
}
