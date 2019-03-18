package com.java.model.player;

public class ReinforcementPhaseState {
	private String toCountry = null;
	private Integer numberOfArmiesPlaced = 0;
	private Integer numberOfArmiesRecived = 0;

	public ReinforcementPhaseState() {
	}

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

	public Integer getNumberOfArmiesRecived() {
		return numberOfArmiesRecived;
	}

	public void setNumberOfArmiesRecived(Integer numberOfArmiesRecived) {
		this.numberOfArmiesRecived = numberOfArmiesRecived;
	}


}
