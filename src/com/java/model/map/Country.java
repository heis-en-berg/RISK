package com.java.model.map;

/**
 * This class models a country by giving a name, a continent to belong, a conqueror 
 * player id, and the number of armies placed on the country.
 * 
 * @author Arnav Bhardwaj
 * @author Karan Dhingra
 * @author Ghalia Elkerdi
 * @author Sahil Singh Sodhi
 * @author Cristian Rodriguez 
 * @version 1.0.0
 * */
public class Country {
	
	/**
	 * The country name.
	 * */
	private String countryName;
	
	/**
	 * The name of the continent which this country belongs.
	 * */
	private String countryContinentName;
	
	/**
	 * Conqueror player id.
	 * */
	private Integer countryConquerorID;
	
	/**
	 * The number of armies placed in the country.
	 */
	private Integer armyCount;
	
	/**
	 * A country is created by provinding the name and the continent name.
	 * */
	public Country(String countryName, String continentName) {
		this.countryName = countryName;
		this.countryContinentName = continentName;
		this.armyCount = 0;
	}
	
	/**
	 * Getter country name
	 * 
	 * @return the country name
	 * */
	public String getCountryName() {
		return this.countryName;
	}
	
	public void updateCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	/**
	 * Getter continent name.
	 * 
	 * @return the continent name
	 * */
	public String getCountryContinentName() {
		return this.countryContinentName;
	}
	
	public void updateCountryContinentName(String countryContinentName) {
		this.countryContinentName = countryContinentName;
	}
	
	public void setConquerorID(Integer countryConquerorID) {
		this.countryConquerorID = countryConquerorID;
	}
	
	/**
	 * Getter conqueror player id.
	 * 
	 * @return the conqueror player id
	 * */
	public Integer getCountryConquerorID() {
		return this.countryConquerorID;
	}
	
	/**
	 * Getter the number of countries placed on the country.
	 * 
	 * @return the number of armies placed on the country.
	 * */
	public Integer getCountryArmyCount() {
		return this.armyCount;
	}
	
	public void setArmyCount(Integer armyCount) {
		this.armyCount = armyCount;
	}
	
	/**
	 * Gets the new value of army after adding new army.
	 * 
	 * @return the new value of army after adding army.
	 * */
	public Integer addArmy(Integer armyCount) {
		this.armyCount += armyCount;
		return this.armyCount;
	}
	
	/**
	 * Gets the new value of army after subtracting new army.
	 * 
	 * @return the new value of army after subtracting army.
	 * */
	public Integer deductArmy(Integer armyCount) {
		if(this.armyCount >= armyCount) {
			this.armyCount -= armyCount;
		}
		return this.armyCount;
	}
}
