package de.embots.touchflow.gui.components.optionpane;

import de.embots.touchflow.gui.inspector.InspectorView;
import de.embots.touchflow.gui.TouchFlow;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.embots.touchflow.module.core.Module;

public class OptionPane implements WindowListener{
Module sender;
Attribute[] options;
JFrame mainFrame;
JCheckBox showInInspector;
private static HashMap<Module,OptionPane> openPanes=new HashMap<Module, OptionPane>();


public void setLocation(int x, int y) {
	mainFrame.setLocation(x, y);
}

public static void showOptionPane(Attribute[] options, Module sender){
	
	//already open, bring old one to front
	if (openPanes.containsKey(sender)){
		openPanes.get(sender).requestFocus();
		return;
	}
	
	
	OptionPane pan=new OptionPane(options, sender);
	openPanes.put(sender,pan);
	pan.setLocation(TouchFlow.getLocation().x + sender.getGraphXPos(), TouchFlow.getLocation().y+sender.getGraphYPos()+150);
}

public void requestFocus() {
	mainFrame.requestFocus();
}

public OptionPane( Attribute[] options,Module sender) {
	
	this.sender = sender;
	this.options = options;
	
	mainFrame=new JFrame("Options");
	JPanel panMain=new JPanel();
	Box hBox=Box.createVerticalBox();
	
	mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	mainFrame.addWindowListener(this);
	mainFrame.getContentPane().add(panMain);
	
	//Module type
	
	DisplayAttribute lblTypShow=new DisplayAttribute("<html><u>"+sender.getModuleName()+"</u></html>");
	hBox.add(lblTypShow);
	
	//Add id Attribute
	
	DisplayAttribute idShow=new DisplayAttribute("ID: "+ sender.getId());
	hBox.add(idShow);
	
	//Add "show in inspector"
	
	showInInspector=new JCheckBox ("Show in Inspector");
	showInInspector.setSelected(sender.showInInspector());
	hBox.add(showInInspector);
	
	for (Attribute a:options){
		a.setMainFrame(mainFrame);

		hBox.add(a);
	}
	
	
	ActionListener buttonListener=new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			applyClicked();
			
		}
	};
	
	JPanel buttonPanel=new JPanel();
	JButton applyButton=new JButton("Apply");
	buttonPanel.add(applyButton);
	applyButton.addActionListener(buttonListener);
	hBox.add(buttonPanel);
	panMain.add(hBox);
	
	
	mainFrame.pack();
	mainFrame.setVisible(true);
}


private void applyClicked() {
	sender.setShowInInspector(showInInspector.isSelected());
	
	if (showInInspector.isSelected()){
		InspectorView.addModule(sender);
	}
	else{
		InspectorView.removeModule(sender);
	}
	sender.reinit(options);
	
	
}



@Override
public void windowActivated(WindowEvent arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void windowClosed(WindowEvent arg0) {
	openPanes.remove(sender);
	
}

@Override
public void windowClosing(WindowEvent arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void windowDeactivated(WindowEvent arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void windowDeiconified(WindowEvent arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void windowIconified(WindowEvent arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void windowOpened(WindowEvent arg0) {
	// TODO Auto-generated method stub
	
}

}
