package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import utility.BoardState;
import utility.Constants;
import utility.TileChange;
import utility.TileState;

public class GameWindowGUI extends JPanel 
{
	private static final long serialVersionUID = 1L;
	private BattleshipPanel mainPanel;
	private JPanel rightPanel, enemyFleetGridPanel, leftPanel, leftTopPanel, myFleetGridPanel, 
		leftBottomPanel, satPanel, paraPanel, reconPanel, returnPanel, myFleetListPanel;
	private JLabel enemyFleetLabel, myFleetLabel, intelToolsLabel, satLabel, paraLabel, reconLabel, 
		fleetListLabel;
	private BattleshipJButton returnButton;
	private BattleshipTile[][] myButtons;
	private BattleshipTile[][] enemyButtons;
	private JList<String> fleetRoster;
	private DefaultListModel<String> fleetModel;
	private ArrayList<Integer> fleetCount;
	private ArrayList<String> shipNames;
	private ArrayList<Integer> shipCosts;
	private BattleshipClient bc;
	public BoardState boardState;
	private int grid;
	private boolean shipDirection, valid;
	private JLabel yourTurnLabel; 
	private JLabel opponentTurnLabel; 
	
	public GameWindowGUI(){}
	
	public void instantiate(BattleshipClient bc){
		this.bc = bc;
		instantiateComponents();
		createGUI();
		addActions();
		displayWhoseTurn();
	}

	private void instantiateComponents(){
		mainPanel = new BattleshipPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		leftPanel = new JPanel();
		leftPanel.setOpaque(false);
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftTopPanel = new JPanel();
		leftTopPanel.setOpaque(false);
		leftTopPanel.setLayout(new BoxLayout(leftTopPanel, BoxLayout.Y_AXIS));
		myFleetLabel = new JLabel("My Fleet");
		myFleetLabel.setAlignmentX(CENTER_ALIGNMENT);
		myFleetLabel.setForeground(Color.WHITE);
		leftBottomPanel = new JPanel();
		leftBottomPanel.setOpaque(false);
		leftBottomPanel.setLayout(new BoxLayout(leftBottomPanel, BoxLayout.Y_AXIS));
		leftBottomPanel.setAlignmentY(BOTTOM_ALIGNMENT);
		intelToolsLabel = new JLabel("Intel Tools");
		intelToolsLabel.setAlignmentX(CENTER_ALIGNMENT);
		intelToolsLabel.setForeground(Color.WHITE);
		satPanel = new JPanel();
		satPanel.setOpaque(false);
		satLabel = new JLabel("Satellite");
		satLabel.setForeground(Color.WHITE);
		paraPanel = new JPanel();
		paraPanel.setOpaque(false);
		paraLabel = new JLabel("Paratrooper");
		paraLabel.setForeground(Color.WHITE);
		reconPanel = new JPanel();
		reconPanel.setOpaque(false);
		reconLabel = new JLabel("Recon Airplane");
		reconLabel.setForeground(Color.WHITE);
		returnPanel = new JPanel();
		returnPanel.setOpaque(false);
		returnButton = new BattleshipJButton("Return to Overview");
		returnButton.setAlignmentX(CENTER_ALIGNMENT);
		returnButton.setFont(new Font("8bitoperator", Font.PLAIN, 36));
		rightPanel = new JPanel();
		rightPanel.setOpaque(false);
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		myFleetListPanel = new JPanel();
		myFleetListPanel.setOpaque(false);
		myFleetListPanel.setLayout(new BoxLayout(myFleetListPanel, BoxLayout.Y_AXIS));
		fleetListLabel = new JLabel("My Fleet Roster");
		fleetListLabel.setAlignmentX(CENTER_ALIGNMENT);
		fleetListLabel.setForeground(Color.WHITE);
	    enemyFleetLabel = new JLabel("Enemy Fleet");
		enemyFleetLabel.setAlignmentX(CENTER_ALIGNMENT);
		enemyFleetLabel.setForeground(Color.WHITE);
		yourTurnLabel = new JLabel ("Your Turn!");
		yourTurnLabel.setAlignmentX(CENTER_ALIGNMENT);
		yourTurnLabel.setForeground(Color.WHITE);
		yourTurnLabel.setFont(new Font("8bitoperator", Font.PLAIN, 30));
		opponentTurnLabel = new JLabel ("Opponent's Turn");
		opponentTurnLabel.setAlignmentX(CENTER_ALIGNMENT);
		opponentTurnLabel.setForeground(Color.WHITE);
		opponentTurnLabel.setFont(new Font("8bitoperator", Font.PLAIN, 30));
		fleetRoster = new JList<String>();
		fleetRoster.setEnabled(false);
		fleetModel = new DefaultListModel<String>();
		fleetRoster.setModel(fleetModel);
		fleetRoster.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		shipNames = new ArrayList<>(Arrays.asList("Supercarrier", "Carrier", "Battleship",
				"Cruiser", "Submarine", "Destroyer", "Patrol Boat"));
		shipCosts = new ArrayList<>(Arrays.asList(Constants.SUPERCARRIER_COST, Constants.CARRIER_COST, 
				Constants.BATTLESHIP_COST, Constants.CRUISER_COST, Constants.SUBMARINE_COST, 
				Constants.DESTROYER_COST, Constants.PATROLBOAT_COST));
	}
	
	private void createGUI(){
		setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.CENTER);
		leftTopPanel.add(myFleetLabel);
		leftTopPanel.add(Box.createRigidArea(new Dimension(0,20)));
		leftPanel.add(leftTopPanel);
		leftPanel.add(Box.createRigidArea(new Dimension(0,60)));
		//leftBottomPanel.add(intelToolsLabel);
		JPanel blank = new JPanel();
		blank.setOpaque(false);
		blank.add(Box.createVerticalGlue());
		leftBottomPanel.add(blank);
		//satPanel.add(satLabel);
		//satPanel.add(satX);
		//satPanel.add(satY);
		//satPanel.add(satDeployButton);
		//leftBottomPanel.add(satPanel);
		//paraPanel.add(paraLabel);
		//paraPanel.add(paraX);
//		paraPanel.add(paraY);
//		paraPanel.add(paraDeployButton);
//		leftBottomPanel.add(paraPanel);
//		reconPanel.add(reconLabel);
//		reconPanel.add(reconX);
//		reconPanel.add(reconY);
//		reconPanel.add(reconDeployButton);
//		leftBottomPanel.add(reconPanel);
		returnPanel.add(returnButton);
		leftBottomPanel.add(returnPanel);
		leftPanel.add(leftBottomPanel);
		leftPanel.add(Box.createRigidArea(new Dimension(0,10)));
		mainPanel.add(leftPanel);
		myFleetListPanel.add(fleetListLabel);
		myFleetListPanel.add(fleetRoster);
		//myFleetListPanel.add(Box.createRigidArea(new Dimension(0,10)));
		rightPanel.add(myFleetListPanel);
		rightPanel.add(Box.createVerticalGlue());
		rightPanel.add(enemyFleetLabel);
		rightPanel.add(Box.createRigidArea(new Dimension(0,10)));
		mainPanel.add(rightPanel);
	}

	private void addActions(){
		
		returnButton.addActionListener(new ActionListener() {
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
				bc.sCommunicator.leaveGame();
				boardState = null;
				bc.swapGUI("overview");
			}
	    });
		
	}
	
	private void cleanUpOldGame() {
		if (myButtons != null && enemyButtons != null) {
			for (int i = 0; i < myButtons.length; i++) {
				for (int j = 0; j < myButtons[i].length; j++) {
					myFleetGridPanel.remove(myButtons[i][j]);
					enemyFleetGridPanel.remove(enemyButtons[i][j]);
				}
			}
		}
		
		if (myFleetGridPanel != null && enemyFleetGridPanel != null) {
			leftTopPanel.remove(myFleetGridPanel);
			rightPanel.remove(enemyFleetGridPanel);
		}
		
		
		fleetModel.clear();
	}
	
	public void loadGame(BoardState bs) 
	{
		cleanUpOldGame();
		
		boardState = bs;
		
		myFleetGridPanel = createMyGrid();
		leftTopPanel.add(myFleetGridPanel);
		
		enemyFleetGridPanel = createEnemyGrid();
		rightPanel.add(enemyFleetGridPanel);
		
		ArrayList<String> fleetList = createFleetList(bs.getShipCount());
		for (String shipCount : fleetList){
			fleetModel.addElement(shipCount);
		}
		fleetRoster.setModel(fleetModel);
		fleetCount = bs.getShipCount();
				
		for (int i=0; i < boardState.getMapSize(); i++) 
		{
			for (int j=0; j < boardState.getMapSize(); j++) 
			{
				if(bc.sCommunicator.getUserID() == boardState.getUser1ID())
				{
					myButtons[i][j].setHit(boardState.user1board[i][j].beenShot);
					myButtons[i][j].setShip(boardState.user1board[i][j].isShip);
					enemyButtons[i][j].setHit(boardState.user2board[i][j].beenShot);
					enemyButtons[i][j].setShip(boardState.user2board[i][j].isShip && boardState.user2board[i][j].beenShot);
				}
				else
				{
					myButtons[i][j].setHit(boardState.user2board[i][j].beenShot);
					myButtons[i][j].setShip(boardState.user2board[i][j].isShip);
					enemyButtons[i][j].setHit(boardState.user1board[i][j].beenShot);
					enemyButtons[i][j].setShip(boardState.user1board[i][j].isShip && boardState.user1board[i][j].beenShot);
				}
			}
		}
		
		updateBoardInteractivity();
	}
	
	public void freshGame(ArrayList<Integer> shipCounts, BoardState bs) {
		cleanUpOldGame();
		
		boardState = bs;
		
		myFleetGridPanel = createMyGrid();
		leftTopPanel.add(myFleetGridPanel);

		enemyFleetGridPanel = createEnemyGrid();
		rightPanel.add(enemyFleetGridPanel);

		valid = false;
		ArrayList<String> fleetList = createFleetList(shipCounts);
		for (String shipCount : fleetList){
			fleetModel.addElement(shipCount);
		}
		fleetRoster.setModel(fleetModel);
		fleetCount = shipCounts;
		
		System.out.println("Fresh game, status " + boardState.getStatus());
		
		updateBoardInteractivity();
		
		returnButton.setEnabled(false);
		
		for (int i = 0; i < fleetCount.size(); i++) {
			if (fleetCount.get(i) > 0) {
				fleetRoster.setSelectedIndex(i);
				break;
			}
		}
	}
	
	public void setBoardState(BoardState bs) {
		if (boardState.getStatus() == 2 && bs.getStatus() == 3) {
			returnButton.setEnabled(true);
		}
		boardState = bs;
		System.out.println("update boardstate, status " + bs.getStatus());
		updateBoardInteractivity();
	}
	
	private ArrayList<String> createFleetList(ArrayList<Integer> fleetCount){		
		ArrayList<String> ships = new ArrayList<String>();
		if(fleetCount != null)
		{
			for (int i = 0; i < fleetCount.size(); i++){
				String ship = shipNames.get(i);
				int num = fleetCount.get(i);
				ships.add(ship + ": " + Integer.toString(num));
		    }
		}
		return ships;
	}
	
	public JPanel createMyGrid(){
		grid = boardState.getMapSize();
		myButtons = new BattleshipTile[grid][grid];
		JPanel jp = new JPanel();
		jp.setOpaque(false);
		jp.setLayout(new GridLayout(0, grid, 0, 0));
		jp.setMaximumSize(new Dimension(480, 480));
		int counter = 0;
		for (int i=1; i <= grid*grid; i++) {
			String letter = Character.toString((char) ((int)'A' + counter));
			int intNumber = i%grid;
			if (intNumber == 0){
				intNumber = grid;
			}
			String number = Integer.toString(intNumber);
			String buttonLabel = letter + number;
			
			TileState[][] myBoard;
			if (bc.sCommunicator.getUserID() == boardState.getUser1ID()) {
				myBoard = boardState.user1board;
			} else {
				myBoard = boardState.user2board;
			}
			
			myButtons[intNumber-1][counter] = new BattleshipTile(buttonLabel, myBoard[intNumber-1][counter].isShip, myBoard[intNumber-1][counter].beenShot, true, intNumber-1, counter);
			myButtons[intNumber-1][counter].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (boardState.getStatus() == Constants.PLACING_SHIPS){
						if (e.isControlDown()) {
							  shipDirection = true;
						  } else {
							  shipDirection = false;
						  }
						valid = addShip(((BattleshipTile)e.getSource()).getXCoord(),((BattleshipTile)e.getSource()).getYCoord(),getBoatSize(),shipDirection);
						if (valid) {
							updateBoats();
						}
					}
				}
			});
			
			myButtons[intNumber-1][counter].setMargin(new Insets(5, 5, 5, 5));
			if(grid == 8){
				myButtons[intNumber-1][counter].setPreferredSize(new Dimension(40, 40));
			} else if(grid == 10){
				myButtons[intNumber-1][counter].setPreferredSize(new Dimension(30, 30));
			} else if(grid == 12){
				myButtons[intNumber-1][counter].setPreferredSize(new Dimension(25, 25));
			}
			jp.add(myButtons[intNumber-1][counter]);
			if (i%grid == 0) {counter++;}
		}
		return jp;
	}
	
	public JPanel createEnemyGrid(){
		grid = boardState.getMapSize();
		enemyButtons = new BattleshipTile[grid][grid];
		JPanel jp = new JPanel();
		jp.setOpaque(false);
		jp.setLayout(new GridLayout(0, grid, 0, 0));
		jp.setMaximumSize(new Dimension(600, 600));
		int counter = 0;
		for (int i=1; i <= grid*grid; i++) {
			String letter = Character.toString((char) ((int)'A' + counter));
			int intNumber = i%grid;
			if (intNumber == 0){
				intNumber = grid;
			}
			String number = Integer.toString(intNumber);
			String buttonLabel = letter + number;
			
			TileState[][] enemyBoard;
			if (bc.sCommunicator.getUserID() == boardState.getUser2ID()) {
				enemyBoard = boardState.user1board;
			} else {
				enemyBoard = boardState.user2board;
			}
			
			enemyButtons[intNumber-1][counter] = new BattleshipTile(buttonLabel, enemyBoard[intNumber-1][counter].isShip, enemyBoard[intNumber-1][counter].beenShot, false, intNumber-1, counter);	
			enemyButtons[intNumber-1][counter].addActionListener(new ActionListener()
			{
			  public void actionPerformed(ActionEvent e)
			  {
			   hitSpace(((BattleshipTile)e.getSource()).getXCoord(),((BattleshipTile)e.getSource()).getYCoord());
			   String soundName = "resources/BattleshipExplosionSound.wav";    
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
			  }
			});
			
			enemyButtons[intNumber-1][counter].setMargin(new Insets(5, 5, 5, 5) );
			if(grid == 8){
				enemyButtons[intNumber-1][counter].setPreferredSize(new Dimension(60, 60));
			} else if(grid == 10){
				enemyButtons[intNumber-1][counter].setPreferredSize(new Dimension(50, 50));
			} else if(grid == 12){
				enemyButtons[intNumber-1][counter].setPreferredSize(new Dimension(45, 45));
			}
			jp.add(enemyButtons[intNumber-1][counter]);
			if (i%grid == 0) {counter++;}
		}
		return jp;
	}
	
	private void updateBoardInteractivity() 
	{
		System.out.println("USER "+bc.sCommunicator.getUserID()+" STATUS "+boardState.getStatus()+" USER1 "+boardState.getUser1ID()+" USER2 "+boardState.getUser2ID());

		for (int i=0; i < boardState.getMapSize(); i++) {
			for (int j=0; j < boardState.getMapSize(); j++) {
				//System.out.println(enemyButtons[i][j].getShip() + " " + enemyButtons[i][j].getHit());
				if (boardState.getStatus() == Constants.PLACING_SHIPS) {
					myButtons[i][j].setEnabled(true);
				} else {
					myButtons[i][j].setEnabled(false);
				}
				if (isMyTurn())
				{
					enemyButtons[i][j].setEnabled(!enemyButtons[i][j].isHit());
				} else {
					enemyButtons[i][j].setEnabled(false);
				}
			}
		}		
	}
	
	public void gameOver(int winner) {
		if (boardState != null && boardState.getStatus() > 2) {
			if (bc.sCommunicator.getUserID() == winner) {
				JOptionPane.showMessageDialog(null,
					    "Congratulations, you won!",
					    "Game Over",
					    JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null,
					    "Sorry, you lost.",
					    "Game Over",
					    JOptionPane.INFORMATION_MESSAGE);
			}	
		}
		bc.swapGUI("overview");
	}
	
	public boolean isMyTurn()
	{
		return ((boardState.getStatus() == Constants.USER1_TURN && boardState.getUser1ID() == bc.sCommunicator.getUserID()) 
			 || (boardState.getStatus() == Constants.USER2_TURN && boardState.getUser2ID() == bc.sCommunicator.getUserID()));
	}
	
	public void OtherTakeTurn(TileChange tc)
	{
		myButtons[tc.getX()][tc.getY()].setHit(true);
		boardState.ApplyChange(tc);
		boardState.ToggleTurn();
		updateBoardInteractivity();
	}
	
	public void HitResponse(TileChange tileChange) 
	{
		enemyButtons[tileChange.getX()][tileChange.getY()].setHit(true);
		if(tileChange.isHit())
		{
			// update button on enemy field as hit
			enemyButtons[tileChange.getX()][tileChange.getY()].setShip(true);
		}
		else
		{
			// update button on enemy field as miss
			enemyButtons[tileChange.getX()][tileChange.getY()].setShip(false);
		}
		boardState.ApplyChange(tileChange);
		boardState.ToggleTurn();
		updateBoardInteractivity();

	}
	
	private void displayWhoseTurn ()
	{
		// if (your turn) 
		// leftTopPanel.add(yourTurnLabel);
		// else
		//leftTopPanel.add(opponentTurnLabel);
	}
	
	private void hitSpace(int x, int y) {
		//enemyButtons[x][y].setHit(true);
		bc.sCommunicator.gameMove(new TileChange(x,y,boardState.getGameID(),bc.sCommunicator.getUserID(), false));
		//return enemyButtons[x][y].getShip();
	}
	
	private int getBoatSize() {
		  for (int i=0; i<fleetCount.size(); i++) {
			  if (fleetCount.get(i)>0) {
				  return shipCosts.get(i);
			  }
		  }
		  return -1;
	}
	
	private boolean isDonePlacingShips() {
		 for (int i=0; i<fleetCount.size(); i++) {
			  if (fleetCount.get(i)>0) {
				  return false;
			  }
		  }
		 return true;
	}
	
	private void finishPlacingShips() {
		fleetRoster.clearSelection();
		 for (int i=0; i < boardState.getMapSize(); i++) {
			for (int j=0; j < boardState.getMapSize(); j++) {
				myButtons[i][j].setEnabled(false);
				if (bc.sCommunicator.getUserID() == boardState.getUser1ID()) {
					boardState.user1board[i][j].isShip = myButtons[i][j].isShip();
				} else {
					boardState.user2board[i][j].isShip = myButtons[i][j].isShip();
				}
			}
		 }
		 
		 bc.sCommunicator.sendShipPlacements(boardState);
		 
	}
	
	private void updateBoats() {
		  for (int i=0; i<fleetCount.size(); i++) {
			  if (fleetCount.get(i)>0) {
				  fleetCount.set(i, fleetCount.get(i)-1);	
				  if (fleetCount.get(i) == 0 && i < fleetCount.size() - 1) {
					  for (int j = i+1; j < fleetCount.size(); j++) {
						  if (fleetCount.get(j) > 0) {
							  fleetRoster.setSelectedIndex(j);
							  break;
						  }
					  }
				  }
				  
				  if (isDonePlacingShips() == true) {
					 finishPlacingShips();					 
				  }
				  return;
			  }
		  }
	}
	
	private boolean addShip(int x, int y, int size, boolean dir) {

		if (dir == false) {
			if (x+size>grid) {
				return false;
			}
			else {
				for (int i=x; i<x+size; i++) {
					if (myButtons[i][y].isShip())
						return false;
				}
				for (int i=x; i<x+size; i++) {
					myButtons[i][y].setShip(true);
				}
				return true;
			}	
		}
		
		if (dir == true) {
			if (y+size>grid) {
				return false;
			}
			else {
				for (int i=y; i<y+size; i++) {
					if (myButtons[x][i].isShip())
						return false;
				}
				for (int i=y; i<y+size; i++) {
					myButtons[x][i].setShip(true);
				}
				return true;
			}
		}
		return false;
	}
}