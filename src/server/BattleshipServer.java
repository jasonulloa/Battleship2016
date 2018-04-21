package server;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import utility.Constants;

public class BattleshipServer extends Thread
{	
	public static BattleshipServer _instance;
	public DatabaseManager db;
	private boolean running;
	
	// GUI elements
	public JLabel output;
	
	// Networking
	public ServerSocket ss;
	private int port = Constants.port; //i made this the default to match the servercommunicator port
	
	private Vector<UserListener> connectedUsers;
	
	public Vector<UserListener> getConnectedUsers() 
	{
		return connectedUsers;
	}
	
	public BattleshipServer() 
	{
		_instance = this;		
		JFrame window = new JFrame("Battleship Server");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(640, 480);
		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());
		output = new JLabel("<html></html>");
		JScrollPane scrPane = new JScrollPane(output);
		container.add(scrPane, BorderLayout.CENTER);		
		window.add(container);
		window.setVisible(true);

		db = new DatabaseManager();
		
		start();
	}
	
	public static void addMessage(String str) 
	{
		if(_instance != null)
		{
			_instance.output.setText(_instance.output.getText().substring(0, _instance.output.getText().length()-7)+"<br>"+str+"</html>");
		}
	}
	
	public void run()
	{
		System.out.println("Server starting...");
		running = true;
	
		try 
		{
			// start server
			ss = new ServerSocket(port);
			connectedUsers = new Vector<UserListener>();
			
			addMessage("Server started on port " + port + ".");
			
			// server running
			while (running)
			{
				Socket s = ss.accept();
				
				addMessage("New connection from " + s.getInetAddress());
				
				UserListener ul = new UserListener(s);
				ul.start();
				connectedUsers.add(ul);
			}
		}
		catch (IOException ioe) 
		{
			System.out.println("ioe: " + ioe.getMessage());
		}
		finally
		{
			// stop the server
			if (ss != null) 
			{
				try 
				{
					ss.close();
				} 
				catch (IOException ioe) 
				{
					System.out.println("ioe closing ss: " + ioe.getMessage());
				}
			}	
		}

		running = false;
		System.out.println("Server stopped.");
	}
	
	public static void main(String[] args) 
	{		
		new BattleshipServer();
	}
}
