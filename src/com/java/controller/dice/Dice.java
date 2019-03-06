package com.java.controller.dice;

import java.util.ArrayList;
import java.util.Random;

/**
 * A Dice class that is used when ever a player needs to obtain a random generated number between 1 and 6
 *  * @author Arnav Bhardwaj
 *  * @author Karan Dhingra
 *  * @author Ghalia Elkerdi
 *  * @author Sahil Singh Sodhi
 *  * @author Cristian Rodriguez
 *  * @version 1.0.0
 */
public class Dice {

	public Dice(){
	}

	/**
	 * When called will generate numbers ranging from 1 to 6
	 * @return a single integer value of random number
	 */
	public Integer rollDice(){
		Random rand1 = new Random();
		return 	rand1.nextInt(6 - 1 +1) + 1;
	}

}
