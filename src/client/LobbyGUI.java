package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import messages.ChatMessage;
import messages.LobbySettingsChange;
import utility.Constants;
import utility.Game;

public class LobbyGUI extends JPanel 
{
	private static final long serialVersionUID = 1L;
	
	//private <Vector> JLabel shipLabels; // all the types of ships
	//private <Vector> JTextField shipCounts; //keep track of the # of ships
	private ButtonGroup mapSizesGroup;
	private JRadioButton smallMapRButton, mediumMapRButton, largeMapRButton;
	private BattleshipPanel mainPanel;
	private JLabel lobbyLabel, playersLabel, player1, player2, gameSettingsLabel, mapSizeLabel, map8Label, 
		map10Label, map12Label, supercarrierLabel, carrierLabel, battleshipLabel, cruiserLabel, submarineLabel, 
		destroyerLabel, patrolboatLabel, pointsLabel;
	private JPanel leftPanel, playersPanel, gameSettingsPanel, sizePanel, groupPanel, chatPanel;
	private OverviewPanel shipsPanel;
	private BattleshipJButton startGameButton, cancelLobbyButton, addButton1, addButton2, addButton3, addButton4, addButton5, 
		addButton6, addButton7,  subButton1, subButton2, subButton3, subButton4, subButton5, subButton6, subButton7, sendButton;
	private JTextField supercarrierCount, carrierCount, battleshipCount, cruiserCount, submarineCount, 
		destroyerCount, patrolboatCount, replyBox;
	private JTextArea messageBox;
	private int shipPoints;
	public int mapSize;
	private Image image;
	
	private BattleshipClient bc;
	private Game game;
	
	public LobbyGUI(BattleshipClient client){ bc = client; }
	
	public void instantiate(){
		instantiateComponents();
		createGUI();
		addActions();		
	}
	
	public void makeSettingsReadOnly(){
		smallMapRButton.setEnabled(false);
		mediumMapRButton.setEnabled(false);
		largeMapRButton.setEnabled(false);
		addButton1.setEnabled(false);
		addButton2.setEnabled(false);
		addButton3.setEnabled(false);
		addButton4.setEnabled(false);
		addButton5.setEnabled(false);
		addButton6.setEnabled(false);
		addButton7.setEnabled(false);
		subButton1.setEnabled(false);
		subButton2.setEnabled(false);
		subButton3.setEnabled(false);
		subButton4.setEnabled(false);
		subButton5.setEnabled(false);
		subButton6.setEnabled(false);
		subButton7.setEnabled(false);
	}
	
	public void makeSettingsEditable(){
		smallMapRButton.setEnabled(true);
		mediumMapRButton.setEnabled(true);
		largeMapRButton.setEnabled(true);
		addButton1.setEnabled(true);
		addButton2.setEnabled(true);
		addButton3.setEnabled(true);
		addButton4.setEnabled(true);
		addButton5.setEnabled(true);
		addButton6.setEnabled(true);
		addButton7.setEnabled(true);
		subButton1.setEnabled(true);
		subButton2.setEnabled(true);
		subButton3.setEnabled(true);
		subButton4.setEnabled(true);
		subButton5.setEnabled(true);
		subButton6.setEnabled(true);
		subButton7.setEnabled(true);
	}
	
	public void changeLobbySettings(LobbySettingsChange lsc){
		System.out.println("SUCCESS");
		if (lsc.mapSize == Constants.SMALL_MAPSIZE){
			mediumMapRButton.setSelected(false);
			largeMapRButton.setSelected(false);
			smallMapRButton.setSelected(true);
			shipPoints = Constants.SMALL_SHIPPOINTS;
		}
		else if (lsc.mapSize == Constants.MEDIUM_MAPSIZE){
			smallMapRButton.setSelected(false);
			largeMapRButton.setSelected(false);		
			mediumMapRButton.setSelected(true);
			shipPoints = Constants.MEDIUM_SHIPPOINTS;
		}
		else {
			smallMapRButton.setSelected(false);
			mediumMapRButton.setSelected(false);
			largeMapRButton.setSelected(true);	
			shipPoints = Constants.LARGE_SHIPPOINTS;
		}
		
		supercarrierCount.setText(lsc.shipCounts.get(0).toString());
		carrierCount.setText(lsc.shipCounts.get(1).toString());
		battleshipCount.setText(lsc.shipCounts.get(2).toString());
		cruiserCount.setText(lsc.shipCounts.get(3).toString());
		submarineCount.setText(lsc.shipCounts.get(4).toString());
		destroyerCount.setText(lsc.shipCounts.get(5).toString());
		patrolboatCount.setText(lsc.shipCounts.get(6).toString());
		
		int sp1 = Integer.parseInt(supercarrierCount.getText())*Constants.SUPERCARRIER_COST;
		int sp2 = Integer.parseInt(carrierCount.getText())*Constants.CARRIER_COST;
		int sp3 = Integer.parseInt(battleshipCount.getText())*Constants.BATTLESHIP_COST;
		int sp4 = Integer.parseInt(cruiserCount.getText())*Constants.CRUISER_COST;
		int sp5 = Integer.parseInt(submarineCount.getText())*Constants.SUBMARINE_COST;
		int sp6 = Integer.parseInt(destroyerCount.getText())*Constants.DESTROYER_COST;
		int sp7 = Integer.parseInt(patrolboatCount.getText())*Constants.PATROLBOAT_COST;
		
		pointsLabel.setText("Total Ship Points: " + (sp1+sp2+sp3+sp4+sp5+sp6+sp7) + "/"+shipPoints);
	}
	
	public void recieveChatMessage(ChatMessage cm) {
		messageBox.setText(cm.chatmessage + '\n'+messageBox.getText());
	}

	private void instantiateComponents() {
		try {
			image = ImageIO.read(new File("resources/border2.png"));
		} catch (IOException e) {
			System.out.println("IOException:" + e);
		}
		mainPanel = new BattleshipPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		leftPanel = new JPanel();
		leftPanel.setOpaque(false);
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		lobbyLabel = new JLabel("Lobby #"); //placeholder
		//lobbyLabel = getLobbyName(lobbyName);
		lobbyLabel.setForeground(Color.WHITE);
		lobbyLabel.setAlignmentX(CENTER_ALIGNMENT);
		playersLabel = new JLabel("Players");
		playersLabel.setForeground(Color.WHITE);
		playersLabel.setAlignmentX(CENTER_ALIGNMENT);
		playersPanel = new JPanel();
		playersPanel.setOpaque(false);
		playersPanel.setLayout(new BoxLayout(playersPanel, BoxLayout.X_AXIS));
		player1 = new JLabel("Player 1");
		player1.setForeground(Color.WHITE);
		player2 = new JLabel ("Player 2");
		player2.setForeground(Color.WHITE);
		chatPanel = new JPanel(new GridBagLayout());
		chatPanel.setOpaque(false);
		messageBox = new JTextArea();
		messageBox.setFocusable(false);
		messageBox.setMargin(new Insets(5,5,5,5));
		messageBox.setPreferredSize(new Dimension(450,350));
		replyBox = new JTextField();
		replyBox.setPreferredSize(new Dimension(400,20));
		sendButton = new BattleshipJButton("send");
		sendButton.setPreferredSize(new Dimension(50,20));
		sendButton.setFont(new Font("8bitoperator", Font.PLAIN, 12));
		sendButton.setBorder(null);
		startGameButton = new BattleshipJButton ("Start Game");
		startGameButton.setAlignmentX(CENTER_ALIGNMENT);
		startGameButton.setFont(new Font("8bitoperator", Font.PLAIN, 36));
		cancelLobbyButton = new BattleshipJButton ("Leave Lobby");
		cancelLobbyButton.setAlignmentX(CENTER_ALIGNMENT);
		cancelLobbyButton.setFont(new Font("8bitoperator", Font.PLAIN, 36));
		gameSettingsPanel = new JPanel();
		gameSettingsPanel.setOpaque(false);
	    gameSettingsPanel.setLayout(new BoxLayout(gameSettingsPanel, BoxLayout.Y_AXIS));
	    gameSettingsLabel = new JLabel ("Game Settings");
		gameSettingsLabel.setForeground(Color.WHITE);
		gameSettingsLabel.setFont(new Font("8bitoperator", Font.PLAIN, 36));
		gameSettingsLabel.setAlignmentX(CENTER_ALIGNMENT);
		groupPanel = new JPanel(new GridLayout(1,0));
		groupPanel.setOpaque(false);
		sizePanel = new JPanel();
		sizePanel.setOpaque(false);
		mapSizeLabel = new JLabel("Map Size:");
		mapSizeLabel.setForeground(Color.WHITE);
		mapSizeLabel.setFont(new Font("8bitoperator", Font.PLAIN, 12));
		mapSizesGroup = new ButtonGroup();
		smallMapRButton = new JRadioButton();
		smallMapRButton.setOpaque(false);
		smallMapRButton.setSelected(true);
		map8Label = new JLabel (Constants.SMALL_MAPSIZE + "x" + Constants.SMALL_MAPSIZE + " (" 
				+ Constants.SMALL_SHIPPOINTS + " ship points)");
		map8Label.setForeground(Color.WHITE);
		map8Label.setFont(new Font("8bitoperator", Font.PLAIN, 12));
		mediumMapRButton = new JRadioButton();
		mediumMapRButton.setOpaque(false);
		map10Label = new JLabel (Constants.MEDIUM_MAPSIZE + "x" + Constants.MEDIUM_MAPSIZE + " (" 
				+ Constants.MEDIUM_SHIPPOINTS + " ship points)");
		map10Label.setForeground(Color.WHITE);
		map10Label.setFont(new Font("8bitoperator", Font.PLAIN, 12));
		largeMapRButton = new JRadioButton();
		largeMapRButton.setOpaque(false);
		map12Label = new JLabel (Constants.LARGE_MAPSIZE + "x" + Constants.LARGE_MAPSIZE + " (" 
				+ Constants.LARGE_SHIPPOINTS + " ship points)");
		map12Label.setForeground(Color.WHITE);
		map12Label.setFont(new Font("8bitoperator", Font.PLAIN, 12));
		shipsPanel = new OverviewPanel(image);
		shipsPanel.setOpaque(false);
		shipsPanel.setLayout(new GridBagLayout());
		supercarrierLabel = new JLabel ("Supercarrier -- Cost: " + Constants.SUPERCARRIER_COST + " ");
		supercarrierLabel.setForeground(Color.WHITE);
		supercarrierLabel.setFont(new Font("8bitoperator", Font.PLAIN, 20));
		supercarrierCount = new JTextField(2);
		carrierLabel = new JLabel ("Carrier -- Cost: " + Constants.CARRIER_COST + " ");
		carrierLabel.setFont(new Font("8bitoperator", Font.PLAIN, 20));
		carrierLabel.setForeground(Color.WHITE);
		carrierCount = new JTextField(2);
		battleshipLabel = new JLabel ("Battleship -- Cost: " + Constants.BATTLESHIP_COST + " ");
		battleshipLabel.setForeground(Color.WHITE);
		battleshipLabel.setFont(new Font("8bitoperator", Font.PLAIN, 20));
		battleshipCount = new JTextField(2);
		cruiserLabel = new JLabel ("Cruiser -- Cost: " + Constants.CRUISER_COST + " ");
		cruiserLabel.setFont(new Font("8bitoperator", Font.PLAIN, 20));
		cruiserLabel.setForeground(Color.WHITE);
		cruiserCount = new JTextField(2);
		submarineLabel = new JLabel ("Submarine -- Cost: " + Constants.SUBMARINE_COST + " ");
		submarineLabel.setFont(new Font("8bitoperator", Font.PLAIN, 20));
		submarineLabel.setForeground(Color.WHITE);
		submarineCount = new JTextField(2);
		destroyerLabel = new JLabel ("Destroyer -- Cost: " + Constants.DESTROYER_COST + " ");
		destroyerLabel.setForeground(Color.WHITE);
		destroyerLabel.setFont(new Font("8bitoperator", Font.PLAIN, 20));
		destroyerCount = new JTextField(2);
		patrolboatLabel = new JLabel ("Patrol Boat -- Cost: " + Constants.PATROLBOAT_COST + " ");
		patrolboatLabel.setFont(new Font("8bitoperator", Font.PLAIN, 20));
		patrolboatLabel.setForeground(Color.WHITE);
		patrolboatCount = new JTextField(2);
		pointsLabel = new JLabel ("Total Ship Points: 0/20");
		pointsLabel.setFont(new Font("8bitoperator", Font.PLAIN, 18));
		pointsLabel.setForeground(Color.WHITE);
		
		supercarrierCount.setText("0");
		supercarrierCount.setEditable(false);
		supercarrierCount.setHorizontalAlignment(JTextField.CENTER);
		carrierCount.setText("0");
		carrierCount.setEditable(false);
		carrierCount.setHorizontalAlignment(JTextField.CENTER);
		battleshipCount.setText("0");
		battleshipCount.setEditable(false);
		battleshipCount.setHorizontalAlignment(JTextField.CENTER);
		cruiserCount.setText("0");
		cruiserCount.setEditable(false);
		cruiserCount.setHorizontalAlignment(JTextField.CENTER);
		submarineCount.setText("0");
		submarineCount.setEditable(false);
		submarineCount.setHorizontalAlignment(JTextField.CENTER);
		destroyerCount.setText("0");
		destroyerCount.setEditable(false);
		destroyerCount.setHorizontalAlignment(JTextField.CENTER);
		patrolboatCount.setText("0");
		patrolboatCount.setEditable(false);
		patrolboatCount.setHorizontalAlignment(JTextField.CENTER);
		
		addButton1 = new BattleshipJButton("+");
		addButton1.setBorder(null);
		addButton1.setPreferredSize(new Dimension(20,20));
		addButton1.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
			   updateCount(supercarrierCount,1);
			  }
			});
		addButton2 = new BattleshipJButton("+");
		addButton2.setBorder(null);
		addButton2.setPreferredSize(new Dimension(20,20));
		addButton2.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
			   updateCount(carrierCount,1);
			  }
			});
		addButton3 = new BattleshipJButton("+");
		addButton3.setBorder(null);
		addButton3.setPreferredSize(new Dimension(20,20));
		addButton3.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
			   updateCount(battleshipCount,1);
			  }
			});
		addButton4 = new BattleshipJButton("+");
		addButton4.setBorder(null);
		addButton4.setPreferredSize(new Dimension(20,20));
		addButton4.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
			   updateCount(cruiserCount,1);
			  }
			});
		addButton5 = new BattleshipJButton("+");
		addButton5.setBorder(null);
		addButton5.setPreferredSize(new Dimension(20,20));
		addButton5.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
			   updateCount(submarineCount,1);
			  }
			});
		addButton6 = new BattleshipJButton("+");
		addButton6.setBorder(null);
		addButton6.setPreferredSize(new Dimension(20,20));
		addButton6.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
			   updateCount(destroyerCount,1);
			  }
			});
		addButton7 = new BattleshipJButton("+");
		addButton7.setBorder(null);
		addButton7.setPreferredSize(new Dimension(20,20));
		addButton7.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
			   updateCount(patrolboatCount,1);
			  }
			});
		
		subButton1 = new BattleshipJButton("-");
		subButton1.setBorder(null);
		subButton1.setPreferredSize(new Dimension(20,20));
		subButton1.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
			   updateCount(supercarrierCount,-1);
			  }
			});
		subButton2 = new BattleshipJButton("-");
		subButton2.setBorder(null);
		subButton2.setPreferredSize(new Dimension(20,20));
		subButton2.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
			   updateCount(carrierCount,-1);
			  }
			});
		subButton3 = new BattleshipJButton("-");
		subButton3.setBorder(null);
		subButton3.setPreferredSize(new Dimension(20,20));
		subButton3.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
			   updateCount(battleshipCount,-1);
			  }
			});
		subButton4 = new BattleshipJButton("-");
		subButton4.setBorder(null);
		subButton4.setPreferredSize(new Dimension(20,20));
		subButton4.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
			   updateCount(cruiserCount,-1);
			  }
			});
		subButton5 = new BattleshipJButton("-");
		subButton5.setBorder(null);
		subButton5.setPreferredSize(new Dimension(20,20));
		subButton5.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
			   updateCount(submarineCount,-1);
			  }
			});
		subButton6 = new BattleshipJButton("-");
		subButton6.setBorder(null);
		subButton6.setPreferredSize(new Dimension(20,20));
		subButton6.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
			   updateCount(destroyerCount,-1);
			  }
			});
		subButton7 = new BattleshipJButton("-");
		subButton7.setBorder(null);
		subButton7.setPreferredSize(new Dimension(20,20));
		subButton7.addActionListener(new ActionListener()
		{
			  public void actionPerformed(ActionEvent e)
			  {
			   updateCount(patrolboatCount,-1);
			  }
			});
		
		shipPoints = Constants.SMALL_SHIPPOINTS;
		mapSize = Constants.SMALL_MAPSIZE;
	}
	
	private void createGUI() {
		setLayout(new GridLayout());
		add(mainPanel);
		leftPanel.add(lobbyLabel);
		leftPanel.add(playersLabel);
		playersPanel.add(Box.createHorizontalGlue());
		playersPanel.add(player1);
		playersPanel.add(Box.createHorizontalGlue());
		playersPanel.add(player2);
		playersPanel.add(Box.createHorizontalGlue());
		leftPanel.add(playersPanel);
		leftPanel.add(Box.createHorizontalStrut(500));
		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc2.gridx=0;
		gbc2.gridy=0;
		chatPanel.add(messageBox, gbc2);
		gbc2.gridy=1;
		chatPanel.add(replyBox, gbc2);
		gbc2.anchor = GridBagConstraints.FIRST_LINE_END;
		chatPanel.add(sendButton, gbc2);

		leftPanel.add(chatPanel);
		JLabel blank3 = new JLabel();
		blank3.setPreferredSize(new Dimension(20,100));
		leftPanel.add(blank3);
		mainPanel.add(leftPanel);
		gameSettingsPanel.add(gameSettingsLabel);
		sizePanel.add(mapSizeLabel);
		sizePanel.add(smallMapRButton);
		sizePanel.add(map8Label);
		mapSizesGroup.add(smallMapRButton);
		sizePanel.add(mediumMapRButton);
		sizePanel.add(map10Label);
		mapSizesGroup.add(mediumMapRButton);
		sizePanel.add(largeMapRButton); 
		sizePanel.add(map12Label);
		mapSizesGroup.add(largeMapRButton);
		groupPanel.add(sizePanel);
		gameSettingsPanel.add(groupPanel);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.FIRST_LINE_END;
		gbc.gridx = 0;
		gbc.gridy = 0;
		shipsPanel.add(supercarrierLabel, gbc);
		gbc.gridx = 1;
		shipsPanel.add(subButton1, gbc);
		gbc.gridx = 2;
		shipsPanel.add(supercarrierCount, gbc);
		gbc.gridx = 3;
		shipsPanel.add(addButton1, gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		shipsPanel.add(carrierLabel, gbc);
		gbc.gridx = 1;
		shipsPanel.add(subButton2, gbc);
		gbc.gridx = 2;
		shipsPanel.add(carrierCount, gbc);
		gbc.gridx = 3;
		shipsPanel.add(addButton2, gbc);
		gbc.gridx = 0;
		gbc.gridy = 2;
		shipsPanel.add(battleshipLabel, gbc);
		gbc.gridx = 1;
		shipsPanel.add(subButton3, gbc);
		gbc.gridx = 2;
		shipsPanel.add(battleshipCount, gbc);
		gbc.gridx = 3;
		shipsPanel.add(addButton3, gbc);
		gbc.gridx = 0;
		gbc.gridy = 3;
		shipsPanel.add(cruiserLabel, gbc);
		gbc.gridx = 1;
		shipsPanel.add(subButton4, gbc);
		gbc.gridx = 2;
		shipsPanel.add(cruiserCount, gbc);
		gbc.gridx = 3;
		shipsPanel.add(addButton4, gbc);
		gbc.gridx = 0;
		gbc.gridy = 4;
		shipsPanel.add(submarineLabel, gbc);
		gbc.gridx = 1;
		shipsPanel.add(subButton5, gbc);
		gbc.gridx = 2;
		shipsPanel.add(submarineCount, gbc);
		gbc.gridx = 3;
		shipsPanel.add(addButton5, gbc);
		gbc.gridx = 0;
		gbc.gridy = 5;
		shipsPanel.add(destroyerLabel, gbc);
		gbc.gridx = 1;
		shipsPanel.add(subButton6, gbc);
		gbc.gridx = 2;
		shipsPanel.add(destroyerCount, gbc);
		gbc.gridx = 3;
		shipsPanel.add(addButton6, gbc);
		gbc.gridx = 0;
		gbc.gridy = 6;
		shipsPanel.add(patrolboatLabel, gbc);
		gbc.gridx = 1;
		shipsPanel.add(subButton7, gbc);
		gbc.gridx = 2;
		shipsPanel.add(patrolboatCount, gbc);
		gbc.gridx = 3;
		shipsPanel.add(addButton7, gbc);
		gbc.gridy = 7;
		gbc.gridx = 0;
		shipsPanel.add(pointsLabel, gbc);
		gameSettingsPanel.add(shipsPanel);
		gameSettingsPanel.add(startGameButton);
		JLabel blank1 = new JLabel();
		blank1.setPreferredSize(new Dimension(20,35));
		gameSettingsPanel.add(blank1);
		gameSettingsPanel.add(cancelLobbyButton);
		JLabel blank2 = new JLabel();
		blank2.setPreferredSize(new Dimension(20,35));
		gameSettingsPanel.add(blank2);
		mainPanel.add(gameSettingsPanel);
		mainPanel.setOpaque(false);
	}
	
	public void setGame(Game game) {
		this.game = game;
		if (game.getUser2Name().isEmpty() || bc.sCommunicator.getUserID() != game.getUser1ID()) {
			startGameButton.setEnabled(false);
		} else {
			startGameButton.setEnabled(true);
		}
		this.player1.setText(game.getUser1Name());
		this.player2.setText(game.getUser2Name());
		this.lobbyLabel.setText("Lobby #" + game.getGameID());
	}
	
	private void updateCount(JTextField count, int amount) {		
		if (Integer.parseInt(count.getText())+amount>=0) {
			count.setText(""+(Integer.parseInt(count.getText())+amount));
			int sp1 = Integer.parseInt(supercarrierCount.getText())*Constants.SUPERCARRIER_COST;
			int sp2 = Integer.parseInt(carrierCount.getText())*Constants.CARRIER_COST;
			int sp3 = Integer.parseInt(battleshipCount.getText())*Constants.BATTLESHIP_COST;
			int sp4 = Integer.parseInt(cruiserCount.getText())*Constants.CRUISER_COST;
			int sp5 = Integer.parseInt(submarineCount.getText())*Constants.SUBMARINE_COST;
			int sp6 = Integer.parseInt(destroyerCount.getText())*Constants.DESTROYER_COST;
			int sp7 = Integer.parseInt(patrolboatCount.getText())*Constants.PATROLBOAT_COST;
			if ((sp1 + sp2 + sp3 + sp4 + sp5 + sp6 + sp7)>shipPoints) {
				count.setText(""+(Integer.parseInt(count.getText())-amount));
			}
			else
			{
				pointsLabel.setText("Total Ship Points: " + (sp1+sp2+sp3+sp4+sp5+sp6+sp7) + "/"+shipPoints);
			}
		}
		bc.sCommunicator.sendLobbySettingChange(mapSize, generateShipCounts());
	}
	
	public ArrayList<Integer> generateShipCounts() {
		int sc1 = Integer.parseInt(supercarrierCount.getText());
		int sc2 = Integer.parseInt(carrierCount.getText());
		int sc3 = Integer.parseInt(battleshipCount.getText());
		int sc4 = Integer.parseInt(cruiserCount.getText());
		int sc5 = Integer.parseInt(submarineCount.getText());
		int sc6 = Integer.parseInt(destroyerCount.getText());
		int sc7 = Integer.parseInt(patrolboatCount.getText());
		
		ArrayList<Integer> shipCounts = new ArrayList<Integer>();
		shipCounts.add(sc1); 
		shipCounts.add(sc2);
		shipCounts.add(sc3); 
		shipCounts.add(sc4); 
		shipCounts.add(sc5); 
		shipCounts.add(sc6); 
		shipCounts.add(sc7); 
		
		return shipCounts;
	}
	
	private void addActions() {
		cancelLobbyButton.addActionListener(new ActionListener() {
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
				
				if (bc.sCommunicator.getUserID() == game.getUser1ID()) {
					bc.sCommunicator.deleteLobby();
				} else {
					bc.sCommunicator.leaveGame();
				}
				messageBox.setText("");
				replyBox.setText("");
				bc.swapGUI("overview");
			}
	    });
	
		startGameButton.addActionListener(new ActionListener() {
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
				
				int sc1 = Integer.parseInt(supercarrierCount.getText());
				int sc2 = Integer.parseInt(carrierCount.getText());
				int sc3 = Integer.parseInt(battleshipCount.getText());
				int sc4 = Integer.parseInt(cruiserCount.getText());
				int sc5 = Integer.parseInt(submarineCount.getText());
				int sc6 = Integer.parseInt(destroyerCount.getText());
				int sc7 = Integer.parseInt(patrolboatCount.getText());
				
				int sp1 = sc1*Constants.SUPERCARRIER_COST;
				int sp2 = sc2*Constants.CARRIER_COST;
				int sp3 = sc3*Constants.BATTLESHIP_COST;
				int sp4 = sc4*Constants.CRUISER_COST;
				int sp5 = sc5*Constants.SUBMARINE_COST;
				int sp6 = sc6*Constants.DESTROYER_COST;
				int sp7 = sc7*Constants.PATROLBOAT_COST;
				
				if ((sp1 + sp2 + sp3 + sp4 + sp5 + sp6 + sp7) <= 0){
					JOptionPane.showMessageDialog(null,
						    "Place at least one ship!",
						    "Start game failed.",
						    JOptionPane.ERROR_MESSAGE);
				}
				
				else if ((sp1 + sp2 + sp3 + sp4 + sp5 + sp6 + sp7) <= shipPoints){
					bc.sCommunicator.startGame(mapSize, generateShipCounts());
					replyBox.setText("");
					messageBox.setText("");
					bc.swapGUI("game");
				}
				else {
					JOptionPane.showMessageDialog(null,
						    "Ship cost too high.",
						    "Start game failed.",
						    JOptionPane.ERROR_MESSAGE);
				}
			}
	    });
		
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bc.sCommunicator.sendChatMessage(bc.username,replyBox.getText());
				messageBox.setText(bc.username+": "+replyBox.getText() + '\n' + messageBox.getText());
				replyBox.setText("");
			}
		});
		
		replyBox.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER){
					bc.sCommunicator.sendChatMessage(bc.username,replyBox.getText());
					messageBox.setText(bc.username+": "+replyBox.getText() + '\n' + messageBox.getText());
					replyBox.setText("");
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				
			}
		});
		
		smallMapRButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				shipPoints = Constants.SMALL_SHIPPOINTS;
				mapSize = Constants.SMALL_MAPSIZE;
				pointsLabel.setText("Total Ship Points: 0/"+shipPoints);
				supercarrierCount.setText("0");
				carrierCount.setText("0");
				battleshipCount.setText("0");
				cruiserCount.setText("0");
				submarineCount.setText("0");
				destroyerCount.setText("0");
				patrolboatCount.setText("0");
				bc.sCommunicator.sendLobbySettingChange(Constants.SMALL_MAPSIZE, generateShipCounts());
			}
	    });
		mediumMapRButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				shipPoints = Constants.MEDIUM_SHIPPOINTS;
				mapSize = Constants.MEDIUM_MAPSIZE;
				pointsLabel.setText("Total Ship Points: 0/"+shipPoints);
				supercarrierCount.setText("0");
				carrierCount.setText("0");
				battleshipCount.setText("0");
				cruiserCount.setText("0");
				submarineCount.setText("0");
				destroyerCount.setText("0");
				patrolboatCount.setText("0");
				bc.sCommunicator.sendLobbySettingChange(Constants.MEDIUM_MAPSIZE, generateShipCounts());
			}
	    });
		largeMapRButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				shipPoints = Constants.LARGE_SHIPPOINTS;
				mapSize = Constants.LARGE_MAPSIZE;
				pointsLabel.setText("Total Ship Points: 0/"+shipPoints);
				supercarrierCount.setText("0");
				carrierCount.setText("0");
				battleshipCount.setText("0");
				cruiserCount.setText("0");
				submarineCount.setText("0");
				destroyerCount.setText("0");
				patrolboatCount.setText("0");
				bc.sCommunicator.sendLobbySettingChange(Constants.LARGE_MAPSIZE, generateShipCounts());
			}
	    });
	}
}