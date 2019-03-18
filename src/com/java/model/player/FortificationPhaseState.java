package com.java.model.player;

public class FortificationPhaseState {

	private String fromCountry = null;
	private String toCountry = null;
	private Integer numberOfArmiesMoved = 0;
	private Integer numberOfArmiesLeft = 0;

	public FortificationPhaseState() {
	}

	public String getFromCountry() {
		return fromCountry;
	}

	public void setFromCountry(String fromCountry) {
		this.fromCountry = fromCountry;
	}

	public String getToCountry() {
		return toCountry;
	}

	public void setToCountry(String toCountry) {
		this.toCountry = toCountry;
	}

	public Integer getNumberOfArmiesMoved() {
		return numberOfArmiesMoved;
	}

	public void setNumberOfArmiesMoved(Integer numberOfArmiesMoved) {
		this.numberOfArmiesMoved = numberOfArmiesMoved;
	}

}
