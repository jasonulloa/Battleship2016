package utility;

import java.io.Serializable;

public class TileChange implements Serializable{
	private static final long serialVersionUID = 1L;
	private int x;
	private int y;
	private int gameID;
	private int userID;
	private boolean isHit; //you know if you read this a different way...
	public TileChange(int x, int y, int GameID, int UserID, boolean IsHit){
		gameID = GameID;
		userID = UserID;
		isHit = IsHit;
		this.x = x;
		this.y = y;
	}	
	public int getGameID(){
		return gameID;
	}
	public int getUserID(){
		return userID;
	}
	public boolean isHit(){
		return isHit;
	}
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
}
