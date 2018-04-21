package messages;

import java.io.Serializable;
import java.util.Vector;

import utility.Game;

public class ActiveGamesResponse implements Serializable
{
	private static final long serialVersionUID = 1L;	
	private Vector<Game> agList;
	
	public ActiveGamesResponse (Vector<Game> ActiveGames){
		agList = new Vector<Game>();
		agList = ActiveGames;
	}
	
	public Vector<Game> getActiveGames() {
		return agList;
	}
}
