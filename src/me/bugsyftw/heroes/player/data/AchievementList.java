package me.bugsyftw.heroes.player.data;

public enum AchievementList {
	
	FIRST_KILL(1, "Nice Move!", "Player's First Kill!", false),
	TEN_KILLS(2, "Gaining Experience", "10 Kills!", false),
	HUNDRED_KILLS(3, "I Can Do This All Day", "100 Kills!", false),
	THOUSAND_KILLS(4, "Oh Kill 'em!", "1000 Kills", false),
	TEN_THOUSAND_KILLS(5, "Welcome To The Team", "10000 Kills!", false),
	KILL_TEN(6, "What The..?!", "Kill 10 Heroes in a single Free For All match", false),
	KILL_SUPERMAN(7, "Yeah right. Liar", "Kill Superman!", false),
	TESSERACT_USE(8, "What this shining blue cube does?", "Use the Tesseract", false),
	SSS_USE(9, "Twice Stronger", "Use the Super Soldier Serum", false),
	MB_USE(10, "Hmmm..Interesting", "Use a Mystery Box", false);
	// TODO Rest of Achievements...

	private int ID;
	private String msg;
	private String desc;
	private boolean achieved;

	private AchievementList(int id, String msg, String desc, boolean achieved) {
		this.ID = id;
		this.msg = msg;
		this.desc = desc;
		this.achieved = achieved;
	}
	
	public boolean isAchieved() {
		return achieved;
	}
	
	public void setAchieved(boolean achieved) {
		this.achieved = achieved;
	}
	
	public int getID() {
		return ID;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public String dataName(){
		return "ID" + getID();
	}
	
	public static AchievementList fromDataID(String id){
		for (AchievementList a : values()) {
			if (a.dataName().equalsIgnoreCase(id)){
				return a;
			}
		}
		return  null;
	}
	
	public static AchievementList fromID(int id) {
		for(AchievementList a : values()){
			if (a.getID() == id) {
				return a;
			}
		}
		return null;
	}
}
