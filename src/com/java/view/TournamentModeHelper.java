package com.java.view;

import java.util.ArrayList;
import java.util.Scanner;

import com.java.controller.map.MapLoader;
import com.java.model.gamedata.GameData;

public class TournamentModeHelper {

	Scanner scanner;
	private MapLoader maploader;

	protected ArrayList<GameData> tournamentModeGameData;
	
	protected static Integer TM_NUMBER_OF_MAPS = 1;
	protected static Integer TM_NUMBER_OF_GAMES_ON_EACH_MAP = 1;
	protected static Integer TM_NUMBER_OF_PLAYERS = 2;
	protected static Integer TM_NUMBER_OF_TURNS = 10;

	protected static final Integer TM_MIN_NUMBER_OF_MAPS = 1;
	protected static final Integer TM_MAX_NUMBER_OF_MAPS = 5;
	protected static final Integer TM_MIN_NUMBER_OF_GAMES_ON_EACH_MAP = 1;
	protected static final Integer TM_MAX_NUMBER_OF_GAMES_ON_EACH_MAP = 5;
	protected static final Integer TM_MIN_NUMBER_OF_PLAYERS = 2;
	protected static final Integer TM_MAX_NUMBER_OF_PLAYERS = 4;
	protected static final Integer TM_MIN_NUMBER_OF_TURNS = 10;
	protected static final Integer TM_MAX_NUMBER_OF_TURNS = 50;

	public TournamentModeHelper() {
		scanner = new Scanner(System.in);
		maploader = new MapLoader();
	}

	/**
	 * Prompts user to get details required to execute Tournament mode
	 */
	protected void getTournamentModeDetailsFromUser() {
		setAndGetNumberOfMapsToBeUsed();
		setAndGetNumberOfPlayers();
		setAndGetNumberOfGamesToBePlayedOnEachMap();
		setAndGetMaxNumberOfTurnsForEachGame();
	}

	private void setAndGetMaxNumberOfTurnsForEachGame() {
		String usersInput = "";

		do {
			System.out.print("Enter the maximum number of Turns to be played in each game (min: "
					+ TournamentModeHelper.TM_MIN_NUMBER_OF_TURNS + ", max: "
					+ TournamentModeHelper.TM_MAX_NUMBER_OF_TURNS + "): ");

			usersInput = scanner.nextLine();

		} while (isNaN(usersInput) || Integer.parseInt(usersInput) < TournamentModeHelper.TM_MIN_NUMBER_OF_TURNS
				|| Integer.parseInt(usersInput) > TournamentModeHelper.TM_MAX_NUMBER_OF_TURNS);

		// Stores the number of players in game data.
		TournamentModeHelper.TM_NUMBER_OF_TURNS = Integer.parseInt(usersInput);

	}

	private void setAndGetNumberOfGamesToBePlayedOnEachMap() {
		String usersInput = "";

		do {
			System.out.print("Enter the number of Games to be played on each map (min: "
					+ TournamentModeHelper.TM_MIN_NUMBER_OF_GAMES_ON_EACH_MAP + ", max: "
					+ TournamentModeHelper.TM_MAX_NUMBER_OF_GAMES_ON_EACH_MAP + "): ");

			usersInput = scanner.nextLine();

		} while (isNaN(usersInput) || Integer.parseInt(usersInput) < TournamentModeHelper.TM_MIN_NUMBER_OF_GAMES_ON_EACH_MAP
				|| Integer.parseInt(usersInput) > TournamentModeHelper.TM_MAX_NUMBER_OF_GAMES_ON_EACH_MAP);

		// Stores the number of players in game data.
		TournamentModeHelper.TM_NUMBER_OF_GAMES_ON_EACH_MAP = Integer.parseInt(usersInput);
	}

	private void setAndGetNumberOfPlayers() {
		String usersInput = "";

		do {
			System.out.print("Enter the number of Players (min: " + TournamentModeHelper.TM_MIN_NUMBER_OF_PLAYERS
					+ ", max: " + TournamentModeHelper.TM_MAX_NUMBER_OF_PLAYERS + "): ");

			usersInput = scanner.nextLine();

		} while (isNaN(usersInput) || Integer.parseInt(usersInput) < TournamentModeHelper.TM_MIN_NUMBER_OF_PLAYERS
				|| Integer.parseInt(usersInput) > TournamentModeHelper.TM_MAX_NUMBER_OF_PLAYERS);

		// Stores the number of players in game data.
		TournamentModeHelper.TM_NUMBER_OF_PLAYERS = Integer.parseInt(usersInput);
	}

	private void setAndGetNumberOfMapsToBeUsed() {
		String usersInput = "";

		do {
			System.out.print("Enter the number of Maps (min: " + TournamentModeHelper.TM_MIN_NUMBER_OF_MAPS + ", max: "
					+ TournamentModeHelper.TM_MAX_NUMBER_OF_MAPS + "): ");

			usersInput = scanner.nextLine();

		} while (isNaN(usersInput) || Integer.parseInt(usersInput) < TournamentModeHelper.TM_MIN_NUMBER_OF_MAPS
				|| Integer.parseInt(usersInput) > TournamentModeHelper.TM_MAX_NUMBER_OF_MAPS);

		// Stores the number of players in game data.
		TournamentModeHelper.TM_NUMBER_OF_MAPS = Integer.parseInt(usersInput);
	}

	/**
	 * Helper method to test if a given strin can be converted to a int.
	 * 
	 * @param stringInput determines if the string typed by user is an integer
	 * @return the evaluation of true if it is an integer or false otherwise
	 */
	private boolean isNaN(final String stringInput) {
		try {
			Integer.parseInt(stringInput);

		} catch (final Exception e) {
			return true;
		}
		return false;
	}

}
