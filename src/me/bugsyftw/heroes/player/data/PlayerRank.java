package me.bugsyftw.heroes.player.data;

public class PlayerRank {

	private SkyRank rank;

	public PlayerRank(SkyRank rank) {
		this.rank = rank;
	}

	public void setRank(SkyRank rank) {
		if (getRank() == rank) return;
		this.rank = rank;
	}

	public SkyRank getRank() {
		return rank;
	}

}
