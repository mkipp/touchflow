package de.embots.touchflow.gui.components.optionpane;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class ChooseAttribute extends Attribute {
	
	JRadioButton[] options;
	ButtonGroup gruppe;
	
	public ChooseAttribute(String Caption, String[] options){
		this.Caption=Caption;
		JLabel bezeichnung=new JLabel(Caption);
		
		JPanel hBox=new JPanel();
		hBox.add(bezeichnung);
		this.add(hBox);
		hBox.add(Box.createHorizontalGlue());
		
		gruppe=new ButtonGroup();
		this.options=new JRadioButton[options.length];
		
		ActionListener Listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				updateContent();
				
			}
		};
		
		for (int i=0; i< options.length;i++){
			this.options[i]=new JRadioButton(options[i]);
			this.options[i].addActionListener(Listener);
			gruppe.add(this.options[i]);
			hBox.add(this.options[i]);
		}
		/*trueButton=new JRadioButton("true");
		trueButton.setActionCommand("true");
		
		falseButton=new JRadioButton("false");
		falseButton.setActionCommand("false");
		*/
		
		
		
		hBox.setLayout(new FlowLayout());//BoxLayout(hBox, BoxLayout.PAGE_AXIS));
		hBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		 
		    
		    hBox.add(Box.createRigidArea(new Dimension(10,10)));
		    bezeichnung.setAlignmentX(0);

	}
	
	@Override
	public void setContent(Object in) {
		if (!(in instanceof Integer)) throw new IllegalArgumentException("Must have Integer to set content in ChoosePane");
		
		int selected=(Integer) in;
		
		if (selected <0 || selected >= options.length) throw new IllegalArgumentException("Selection index out of bounds");

		options[selected].setSelected(true);
		content=selected;
	}

	@Override
	protected void updateContent() {
		super.updateContent();
		for (int i=0; i< options.length;i++){
			if (options[i].isSelected()) {
				content=i;
				return;
			}
		}

	}

}
