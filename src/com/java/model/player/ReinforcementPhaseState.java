package com.java.model.player;

/**
 * This class models the state of Reinforcement phase to be presented in phase View.
 * 
 * @author Arnav Bhardwaj
 * @author Karan Dhingra
 * @author Ghalia Elkerdi
 * @author Sahil Singh Sodhi
 * @author Cristian Rodriguez 
 * @version 1.0.0
 * */
public class ReinforcementPhaseState {
	private String toCountry = null;
	private Integer numberOfArmiesPlaced = 0;
	private Integer numberOfArmiesReceived = 0;
	
	/**
	 * Default constructor to instance ReinforcementPhaseState.
	 * */
	public ReinforcementPhaseState() {
	}

	/**
	 * Gets the name of country 
	 * */
	public String getToCountry() {
		return toCountry;
	}

	public void setToCountry(String toCountry) {
		this.toCountry = toCountry;
	}

	public Integer getNumberOfArmiesPlaced() {
		return numberOfArmiesPlaced;
	}

	public void setNumberOfArmiesPlaced(Integer numberOfArmiesPlaced) {
		this.numberOfArmiesPlaced = numberOfArmiesPlaced;
	}

	public Integer getNumberOfArmiesReceived() {
		return numberOfArmiesReceived;
	}

	public void setNumberOfArmiesReceived(Integer numberOfArmiesReceived) {
		this.numberOfArmiesReceived = numberOfArmiesReceived;
	}


}
