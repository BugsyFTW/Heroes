package me.bugsyftw.heroes.player.data;

public enum SkyRank {

	DEFAULT("Piggy", 0, 1),
	PRO_PIGGY("Pro Piggy", 1, 2),
	SUPER_PIGGY("Super Piggy", 2, 3),
	SKY_PIGGY("Sky Piggy", 3, 5),
	//MOD ADMIN RANKS
	HELPER("S.P.H.D", 4, 1),
	PIGGY_ANGEL("Piggy Angel", 5, 1),
	PIGGY_GOD("Piggy God", 6, 1),
	//PERKS RANKS
	TRUE_HERO("True Hero", 7, 5),
	YOUTUBER("YouTuber", 8, 5);
	

	private String name;
	private int id;
	private int boost;
	private SkyRank(String name, int id, int boost) {
		this.name = name;
		this.id = id;
		this.boost = boost;
	}

	public String getName() {
		return name;
	}

	public int getID() {
		return id;
	}

	public int getBoost() {
		return boost;
	}

	public static SkyRank fromString(String rank) {
		for (SkyRank sk : values()) {
			if (sk.getName().equalsIgnoreCase(rank)) {
				return sk;
			}
		}
		return null;
	}
	
	public static SkyRank fromID(int id) {
		for (SkyRank sk : values()) {
			if (sk.getID() == id) {
				return sk;
			}
		}
		return null;
	}
}
