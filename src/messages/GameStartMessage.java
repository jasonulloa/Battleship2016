package messages;

import java.io.Serializable;
import java.util.ArrayList;

import utility.BoardState;

//passed to clients when new game is started from lobby or when existing game is loaded from active games list
//contains boardstate which lets client know what status the game is in

public class GameStartMessage implements Serializable{
	private static final long serialVersionUID = 1L;
	public int mapsize;
	public ArrayList<Integer> shipCounts;
	public BoardState boardState;
		
	public GameStartMessage(BoardState bs) {
		mapsize = bs.getMapSize();
		shipCounts = bs.getShipCount();
		boardState = bs;
	}
}
