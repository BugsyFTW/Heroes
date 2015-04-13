package me.bugsyftw.heroes.player.data;

public class HeroCoin {

	private Integer amount;
	
	public HeroCoin(int amount){
		this.amount = amount;
	}
	
	public HeroCoin(){
		new HeroCoin(0);
	}
	
	public Integer getAmount(){
		return amount;
	}
	
	public Integer addHeroCoins(int a){
		amount += a;
		return amount;
	}
	
	public Integer removeHeroCoins(int a){
		this.amount -= a;
		return amount;
	}
	
}
