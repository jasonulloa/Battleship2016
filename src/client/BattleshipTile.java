package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;

public class BattleshipTile extends JButton {
	
	private static final long serialVersionUID = 1L;
	

	
	
	private boolean ship;
	private boolean hit;
	private boolean yours;
	private int xcoord;
	private int ycoord;
	private int shipend;
	
	
	public BattleshipTile(boolean ship, boolean hit, boolean yours, int xcoord, int ycoord) {
		super();
		this.ship = ship;
		this.hit = hit;
		this.yours = yours;
		this.xcoord = xcoord;
		this.ycoord = ycoord;
	}
	
	public BattleshipTile(String text, boolean ship, boolean hit, boolean yours, int xcoord, int ycoord) {
		super(text);
		this.ship = ship;
		this.hit = hit;
		this.yours = yours;
		this.xcoord = xcoord;
		this.ycoord = ycoord;
	}
	

 
	
	@Override
    protected void paintComponent(Graphics g) {
        this.setOpaque(false);
        this.setContentAreaFilled(false);
        //this.setBorderPainted(false);
        Dimension size = this.getSize();
        if (ship == false && hit == false) {
            this.setBackground(Color.CYAN);
            try {
				g.drawImage(ImageIO.read(new File("resources/watertile.png")), 0, 0, size.width, size.height, this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("IOException:" + e);
			}
        } else if (ship == false && hit == true) {
        	this.setBackground(Color.ORANGE);
            try {
				g.drawImage(ImageIO.read(new File("resources/misstile.png")), 0, 0, size.width, size.height, this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("IOException:" + e);
			}
        } else if (ship == true && hit == false) {
        	this.setBackground(Color.GRAY);
        	if (shipend==0) {
        		try {
					g.drawImage(ImageIO.read(new File("resources/shiptile.png")), 0, 0, size.width, size.height, this);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("IOException:" + e);
				}
        	}
        	else {    		
        		try {
					g.drawImage(ImageIO.read(new File("resources/shiptile"+shipend+".png")), 0, 0, size.width, size.height, this);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("IOException:" + e);
				}
        			
        	}
        } else if (ship == true && hit == true) {
        	this.setBackground(Color.RED);
            try {
				g.drawImage(ImageIO.read(new File("resources/hittile.png")), 0, 0, size.width, size.height, this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("IOException:" + e);
			}
        }
        super.paintComponent(g);
        
    }
	
	public int getXCoord() {
		return xcoord;
	}
	
	public int getYCoord() {
		return ycoord;
	}
	
	public boolean isShip() {
		return ship;
	}
	
	public boolean isHit() {
		return hit;
	}
	
	public boolean isYours() {
		return yours;
	}
	
	public void setShip(boolean s) {
		ship = s;
	}
	
	public void setHit(boolean h) {
		hit = h;
	}
	
	public void setEnd(int e) {
		shipend=e;
	}
}
