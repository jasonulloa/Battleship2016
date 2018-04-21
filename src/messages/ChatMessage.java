package messages;

import java.io.Serializable;

public class ChatMessage implements Serializable
{
	private static final long serialVersionUID = 1L;

	public String chatmessage;
	
	public ChatMessage(String cm) {
		chatmessage = cm;
	}
}
