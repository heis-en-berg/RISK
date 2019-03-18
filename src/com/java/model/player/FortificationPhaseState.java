package com.java.model.player;

/**
 * This class models the state of Fortification phase to be presented in phase View.
 * 
 * @author Arnav Bhardwaj
 * @author Karan Dhingra
 * @author Ghalia Elkerdi
 * @author Sahil Singh Sodhi
 * @author Cristian Rodriguez 
 * @version 1.0.0
 * */

public class FortificationPhaseState {

	private String fromCountry = null;
	private String toCountry = null;
	private Integer numberOfArmiesMoved = 0;
	private Integer numberOfArmiesLeft = 0;
	
	/**
	 * Default constructor to instance AttackPhaseState.
	 * */
	public FortificationPhaseState() {
	}
	
	/**
	 * Gets the name of the country from where the player wants to move the army.
	 * 
	 * @return the name of the country from where the player wants to move the army.
	 * */
	public String getFromCountry() {
		return fromCountry;
	}
	
	/**
	 * Sets the name of the country from where the player wants to move the army.
	 * 
	 * @param fromCountry the name of the country from where the player wants to move the army.
	 * */
	public void setFromCountry(String fromCountry) {
		this.fromCountry = fromCountry;
	}
	
	/**
	 * Gets the name of the destination country to move the army.
	 * 
	 * @return the name of the destination country to move the army.
	 * */
	public String getToCountry() {
		return toCountry;
	}

	/**
	 * Sets the name of the destination country to move the army.
	 * 
	 * @param toCountry the name of the destination country to move the army.
	 * */
	public void setToCountry(String toCountry) {
		this.toCountry = toCountry;
	}
	
	/**
	 * Gets the number of armies moved.
	 * 
	 * @return the number of armies moved.
	 * */
	public Integer getNumberOfArmiesMoved() {
		return numberOfArmiesMoved;
	}
	
	/**
	 * Sets the number of armies moved.
	 * 
	 * @param numberOfArmiesMoved the number of armies moved.
	 * */
	public void setNumberOfArmiesMoved(Integer numberOfArmiesMoved) {
		this.numberOfArmiesMoved = numberOfArmiesMoved;
	}
	
	/**
	 * Gets the number of armies left to be moved.
	 * 
	 * @return the number of armies left to be moved.
	 * */
	public Integer getNumberOfArmiesLeft() {
		return numberOfArmiesLeft;
	}
	
	/**
	 * Sets the number of armies left to be moved.
	 * 
	 * @param numberOfArmiesLeft the number of armies left to be moved.
	 * */
	public void setNumberOfArmiesLeft(Integer numberOfArmiesLeft) {
		this.numberOfArmiesLeft = numberOfArmiesLeft;
	}
}
