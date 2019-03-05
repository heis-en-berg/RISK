package com.java.model.gamedata;

import com.java.model.map.GameMap;
import com.java.model.player.Player;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class hold the whole data that is going to be used during the game. For instance,
 * the map, the players and the number of players are saved. This allows to keep the data in only place.
 * 
 * @author Arnav Bhardwaj
 * @author Karan Dhingra
 * @author Ghalia Elkerdi
 * @author Sahil Singh Sodhi
 * @author Cristian Rodriguez 
 * @version 1.0.0
 */
public class GameData {
	
	/**
	 * Holds the map. Countries, continents, and adjacencies.
	 * */
	public GameMap gameMap;
	
	/**
	 * The number of players.
	 * */
	private Integer noOfPlayers;
	
	/**
	 * The players ordered in order to play.
	 * */
	private ArrayList<Player> players;

	/**
	 * The minimum number of players is 2.
	 * */
	public static final Integer MIN_PLAYERS = 2;
	
	/**
	 * The maximum number of players is 6.
	 * */
	public static final Integer MAX_PLAYERS = 6;
	
	
	/**
	 * Getter players.
	 * 
	 * @return the collection of players.
	 * */
	public ArrayList<Player> getPlayers() {
		return this.players;
	}
	
	/**
	 * Setter player.
	 * */
	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	
	/**
	 * Remove one player of the main collection of players.
	 * */
	public void removePlayers(Player player){
		this.players.remove(player);
	}
	
	/**
	 * Get the number of players.
	 * 
	 * @return the number of players.
	 * */
	public Integer getNoOfPlayers() {
		return noOfPlayers;
	}
	
	/**
	 * 
	 * */
	public void setNoOfPlayers(Integer noOfPlayers) {
		this.noOfPlayers = noOfPlayers;
	}

}
