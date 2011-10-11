package de.embots.touchflow.gui.components;

import de.embots.touchflow.gui.inspector.InspectorView;
import de.embots.touchflow.TouchFlow;
import de.embots.touchflow.gui.serializer.GraphSerializer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import de.embots.touchflow.module.core.Module;
import de.embots.touchflow.module.core.ModuleGraph;
import de.embots.touchflow.module.core.OutputModule;
import de.embots.touchflow.controller.ModulesController;

import de.embots.touchflow.util.RAClass;

public class Toolbar extends JToolBar implements ActionListener{
	
	private JFrame Inspector;
	private static JLabel lblStatus;
	public static ModulesController ctrl;
	
	
public Toolbar(){
	Box hBox=Box.createHorizontalBox();
	this.add(hBox);
	lblStatus=new JLabel();
	setStoppedText();
	
	hBox.add(Box.createRigidArea(new Dimension(15,30)));
	hBox.add(makeNavigationButton("gfx/toolbar/start.png", "start", "Run", "Run"));
	hBox.add(makeNavigationButton("gfx/toolbar/stop.png", "stop", "Stop", "Stop"));
	hBox.add(Box.createRigidArea(new Dimension(15,30)));
	hBox.add(makeNavigationButton("gfx/toolbar/zoom_in.png", "zoomin", "Zoom in", "Zoom in"));
	hBox.add(makeNavigationButton("gfx/toolbar/zoom_out.png", "zoomout", "Zoom out", "Zoom out"));
	hBox.add(Box.createRigidArea(new Dimension(15,30)));
	
	hBox.add(makeNavigationButton("gfx/toolbar/cross.png", "purge", "Delete all", "Delete all"));
	hBox.add(Box.createRigidArea(new Dimension(15,30)));
	hBox.add(lblStatus);
	
	setPreferredSize(new Dimension(450, 40));
}


protected JButton makeNavigationButton(String imageName,
        String actionCommand,
        String toolTipText,
        String altText)  {
//Look for the image.
String imgLocation = imageName;

//Create and initialize the button.
JButton button = new JButton();
button.setActionCommand(actionCommand);
button.setToolTipText(toolTipText);
button.addActionListener(this);

button.setIcon(new ImageIcon(imgLocation));

button.setPreferredSize(new Dimension(30,30));

return button;
}


/**
 * on button click
 */
@Override
public void actionPerformed(ActionEvent e) {
	String cmd = e.getActionCommand();
	
	if (cmd.equals("start")){
		startGraph();
	}
	

	if (cmd.equals("zoomin")){
		TouchFlow.incZoom();
	}
	
	if (cmd.equals("stop")){
		if (ctrl !=null) ctrl.stopAll();
		if (Inspector!=null) Inspector.dispose();
		setStoppedText();
	}
	

	if (cmd.equals("zoomout")){
		TouchFlow.decZoom();
	}
	

	if (cmd.equals("purge")){
		String Message="Achtung!\n\nDer gesamte Graph wird geloescht.\n\nWollen Sie tatsaechlich fortfahren?";
		if (RAClass.ConfirmMsgbox(Message))TouchFlow.clearGraph();
	}

	
}


public static void setStoppedText() {
	lblStatus.setText("Stopped");
	
}


public void startGraph() {

	
	Module[] module=GraphSerializer.getModules(TouchFlow.getNodes(), TouchFlow.getEdges());

	//close old inspector
	if (Inspector!=null) InspectorView.stopClicked();
	lblStatus.setText("Running...");
	
	ModuleGraph graph=new ModuleGraph();
	
	for (Module m:module){
		graph.addModul(m, m instanceof OutputModule);
	}
	ctrl=new ModulesController(graph);
	ctrl.startSystem();
	Inspector= InspectorView.startInspectors(module);
}

}
