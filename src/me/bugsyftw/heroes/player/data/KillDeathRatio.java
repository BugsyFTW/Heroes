package me.bugsyftw.heroes.player.data;

public class KillDeathRatio {
	
	private Integer kills;
	private Integer deaths;
	private double ratio;

	public KillDeathRatio(int kills, int deaths){
		this.kills = kills;
		this.deaths = deaths;
	}
	
	public KillDeathRatio(double ratio) {
		this.ratio = ratio;
	}
	
	public KillDeathRatio(){
		this(0,0);
	}
	
	public Integer getKills(){
		return kills;
	}
	
	public Integer addKill(){
		if(kills == null){
			System.out.println("KILLS NULL");
			return 0;
		}
		kills += 1;
		return kills;
	}
	
	public Integer addKills(int amount){
		kills += amount;
		return kills;
	}
	
	public Integer getDeaths(){
		return deaths;
	}
	
	public Integer addDeath(){
		deaths += 1;
		return deaths;
	}
	
	public Integer addDeaths(int amount){
		deaths += amount;
		return deaths;
	}
	
	public KillDeathRatio addKd(KillDeathRatio kd){
		kills += kd.getKills();
		deaths += kd.getDeaths();
		return this;
	}
	
	public Double getRatio(){
		return ratio;
	}
	
}
