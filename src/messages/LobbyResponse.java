package messages;

import java.io.Serializable;
import java.util.Vector;

import utility.Game;

public class LobbyResponse implements Serializable
{
	private static final long serialVersionUID = 1L;
	private Vector<Game> LobbyList;
	public LobbyResponse (Vector<Game> lobbyList){
		LobbyList = lobbyList;
	}
	
	public Vector<Game> getLobbys() {
		return LobbyList;
	}
}
