package messages;

import java.io.Serializable;

public class AuthRequest implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public boolean isGuest;
	public boolean newUser;
	
	public String username;
	public String password;
}
