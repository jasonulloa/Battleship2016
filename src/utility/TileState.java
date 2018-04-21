package utility;

import java.io.Serializable;

public class TileState implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public int x;
	public int y;
	public boolean beenShot;
	public boolean isShip;
	
	public TileState(int x2, int y2, boolean shot, boolean ship) {
		x = x2;
		y = y2;
		beenShot = shot;
		isShip = ship;
	}
}