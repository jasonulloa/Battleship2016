package client;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

import server.Encrypt;

public class LoginGUI extends JPanel 
{
	private static final long serialVersionUID = 1L;
	private BattleshipPanel mainPanel;
	private JPanel topPanel, bottomPanel, loginPanel, signupPanel;
	private JLabel titleLabel, loginUsernameLabel, loginPasswordLabel, signupUsernameLabel, signupPasswordLabel, signupRepeatLabel;
	private JTextField loginUsernameText, signupUsernameText;
	private JPasswordField loginPasswordText, signupPasswordText, signupRepeatText;
	private BattleshipJButton loginButton, guestButton, registerButton, signupButton, cancelButton;
	private BattleshipClient bc;
	
	public LoginGUI(BattleshipClient bc){
		this.bc = bc;
		instantiateComponents();
		createGUI();
		addActions();
	}
	
	private void instantiateComponents(){	
		UIManager.put("OptionPane.messageFont", new Font("8bitoperator", Font.PLAIN, 18));
		mainPanel = new BattleshipPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		topPanel = new JPanel();
		topPanel.setOpaque(false);
		topPanel.setLayout(new GridBagLayout());
		titleLabel = new JLabel("Battleship");
		titleLabel.setFont(new Font("8bitoperator", Font.BOLD, 108));
		titleLabel.setForeground(Color.WHITE);
		bottomPanel = new JPanel();
		bottomPanel.setOpaque(false);
		bottomPanel.setLayout(new CardLayout());
		loginPanel = new JPanel();
		loginPanel.setOpaque(false);
		loginPanel.setLayout(new GridBagLayout());
		loginUsernameLabel = new JLabel("Username: ");
		loginUsernameLabel.setForeground(Color.WHITE);
		loginUsernameText = new JTextField(30);
		loginPasswordLabel = new JLabel("Password: ");
		loginPasswordLabel.setForeground(Color.WHITE);
		loginPasswordText = new JPasswordField(30);
		loginButton = new BattleshipJButton("Login");
		guestButton = new BattleshipJButton("Play as Guest");
		registerButton = new BattleshipJButton("Register New Account");
		registerButton.addActionListener(new ButtonClicked("signup", bottomPanel));
		signupPanel = new JPanel();
		signupPanel.setOpaque(false);
		signupPanel.setLayout(new GridBagLayout());
		signupUsernameLabel = new JLabel("Username: ");
		signupUsernameLabel.setForeground(Color.WHITE);
		signupUsernameText = new JTextField(30);
		signupPasswordLabel = new JLabel("Password: ");
		signupPasswordLabel.setForeground(Color.WHITE);
		signupPasswordText = new JPasswordField(30);
		signupRepeatLabel = new JLabel("Repeat: ");
		signupRepeatLabel.setForeground(Color.WHITE);
		signupRepeatText = new JPasswordField(30);
		signupButton = new BattleshipJButton("Add New Account");
		cancelButton = new BattleshipJButton("Cancel Registration");
		cancelButton.addActionListener(new ButtonClicked("login", bottomPanel));
	}
	
	private void createGUI(){
		setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.CENTER);
		mainPanel.add(topPanel);
		GridBagConstraints gbc0 = new GridBagConstraints();
		gbc0.gridx = 0;
		gbc0.gridy = 0;
		topPanel.add(titleLabel, gbc0);
		mainPanel.add(bottomPanel);
		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridx = 0;
		gbc1.gridy = 0;
		loginPanel.add(loginUsernameLabel, gbc1);
		gbc1.gridx = 1;
		gbc1.gridwidth = 2;
		loginPanel.add(loginUsernameText, gbc1);
		gbc1.gridx = 0;
		gbc1.gridy = 1;
		gbc1.gridwidth = 1;
		loginPanel.add(loginPasswordLabel, gbc1);
		gbc1.gridx = 1;
		gbc1.gridwidth = 2;
		loginPanel.add(loginPasswordText, gbc1);
		gbc1.gridx = 0;
		gbc1.gridy = 2;
		gbc1.gridwidth = 1;
		gbc1.gridx = 1;
		gbc1.fill = GridBagConstraints.HORIZONTAL;
		gbc1.ipadx = 40;
		gbc1.insets = new Insets(5, 5, 5, 5);
		loginPanel.add(loginButton, gbc1);
		gbc1.gridx = 2;
		gbc1.ipadx = 0;
		loginPanel.add(guestButton, gbc1);
		gbc1.gridx = 0;
		gbc1.gridy = 3;
		JLabel blank1 = new JLabel("");
		loginPanel.add(blank1, gbc1);
		gbc1.gridx = 1;
		gbc1.gridwidth = 2;
		loginPanel.add(registerButton, gbc1);
		bottomPanel.add(loginPanel, "login");
		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridx = 0;
		gbc2.gridy = 0;
		signupPanel.add(signupUsernameLabel, gbc2);
		gbc2.gridx = 1;
		gbc2.gridwidth = 2;
		signupPanel.add(signupUsernameText, gbc2);
		gbc2.gridx = 0;
		gbc2.gridy = 1;
		gbc2.gridwidth = 1;
		signupPanel.add(signupPasswordLabel, gbc2);
		gbc2.gridx = 1;
		gbc2.gridwidth = 2;
		signupPanel.add(signupPasswordText, gbc2);
		gbc2.gridx = 0;
		gbc2.gridy = 2;
		gbc2.gridwidth = 1;
		signupPanel.add(signupRepeatLabel, gbc2);
		gbc2.gridx = 1;
		gbc2.gridwidth = 2;
		signupPanel.add(signupRepeatText, gbc2);
		gbc2.gridx = 0;
		gbc2.gridy = 3;
		gbc2.gridwidth = 1;
		JLabel blank2 = new JLabel("");
		signupPanel.add(blank2, gbc2);
		gbc2.gridx = 1;
		gbc2.fill = GridBagConstraints.HORIZONTAL;
		gbc2.insets = new Insets(5, 5, 5, 5);
		signupPanel.add(signupButton, gbc2);
		gbc2.gridx = 2;
		signupPanel.add(cancelButton, gbc2);
		bottomPanel.add(signupPanel, "signup");
	}
	
	public void loginResponse(boolean success) {
		if (success){
			bc.instantiateOverview();
			bc.swapGUI("overview");
		} else {
			JOptionPane.showMessageDialog(null,
				    "Username or password is invalid, or already logged in.",
				    "Log-in failed.",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void signupResponse(boolean success) {
		if (success){
			bc.instantiateOverview();
			bc.swapGUI("overview");
		}
		else {
			//Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 19);
			//UIManager.put("OptionPane.messageFont", font);
			//UIManager.put("OptionPane.buttonFont", font);
			JOptionPane.showMessageDialog(null,
				    "Sign-up failed.",
				    "Sign-up failure.",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void guestResponse(boolean success) {
		if (success){
			bc.instantiateOverview();
			bc.swapGUI("overview");
		}
		else {
			JOptionPane.showMessageDialog(null,
				    "Guest login failed?",
				    "Login Failure",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void addActions(){
		loginButton.addActionListener(new ActionListener() {
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
				
				String username = loginUsernameText.getText();
				char[] password = loginPasswordText.getPassword();
				String passString = new String(password);
				
				if (username.equals("") || passString.equals("")){
					JOptionPane.showMessageDialog(null,
						    "Username/password cannot be empty.",
						    "Log-in failure.",
						    JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (bc.sCommunicator.isConnected()){
					bc.username = username;
					bc.sCommunicator.LoginAsUser(username, Encrypt.MD5(passString));
				}
				else {
					JOptionPane.showMessageDialog(null,
						    "Cannot connect to server.",
						    "Connection failed.",
						    JOptionPane.ERROR_MESSAGE);
				}
			}
	    });
		
		guestButton.addActionListener(new ActionListener() {
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
				
				if (bc.sCommunicator.isConnected()){
					bc.username = "";
					bc.sCommunicator.LoginAsGuest();
				}
				else {
					JOptionPane.showMessageDialog(null,
						    "Cannot connect to server.",
						    "Connection failed.",
						    JOptionPane.ERROR_MESSAGE);
				}
			}
	    });
		
		signupButton.addActionListener(new ActionListener() {
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
				
				String username = signupUsernameText.getText();
				
				char[] password = signupPasswordText.getPassword();
				String passString = new String(password);
				
				char[] passwordConfirm = signupRepeatText.getPassword();
				String passConfirmString = new String(passwordConfirm);
				
				if (username.equals("") || passString.equals("")){
					JOptionPane.showMessageDialog(null,
						    "Username/password cannot be empty.",
						    "Sign-up failure.",
						    JOptionPane.ERROR_MESSAGE);
					return;
				}
								
				if (passString.equals(passConfirmString)){
					if (bc.sCommunicator.isConnected()){
						bc.username = username;
						bc.sCommunicator.Signup(username, Encrypt.MD5(passString));						
					}
					else {
						JOptionPane.showMessageDialog(null,
							    "Cannot connect to server.",
							    "Connection failed.",
							    JOptionPane.ERROR_MESSAGE);
					}
				}
				else {
					JOptionPane.showMessageDialog(null,
						    "Password does not match.",
						    "Sign-up failed.",
						    JOptionPane.ERROR_MESSAGE);
				}
			}
	    });
		
		registerButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent ae){
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
				
				loginUsernameText.setText("");
				loginPasswordText.setText("");
			}
		});
		
		cancelButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent ae){
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
				
				signupUsernameText.setText("");
				signupPasswordText.setText("");
				signupRepeatText.setText("");
			}
		});
	}
	
	//class for flipping cards in CardLayout using JButtons
	class ButtonClicked implements ActionListener {
		private String action;
		private JPanel jp;
		
		public ButtonClicked(String action, JPanel jp) {
			this.action = action;
			this.jp = jp;
		}
		
		public void actionPerformed(ActionEvent ae) {
			CardLayout cl = (CardLayout)jp.getLayout();
			cl.show(jp, action);
		}
	}
}