package client;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class OverviewPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Image background;

    public OverviewPanel(Image image) {     
        background = image;            
    }

    @Override
    public Dimension getPreferredSize() {
        return background == null ? new Dimension(0, 0) : new Dimension(background.getWidth(this), background.getHeight(this));            
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (background != null) {                

            int width = getWidth();
            int height = getHeight();

            int x = (width - background.getWidth(this)) / 2;
            int y = (height - background.getHeight(this)) / 2 - 33;

            g.drawImage(background, x, y, this);                
        }

    }
}