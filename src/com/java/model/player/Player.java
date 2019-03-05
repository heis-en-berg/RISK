package com.java.model.player;


/**
 * This class models the player, it holds the id, the name, and the order to play
 * for the round robin.
 * 
 * @author Arnav Bhardwaj
 * @author Karan Dhingra
 * @author Ghalia Elkerdi
 * @author Sahil Singh Sodhi
 * @author Cristian Rodriguez 
 * @version 1.0.0
 * */
public class Player implements Comparable<Player> {
	
	/**
	 * Player Id.
	 * */
	private Integer playerID;
	
	/**
	 * Player name.
	 * */
	private String playerName;
	
	/**
	 * The order of play.
	 * */
	private Integer orderOfPlay;

	/**
	 * Creates a player by giving the id and the name
	 * 
	 * @param playerID the player id.
	 * @param playerName the player name.
	 * */
	public Player(Integer playerID, String playerName) {
		this.playerID = playerID;
		this.playerName = playerName;
	}
	
	/**
	 * Getter of the player name.
	 * 
	 * @return the player name.
	 * */
	public String getPlayerName() {
		return this.playerName;
	}
	
	/**
	 * Getter of the player id.
	 * 
	 * @return the player id.
	 * */
	public Integer getPlayerID() {
		return this.playerID;
	}
	
	/**
	 * Getter of the order to play.
	 * 
	 * @return the order of playe.
	 * */
	public Integer getOrderOfPlay() {
		return orderOfPlay;
	}
	
	/**
	 * Setter of the order of play.
	 * 
	 * @param orderOfPlay order of play.
	 * */
	public void setOrderOfPlay(Integer orderOfPlay) {
		this.orderOfPlay = orderOfPlay;
	}
	
	/**
	 * Compare method to decide if a player is equal to another.
	 * 
	 * @param player the player to be compared.
	 * */
	@Override
	public int compareTo(Player player) {
	    Integer compareOrderPlay = ((Player) player).getOrderOfPlay();

	    // based on decending sort
		return compareOrderPlay-this.orderOfPlay;
	}
}
