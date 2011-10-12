package de.embots.touchflow.gui.components.optionpane;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.embots.touchflow.module.Globals;

public class AffineMapAttribute extends Attribute{
	JTextField textFeldGain, textFeldOffset;
	


	public AffineMapAttribute(String Caption){
		this.Caption=Caption;

		JPanel rahmen=new JPanel();
		rahmen.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(1), Caption));
		Box hBox=Box.createHorizontalBox();
		Box gainBox=Box.createVerticalBox();
		Box offsetBox=Box.createVerticalBox();
		this.add(rahmen);
		rahmen.add(hBox);
		hBox.add(gainBox);
		hBox.add(Box.createRigidArea(new Dimension(10,10)));
		hBox.add(offsetBox);
		
		JLabel gainLabel=new JLabel("Slope");
		gainBox.add(gainLabel);
		gainBox.add(Box.createRigidArea(new Dimension(10,10)));
		
		JLabel offsetLabel=new JLabel("Intercept");
		offsetBox.add(offsetLabel);
		offsetBox.add(Box.createRigidArea(new Dimension(10,10)));
		
		textFeldGain=new JTextField("0");
		textFeldGain.setPreferredSize(new Dimension(50,textFeldGain.getPreferredSize().height));
		gainBox.add(textFeldGain);
		
		textFeldOffset=new JTextField("0");
		textFeldOffset.setPreferredSize(new Dimension(50,textFeldOffset.getPreferredSize().height));
		offsetBox.add(textFeldOffset);
		
		//textFeld.setAlignmentX(0);
		//this.setAlignmentX(Component.LEFT_ALIGNMENT);
		/*hBox.setLayout(new BoxLayout(hBox, BoxLayout.X_AXIS));
		gainBox.setLayout(new BoxLayout(hBox, BoxLayout.Y_AXIS));
		offsetBox.setLayout(new BoxLayout(hBox, BoxLayout.Y_AXIS));
		*///hBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		//horBox.setAlignmentX(Component.BOTTOM_ALIGNMENT);
		
		content="0.0 0.0";
		
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
		
		    textFeldGain.addKeyListener(keyListener);
		    textFeldOffset.addKeyListener(keyListener);
		    
		    //horBox.add(Box.createRigidArea(new Dimension(10,10)));
		   //horBox.add(textFeld);

		//hBox.add(horBox);
		
	}
	
	public void setContent(Object in) {
		if (! (in instanceof String)){
			System.err.println("Warning: non-String content in AffineMapAttribute. Ignored");
			return;
			//throw new IllegalArgumentException("only Double allowed as content");
		}
		content=in;
		String[] params=((String)in).split(" ");
		
		if (params.length!=2){
			System.err.println("more or less than 2 params in AffineMapAttribute.setcontent. Ignored");
			return;
		}
		textFeldGain.setText(params[0].toString());
		textFeldOffset.setText(params[1].toString());
		updateContent();
	}
	
	protected void updateContent(){
		super.updateContent();
		try{
			Double gain=Double.parseDouble(textFeldGain.getText());
			Double offset=Double.parseDouble(textFeldOffset.getText());
			content=gain + " " + offset;
			
			textFeldGain.repaint();
			textFeldOffset.repaint();
			if (mainFrame!=null){
				mainFrame.mainFrame.pack();
				mainFrame.mainFrame.repaint();
			}
			
		}
		catch(NumberFormatException nf){
			if (Globals.isDebug)
				System.out.println("WARNING: AffineMapAttribute: number not parsed");
		}
	}



}
