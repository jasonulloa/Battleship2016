package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import messages.*;
import utility.BoardState;
import utility.Constants;
import utility.Game;
import utility.TileChange;

public class ServerCommunicator extends Thread
{
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	public String hostname;
	public int port;
	
	private Socket s;
	
	private boolean connected;
	
	private BattleshipClient bc;
	
	private int userid;

	/**
	 * Reports whether the client connected to the server currently.
	 * @return client connection status
	 */
	public boolean isConnected()
	{
		return connected && s.isConnected();
	}
	
	public ServerCommunicator(BattleshipClient BC)
	{
		bc = BC;
		port = Constants.port;
		hostname = "localhost";
	}
	
	/**
	 * Attempts to establish a connection with a server at the 
	 * hostname and port determined by the ServerCommunicator's
	 * current values.
	 */
	public void Connect()
	{
		try
		{
			if (s != null) 
			{
				s.close();
			}
			
			s = new Socket(hostname, port);
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			
			connected = true;
		} 
		catch (IOException ioe) 
		{
			System.out.println("ioe: " + ioe.getMessage());
		} 
	}
	
	/**
	 * Attempts to disconnect from its current session, if possible.
	 */
	public void Disconnect()
	{
		connected = false;
		
		try 
		{
			if (s != null) 
			{
				s.close();
			}
		}
		catch (IOException ioe) 
		{
			System.out.println("ioe: " + ioe.getMessage());
		}
		
		s = null;
	}
	
	/**
	 * Sends a login request to the server as a guest. 
	 * Returns when it receives confirmation from the server.
	 * @return true if the server sends back a response.
	 */
	public void LoginAsGuest()
	{
		try 
		{
			AuthRequest message = new AuthRequest();
			
			message.isGuest = true;
			
			oos.writeObject(message);
			oos.flush();			
		} 
		catch (IOException ioe) 
		{
			System.out.println("ioe: " + ioe.getMessage());
		} 
	}
	
	/**
	 * Sends a login request to the server as a user. 
	 * Returns when it receives confirmation from the server.
	 * @return true if the server successfully authorizes the credentials
	 */
	// TODO add enum return type to indicate failure reason
	public void LoginAsUser(String username, String password)
	{
		try 
		{
			AuthRequest message = new AuthRequest();
			
			message.username = username;
			message.password = password;
			
			oos.writeObject(message);
			oos.flush();			
		} 
		catch (IOException ioe) 
		{
			System.out.println("ioe: " + ioe.getMessage());
		} 
	}
	
	/**
	 * Sends a signup request to the server. 
	 * Returns when it receives confirmation from the server.
	 * @return true if the server successfully adds the user to the database.
	 */
	// TODO add enum return type to indicate failure reason
	public void Signup(String username, String password)
	{
		try 
		{
			AuthRequest message = new AuthRequest();
			
			message.username = username;
			message.password = password;
			message.newUser = true;
			
			oos.writeObject(message);
			oos.flush();
		} 
		catch (IOException ioe) 
		{
			System.out.println("ioe: " + ioe.getMessage());
		} 
	}
	
	//sends the current board with newly fired tile to the server, check if it was a hit
	public boolean checkHit(TileChange tile){
		
		GameMoveMessage message = new GameMoveMessage(tile);
		try {
			oos.writeObject(message);
			oos.flush();		
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public void createLobby() {
		CreateLobbyRequest message = new CreateLobbyRequest();
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startGame(int mapsize, ArrayList<Integer> shipCounts) {
		StartGameRequest message = new StartGameRequest(mapsize, shipCounts);
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteLobby() {
		DeleteLobbyRequest message = new DeleteLobbyRequest();
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void joinLobby(int gameID) {
		JoinGameRequest message = new JoinGameRequest();
		message.gameID = gameID;
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void continueGame(int gameID) {
		JoinGameRequest message = new JoinGameRequest();
		message.gameID = gameID;
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void leaveGame() {
		LeaveGameRequest message = new LeaveGameRequest();
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendShipPlacements(BoardState bs) {
		ShipPlacementMessage message = new ShipPlacementMessage(bs);
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void gameMove(TileChange tc) {
		GameMoveMessage message = new GameMoveMessage(tc);
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void requestLobbies() {
		LobbyRequest message = new LobbyRequest();
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void requestActiveGames() {
		ActiveGamesRequest message = new ActiveGamesRequest();
		try {
			oos.writeObject(message);
			oos.flush();
			
		} catch (IOException | NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	public void sendLobbySettingChange(int mapSize, ArrayList<Integer> shipCounts) {
		try {
			LobbySettingsChange lsc = new LobbySettingsChange(mapSize, shipCounts);
			oos.writeObject(lsc);
			oos.flush();
			
		} catch (IOException | NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	
	public void sendChatMessage(String username, String text) {
		try {
			ChatMessage cm = new ChatMessage(username + ": " + text);
			oos.writeObject(cm);
			oos.flush();
			
		} catch (IOException | NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	public void requestLeaderList() {
		try {
			LeaderListRequest llr = new LeaderListRequest();
			oos.writeObject(llr);
			oos.flush();
			
		} catch (IOException | NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	public int getUserID() {
		return userid;
	}

	public void run()
	{
		try {
			while(this.isConnected()) 
			{
				// get request
				Object message = ois.readObject();
				
				//if a user joined your lobby
				if (message instanceof GameStartMessage) {
					GameStartMessage m = (GameStartMessage) message;
					if (m.boardState.getStatus() == Constants.PLACING_SHIPS) {
						bc.gwingui.freshGame(m.shipCounts, m.boardState);
					} else {
						bc.gwingui.loadGame(m.boardState);
					}
					bc.swapGUI("game");
				}
				if (message instanceof ShipPlacementDoneMessage)
				{
					System.out.println(((ShipPlacementDoneMessage) message).boardState.getStatus());
					bc.gwingui.setBoardState(((ShipPlacementDoneMessage) message).boardState);
				}
				if (message instanceof UserJoinedMessage) {
					bc.lobbygui.setGame(((UserJoinedMessage) message).game);
					if (this.userid == (((UserJoinedMessage) message).game).getUser1ID()) {
						this.sendLobbySettingChange(bc.lobbygui.mapSize, bc.lobbygui.generateShipCounts());
					}
				}
				if (message instanceof UserLeftMessage) {
					bc.lobbygui.setGame(((UserLeftMessage) message).game);
					Game game = (((UserLeftMessage) message).game);
					if (game.getStatus() == Constants.PLACING_SHIPS){
						bc.gwingui.gameOver(this.userid);
						bc.swapGUI("overview");
					}
					
				}
				if (message instanceof CreateLobbyResponse) {
					bc.lobbygui.setGame(((CreateLobbyResponse) message).game);
				}
				if (message instanceof AuthResponse)
				{
					AuthResponse ar = (AuthResponse) message;
					userid = ar.userid;
					if (ar.isGuest) {
						bc.logingui.guestResponse(ar.success);
					} else if (ar.newUser) {
						bc.logingui.signupResponse(ar.success);
					} else {
						bc.logingui.loginResponse(ar.success);
					}
				}
				if (message instanceof GameOverMessage)
				{
					bc.gwingui.gameOver(((GameOverMessage)message).winner);
				}
				if (message instanceof GameMoveMessage)
				{
					if(bc.gwingui.isMyTurn())
					{
						bc.gwingui.HitResponse(((GameMoveMessage) message).getTileChange());
					}
					else
					{
						bc.gwingui.OtherTakeTurn(((GameMoveMessage) message).getTileChange());
					}
				}
				if (message instanceof ActiveGamesResponse)
				{
					bc.overgui.getActiveGamesResponse(((ActiveGamesResponse)message).getActiveGames());
				}
				if (message instanceof LobbyResponse){
					bc.overgui.getLobbiesResponse(((LobbyResponse)message).getLobbys());
				}
				if (message instanceof LobbySettingsChange){
					bc.lobbygui.changeLobbySettings((LobbySettingsChange)message);
				}
				if (message instanceof ChatMessage){
					bc.lobbygui.recieveChatMessage((ChatMessage)message);
				}
				if (message instanceof LeaderListResponse){
					bc.overgui.getLeaderListResponse(((LeaderListResponse)message).getLeaderList());
				}
				if (message instanceof RefreshOverview) {
					bc.overgui.refresh();
				}
			}
		} catch (ClassNotFoundException cnfe) 
		{
			System.out.println("cnfe in run: " + cnfe.getMessage());
		} catch (IOException ioe) 
		{
			System.out.println("ioe in run: " + ioe.getMessage());
			//if (bc.gwingui.boardState.getStatus() == 2){
				//bc.sCommunicator.leaveGame();
			//}
		}
	}
}
