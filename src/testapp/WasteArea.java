package testapp;

import gui.inspector.StringOP;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;


public class WasteArea extends JComponent {

	@Override
	protected void paintComponent(Graphics arg0) {
		// TODO Auto-generated method stub
		super.paintComponent(arg0);
		arg0.setColor(Color.red);
		arg0.fillRect(0, 0, getWidth(), getHeight());
		arg0.setColor(Color.white);
		String text="waste area";
		int xpos=(int) ((getWidth() - StringOP.getStringWidth(text, arg0))/2);
		int ypos=(int) ((getHeight() -StringOP.getStringHeight(text, arg0))/2);
		arg0.drawString(text, xpos, ypos);
	}
	public boolean isInside(Rectangle other){
		Rectangle bounds=getBounds();
		return bounds.intersects(other);
	}

}
