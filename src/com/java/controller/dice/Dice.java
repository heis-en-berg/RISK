package com.java.controller.dice;

import java.util.ArrayList;
import java.util.Random;

public class Dice {

	public Dice(){
	}

	// simple roll for each player to uses
	public Integer rollDice(){
		Random rand1 = new Random();
		return 	rand1.nextInt(6-1 +1) + 1;
	}

	public ArrayList<Integer> rollDice(Integer numberOfDice) {
		return null;
	}
	
}
