package messages;

import java.io.Serializable;

public class GameOverMessage implements Serializable
{
	private static final long serialVersionUID = 1L;

	public boolean gameFinished;
	public int winner;
}
