package com.java.model.map;

public class Continent {

	private String continentName;
	private Integer continentControlValue;
	private Integer continentConquerorID;
	
	public Continent(String continentName, Integer controlValue) {
		this.continentName = continentName;
		this.continentControlValue = controlValue;
	}
	
	public String getContinentName() {
		return continentName;
	}

	public void updateContinentName(String continentName) {
		this.continentName = continentName;
	}

	public Integer getContinentControlValue() {
		return continentControlValue;
	}

	public void setContinentControlValue(Integer continentControlValue) {
		this.continentControlValue = continentControlValue;
	}

	public Integer getContinentConquerorID() {
		return continentConquerorID;
	}

	public void updateContinentConquerorID(Integer continentConquerorID) {
		this.continentConquerorID = continentConquerorID;
	}
	
}
