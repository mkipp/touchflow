package de.embots.touchflow.gui.components.optionpane;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DisplayAttribute extends Attribute{
	JTextField textFeld;
	


	public DisplayAttribute(String Caption){
		this.Caption=Caption;
		JLabel bezeichnung=new JLabel(Caption);
		
		JPanel hBox=new JPanel();
		Box horBox=Box.createHorizontalBox();
		horBox.add(bezeichnung);
		this.add(hBox);
		hBox.setLayout(new BoxLayout(hBox, BoxLayout.X_AXIS));
		
	

		//hBox.add(Box.createHorizontalGlue());
		hBox.add(horBox);
		
	}
	
	public void setContent(Object in) {
		
	}
	
	protected void updateContent(){
		
	}

	

}
