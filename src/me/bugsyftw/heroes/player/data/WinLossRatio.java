package me.bugsyftw.heroes.player.data;

public class WinLossRatio {
	private Integer wins;
	private Integer losses;
	private double ratio;
	
	public WinLossRatio(int w, int l){
		wins = w;
		losses = l;
	}
	
	public WinLossRatio(double ratio){
		this.ratio = ratio;
	}
	
	public WinLossRatio(){
		new WinLossRatio(0,0);
	}
	
	public Integer getWins(){
		return wins;
	}
	
	public Integer addWin(){
		wins += 1;
		return wins;
	}

	public Integer addWins(int amount){
		wins += amount;
		return wins;
	}
	
	public Integer getLosses(){
		return losses;
	}
	
	public Integer addLoss(){
		losses += 1;
		return losses;
	}
	
	public Integer addLosses(int amount){
		losses += amount;
		return losses;
	}
	
	public Double getRation(){
		return ratio;
	}
}
