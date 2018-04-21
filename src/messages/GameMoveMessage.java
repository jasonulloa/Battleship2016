package messages;

import java.io.Serializable;

import utility.TileChange;

public class GameMoveMessage implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private TileChange tileChange;
	
	public GameMoveMessage(TileChange tile){
		tileChange = tile;
	}

	public TileChange getTileChange() {
		return tileChange;
	}
}
