package com.java.model.gamedata;

import com.java.model.cards.CardsDeck;
import com.java.model.map.GameMap;
import com.java.model.player.PlayerStrategy;

import java.util.ArrayList;

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
	private ArrayList<PlayerStrategy> players;

	/**
	 * The minimum number of players is 2.
	 * */
	public static final Integer MIN_PLAYERS = 2;

	/**
	 * The maximum number of players is 6.
	 * */
	public static final Integer MAX_PLAYERS = 6;

	public CardsDeck cardsDeck;
	/**
	 * Getter players.
	 *
	 * @return the collection of players.
	 * */
	public ArrayList<PlayerStrategy> getPlayers() {
		return this.players;
	}

	/**
	 * Setter player.
	 *
	 * @param players the collection of players.
	 * */
	public void setPlayers(ArrayList<PlayerStrategy> players) {
		this.players = players;
	}
	
	/**
	 * Getter player.
	 *
	 * @param playerId player id.
	 * @return player.
	 * */
	public PlayerStrategy getPlayer(Integer playerId) {
		for(PlayerStrategy player : this.players) {
			if(player.getPlayerID().equals(playerId)) {
				return player;
			}
		}
		return null;
	}

	/**
	 * Remove one player of the main collection of players.
	 *
	 * @param player the player to be removed
	 * */
	public void removePlayers(PlayerStrategy player){
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
	 * Set the number of players.
	 *
	 * @param noOfPlayers the number of players.
	 * */
	public void setNoOfPlayers(Integer noOfPlayers) {
		this.noOfPlayers = noOfPlayers;
	}
	
	/**
	 * Get player's status
	 * @param player player id
	 * @return true if player is active else false
	 */
	public Boolean getPlayerStatus(PlayerStrategy player) {
		return player.isActive;
	}
	
	/**
	 * Set player's status
	 * @param player player id.
	 * @param isActive set player status
	 */
	public void setPlayerStatus(PlayerStrategy player, Boolean isActive) {
		player.isActive = isActive;
	}

}