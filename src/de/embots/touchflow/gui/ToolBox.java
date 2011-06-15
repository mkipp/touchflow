package de.embots.touchflow.gui;

import de.embots.touchflow.gui.ButtonType;
import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.exceptions.ModulFactoryException;
import de.embots.touchflow.gui.inspector.InspectorView;
import de.embots.touchflow.gui.serializer.GraphSerializer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import de.embots.touchflow.module.Globals;
import de.embots.touchflow.module.core.InputModule;
import de.embots.touchflow.module.core.ModifyModule;
import de.embots.touchflow.module.core.Module;
import de.embots.touchflow.module.core.ModuleGraph;
import de.embots.touchflow.module.core.OutputModule;
import de.embots.touchflow.module.factory.LibraryManager;
import de.embots.touchflow.util.RAClass;
import de.embots.touchflow.controller.ModulesController;


public class ToolBox implements ActionListener{
	public void dispose() {
		frame.dispose();
	}
	private JFrame frame;
	public void setLocation(int x, int y) {
		frame.setLocation(x, y);
	}
	private JFrame Inspector;
	private JButton[] buttons;
	private String selectedModule;
	private TouchFlow parentGraphFrame;
	public static ModulesController ctrl;
	private static JButton lastClickedBtn=null;
	private static Color btnColor;
	private JLabel statusLabel=new JLabel("Select mode");
	private ButtonType lastClicked=ButtonType.Select;
	
private void setupTools() throws ModulException{
	 frame = new JFrame();
	 
	 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	 frame.setTitle("ToolBox");
	 JPanel panAll=new JPanel();
	 JPanel panMModules=new JPanel();
	 JPanel panIModules=new JPanel();
	 JPanel panOModules=new JPanel();
	 /*JPanel pancmdButtons=new JPanel();
	 JSeparator strich=new JSeparator(JSeparator.HORIZONTAL);
	 JSeparator strich2=new JSeparator(JSeparator.HORIZONTAL);
	 
	 
	 pancmdButtons.setLayout(new GridLayout(2,1));
	*/
	 
	 frame.getContentPane().add(panAll);
	 
	 Box modulBox=Box.createVerticalBox();
	 JPanel ilblPanel=new JPanel();
	 JLabel iLabel=new JLabel("Input");
	 ilblPanel.add(iLabel);
	 
	 modulBox.add(ilblPanel);
	 modulBox.add(Box.createRigidArea(new Dimension(0,Globals.RigidAreaSize)));
	 modulBox.add(panIModules);
	 
	 modulBox.add(Box.createRigidArea(new Dimension(0,Globals.RigidAreaSize)));
	 modulBox.add(new JSeparator(JSeparator.HORIZONTAL));
	 JPanel mlblPanel=new JPanel();
	 JLabel mLabel=new JLabel("Modify");
	 mlblPanel.add(mLabel);

	 modulBox.add(mlblPanel);
	 modulBox.add(Box.createRigidArea(new Dimension(0,Globals.RigidAreaSize)));
	 modulBox.add(panMModules);
	 modulBox.add(Box.createRigidArea(new Dimension(0,Globals.RigidAreaSize)));
	 modulBox.add(new JSeparator(JSeparator.HORIZONTAL));
	 JPanel olblPanel=new JPanel();
	 JLabel oLabel=new JLabel("Output");
	 olblPanel.add(oLabel);
	 
	
	 modulBox.add(olblPanel);
	 modulBox.add(Box.createRigidArea(new Dimension(0,Globals.RigidAreaSize)));
	 modulBox.add(panOModules);
	 
	 
	 JPanel panama=new JPanel();
	 panAll.setLayout(new BoxLayout(panAll, BoxLayout.PAGE_AXIS));
	 //panAll.add(Box.createRigidArea(new Dimension(0,Globals.RigidAreaSize)));
	 //panAll.add(pancmdButtons);
	 //panAll.add(Box.createRigidArea(new Dimension(0,Globals.RigidAreaSize)));
	 //panAll.add(strich, BorderLayout.CENTER);
	 //panAll.add(strich2, BorderLayout.CENTER);
	 panAll.add(Box.createRigidArea(new Dimension(0,Globals.RigidAreaSize)));
	 panAll.add(modulBox);
	 panAll.add(Box.createVerticalGlue());
	 panAll.add(Box.createRigidArea(new Dimension(0,Globals.RigidAreaSize)));	 
	 panAll.add(new JSeparator(JSeparator.HORIZONTAL));
	 panAll.add(new JSeparator(JSeparator.HORIZONTAL));
	 panAll.add(Box.createRigidArea(new Dimension(0,Globals.RigidAreaSize)));
	 panAll.add(panama);
	 panama.add(statusLabel);
	 panAll.add(Box.createRigidArea(new Dimension(0,Globals.RigidAreaSize)));
	 
	 
	 
	 
	 
	 


	for (Class c:LibraryManager.manager.getInputModules()){
		InputModule instance=(InputModule) LibraryManager.manager.getInstance(c.getSimpleName());
		
		JButton button=new JButton();
		 button.setPreferredSize(new Dimension(100,50));
		 button.setText(instance.getModuleName());
		 panIModules.add(button);
		 button.addActionListener(this);
		 button.setForeground(Globals.InputModuleColor);
		 button.setToolTipText(instance.getDescription());
	}
	for (Class c:LibraryManager.manager.getModifyModules()){
		ModifyModule instance=(ModifyModule) LibraryManager.manager.getInstance(c.getSimpleName());
		JButton button=new JButton();
		 button.setPreferredSize(new Dimension(100,50));
		 button.setText(instance.getModuleName());
		 panMModules.add(button);
		 button.addActionListener(this);
		 button.setForeground(Globals.ModifyModuleColor);
		 button.setToolTipText(instance.getDescription());
	}
	for (Class c:LibraryManager.manager.getOutputModules()){
		
		OutputModule instance=(OutputModule) LibraryManager.manager.getInstance(c.getSimpleName());
		
		JButton button=new JButton();
		 button.setPreferredSize(new Dimension(100,50));
		 button.setText(instance.getModuleName());
		 panOModules.add(button);
		 button.addActionListener(this);
		 button.setForeground(Globals.OutputModuleColor);
		 button.setToolTipText(instance.getDescription());
	}
	 //set layout
	 
	 
		 panIModules.setLayout(new GridLayout((int) Math.ceil(panIModules.getComponentCount()/Globals.ModuleCols),1));
		 panMModules.setLayout(new GridLayout((int) Math.ceil(panMModules.getComponentCount()/Globals.ModuleCols),1));
		 panOModules.setLayout(new GridLayout((int) Math.ceil(panOModules.getComponentCount()/Globals.ModuleCols),1));
	 

	 
	 
	 frame.pack();
	 frame.setLocation(0,0);//Globals.ScreenWidth-frame.getWidth(), 50);
	 frame.setVisible(true);
     
}

public int getWidth() {
	return frame.getWidth();
}

public JFrame getFrame() {
	return frame;
}

public void setStatusText(String text){
	statusLabel.setText(text);
	statusLabel.repaint();
}
public ButtonType getLastClicked() {
	return lastClicked;
}
public ToolBox(TouchFlow parent) {
	super();
	parentGraphFrame=parent;
	try {
		setupTools();
	} catch (ModulException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
@Override
public void actionPerformed(ActionEvent arg0) {
	parentGraphFrame.resetConnectionMode();
	
	if (arg0.getSource()==lastClickedBtn){
		resetClicked();
		return;
	}
	
	resetClicked();
	
	lastClickedBtn=(JButton) arg0.getSource();
	btnColor=lastClickedBtn.getBackground();
	lastClickedBtn.setBackground(Globals.selectedButtonColor);
	
	
	
	lastClicked=ButtonType.Module;
	selectedModule=((JButton)arg0.getSource()).getText();
	setStatusText("Add mode for:" + selectedModule); //" - " + LibraryManager.manager.getInstance(selectedModule.toString()).getDescription());
	
}

public String getSelectedModule() {
	return selectedModule;
}
public void setLastClicked(ButtonType lastClicked) {
	this.lastClicked = lastClicked;
}
public void resetClicked(){
	lastClicked=ButtonType.Select;
	setStatusText("Select mode");
	if (lastClickedBtn!=null) lastClickedBtn.setBackground(btnColor);
	lastClickedBtn=null;
}



}
