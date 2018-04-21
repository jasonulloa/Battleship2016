//informs lobby joiner about a change in the current lobby settings 
package messages;

import java.io.Serializable;
import java.util.ArrayList;

public class LobbySettingsChange implements Serializable{
	private static final long serialVersionUID = 1L;
	public int mapSize;
	public ArrayList<Integer> shipCounts;
	
	public LobbySettingsChange(int MapSize, ArrayList<Integer> sc){
		this.mapSize = MapSize;
		this.shipCounts = sc;
	}
}
