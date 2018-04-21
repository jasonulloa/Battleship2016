package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import messages.*;
import utility.*;

public class UserListener extends Thread 
{
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	// -1 if not logged in or logged in as a guest,
	// otherwise set to the ID of the user in the DB.
	private int userID;
	// true if logged in as a guest
	private boolean guest;
	
	private int guestID;
	
	private boolean connected;
	public boolean isConnected()
	{
		return connected;
	}
	
	private int activeGameID;
	
	private Socket socket;
	
	public UserListener otherThread; 
	
	private BoardState gameState;
	
	public UserListener(Socket s) 
	{
		userID = -1;
		guest = false;
		
		activeGameID = -1;
		
		socket = s;
		
		connected = false;
		
		try
		{
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
		}
		catch (IOException ioe) 
		{
			System.out.println("ioe: " + ioe.getMessage());
		}
	}
	
	public void LobbySettingsChange(LobbySettingsChange lsc){
		try {
			oos.writeObject(lsc);
			oos.flush();
		} catch (IOException ioe){
			System.out.println("ioe in run: " + ioe.getMessage());
		}
	}
	
	public void ChatMessage(ChatMessage cm) {
		try {
			oos.writeObject(cm);
			oos.flush();
		} catch (IOException ioe){
			System.out.println("ioe in run: " + ioe.getMessage());
		}
	}
	
	public void OtherTurnTaken(TileChange lastMove)
	{
		gameState.ApplyChange(lastMove);
		try {
			GameMoveMessage gmm = new GameMoveMessage(lastMove);
			
			// write response
			oos.writeObject(gmm);
			oos.flush();
		} catch (IOException ioe) 
		{
			System.out.println("ioe in run: " + ioe.getMessage());
		}
	}
	
	public void UserLeft(Game game)
	{
		try
		{
			UserLeftMessage m = new UserLeftMessage(game);
			
			// write response
			oos.writeObject(m);
			oos.flush();
		} 
		catch (IOException ioe) 
		{
			System.out.println("ioe in run: " + ioe.getMessage());
		}
	}
	
	public void UserJoined(Game game)
	{
		try
		{
			UserJoinedMessage m = new UserJoinedMessage(game);
			
			// write response
			oos.writeObject(m);
			oos.flush();
		} 
		catch (IOException ioe) 
		{
			System.out.println("ioe in run: " + ioe.getMessage());
		}
	}
	
	public void refreshOverviewForAll() {
		for(UserListener thread : BattleshipServer._instance.getConnectedUsers()){
			if (thread.isConnected() && thread.userID != this.userID)
			{
				thread.sendRefreshOverviewMessage();
			}
		}
	}
	
	public void sendRefreshOverviewMessage()
	{
		try
		{
			RefreshOverview ro = new RefreshOverview();
			oos.writeObject(ro);
			oos.flush();
		} 
		catch (IOException ioe) 
		{
			System.out.println("ioe in run: " + ioe.getMessage());
		}
	}
	
	public void SignalPlacementsDone() {
		try
		{
			ShipPlacementDoneMessage m = new ShipPlacementDoneMessage(gameState);
					
			// write response
			oos.writeObject(m);
			oos.flush();
		} 
		catch (IOException ioe) 
		{
			System.out.println("ioe in run: " + ioe.getMessage());
		}
	}
	
	public void GameStarted(BoardState boardState)
	{
		try
		{
			GameStartMessage m = new GameStartMessage(boardState);
			
			// write response
			oos.writeObject(m);
			oos.flush();
		} 
		catch (IOException ioe) 
		{
			System.out.println("ioe in run: " + ioe.getMessage());
		}
	}
	
	public void GameDestroyed()
	{
		try
		{
			GameOverMessage m = new GameOverMessage();
			
			// write response
			oos.writeObject(m);
			oos.flush();
		} 
		catch (IOException ioe) 
		{
			System.out.println("ioe in run: " + ioe.getMessage());
		}
	}
	
	public void GameOver(int winner)
	{
		try
		{
			GameOverMessage m = new GameOverMessage();
			
			m.gameFinished = true;
			m.winner = winner;
			
			// write response
			oos.writeObject(m);
			oos.flush();
		} 
		catch (IOException ioe) 
		{
			System.out.println("ioe in run: " + ioe.getMessage());
		}
	}

	public void run()
	{
		try {
			connected = true;
			while(socket.isConnected()) 
			{
				// get request
				Object message = ois.readObject();
				
				//if board state message, check if the tile is a hit 
				if (message instanceof GameMoveMessage)
				{
					GameMoveMessage bsm = (GameMoveMessage) message;
					TileChange tc = bsm.getTileChange();
					//int tile = tc.getTileID();
					//int user1 = tc.getUserID();
					
					boolean isHit = gameState.ApplyChange(tc);
					
					if(gameState.hasLost)
					{
						DatabaseManager.GameOver(activeGameID, gameState.winner, gameState.loser);
						
						this.GameOver(gameState.winner);
						if(otherThread != null)
						{
							otherThread.GameOver(gameState.winner);
						}
					}
					else
					{
						TileChange result = new TileChange(tc.getX(), tc.getY(),
														   tc.getGameID(),
														   tc.getUserID(),
														   isHit);
						GameMoveMessage gmm = new GameMoveMessage(result);
						
						// write response
						oos.writeObject(gmm);
						oos.flush();
						
						gameState.ToggleTurn();
						
						DatabaseManager.TakeTurn(gameState);
						
						if(otherThread != null)
						{
							otherThread.OtherTurnTaken(result);
						}
					}
				}
				
				//if receiving lobby
				if (message instanceof LobbyRequest){
					//get all possible lobbies from database and send them to the client
					
					LobbyResponse response =  new LobbyResponse(DatabaseManager.GetLobbies());
					
					// write response
					oos.writeObject(response);
					oos.flush();
				}
				
				//if trying to get active games
				if (message instanceof ActiveGamesRequest){
					//get all currently active games for the user from database and send them to the client
					//ActiveGamesRequest(new Vector<BoardState>())
					
					ActiveGamesResponse response = new ActiveGamesResponse(DatabaseManager.GetGames(userID));
					
					// write response
					oos.writeObject(response);
					oos.flush();
				}
				
				if (message instanceof LeaderListRequest){
					LeaderListResponse response = new LeaderListResponse(DatabaseManager.GetLeaderListNames(), DatabaseManager.GetLeaderListGamesWon());
					oos.writeObject(response);
					oos.flush();
				}
				
				if(message instanceof ShipPlacementMessage)
				{
					boolean firstUser = (userID == gameState.getUser1ID());
					
					BattleshipServer.addMessage("UserID "+userID+" has placed their ships");
					gameState.setBoard(((ShipPlacementMessage) message).shipPlacements, firstUser);
					boolean bothUsers = DatabaseManager.InitializeTiles(gameState, firstUser);
					gameState = DatabaseManager.GetGameState(gameState.getGameID());
					System.out.println("Ship placement message recieved, both users done = " + bothUsers);
					
					if(bothUsers)
					{						
						SignalPlacementsDone();
						
						if(otherThread != null)
						{
							otherThread.gameState = gameState;
							otherThread.SignalPlacementsDone();
						}
					}
				}
				
				if(message instanceof JoinGameRequest)
				{
					if(DatabaseManager.GameExists(((JoinGameRequest)message).gameID))
					{
						activeGameID = ((JoinGameRequest)message).gameID;
						gameState = DatabaseManager.GetGameState(activeGameID);

						for(UserListener thread : BattleshipServer._instance.getConnectedUsers())
						{
							if(thread.activeGameID == activeGameID && thread.userID != this.userID)
							{
								thread.otherThread = this;
								this.otherThread = thread;
								break;
							}
						}
						
						if(gameState.getStatus() == 0)
						{
							DatabaseManager.JoinLobby(activeGameID, userID);
							gameState.setUser2ID(userID);
							//refresh gameState
							//gameState = DatabaseManager.GetGameState(activeGameID);
							
							if(otherThread != null)
							{
								//refresh gameState
								otherThread.gameState = gameState;
								
								Game game = DatabaseManager.GetGame(activeGameID);
								BattleshipServer.addMessage("UserID "+gameState.getUser2ID()+" joined the lobby of userID "+gameState.getUser1ID());

								otherThread.UserJoined(game);
								this.UserJoined(game);
							}
						}
						if(gameState.getStatus() > 1)
						{
							this.GameStarted(gameState);
						}
					}
					refreshOverviewForAll();
				}
				
				if(message instanceof StartGameRequest)
				{
					if(activeGameID >= 0)
					{
						StartGameRequest m = (StartGameRequest) message;
						
						gameState.setStatus(Constants.PLACING_SHIPS);
						gameState.UpdateBoardSize(m.mapsize);
						gameState.setShipCount(m.shipCounts);
						DatabaseManager.StartGame(activeGameID);
						
						this.GameStarted(gameState);

						if(otherThread != null)
						{
							otherThread.gameState = gameState;
							otherThread.GameStarted(gameState);
						}
						
						refreshOverviewForAll();
					}

				}
				
				if(message instanceof DeleteLobbyRequest)
				{
					if(activeGameID >= 0)
					{	
						DatabaseManager.DeleteGame(activeGameID);
						BattleshipServer.addMessage("UserID "+userID+" deleted their lobby");
						
						if(otherThread != null)
						{
							otherThread.GameDestroyed();
						}
					}
					refreshOverviewForAll();
				}
				
				if(message instanceof CreateLobbyRequest)
				{	
					activeGameID = DatabaseManager.CreateLobby(userID);

					gameState = new BoardState(userID, -1, activeGameID, Constants.LOBBY_STARTED, 1);
					BattleshipServer.addMessage("UserID "+userID+" has created a lobby");
					
					Game game = DatabaseManager.GetGame(activeGameID);
					try {
						oos.writeObject(new CreateLobbyResponse(game));
						oos.flush();
					} catch (IOException ioe){
						ioe.getMessage();
					}
					refreshOverviewForAll();
				}
				
				
				if(message instanceof LeaveGameRequest)
				{
					LeaveGame();
					
				}
				if (message instanceof LobbySettingsChange){
					if (otherThread != null){
						otherThread.LobbySettingsChange((LobbySettingsChange)message);
					}
				}
				if (message instanceof ChatMessage){
					if (otherThread != null){
						otherThread.ChatMessage((ChatMessage)message);
					}
				}
				// process request
				if(message instanceof AuthRequest)
				{
					AuthRequest ar = (AuthRequest) message;
					
					AuthResponse response = new AuthResponse();
					response.isGuest = ar.isGuest;
					response.newUser = ar.newUser;
					
					if(ar.newUser)
					{
						BattleshipServer.addMessage(String.format("Recieved signup request for user %s (pass: %s)", ar.username, ar.password));
						
						// add user to DB
						userID = DatabaseManager.AddUser(ar.username, ar.password);
						response.userid = userID;

						// create reply
						response.success = (userID != -1);
						
						// log result
						if(response.success)
						{
							BattleshipServer.addMessage("User added as " + userID);
						}
						else
						{
							BattleshipServer.addMessage("Signup failure");
						}
					}
					else
					{
						if(ar.isGuest)
						{
							BattleshipServer.addMessage(String.format("Recieved guest login request"));
							guest = true;
							guestID = 1;
							while (userID==-1) {
								userID = DatabaseManager.AddUser("Guest"+guestID, "");
								guestID++;
							}
							System.out.println(guestID);
							response.userid = userID;
							response.success = (userID != -1);

						}
						else
						{
							BattleshipServer.addMessage(String.format("Recieved login request from user %s (pass: %s)", ar.username, ar.password));
							userID = DatabaseManager.GetUser(ar.username, ar.password);
							response.success = (userID != -1);
							response.userid = userID;
						}
					}
					
					// write response
					oos.writeObject(response);
					oos.flush();
				}
			}
		} catch (ClassNotFoundException cnfe) 
		{
			System.out.println("cnfe in run: " + cnfe.getMessage());
		} catch (IOException ioe) 
		{
			System.out.println("ioe in run: " + ioe.getMessage());
		}
		
		connected = false;
		
		DatabaseManager.UpdateUserLoggedIn(this.userID, 0);
		// disconnecting is like leaving a game
		LeaveGame();
		
		BattleshipServer.addMessage("User disconnected: " + socket.getInetAddress());
	}
	
	private void LeaveGame()
	{
		if(activeGameID > 0)
		{
			//make sure gameState is up to date
			gameState = DatabaseManager.GetGameState(activeGameID);
			if (gameState != null) {
				// IF A LOBBY, REMOVE THIS USER FROM THE LOBBY IN THE DATABASE
				if(gameState.getStatus() == Constants.LOBBY_STARTED)
				{
					DatabaseManager.DeleteGame(activeGameID);
	
					if(otherThread != null)
					{
						otherThread.GameDestroyed();
					}
				}
	
				if(gameState.getStatus() == Constants.SECOND_USER)
				{
					if(otherThread != null)
					{
						BattleshipServer.addMessage("UserID "+userID+" left the lobby of userID "+otherThread.userID);
						DatabaseManager.LeaveLobby(activeGameID, userID);
						Game game = DatabaseManager.GetGame(activeGameID);
						otherThread.UserLeft(game);
					}
				}
				if (gameState.getStatus() == Constants.PLACING_SHIPS){
					if (otherThread != null){
						BattleshipServer.addMessage("UserID " +userID+" left during ship placement of game "+activeGameID);
						DatabaseManager.LeaveLobby(activeGameID, userID);
						Game game = DatabaseManager.GetGame(activeGameID);
						game.setStatus(Constants.PLACING_SHIPS);
						otherThread.UserLeft(game);
						DatabaseManager.DeleteGame(activeGameID);
						DatabaseManager.UpdateUserLoggedIn(this.userID, 0);
					}
				}
				// IF THE USER IS A GUEST, CONCEDE THE GAME
				if(guest)
				{
					DatabaseManager.DeleteGame(activeGameID);
					if(otherThread != null)
					{
						otherThread.GameDestroyed();
					}
				}
			}
		}
		activeGameID = -1;
		
		if(otherThread != null)
		{
			otherThread.otherThread = null;
		}
		otherThread = null;
	}
}
