package com.java.model.player;

import java.util.ArrayList;

import com.java.model.cards.Card;

public class Player implements Comparable<Player> {

	private Integer playerID;
	private String playerName;
	private ArrayList<Card> cards;
	private Integer orderOfPlay;

	public Player(Integer playerID, String playerName) {
		this.playerID = playerID;
		this.playerName = playerName;
		cards = new ArrayList<>();
	}

	public ArrayList<Card> getCards(){
		return this.cards;
	}

	public String getPlayerName() {
		return this.playerName;
	}
	public Integer getPlayerID() {
		return this.playerID;
	}

	public Integer getOrderOfPlay() {
		return orderOfPlay;
	}

	public void setOrderOfPlay(Integer orderOfPlay) {
		this.orderOfPlay = orderOfPlay;
	}

	@Override
	public int compareTo(Player player) {
	    Integer compareOrderPlay = ((Player) player).getOrderOfPlay();

	    // based on decending sort
		return compareOrderPlay-this.orderOfPlay;
	}
}
