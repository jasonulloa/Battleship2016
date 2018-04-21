package utility;

import java.io.Serializable;

public class Game implements Serializable{
	private static final long serialVersionUID = 1L;
	private int gameID;
	private int user1ID;
	private int user2ID;
	private String user1;
	private String user2;
	private int status;
	
	public Game(int gid, int uid1, String u1, int uid2, String u2) {
		gameID = gid;
		user1 = u1;
		user2 = u2;
		user1ID = uid1;
		user2ID = uid2;
	}
	
	public int getUser1ID(){
		return user1ID;
	}
	public int getUser2ID(){
		return user2ID;
	}
	
	public String getUser1Name(){
		return user1;
	}
	public String getUser2Name(){
		return user2;
	}
	public int getGameID(){
		return gameID;
	}
	public int getStatus(){
		return status;
	}
	public void setStatus(int status){
		this.status = status;
	}
}
