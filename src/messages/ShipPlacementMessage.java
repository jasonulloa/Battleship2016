package messages;

import java.io.Serializable;

import utility.BoardState;

public class ShipPlacementMessage implements Serializable
{
	private static final long serialVersionUID = 1L;
	public BoardState shipPlacements;
	public ShipPlacementMessage(BoardState bs) {
		shipPlacements = bs;
	}
}
