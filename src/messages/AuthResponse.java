package messages;

import java.io.Serializable;

public class AuthResponse implements Serializable
{
	private static final long serialVersionUID = 1L;
	public boolean isGuest;
	public boolean newUser;
	public boolean success;
	
	public int userid;
}