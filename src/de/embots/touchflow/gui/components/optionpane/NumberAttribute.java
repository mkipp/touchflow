package de.embots.touchflow.gui.components.optionpane;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.embots.touchflow.module.Globals;

public class NumberAttribute extends Attribute{
	JTextField textFeld;
	


	public NumberAttribute(String Caption){
		this.Caption=Caption;
		JLabel bezeichnung=new JLabel(Caption);
		
		JPanel hBox=new JPanel();
		Box horBox=Box.createHorizontalBox();
		horBox.add(bezeichnung);
		this.add(hBox);
		textFeld=new JTextField("0");
		textFeld.setPreferredSize(new Dimension(50,textFeld.getPreferredSize().height));
		//textFeld.setAlignmentX(0);
		//this.setAlignmentX(Component.LEFT_ALIGNMENT);
		hBox.setLayout(new BoxLayout(hBox, BoxLayout.X_AXIS));
		//hBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		//horBox.setAlignmentX(Component.BOTTOM_ALIGNMENT);
		
		content=0.0;
		
		KeyListener keyListener = new KeyListener() {
		      public void keyPressed(KeyEvent keyEvent) {
		    	  
		      }

			@Override
			public void keyReleased(KeyEvent arg0) {
				updateContent();
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				
			}
		    };
		
		    textFeld.addKeyListener(keyListener);
		    horBox.add(Box.createRigidArea(new Dimension(10,10)));
		    //bezeichnung.setAlignmentX(0);

		horBox.add(textFeld);
		//hBox.add(Box.createHorizontalGlue());
		hBox.add(horBox);
		
	}
	
	public void setContent(Object in) {
		if (! (in instanceof Double)){
			System.err.println("Warning: non-double content in Numberattribute");
			//throw new IllegalArgumentException("only Double allowed as content");
		}
		content=in;
		
		textFeld.setText(in.toString());
		updateContent();
	}
	
	protected void updateContent(){
		try{
			Double d=Double.parseDouble(textFeld.getText());
			content=d;
			
			textFeld.repaint();
			if (mainFrame!=null){
				mainFrame.pack();
				mainFrame.repaint();
			}
			
		}
		catch(NumberFormatException nf){
			if (Globals.isDebug)
				System.out.println("WARNING: numberattribute: number not parsed");
		}
	}



}
