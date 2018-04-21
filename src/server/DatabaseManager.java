package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.mysql.jdbc.Driver;

import utility.BoardState;
import utility.Constants;
import utility.Game;
import utility.TileState;

public class DatabaseManager {
	private static Connection con;
	
	public static String configFileName = "sql_settings.cfg";
	public static String sqlusername = "PLEASE ENTER YOUR USERNAME";
	public static String sqlpassword = "nope";
	
	public static String schemaName = "battleship";
	public static String usersTableName = "users";
	public static String gamesTableName = "games";
	public static String shipsTableName = "ships";
	public static String tilesTableName = "tiles";
	
	private final static String updateTile = "UPDATE " + tilesTableName + " SET isHit=?, isShip=? WHERE gameid=? AND userid=? AND x=? AND y=?";
	private final static String insertTile = "INSERT INTO " + tilesTableName + " (gameid,userid,x,y,isHit,isShip) VALUES(?,?,?,?,?,?)";
	private final static String getTiles = "SELECT * FROM " + tilesTableName + " WHERE gameid=? AND userid=?";
	
	private final static String selectLobbies = "SELECT * FROM " + gamesTableName + " WHERE gamestate="+Constants.LOBBY_STARTED;
	private final static String selectActiveGames = "SELECT * FROM " + gamesTableName + " WHERE (user1=? OR user2=?) AND (gamestate="+Constants.PLACING_SHIPS+" OR gamestate="+Constants.USER1_TURN+" OR gamestate="+Constants.USER2_TURN+")";
	private final static String updateGameState = "UPDATE " + gamesTableName + " SET gamestate=? WHERE id=?";
	private final static String updateGameUser2 = "UPDATE " + gamesTableName + " SET user2=? WHERE id=?";
	private final static String updateMapSize = "UPDATE " + gamesTableName + " SET mapsize=? WHERE id=?";
	private final static String deleteGame = "DELETE FROM " + gamesTableName + " WHERE id=?";
	private final static String insertGame = "INSERT INTO " + gamesTableName + " (user1,gamestate) VALUES(?,0)";
	private final static String getGame = "SELECT * FROM " + gamesTableName + " WHERE id=?";
	
	private final static String updateUserWins = "UPDATE " + usersTableName + " SET gamesplayed=?, gameswon=? WHERE id=?";
	private final static String selectUserID = "SELECT * FROM " + usersTableName + " WHERE id=?";
	private final static String selectUser = "SELECT * FROM " + usersTableName + " WHERE USERNAME=? AND PASSWORD=? AND loggedin=0";
	private final static String updateUserLoggedIn = "UPDATE " + usersTableName + " SET loggedin=? WHERE id=?";	
	private final static String deleteGuest = "DELETE FROM " + usersTableName + " WHERE id=?";
	private final static String isGuest = "SELECT * FROM " + usersTableName + " WHERE id=? AND PASSWORD=?";
	private final static String existsUser = "SELECT * FROM " + usersTableName + " WHERE USERNAME=?";
	private final static String insertUser = "INSERT INTO " + usersTableName + " (USERNAME,PASSWORD) VALUES(?,?)";
	private final static String getLeaders = "SELECT * FROM " + usersTableName + " ORDER BY gameswon DESC LIMIT 10 ";
	
	public DatabaseManager() {
		try {
			new Driver();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(new File(configFileName)));
			
		    String line = null;
		    while ((line = reader.readLine()) != null) 
		    {
		        if (line.length() > 3)
		        {
		        	String[] lineElements = line.split("=");
		        	if(lineElements[0].equals("sqlusername"))
		        	{
		        		sqlusername = lineElements[1];
		        	}
		        	if(lineElements[0].equals("sqlpassword"))
		        	{
		        		sqlpassword = lineElements[1];
		        	}
		        }
		    }
		    
		    reader.close();
		} 
		catch (IOException x) 
		{
		    System.err.format("IOException: %s%n", x);
		}
		
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + schemaName + "?user=" + sqlusername + "&password=" + sqlpassword);	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean doesExist(String username) {
		try {
			PreparedStatement ps = con.prepareStatement(existsUser);
			ps.setString(1, username);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean isGuest(int userid) {
		try {
			PreparedStatement ps = con.prepareStatement(isGuest);
			ps.setInt(1, userid);
			ps.setString(2, "");
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static int AddUser(String username, String password) 
	{	
		if (doesExist(username) == true) {
			return -1;
		}
		
		try {
			PreparedStatement ps = con.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, username);
			ps.setString(2, password);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
	        if (rs.next()){
				DatabaseManager.UpdateUserLoggedIn(rs.getInt(1), 1);
	            return rs.getInt(1);
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public static void UpdateUserLoggedIn(int userid, int loggedin) {
		try {
			PreparedStatement ps = con.prepareStatement(updateUserLoggedIn);
			
			ps.setInt(1, loggedin);
			ps.setInt(2, userid);
			ps.executeUpdate();
			if (loggedin==0 && isGuest(userid)) {
				PreparedStatement ps2 = con.prepareStatement(deleteGuest);
				ps2.setInt(1, userid);
				ps2.executeUpdate();
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static int GetUser(String username, String password) 
	{
		try {
			PreparedStatement ps = con.prepareStatement(selectUser);
			ps.setString(1, username);
			ps.setString(2, password);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				DatabaseManager.UpdateUserLoggedIn(result.getInt("id"), 1);
				return result.getInt("id");
			}
			return -1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public static void StartGame(int gameID) 
	{			
		try {
			PreparedStatement ps = con.prepareStatement(updateGameState);
			ps.setInt(1, Constants.PLACING_SHIPS);
			ps.setInt(2, gameID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean InitializeTiles(BoardState bs, boolean firstUser) {
		try {
			PreparedStatement mp = con.prepareStatement(updateMapSize);
			mp.setInt(1, bs.getMapSize());
			mp.setInt(2, bs.getGameID());
			mp.executeUpdate();
			
			if (firstUser == true) {
				for(int x = 0; x < bs.user1board.length; x++)
				{
					for(int y = 0; y < bs.user1board[x].length; y++)
					{
						PreparedStatement ps = con.prepareStatement(insertTile);
						ps.setInt(1, bs.getGameID());
						ps.setInt(2, bs.getUser1ID());
						ps.setInt(3, bs.user1board[x][y].x);
						ps.setInt(4, bs.user1board[x][y].y);
						ps.setInt(5, bs.user1board[x][y].beenShot ? 1 : 0);
						ps.setInt(6, bs.user1board[x][y].isShip ? 1 : 0);
						ps.executeUpdate();
					}
				}
				
				PreparedStatement ps2 = con.prepareStatement(getTiles);
				ps2.setInt(1, bs.getGameID());
				ps2.setInt(2, bs.getUser2ID());
				ResultSet user2Tiles = ps2.executeQuery();
				while (user2Tiles.next()) {
					PreparedStatement gs = con.prepareStatement(updateGameState);
					gs.setInt(1, Constants.USER1_TURN);
					gs.setInt(2, bs.getGameID());
					gs.executeUpdate();
					
					return true;
				}
				
				return false;
			} else {
				for(int x = 0; x < bs.user2board.length; x++)
				{
					for(int y = 0; y < bs.user2board[x].length; y++)
					{
						PreparedStatement pss = con.prepareStatement(insertTile);
						pss.setInt(1, bs.getGameID());
						pss.setInt(2, bs.getUser2ID());
						pss.setInt(3, bs.user2board[x][y].x);
						pss.setInt(4, bs.user2board[x][y].y);
						pss.setInt(5, bs.user2board[x][y].beenShot ? 1 : 0);
						pss.setInt(6, bs.user2board[x][y].isShip ? 1 : 0);
						pss.executeUpdate();
					}
				}
				
				PreparedStatement ps1 = con.prepareStatement(getTiles);
				ps1.setInt(1, bs.getGameID());
				ps1.setInt(2, bs.getUser1ID());
				ResultSet user1Tiles = ps1.executeQuery();
				while (user1Tiles.next()) {
					PreparedStatement gs1 = con.prepareStatement(updateGameState);
					gs1.setInt(1, Constants.USER1_TURN);
					gs1.setInt(2, bs.getGameID());
					gs1.executeUpdate();
					
					return true;
				}
				
				return false;
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void TakeTurn(BoardState bs) {
		try {
			for(int x = 0; x < bs.user1board.length; x++)
			{
				for(int y = 0; y < bs.user1board[x].length; y++)
				{
					PreparedStatement ps = con.prepareStatement(updateTile);
					ps.setInt(1, bs.user1board[x][y].beenShot ? 1 : 0);
					ps.setInt(2, bs.user1board[x][y].isShip ? 1 : 0);
					ps.setInt(3, bs.getGameID());
					ps.setInt(4, bs.getUser1ID());
					ps.setInt(5, bs.user1board[x][y].x);
					ps.setInt(6, bs.user1board[x][y].y);
					ps.executeUpdate();
				}
			}
			
			for(int x = 0; x < bs.user2board.length; x++)
			{
				for(int y = 0; y < bs.user2board[x].length; y++)
				{
					PreparedStatement pss = con.prepareStatement(updateTile);
					pss.setInt(1, bs.user2board[x][y].beenShot ? 1 : 0);
					pss.setInt(2, bs.user2board[x][y].isShip ? 1 : 0);
					pss.setInt(3, bs.getGameID());
					pss.setInt(4, bs.getUser2ID());
					pss.setInt(5, bs.user2board[x][y].x);
					pss.setInt(6, bs.user2board[x][y].y);
					pss.executeUpdate();
				}
			}
			
			PreparedStatement gs = con.prepareStatement(updateGameState);
			gs.setInt(1, bs.getStatus());
			gs.setInt(2, bs.getGameID());
			gs.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Vector<String> GetLeaderListNames() {
		Vector<String> names = new Vector<String>();
		
		try {
			PreparedStatement ps = con.prepareStatement(getLeaders);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				names.add(result.getString("username"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return names;
	}
	
	public static Vector<Integer> GetLeaderListGamesWon() {
		Vector<Integer> won = new Vector<Integer>();
		
		try {
			PreparedStatement ps = con.prepareStatement(getLeaders);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				won.add(result.getInt("gameswon"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return won;
	}
	
	public static void GameOver(int gameID, int winnerID, int loserID) {
		try {
			PreparedStatement ps1 = con.prepareStatement(selectUserID);
			ps1.setInt(1, winnerID);
			ResultSet winner = ps1.executeQuery();
			winner.next();
			
			PreparedStatement pss1 = con.prepareStatement(updateUserWins);
			pss1.setInt(1, 1+winner.getInt("gamesplayed"));
			pss1.setInt(2, 1+winner.getInt("gameswon"));
			pss1.setInt(3, winnerID);
			pss1.executeUpdate();
			
			PreparedStatement ps2 = con.prepareStatement(selectUserID);
			ps2.setInt(1, loserID);
			ResultSet loser = ps2.executeQuery();
			loser.next();
			
			PreparedStatement pss2 = con.prepareStatement(updateUserWins);
			pss2.setInt(1, 1+loser.getInt("gamesplayed"));
			pss2.setInt(2, loser.getInt("gameswon"));
			pss2.setInt(3, loserID);
			pss2.executeUpdate();
			
			PreparedStatement gs = con.prepareStatement(updateGameState);
			gs.setInt(1, Constants.GAME_OVER);
			gs.setInt(2, gameID);
			gs.executeUpdate();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void DeleteGame(int gameID) 
	{			
		try {
			PreparedStatement ps = con.prepareStatement(deleteGame);
			ps.setInt(1, gameID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static int CreateLobby(int user1ID) 
	{			
		try {
			PreparedStatement ps = con.prepareStatement(insertGame, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, user1ID);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
	        if (rs.next()){
	            return rs.getInt(1);
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public static void JoinLobby(int gameID, int user2ID) {
		try {
			PreparedStatement ps = con.prepareStatement(updateGameState);
			ps.setInt(1, Constants.SECOND_USER);
			ps.setInt(2, gameID);
			ps.executeUpdate();
			
			PreparedStatement ps2 = con.prepareStatement(updateGameUser2);
			ps2.setInt(1, user2ID);
			ps2.setInt(2, gameID);
			ps2.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void LeaveLobby(int gameID, int user2ID) {
		try {
			PreparedStatement ps = con.prepareStatement(updateGameState);
			ps.setInt(1, Constants.LOBBY_STARTED);
			ps.setInt(2, gameID);
			ps.executeUpdate();
			
			PreparedStatement ps2 = con.prepareStatement(updateGameUser2);
			ps2.setInt(1, 0);
			ps2.setInt(2, gameID);
			ps2.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Vector<Game> GetLobbies() {
		Vector<Game> lobbies = new Vector<Game>();
		
		try {
			PreparedStatement ps = con.prepareStatement(selectLobbies);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				PreparedStatement ps1 = con.prepareStatement(selectUserID);
				ps1.setInt(1, result.getInt("user1"));
				ResultSet user1 = ps1.executeQuery();
				
				String username1 = "";
				if (user1.next()) {
					username1 = user1.getString("username");
				}
				
				PreparedStatement ps2 = con.prepareStatement(selectUserID);
				ps2.setInt(1, result.getInt("user2"));
				ResultSet user2 = ps2.executeQuery();
				
				String username2 = "";
				if (user2.next()) {
					username2 = user2.getString("username");
				}
				
				Game l = new Game(result.getInt("id"), result.getInt("user1"), username1, result.getInt("user2"), username2);
				
				lobbies.add(l);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lobbies;
	}
	
	public static boolean GameExists(int gameID) {
		try {
			PreparedStatement ps = con.prepareStatement(getGame);
			ps.setInt(1, gameID);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static Game GetGame(int gameID) {
		try {
			PreparedStatement ps = con.prepareStatement(getGame);
			ps.setInt(1, gameID);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				PreparedStatement ps1 = con.prepareStatement(selectUserID);
				ps1.setInt(1, result.getInt("user1"));
				ResultSet user1 = ps1.executeQuery();
				
				String username1 = "";
				if (user1.next()) {
					username1 = user1.getString("username");
				}
				
				PreparedStatement ps2 = con.prepareStatement(selectUserID);
				ps2.setInt(1, result.getInt("user2"));
				ResultSet user2 = ps2.executeQuery();
				
				String username2 = "";
				if (user2.next()) {
					username2 = user2.getString("username");
				}
				
				return new Game(result.getInt("id"), result.getInt("user1"), username1, result.getInt("user2"), username2);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static BoardState GetGameState(int gameID)
	{
		BoardState bs = new BoardState();
		try {
			PreparedStatement ps = con.prepareStatement(getGame);
			ps.setInt(1, gameID);
			ResultSet result = ps.executeQuery();
			
			if(result.next())
			{
				bs.setGameID(result.getInt("id"));
				bs.setUser1ID(result.getInt("user1"));
				bs.setUser2ID(result.getInt("user2"));
				bs.setStatus(result.getInt("gamestate"));			
				bs.setMapSize(result.getInt("mapsize"));
				
				if (result.getInt("gamestate") < 2) {
					return bs;
				}
				
				bs.user1board = new TileState[bs.getMapSize()][bs.getMapSize()];
				bs.user2board = new TileState[bs.getMapSize()][bs.getMapSize()];
				
				PreparedStatement ps1 = con.prepareStatement(getTiles);
				ps1.setInt(1, bs.getGameID());
				ps1.setInt(2, bs.getUser1ID());
				ResultSet user1Tiles = ps1.executeQuery();
				while (user1Tiles.next()) {
					bs.user1board[user1Tiles.getInt("x")][user1Tiles.getInt("y")] = new TileState(user1Tiles.getInt("x"),user1Tiles.getInt("y"),user1Tiles.getInt("isHit") == 1 ? true : false,user1Tiles.getInt("isShip") == 1 ? true : false);
				}
				
				PreparedStatement ps2 = con.prepareStatement(getTiles);
				ps2.setInt(1, bs.getGameID());
				ps2.setInt(2, bs.getUser2ID());
				ResultSet user2Tiles = ps2.executeQuery();
				while (user2Tiles.next()) {
					bs.user2board[user2Tiles.getInt("x")][user2Tiles.getInt("y")] = new TileState(user2Tiles.getInt("x"),user2Tiles.getInt("y"),user2Tiles.getInt("isHit") == 1 ? true : false,user2Tiles.getInt("isShip") == 1 ? true : false);
				}
			}
			else
			{
				// no such game
				return null;
			}
			
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return bs;
	}
	
	public static Vector<Game> GetGames(int userID) {
		// user
		if(userID >= 0)
		{
			Vector<Game> games = new Vector<Game>();
			
			try {
				PreparedStatement ps = con.prepareStatement(selectActiveGames);
				ps.setInt(1, userID);
				ps.setInt(2, userID);
				ResultSet result = ps.executeQuery();
				while (result.next()) {
					PreparedStatement ps1 = con.prepareStatement(selectUserID);
					ps1.setInt(1, result.getInt("user1"));
					ResultSet user1 = ps1.executeQuery();

					String username1 = "";
					if (user1.next()) {
						username1 = user1.getString("username");
					}
					
					PreparedStatement ps2 = con.prepareStatement(selectUserID);
					ps2.setInt(1, result.getInt("user2"));
					ResultSet user2 = ps2.executeQuery();
					
					String username2 = "";
					if (user2.next()) {
						username2 = user2.getString("username");
					}
					
					Game g = new Game(result.getInt("id"), result.getInt("user1"), username1, result.getInt("user2"), username2);
					games.add(g);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return games;
		}
		// guest
		else
		{
			return new Vector<Game>();
		}
	}
	
}
