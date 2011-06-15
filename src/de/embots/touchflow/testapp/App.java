package de.embots.touchflow.testapp;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import de.embots.touchflow.module.Globals;
import de.embots.touchflow.module.core.pinName;
import de.embots.touchflow.module.pin.InputPin2D;
import de.embots.touchflow.module.pin.OutputPin;
import de.embots.touchflow.module.pin.OutputPin2D;
import de.embots.touchflow.controller.ModulesController;
import de.embots.touchflow.controller.XMLParser;
import de.embots.touchflow.exceptions.ModulException;

public class App implements Observer{

	/**
	 * @param args
	 */
	
	private static ModulesController controller;
	private static OutputPin2D finger1out;
	private static OutputPin2D finger0out;
	private static OutputPin2D mausout;
	private static OutputPin distanz;
	private static OutputPin winkel;
	private static InputPin2D distIn;
	private static ArrayList<Viereck> ecke=new ArrayList<Viereck>();
	private static JFrame frame;
	private static WasteArea trash;
	
	private static boolean clickedFirstTime,justMarked, clickedEmptyTime;
	private static long clickedSystemTime;
	
	private boolean removed;
	
	public static void main(String[] args) {
		setupGUI();
		startSystem();
	}

	private static void startSystem() {
		XMLParser parser=new XMLParser();
		try {
			parser.parseXMLFile("ressources/testappctracryl.xml");
		} catch (ModulException e) {
			System.err.println("Fehler beim laden des XML-files: " + e.getMessage());
			return;
		}
		
		controller=new ModulesController(parser.getGraph());
		
		//Observer
		
		try {
			App obs=new App();
			controller.attachObserver(obs, 19);
			controller.attachObserver(obs, 20);
			finger1out=controller.findModul(20).getOutputPin2D(pinName.OUT);
			finger0out=controller.findModul(19).getOutputPin2D(pinName.OUT);
			distanz=controller.findModul(23).getOutputPin(pinName.OUT);
			distIn=controller.findModul(21).getInputPin2D(pinName.P1);
			winkel=controller.findModul(22).getOutputPin(pinName.ANGLE);
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
	public int finger0X(){
		return (int) finger0out.getData()-frame.getBounds().x;
	}
	public int finger0Y(){
		return (int) finger0out.getData2()-frame.getBounds().y;
	}
	@Override
	public synchronized void update(Observable arg0, Object arg1) {
		
		//berechnet, wie lange finger0 nicht auf dem display ist
		if (finger0out.getData()==0 && finger0out.getData2()==0 ){
			justMarked=false;
			if (clickedFirstTime) clickedEmptyTime=true;
		}

		
		if (finger1out.getData()==0 && finger1out.getData2()==0){
			//finger-tip
			
			//finger 0 muss da sein
			if (finger0out.getData()>0 && finger0out.getData2()>0 ){

				if (!removed){
					Viereck chosen=getCoveredViereck(finger0X(), finger0Y());
					if (chosen !=null && !chosen.isMarked()){
						//unmark all others
						for (Viereck v:ecke){
							v.unmark();
						}
						
						chosen.mark();

						
						justMarked=true;
						
					}
					if (chosen!=null && chosen.isMarked()&&!justMarked){
						chosen.setBounds(finger0X()-chosen.getVSize()/2,finger0Y()-chosen.getVSize()/2,chosen.getVSize()*2,chosen.getVSize()*2);
						chosen.calcPoints();
						//remove
						
						if (trash.isInside(chosen.getBounds())){

							frame.remove(chosen);
							ecke.remove(chosen);
							removed=true; //erst bei n�chstem finger aufsetzen wieder was machen
						}
						else{
							chosen.repaint();	
						}
					}
					
					else{
						Viereck eck=new Viereck();
						//nichts im trash erstellen
						if (!trash.isInside(eck.getBounds())){
							//neues anlegen
							//wurde vorher mal geklickt und war dazwischen eine Pause?
							if (clickedFirstTime && clickedEmptyTime){
								
								//war as intervall klein genug?
								if (System.currentTimeMillis()-clickedSystemTime<Globals.maxDoubleClickTime){

									clickedFirstTime=false;
									eck.setBounds(finger0X()-eck.getVSize()/2,finger0Y()-eck.getVSize()/2,eck.getVSize()*2,eck.getVSize()*2);
									eck.calcPoints(); //calculate correct bounds
									ecke.add(eck);
									System.out.println("created new at " + eck.getBounds());
									 frame.getContentPane().add(eck);
									 eck.unmark();
									 //mark auf false setzen, wird sonst gleich markiert
									 justMarked=true;
									 eck.repaint();
								}
								//zu langsam: auf aktuelle zeit setzen
								else{
									System.out.println("zu langsam");
									clickedSystemTime=System.currentTimeMillis();
									clickedEmptyTime=false;
								}
							}
							if (!clickedFirstTime){
								
								for (Viereck v:ecke){
									v.unmark();
									v.repaint();
								}
								clickedFirstTime=true;
								clickedSystemTime=System.currentTimeMillis();
								clickedEmptyTime=false;
							}
						}
						else{

						}
					}
				}
			}
			else{
				removed=false;

			}
		}
		else{
			//verhindert spruenge beim ansetzen des 2.fingers: oft hat finger-modul schon koordinaten, distanz hat aber noch nicht neu berechnet
			if (distIn.getData()!=0 && distIn.getData2()!=0){
				//2-finger-move: scale/rot
				Viereck chosen=getCoveredViereck(finger0X(), finger0Y());
				if (chosen!=null && chosen.isMarked()){
					chosen.setBounds(finger0X()-chosen.getVSize()/2,finger0Y()-chosen.getVSize()/2,chosen.getVSize()*2,chosen.getVSize()*2);
					
					chosen.setAngle((int) (winkel.getData() * (180/Math.PI)));
					chosen.setSize((int) distanz.getData());
					chosen.setBounds(finger0X()-chosen.getVSize()/2,finger0Y()-chosen.getVSize()/2,chosen.getVSize()*2,chosen.getVSize()*2);
					
				}
			}
			
		}
		frame.repaint();
		
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
