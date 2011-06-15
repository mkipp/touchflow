package gui.inspector;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public class StringOP {
	/**
	 * returns the Width of the String in Pixels. useful for arrangement
	 * @param text Text to compute width
	 * @param g Graphics objetc with which text will be drawn. uses Font set in Graphics to calculate
	 * @return Width in Pixels
	 */
 public static double getStringWidth(String text, Graphics g){
	 Font font = g.getFont();
	 FontRenderContext frc = g.getFontMetrics().getFontRenderContext();
	 Rectangle2D rect = font.getStringBounds(text, frc);
	 return rect.getWidth();
 }
 
	/**
	 * returns the Height of the String in Pixels. useful for arrangement
	 * @param text Text to compute height
	 * @param g Graphics objetc with which text will be drawn. uses Font set in Graphics to calculate
	 * @return height in Pixels
	 */
public static double getStringHeight(String text, Graphics g){
	 Font font = g.getFont();
	 FontRenderContext frc = g.getFontMetrics().getFontRenderContext();
	 Rectangle2D rect = font.getStringBounds(text, frc);
	 return rect.getHeight();
}
}
