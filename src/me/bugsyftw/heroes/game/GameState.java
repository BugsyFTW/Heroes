package me.bugsyftw.heroes.game;

public enum GameState {
	OFFLINE,PREGAME,PLAYING,ENDGAME;
	
	public String toString() {
		switch (this) {
		case OFFLINE:
			return "Offline";
		case PREGAME:
			return "Pre-Game";
		case PLAYING:
			return "In Progress";
		case ENDGAME:
			return "Resetting";
		default:
			break;
		}
		return null;
	}
}
