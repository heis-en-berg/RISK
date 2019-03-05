package com.java.model.map;

public class Country {

	private String countryName;
	private String countryContinentName;
	private Integer countryConquerorID;
	private Integer armyCount;
	
	public Country(String countryName, String continentName) {
		this.countryName = countryName;
		this.countryContinentName = continentName;
		this.armyCount = 0;
	}
	
	public String getCountryName() {
		return this.countryName;
	}
	
	public void updateCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	public String getCountryContinentName() {
		return this.countryContinentName;
	}
	
	public void updateCountryContinentName(String countryContinentName) {
		this.countryContinentName = countryContinentName;
	}
	
	public void setConquerorID(Integer countryConquerorID) {
		this.countryConquerorID = countryConquerorID;
	}
	
	public Integer getCountryConquerorID() {
		return this.countryConquerorID;
	}
	
	public Integer getCountryArmyCount() {
		return this.armyCount;
	}
	
	public void setArmyCount(Integer armyCount) {
		this.armyCount = armyCount;
	}
	
	public Integer addArmy(Integer armyCount) {
		this.armyCount += armyCount;
		return this.armyCount;
	}
	
	public Integer deductArmy(Integer armyCount) {
		if(this.armyCount >= armyCount) {
			this.armyCount -= armyCount;
		}
		return this.armyCount;
	}
}
