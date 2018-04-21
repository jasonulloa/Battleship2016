package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import utility.Game;

public class OverviewGUI extends JPanel 
{
	private static final long serialVersionUID = 1L;
	private BattleshipPanel mainPanel;
	private JPanel titlePanel, bottomPanel;
	private OverviewPanel leaderPanel, activePanel, lobbyPanel;
	private JLabel titleLabel, iconLabel, playerLabel, leaderLabel, activeLabel, lobbyLabel;
	private JList<String> leaderList, activeList, lobbyList;
	private Vector<Game> gameActiveList, gameLobbyList;
	private BattleshipJButton continueGameButton, joinLobbyButton, createLobbyButton, refreshButton;
	private Image image;
	private BattleshipClient bc;
	
	public OverviewGUI(){}

	public void instantiate(BattleshipClient bc){
		this.bc = bc;
		instantiateComponents();
		createGUI();
		addActions();
	}
	
	private void instantiateComponents(){
		gameActiveList = new Vector<Game>();
		gameLobbyList = new Vector<Game>();
		
		activeList = new JList<String>();
		activeList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		activeList.setCellRenderer(new TransparentListCellRenderer());
		activeList.setOpaque(false);
		activeList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (activeList.isSelectionEmpty()) {
					continueGameButton.setEnabled(false);
				} else {
					continueGameButton.setEnabled(true);
				}
			}
			
		});
		lobbyList = new JList<String>();
		lobbyList.setCellRenderer(new TransparentListCellRenderer());
		lobbyList.setOpaque(false);
		lobbyList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		lobbyList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (lobbyList.isSelectionEmpty()) {
					joinLobbyButton.setEnabled(false);
				} else {
					joinLobbyButton.setEnabled(true);
				}	
			}
			
		});
		

		try {
			image = ImageIO.read(new File("resources/border.png"));
		} catch (IOException e) {
			System.out.println("IOException:" + e);
		}
		
		mainPanel = new BattleshipPanel();
		mainPanel.setLayout(new BorderLayout());
		titlePanel = new JPanel();
		titlePanel.setOpaque(false);
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
		titleLabel = new JLabel("Battleship 2016");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("8bitoperator", Font.PLAIN, 56));
		iconLabel = new JLabel();
		Image newimg = bc.icon.getScaledInstance(100, 100,  java.awt.Image.SCALE_SMOOTH);
		ImageIcon icon = new ImageIcon(newimg);
		iconLabel.setIcon(icon);
		playerLabel = new JLabel("Guest");
		playerLabel.setForeground(Color.WHITE);
		playerLabel.setFont(new Font("8bitoperator", Font.PLAIN, 56));
		
		bottomPanel = new JPanel();
		bottomPanel.setOpaque(false);
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		leaderPanel = new OverviewPanel(image);
		leaderPanel.setOpaque(false);
		leaderPanel.setLayout(new BoxLayout(leaderPanel, BoxLayout.Y_AXIS));
		leaderLabel = new JLabel("Leaderboard");
		leaderLabel.setAlignmentX(CENTER_ALIGNMENT);
		leaderLabel.setForeground(Color.WHITE);
		leaderList = new JList<String>();
		leaderList.setCellRenderer(new TransparentListCellRenderer());
		leaderList.setOpaque(false);
		refreshButton = new BattleshipJButton("Refresh");
		refreshButton.setAlignmentX(CENTER_ALIGNMENT);
		refreshButton.setFont(new Font("8bitoperator", Font.PLAIN, 24));	
		activePanel = new OverviewPanel(image);
		activePanel.setOpaque(false);
		activePanel.setLayout(new BoxLayout(activePanel, BoxLayout.Y_AXIS));
		activeLabel = new JLabel("Active Games");
		activeLabel.setAlignmentX(CENTER_ALIGNMENT);
		activeLabel.setForeground(Color.WHITE);
		
		continueGameButton = new BattleshipJButton("Continue Game");
		continueGameButton.setAlignmentX(CENTER_ALIGNMENT);
		continueGameButton.setFont(new Font("8bitoperator", Font.PLAIN, 24));
		lobbyPanel = new OverviewPanel(image);
		lobbyPanel.setOpaque(false);
		lobbyPanel.setLayout(new BoxLayout(lobbyPanel, BoxLayout.Y_AXIS));
		lobbyLabel = new JLabel("Lobby List");
		lobbyLabel.setAlignmentX(CENTER_ALIGNMENT);
		lobbyLabel.setForeground(Color.WHITE);

		joinLobbyButton = new BattleshipJButton("Join Lobby");
		joinLobbyButton.setPreferredSize(new Dimension(30, 40));
		joinLobbyButton.setAlignmentX(CENTER_ALIGNMENT);
		joinLobbyButton.setFont(new Font("8bitoperator", Font.PLAIN, 24));
		createLobbyButton = new BattleshipJButton("Create New Lobby");
		createLobbyButton.setPreferredSize(new Dimension(30, 40));
		createLobbyButton.setAlignmentX(CENTER_ALIGNMENT);
		createLobbyButton.setFont(new Font("8bitoperator", Font.PLAIN, 24));
		
		refresh();
	}
	
	private void createGUI(){
		setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.CENTER);
		titlePanel.add(Box.createHorizontalGlue());
		titlePanel.add(iconLabel);
		titlePanel.add(titleLabel);
		titlePanel.add(Box.createHorizontalGlue());
		titlePanel.add(playerLabel);
		titlePanel.add(Box.createHorizontalGlue());
		mainPanel.add(titlePanel, BorderLayout.NORTH);
		leaderPanel.add(leaderLabel);
		leaderPanel.add(leaderList);
		leaderPanel.add(Box.createGlue());
		leaderPanel.add(refreshButton);
		JLabel blank1 = new JLabel();
		blank1.setPreferredSize(new Dimension(20,35));
		leaderPanel.add(blank1);
		bottomPanel.add(leaderPanel);
		activePanel.add(activeLabel);
		playerLabel.setText(bc.username);
		JLabel blank2 = new JLabel();
		activePanel.add(blank2);
		activePanel.add(activeList);
		activePanel.add(Box.createGlue());
		activePanel.add(continueGameButton);
		JLabel blank3 = new JLabel();
		blank3.setPreferredSize(new Dimension(20,35));
		activePanel.add(blank3);
		bottomPanel.add(activePanel);
		lobbyPanel.add(lobbyLabel);
		JLabel blank4 = new JLabel();
		lobbyPanel.add(blank4);
		lobbyPanel.add(lobbyList);
		lobbyPanel.add(Box.createGlue());
		lobbyPanel.add(joinLobbyButton);
		JLabel blank5 = new JLabel();
		blank5.setPreferredSize(new Dimension(20,8));
		lobbyPanel.add(blank5);
		lobbyPanel.add(createLobbyButton);
		JLabel blank6 = new JLabel();
		blank6.setPreferredSize(new Dimension(20,13));
		lobbyPanel.add(blank6);
		bottomPanel.add(lobbyPanel);
		mainPanel.add(bottomPanel, BorderLayout.CENTER);
	}
	
	public void getActiveGamesResponse(Vector<Game> activeGames) {
		gameActiveList.removeAllElements();
		
		DefaultListModel<String> activeGamesModel = new DefaultListModel<String>();
	    for (Game bs : activeGames){
	    	gameActiveList.addElement(bs);
		    activeGamesModel.addElement(bs.getUser1Name() + " vs. " + bs.getUser2Name());
	    }
	    activeList.setModel(activeGamesModel);
	    activeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	public void getLobbiesResponse(Vector<Game> lobbyVector) {
		gameLobbyList.removeAllElements();
				
		DefaultListModel<String> lobbyModel = new DefaultListModel<String>();
	    for (Game bs : lobbyVector){
	    	gameLobbyList.addElement(bs);
		    lobbyModel.addElement(bs.getUser1Name() + "'s lobby");
	    }
	    
	    lobbyList.setModel(lobbyModel);
	    lobbyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	public void getLeaderListResponse(Vector<String> LeaderList){
		this.leaderList.removeAll();
		
		DefaultListModel<String> leaderListModel = new DefaultListModel<String>();
		
		for (String s : LeaderList){
			leaderListModel.addElement(s);
		}
		this.leaderList.setModel(leaderListModel);
		this.leaderList.setFocusable(false);
	}
	
	public void refresh(){
		//refresh active games
		if (bc.username.isEmpty()) {
			createLobbyButton.setEnabled(false);
		}
		if (bc != null && bc.sCommunicator != null) {
			bc.sCommunicator.requestActiveGames();
			
			//refresh lobbies
			bc.sCommunicator.requestLobbies();
			
			//refresh leaderboard
			bc.sCommunicator.requestLeaderList();
			
			lobbyList.clearSelection();
			activeList.clearSelection();
			
			joinLobbyButton.setEnabled(false);
			continueGameButton.setEnabled(false);
		}
	}
	
	private void addActions(){
		continueGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String soundName = "resources/GenericSelectSound.wav";    
			    AudioInputStream audioInputStream;
				try {
					audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
					Clip clip = AudioSystem.getClip();
					clip.open(audioInputStream);
					clip.start();
				} catch (UnsupportedAudioFileException | IOException e1) {
					e1.printStackTrace();
				} catch (LineUnavailableException e1) {
					e1.printStackTrace();
				}
				
				if (gameActiveList.size()==0)
				{
					JOptionPane.showMessageDialog(null,
						    "There are no active games to continue",
						    "Continue Game Failure",
						    JOptionPane.ERROR_MESSAGE);
				}
				int selected = activeList.getSelectedIndex();
				Game selectedGame = gameActiveList.elementAt(selected);
				int gameID = selectedGame.getGameID();
				
				bc.sCommunicator.continueGame(gameID);
				//TODO open up game window of the chosen game 
			}
	    });
		
		joinLobbyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String soundName = "resources/GenericSelectSound.wav";    
			    AudioInputStream audioInputStream;
				try {
					audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
					Clip clip = AudioSystem.getClip();
					clip.open(audioInputStream);
					clip.start();
				} catch (UnsupportedAudioFileException | IOException e1) {
					e1.printStackTrace();
				} catch (LineUnavailableException e1) {
					e1.printStackTrace();
				}
				
				if (lobbyList.getModel().getSize()==0)
				{
					JOptionPane.showMessageDialog(null,
						    "There are no lobbies to join",
						    "Join Lobby Failure",
						    JOptionPane.ERROR_MESSAGE);
				}
				int selected = lobbyList.getSelectedIndex();
				Game selectedLobby = gameLobbyList.elementAt(selected);
				int gameID = selectedLobby.getGameID();
				
				bc.swapGUI("lobby");
				bc.sCommunicator.joinLobby(gameID);
				bc.lobbygui.makeSettingsReadOnly();
				bc.lobbygui.setGame(selectedLobby);
			}
	    });
		
		createLobbyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String soundName = "resources/GenericSelectSound.wav";    
			    AudioInputStream audioInputStream;
				try {
					audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
					Clip clip = AudioSystem.getClip();
					clip.open(audioInputStream);
					clip.start();
				} catch (UnsupportedAudioFileException | IOException e1) {
					e1.printStackTrace();
				} catch (LineUnavailableException e1) {
					e1.printStackTrace();
				}
				
				bc.lobbygui.makeSettingsEditable();
				
				bc.swapGUI("lobby");
				
				bc.sCommunicator.createLobby();
			}
	    });
		
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String soundName = "resources/GenericSelectSound.wav";    
			    AudioInputStream audioInputStream;
				try {
					audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
					Clip clip = AudioSystem.getClip();
					clip.open(audioInputStream);
					clip.start();
				} catch (UnsupportedAudioFileException | IOException e1) {
					e1.printStackTrace();
				} catch (LineUnavailableException e1) {
					e1.printStackTrace();
				}
				
				refresh();
			}
	    });
	}
}