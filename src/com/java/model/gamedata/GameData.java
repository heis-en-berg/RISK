package com.java.model.gamedata;

import com.java.model.cards.CardsDeck;
import com.java.model.map.GameMap;
import com.java.model.player.Player;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class GameData {

	public GameMap gameMap;
	private Integer noOfPlayers;
	private ArrayList<Player> players;

	public CardsDeck cardsDeck; /* For build 2 */

	public static final Integer MIN_PLAYERS = 2;
	public static final Integer MAX_PLAYERS = 6;
	
	public ArrayList<Player> getPlayers() {
		return this.players;
	}
	
	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public void removePlayers(Player player){
		this.players.remove(player);
	}

	public Integer getNoOfPlayers() {
		return noOfPlayers;
	}

	public void setNoOfPlayers(Integer noOfPlayers) {
		this.noOfPlayers = noOfPlayers;
	}

}
