package testapp;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import module.Globals;
import module.core.pinName;
import module.pin.OutputPin;
import module.pin.OutputPin2D;
import controller.ModulesController;
import controller.XMLParser;
import exceptions.ModulException;

public class AppMouseVoice implements Observer{

	/**
	 * @param args
	 */
	
	private static ModulesController controller;
		private static OutputPin2D mousePos;
	
	private static OutputPin distanz;
	private static OutputPin distanzvoice;
	private static OutputPin winkel;
	//private static InputPin2D distIn;
	private static ArrayList<Viereck> ecke=new ArrayList<Viereck>();
	private static JFrame frame;
	private static WasteArea trash;

	
	public static void main(String[] args) {
		setupGUI();
		startSystem();
	}

	private static void startSystem() {
		XMLParser parser=new XMLParser();
		try {
			parser.parseXMLFile("ressources/testappctrmousevoice.xml");
		} catch (ModulException e) {
			System.err.println("Fehler beim laden des XML-files: " + e.getMessage());
			return;
		}
		
		controller=new ModulesController(parser.getGraph());
		
		//Observer
		
		try {
			AppMouseVoice obs=new AppMouseVoice();
			controller.attachObserver(obs, 26);
			controller.attachObserver(obs, 27);
			controller.attachObserver(obs, 37);
			controller.attachObserver(obs, 45);
			mousePos=controller.findModul(26).getOutputPin2D(pinName.OUT);
			distanz=controller.findModul(27).getOutputPin(pinName.OUT);
			distanzvoice=controller.findModul(45).getOutputPin(pinName.OUT);
			//distIn=controller.findModul(21).getInputPin2D(pinName.P1);
			winkel=controller.findModul(37).getOutputPin(pinName.OUT);
		} catch (ModulException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		controller.startSystem();
		
	}

	private static void setupGUI() {
		frame=new JFrame("TestApp");
		

		frame.getContentPane().setLayout(null);

		    //toolbox beim beenden schließen 
		 frame.setPreferredSize(new Dimension(500, 500));
		 
		 //waste area erstellen
		 Viereck v1,v2;
		 v1=new Viereck();
		 v2=new Viereck();
		 v2.setAngle(45);
		 trash=new WasteArea();
		 trash.setBounds(new Rectangle(Globals.ScreenWidth-100,Globals.ScreenHeight-100,100,100));
		 frame.add(trash);
		 //frame.add(v1);
		 //frame.add(v2);
		    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    frame.pack();
		    frame.setVisible(true);
	}
	
	
	@Override
	public synchronized void update(Observable arg0, Object arg1) {
		
			
		//scale per mausrad
		
		try {
			if (arg0==controller.findModul(27)){
				for (Viereck chosen2:ecke){
					if (chosen2 !=null && chosen2.isMarked()){
						
						chosen2.setSize((int)distanz.getData());
					}
				}
			}
		} catch (ModulException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//angle durch voicepitch
		
			if (arg0==winkel.getParentModul() && winkel.getData() !=0){
				for (Viereck chosen2:ecke){
					if (chosen2 !=null && chosen2.isMarked()){
						
						chosen2.setAngle((int)winkel.getData());
					}
				}
			}
		
		
			//size durch amplitude
			if (arg0==distanzvoice.getParentModul() && distanzvoice.getData() !=0){
				for (Viereck chosen2:ecke){
					if (chosen2 !=null && chosen2.isMarked()){
						
						chosen2.setSize((int)distanzvoice.getData());
					}
				}
			}
			
			//wert groesser 0 -> mux hat durchgeleitet
			if (mousePos.getData()>0 && mousePos.getData2()>0 ){

				Viereck chosen=getCoveredViereck(finger0X(), finger0Y());
					if (chosen !=null && !chosen.isMarked()){
						//unmark all others
						for (Viereck v:ecke){
							v.unmark();
						}
						
						chosen.mark();

						
					}
					if (chosen!=null && chosen.isMarked()){
						chosen.setBounds(finger0X()-chosen.getVSize()/2,finger0Y()-chosen.getVSize()/2,chosen.getVSize()*2,chosen.getVSize()*2);
						chosen.calcPoints();
						//remove
						
						if (trash.isInside(chosen.getBounds())){

							frame.remove(chosen);
							ecke.remove(chosen);
							
						}
						else{
							chosen.repaint();	
						}
					}
					
					else{
						Viereck eck=new Viereck();
						
							//neues anlegen

							

								
									eck.setBounds(finger0X()-eck.getVSize()/2,finger0Y()-eck.getVSize()/2,eck.getVSize()*2,eck.getVSize()*2);
									eck.calcPoints(); //calculate correct bounds
									ecke.add(eck);
									 frame.getContentPane().add(eck);
									 eck.unmark();

									 eck.repaint();
							
					}
				}
		
			
		
		frame.repaint();
		
	}

private int finger0Y() {
// TODO Auto-generated method stub
return (int) mousePos.getData2();
}

private int finger0X() {
// TODO Auto-generated method stub
return (int) mousePos.getData();
}
	public Viereck getCoveredViereck(int x, int y){
		for (Viereck v:ecke){
			if (isViereckCovered(x, y, v)) return v;
		}
		return null;
	}
	public boolean isViereckCovered(int x, int y, Viereck eck){
		int size=eck.getVSize();
		int eckX=eck.getBounds().x;
		int eckY=eck.getBounds().y;
		
		if (x>eckX && x<eckX+size && y>eckY && y<eckY+size){
			return true;
		}
		return false;
	}

}
