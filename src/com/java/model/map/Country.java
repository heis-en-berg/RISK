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
	 */
	private String countryName;

	/**
	 * The name of the continent which this country belongs.
	 */
	private String countryContinentName;

	/**
	 * Conqueror player id.
	 */
	private Integer countryConquerorID;


	/**
	 * A country is created by provinding the name and the continent name.
	 *
	 * @param countryName   the country name.
	 * @param continentName the continent name.
	 */
	public Country(String countryName, String continentName) {
		this.countryName = countryName;
		this.countryContinentName = continentName;
	}

	/**
	 * Getter country name
	 *
	 * @return the country name
	 */
	public String getCountryName() {
		return this.countryName;
	}

	/**
	 * Update the country's name.
	 *
	 * @param countryName the country name
	 */
	public void updateCountryName(String countryName) {
		this.countryName = countryName;
	}

	/**
	 * Getter continent name.
	 *
	 * @return the continent name
	 */
	public String getCountryContinentName() {
		return this.countryContinentName;
	}

	/**
	 * Updates the continent name.
	 *
	 * @param countryContinentName the continent name
	 */
	public void updateCountryContinentName(String countryContinentName) {
		this.countryContinentName = countryContinentName;
	}

	/**
	 * Sets the conqueror player id.
	 *
	 * @param countryConquerorID the player id
	 */
	public void setConquerorID(Integer countryConquerorID) {
		this.countryConquerorID = countryConquerorID;
	}

	/**
	 * Getter conqueror player id.
	 *
	 * @return the conqueror player id
	 */
	public Integer getCountryConquerorID() {
		return this.countryConquerorID;
	}

}
