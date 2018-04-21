package client;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class BattleshipClient extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	public Image icon;
	public LoginGUI logingui;
	public OverviewGUI overgui;
	public LobbyGUI lobbygui;
	public GameWindowGUI gwingui;
	public ServerCommunicator sCommunicator;

	public String username;
	
	public BattleshipClient() {
		super("Battleship 2016");
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
		ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/8bitoperator.ttf")));
		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		} catch(FontFormatException e) {
			System.out.println("FontFormatException: " + e.getMessage());
		}
		
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			UIManager.put("Button.font", new Font("8bitoperator", 0, 16));
			UIManager.put("List.font", new Font("8bitoperator", 0, 14));
			UIManager.put("Label.font", new Font("8bitoperator", 0, 28));
			UIManager.put("Panel.font", new Font("8bitoperator", 0, 16));
		} catch (Exception e) {
			System.out.println("Warning! Cross-platform L&F not used!");
		}
		icon = new ImageIcon("resources/crosshairs.png").getImage();
		setIconImage(icon);
		
		instantiateComponents();
		createGUI();
		addActions();
		setVisible(true);
		
		sCommunicator = new ServerCommunicator(this);
		sCommunicator.Connect();
		sCommunicator.start();
	}
	
	private void instantiateComponents() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new CardLayout());
		logingui = new LoginGUI(this);
		overgui = new OverviewGUI();
		lobbygui = new LobbyGUI(this);
		instantiateLobby();
		gwingui = new GameWindowGUI();
		instantiateGameWindow();
	}
	
	private void createGUI() {
		setSize(1200, 700);
		setLocation(200, 200);
		setResizable(false);
		add(mainPanel);
		mainPanel.add(logingui, "login");
		mainPanel.add(overgui, "overview");
		mainPanel.add(lobbygui, "lobby");
		mainPanel.add(gwingui, "game");
	}
	
	private void addActions() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void swapGUI(String action){
		if (action.equals("overview")){
			overgui.refresh();
		}
		
		CardLayout cl = (CardLayout)mainPanel.getLayout();
		cl.show(mainPanel, action);
		mainPanel.validate();
		mainPanel.repaint();
	}
	
	public void instantiateOverview(){
		overgui.instantiate(this);
	}
	
	public void instantiateGameWindow(){
		gwingui.instantiate(this);
	}
	
	public void instantiateLobby(){
		lobbygui.instantiate();
	}
	
	public static void main(String[] args) {		
		new BattleshipClient();
	}
}