package gui.touchflow.components.OptionPane;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class BooleanAttribute extends Attribute{
	JRadioButton trueButton, falseButton;
	ButtonGroup gruppe;
	


	public BooleanAttribute(String Caption){
		this.Caption=Caption;
		JLabel bezeichnung=new JLabel(Caption);
		
		JPanel hBox=new JPanel();
		hBox.add(bezeichnung);
		this.add(hBox);
		hBox.add(Box.createHorizontalGlue());
		
		trueButton=new JRadioButton("true");
		trueButton.setActionCommand("true");
		
		falseButton=new JRadioButton("false");
		falseButton.setActionCommand("false");
		
		gruppe=new ButtonGroup();
		gruppe.add(trueButton);
		gruppe.add(falseButton);
		
		hBox.setLayout(new FlowLayout());//BoxLayout(hBox, BoxLayout.PAGE_AXIS));
		hBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		content=0;
		
		ActionListener Listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				updateContent();
				
			}
		};
		   
		
		    trueButton.addActionListener(Listener);
		    falseButton.addActionListener(Listener);
		    
		    hBox.add(Box.createRigidArea(new Dimension(10,10)));
		    bezeichnung.setAlignmentX(0);

		hBox.add(trueButton);
		hBox.add(falseButton);
		
	}
	
	public void setContent(Object in) {
		content=in;
		if ((Boolean) in){
			trueButton.setSelected(true);
		}
		else{
			falseButton.setSelected(true);
		}
		updateContent();
	}
	
	protected void updateContent(){
		
		content=trueButton.isSelected();
		/*try{
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
		}*/
	}



}
