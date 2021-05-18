package digitsApplication;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class DrawArea extends JPanel implements MouseListener, MouseMotionListener {
	
	private Image image;
	private Graphics2D g2;
	private int currentX, currentY, oldX, oldY;
	
	public DrawArea() {
		setDoubleBuffered(false);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	protected void paintComponent(Graphics g) {
		if (image == null) {
			image = createImage(getSize().width, getSize().height);
			g2 = (Graphics2D) image.getGraphics();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			clear();
		}
		g.drawImage(image, 0, 0, null);
		g.setColor(Color.LIGHT_GRAY);
		for (int i=0; i< getHeight(); i += getHeight()/28 )
			g.drawLine(0, i, getWidth(), i);
		for (int j=0; j< getWidth(); j += getHeight()/28 )
			g.drawLine(j, 0, j, getHeight());
	}
	
	public void clear() {
		g2.setPaint(Color.WHITE);
		g2.fillRect(0, 0, getSize().width, getSize().height);
		repaint();
	}
	
	public Image getImage() {
		return image;
	}
	
	public void setImage(Image image) {
		this.image = image;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		oldX = e.getX();
		oldY = e.getY();
	}
	
	@Override
	public void mouseDragged (MouseEvent e) {
		currentX = e.getX();
		currentY = e.getY();
		if (g2 != null) {
			g2.setColor(Color.GRAY);
			g2.setStroke(new BasicStroke(25));
			g2.drawLine(oldX, oldY, currentX, currentY);
			repaint();
			// update coordinates
			oldX = currentX;
			oldY = currentY;
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) { }
	
	@Override
	public void mouseReleased(MouseEvent e) { }

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

	@Override
	public void mouseClicked(MouseEvent e) { }

}
