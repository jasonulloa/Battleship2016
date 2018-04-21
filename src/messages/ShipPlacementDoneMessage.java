package messages;

import java.io.Serializable;

import utility.BoardState;

public class ShipPlacementDoneMessage implements Serializable
{
	private static final long serialVersionUID = 1L;
	public BoardState boardState;
	public ShipPlacementDoneMessage(BoardState bs) {
		boardState = bs;
	}
}
