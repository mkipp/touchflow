package gui.touchflow.components.OptionPane;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StringAttribute extends Attribute{
	JTextField textFeld;
	


	public StringAttribute(String Caption){
		this.Caption=Caption;
		JLabel bezeichnung=new JLabel(Caption);
		
		JPanel hBox=new JPanel();
		hBox.add(bezeichnung);
		this.add(hBox);
		hBox.add(Box.createHorizontalGlue());
		textFeld=new JTextField("0");
		textFeld.setPreferredSize(new Dimension(100,textFeld.getPreferredSize().height));
		
		hBox.setLayout(new FlowLayout());//BoxLayout(hBox, BoxLayout.PAGE_AXIS));
		hBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		content=0;
		
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
		    hBox.add(Box.createRigidArea(new Dimension(10,10)));
		    bezeichnung.setAlignmentX(0);

		hBox.add(textFeld);
		
	}
	
	public void setContent(Object in) {
		content=in;
		textFeld.setText(in.toString());
		updateContent();
	}
	
	protected void updateContent(){

			content=textFeld.getText();
			textFeld.repaint();
			if (mainFrame!=null){
				mainFrame.pack();
				mainFrame.repaint();
			}
			
	}



}
