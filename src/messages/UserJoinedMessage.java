package messages;

import java.io.Serializable;

import utility.Game;

/**
 * Only sent to the owning client when a second user joins a lobby
 */
public class UserJoinedMessage implements Serializable
{
	private static final long serialVersionUID = 1L;

	public UserJoinedMessage(Game game)
	{
		this.game = game;
	}
	public Game game;
}
