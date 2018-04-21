package client;

import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;

public class BattleshipJButton extends JButton {
   
	private static final long serialVersionUID = 1L;

	
	public BattleshipJButton(String s) {
		super(s);
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        super.paintComponent(g);
        Dimension size = this.getSize();
        if(!getModel().isRollover() && !getModel().isPressed()) {
            try {
				g.drawImage(ImageIO.read(new File("resources/button.png")), 0, 0, size.width, size.height, this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("IOException:" + e);
			}
        } else { 
        	if (getModel().isRollover() && !getModel().isPressed()){
            	try {
					g.drawImage(ImageIO.read(new File("resources/buttonhover.png")), 0, 0, size.width, size.height, this);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("IOException:" + e);
				}       
        	}
        	if (getModel().isPressed()) {
            	try {
					g.drawImage(ImageIO.read(new File("resources/buttonpress.png")), 0, 0, size.width, size.height, this);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("IOException:" + e);
				}   
        	}
        }
        super.paintComponent(g);
    }


}
