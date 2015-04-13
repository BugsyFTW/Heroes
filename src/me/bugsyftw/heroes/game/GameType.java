package me.bugsyftw.heroes.game;

public enum GameType {

	FREE_FOR_ALL("FFA"), ONE_VS_ONE("1v1"), TOURNAMENT("Tournament");

	private String name;

	GameType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getMinSpawns() {
		switch (this) {
		case FREE_FOR_ALL:
			return 2;
		case ONE_VS_ONE:
			return 2;
		case TOURNAMENT:
			return 4;
		default:
			break;
		}

		return 0;
	}

	public int getMaxSpawns() {
		switch (this) {
		case FREE_FOR_ALL:
			return 8;
		case ONE_VS_ONE:
			return 2;
		case TOURNAMENT:
			return 16;
		default:
			break;
		}

		return 0;
	}

	public int pointsToWin() {
		switch (this) {
		case FREE_FOR_ALL:
			return 16;
		case ONE_VS_ONE:
			return 3;
		case TOURNAMENT:
			return 9;
		default:
			break;
		}

		return 0;
	}

	public int getWinAmmount() {
		switch (this) {
		case FREE_FOR_ALL:
			return 30;
		case ONE_VS_ONE:
			return 40;
		case TOURNAMENT:
			return 50;
		default:
			break;
		}
		return 0;
	}

	public String toString() {
		switch (this) {
		case FREE_FOR_ALL:
			return "FFA";
		case ONE_VS_ONE:
			return "1v1";
		case TOURNAMENT:
			return "Tournament";
		default:
			break;
		}
		return null;
	}

	public static GameType fromString(String s) {
		for (GameType type : GameType.values()) {
			if (s.equalsIgnoreCase(type.getName())) {
				return type;
			}
		}
		return null;
	}

	public static GameType fromConfigString(String s) {
		for (GameType type : GameType.values()) {
			if (s.equalsIgnoreCase(type.toString())) {
				return type;
			}
		}
		return null;
	}

	public static int getTimer() {
		return 600;
	}
}
