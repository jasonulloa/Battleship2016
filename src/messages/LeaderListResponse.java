package messages;

import java.io.Serializable;
import java.util.Vector;

public class LeaderListResponse implements Serializable
{
	private static final long serialVersionUID = 1L;	
	private Vector<String> leaderListNames;
	private Vector<Integer> leaderListNumbers;
	private Vector<String> leaderList;

	
	public LeaderListResponse (Vector<String> LeaderListNames, Vector<Integer> LeaderListNumbers) {

		this.leaderList = new Vector<String>();
			
		this.leaderListNames = LeaderListNames;
		this.leaderListNumbers = LeaderListNumbers;
		
		for (int i = 0; i < leaderListNames.size(); i++){
			leaderList.add(leaderListNames.elementAt(i) + ", Games Won: " + String.valueOf(leaderListNumbers.elementAt(i)));
		}
	}
	
	public Vector<String> getLeaderList() {
		return leaderList;
	}
}
