package de.embots.touchflow.gui.inspector;

import de.embots.touchflow.gui.KeyListenerImpl;
import de.embots.touchflow.gui.ToolBox;
import de.embots.touchflow.gui.components.Toolbar;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import de.embots.touchflow.module.core.Module;
import de.embots.touchflow.module.implementation.input.Keyboard;
import de.embots.touchflow.module.implementation.input.KinectSimulator3D;
import de.embots.touchflow.module.implementation.input.Mouse2D;


public class InspectorView {
	
	static JFrame frame, mainFrame;
	static KeyListenerImpl listener;
	private static HashMap <Module, InspectorComponent> components=new HashMap<Module, InspectorComponent>();
	
	 private static JFrame createAndShowGUI(Module[] module) {
	        //Create and set up the window.
	        
		 	frame = new JFrame("Inspector-View");
	        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	        
	        
		 	mainFrame = new JFrame();
	        
	        
	        Box vbox=Box.createVerticalBox();
	        
	        Box hBox=Box.createHorizontalBox();
	        
	        hBox.add(new JLabel("Running......"));
	       hBox.add(Box.createHorizontalGlue());
	       JButton stop=new JButton("Stop");
	       stop.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {stopClicked();} } ); 
			
			
	        hBox.add(stop);
	        
	        //------ mainframe
	        vbox.add(hBox);
	        vbox.add(mainFrame.getContentPane());
	        frame.add(vbox);
	        // das textfeld f��r keyboardlistener
	        
	        JTextField feld=new JTextField();
	        listener=new KeyListenerImpl();
	        feld.addKeyListener(listener);
	        mainFrame.getContentPane().add(feld);
	        
	        //---------
	        mainFrame.setLayout(new GridLayout(3,6));
	        components.clear();
	        for (Module m:module){
	        	if (m.showInInspector()) addModule(m);
	        }
	        
	        //Display the window.
	        frame.pack();
	        frame.setVisible(true);
	        return frame;
	    }



	public static void addModule(Module m) {
		if (components.containsKey(m)||!m.showInInspector())
		{
			//don't add it twice!
			return;
		}
		InspectorComponent ins=new InspectorComponent(m);
		components.put(m, ins);
		if (m instanceof Mouse2D){
			((Mouse2D) m).attachComponent(ins);
		}
		if (m instanceof KinectSimulator3D){
			((KinectSimulator3D) m).attachComponent(ins);
		}
		
		if (m instanceof Keyboard){
			((Keyboard) m).attachComponent(listener);
		}
		if (mainFrame==null) return;
		mainFrame.getContentPane().add(ins);	
		frame.pack();
	}
	
	public static void removeModule(Module m){
		if (!components.containsKey(m))
		{
			//System.err.println("InspectorView:removeModule: Module not found!");
			return;
		}
		InspectorComponent component=components.get(m);
		components.remove(m);
		mainFrame.remove(component);
		frame.pack();
		frame.repaint();
	}
	 
	 

	    public static JFrame startInspectors(Module[] module) {
	        //Schedule a job for the event-dispatching thread:
	        //creating and showing this application's GUI.
	       
	    	/*Modul[] arr=new Modul[3];
	    	arr[0]=new Mouse();
	    	arr[1]=new Distance();
	    	arr[2]=new Print();*/
	    	// javax.swing.SwingUtilities.invokeLater(new Runnable() {
	        //    public void run() {
	                return createAndShowGUI(module);
	        //    }
	        //});
	           
	    }
	    
	    public static void stopClicked(){
	    	if (Toolbar.ctrl!=null) {
	    		Toolbar.ctrl.stopAll();
	    		Toolbar.setStoppedText();
	    	}
	    	frame.dispose();
	    }
	    public static boolean isRunning(){
	    	return frame !=null;
	    }

}
