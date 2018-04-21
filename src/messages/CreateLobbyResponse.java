package messages;

import java.io.Serializable;

import utility.Game;

public class CreateLobbyResponse implements Serializable{
	private static final long serialVersionUID = 1L;
	public Game game;
	
	public CreateLobbyResponse(Game game){
		this.game = game;
	}
	
}
