package client;

import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class BattleshipPanel extends JPanel{
	public static final long serialVersionUID = 1;
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(Toolkit.getDefaultToolkit().getImage("resources/battleship.jpg"), 0, 0, getWidth(), getHeight(), null);
		validate(); 
		repaint();
	}
}