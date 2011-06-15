package de.embots.touchflow.gui.components;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import de.embots.touchflow.module.Globals;

import org.jgraph.graph.GraphConstants;

public class Descriptor {
	public static JFrame getDescription (GraphModul toShow){
		JFrame mainFrame=new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JScrollPane scr=new JScrollPane();
		
		JTextPane textBox=new JTextPane();

		mainFrame.setPreferredSize(new Dimension(Globals.DescriptorWidth, Globals.DescriptorHeight));
		Rectangle2D pos=GraphConstants.getBounds(toShow.getAttributes());
		
		mainFrame.setBounds((int)pos.getX(),(int)pos.getY() + Globals.DescriptorYOffset,mainFrame.getWidth(), mainFrame.getHeight());
		textBox.setText(toShow.getCorrespondingModule().getDescription());

		scr.add(textBox);
		mainFrame.getContentPane().add(textBox);

		mainFrame.pack();
		mainFrame.setVisible(true);
		return mainFrame;
	}
}
