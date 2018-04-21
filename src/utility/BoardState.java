package utility;

import java.io.Serializable;
import java.util.ArrayList;

public class BoardState implements Serializable{
	private static final long serialVersionUID = 1L;
	private int user1ID;
	private int user2ID;
	private int gameID;
	private int status;
	
	private ArrayList<Integer> shipCount;
	private int mapSize;
	public TileState[][] user1board;
	public TileState[][] user2board;
	
	public boolean hasLost;
	public int winner;
	public int loser;
	
	public BoardState(int User1ID, int User2ID, int GameID, int Status, int mapsize){
		user1ID = User1ID;
		user2ID = User2ID;
		gameID = GameID;
		status = Status;
		mapSize = mapsize;
		
		shipCount = null;
	}
	
	public void UpdateBoardSize(int newMapSize)
	{
		mapSize = newMapSize;
		
		user1board = new TileState[newMapSize][newMapSize];
		user2board = new TileState[newMapSize][newMapSize];
		
		for(int x = 0; x < newMapSize; x++)
		{
			for(int y = 0; y < newMapSize; y++)
			{
				user1board[x][y] = new TileState(x,y,false,false);
				user2board[x][y] = new TileState(x,y,false,false);
			}
		}
	}
	
	public void setBoard(BoardState newBoard, boolean firstUser)
	{
		if(firstUser)
		{
			user1board = newBoard.user1board;
		}
		else
		{
			user2board = newBoard.user2board;
		}
	}
	
	public BoardState() {
		
	}
	
	public void SetShip(int x, int y, boolean firstUser)
	{
		if(firstUser)
		{
			user1board[x][y].isShip = true;
		}
		else
		{
			user2board[x][y].isShip = true;
		}
	}
	
	public void SetShot(int x, int y, boolean firstUser)
	{
		if(firstUser)
		{
			user1board[x][y].beenShot = true;
		}
		else
		{
			user2board[x][y].beenShot = true;
		}
	}
	
	public ArrayList<Integer> getShipCount(){
		return shipCount;
	}
	public int getMapSize(){
		return mapSize;
	}
	public void setMapSize(int mapsize){
		mapSize = mapsize;
	}
	public int getUser1ID(){
		return user1ID;
	}
	public int getUser2ID(){
		return user2ID;
	}
	public int getGameID(){
		return gameID;
	}

	public int getStatus(){
		return status;
	}
	
	public void setShipCount(ArrayList<Integer> sc)
	{
		shipCount = sc;
	}
	public void setUser1ID(int u1){
		user1ID = u1;
	}
	public void setUser2ID(int u2){
		user2ID = u2;
	}
	public void setGameID(int gi){
		gameID = gi;
	}

	public void setStatus(int s){
		status = s;
	}
	
	public void ToggleTurn()
	{
		if(status == Constants.USER1_TURN)
		{
			status = Constants.USER2_TURN;
		}
		else if(status == Constants.USER2_TURN)
		{
			status = Constants.USER1_TURN;
		}
	}
	
	public boolean ApplyChange(TileChange tc)
	{
		// TODO take a tilechange and modify the boardstate, then return whether it was a hit or not
		if(tc.getUserID() == user1ID)
		{
			user2board[tc.getX()][tc.getY()].beenShot = true;
			CheckLose();
			return user2board[tc.getX()][tc.getY()].isShip;
		}
		
		if(tc.getUserID() == user2ID)
		{
			user1board[tc.getX()][tc.getY()].beenShot = true;
			CheckLose();
			return user1board[tc.getX()][tc.getY()].isShip;
		}
		
		return false;
	}
	
	public void CheckLose()
	{
		boolean user1Lost = true;
		boolean user2Lost = true;
		
		for(int x = 0; x < user1board.length; x++)
		{
			for(int y = 0; y < user1board[x].length; y++)
			{
				if(user1board[x][y].isShip && !user1board[x][y].beenShot)
				{
					user1Lost = false;
				}
				if(user2board[x][y].isShip && !user2board[x][y].beenShot)
				{
					user2Lost = false;
				}
			}
		}
		
		if(user1Lost)
		{
			hasLost = true;
			winner = user2ID;
			loser = user1ID;
		}
		if(user2Lost)
		{
			hasLost = true;
			winner = user1ID;
			loser = user2ID;
		}
	}
}