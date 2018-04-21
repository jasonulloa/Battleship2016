package messages;

import java.io.Serializable;
import java.util.ArrayList;

public class StartGameRequest implements Serializable
{
	private static final long serialVersionUID = 1L;
	public int mapsize;
	public ArrayList<Integer> shipCounts;
	
	public StartGameRequest(int ms, ArrayList<Integer> sc) {
		mapsize = ms;
		shipCounts = sc;
	}
}
