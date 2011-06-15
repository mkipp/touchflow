package de.embots.touchflow.module.core;

import java.util.HashMap;
import java.util.Observable;

import de.embots.touchflow.module.Globals;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.InputPin2D;
import de.embots.touchflow.module.pin.OutputPin;
import de.embots.touchflow.module.pin.OutputPin2D;
import de.embots.touchflow.module.pin.Pin;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.exceptions.PinException;
import de.embots.touchflow.gui.components.optionpane.Attribute;
import de.embots.touchflow.gui.components.optionpane.OptionPane;


public abstract class Module extends Observable implements Runnable{
	
protected InputPin[] inputPins;
protected OutputPin[] outputPins;
protected String Typ=this.getClass().getSimpleName();
protected boolean showInInspector;

public abstract String getModuleName();

public void setShowInInspector(boolean showInInspector) {
	this.showInInspector = showInInspector;
}


public boolean showInInspector() {
	return showInInspector;
}



//fuer Grafik: sagt an welchem gemalten Anschluss welcher Pin ist
protected HashMap<Integer,pinName> portMap=new HashMap<Integer, pinName>();

private static int idcounter;
private int id;
protected boolean forceAlwaysUpdate;

//GFX
protected int graphXPos=0;
protected int graphYPos=0;

public int getGraphXPos() {
	return graphXPos;
}


public String getDescription(){
	return Typ.toString();
}

public void setGraphXPos(int graphXPos) {
	this.graphXPos = graphXPos;
}

public void init(String params) throws ModulException{
	
}

public int getGraphYPos() {
	return graphYPos;
}



public void setGraphYPos(int graphYPos) {
	this.graphYPos = graphYPos;
}



//fuer FPS-Berechnung
private long timeStart; //zeitpunkt beim start der Messung
private int cycles; //Anzahl der vergangenen Zyklen seit Start der Zeitmessung
private int fps;

public Module() {
	//auto id-management
	id=idcounter;
	idcounter++;
}

public void addPortMapEntry(int portNum, pinName name) throws ModulException{
	if (portMap.containsKey(portNum)) throw new ModulException("Portmap-Eintrag bereits vorhanden: " + portNum);
	if (portNum<0 || portNum>7) System.err.println("WARNING: Portnum out of bounds:" + portNum);
	portMap.put(portNum, name);
}
public pinName getPortMapEntry(int portNum){
	return portMap.get(portNum);
}


public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}



private boolean isRunning=true; //soll der Thread noch laufen?
private Thread thisThread; //der Thread den das Modul repräsentiert


/**
 * mainloop des Threads
 */
@Override
public void run() {
	while (isRunning){
		runOnce();
	}
}

/**
 * reinitializes the object, e.g. for changing the konstructor args.
 * @param args
 */
public void reinit(Attribute[] args){
	
}

/**
 * create Argument objects and open OptionPanel
 */
public void openOptions(){
	OptionPane.showOptionPane(new Attribute[0], this);
}

/**
 * startet genau eine Datenberechnung. Fuer Debug-Zwecke
 */
public void runOnce() {
	//FPS-Berechnung
	
	if (timeStart==0){
		timeStart=System.currentTimeMillis();
	}
	
	
	if (cycles > Globals.CyclesCountAmount || (System.currentTimeMillis()-timeStart)>Globals.timeDiffCountAmount){
		double timediff=System.currentTimeMillis()-timeStart;
		fps=(int) ((1000/timediff)*cycles);
		timeStart=System.currentTimeMillis();
		cycles=0;
	}
	
	
	
	//pindaten updaten
	for (InputPin pin: inputPins){
		try {
			pin.pullData();
		} catch (PinException e) {
			// TODO Auto-generated catch block
			System.err.println("Modul - Threadloop control: Exception beim Datenupdate:" + e.getMessage());
		}
	}
	
	//berechnungen durchführen
	try {
		if (!Globals.useModuleHandshake || dataPushed()){
			if (!Globals.onlyCalculateIfNewData || newDataArrived()||forceAlwaysUpdate) {
				processData();
				
				//changed-Flag von Observable auf true setzen und observer notifien
				setChanged();
				notifyObservers();
				
				//wir haben Daten verarbeitet-> cycles hochzaehlen
				cycles++;
			}
		}
	} catch (ModulException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	//Thread kurz schlafen lege, damit andere drankommen
	if (Globals.sleepAfterWork){
		try {
			thisThread.sleep(Globals.moduleDelay);//our work is finished now, sleep for a while so the other modules can process the new data
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	else{
		//kein schlafen, dann wenigstens anderen Threads vorrang geben.
		thisThread.yield();
	}
}

public int getFps() {
	return fps;
}



/**
 * gibt OutputPin anhand des Namens zurück
 * @param name
 * @return
 * @throws ModulException
 */

public OutputPin getOutputPin(pinName name) throws ModulException{

	//alle pins durchgehen
	for (OutputPin pin:outputPins){
		
		//Name identisch->zurückgeben
		if (pin.getName()==name){
			return pin;
		}
	}
	//	Name nicht gefunden: Exception
	
	throw new ModulException("getOutputPin: Pin not found: " + name + " at Modul Type " + Typ + ",ID " + getId());

}

/**
 * sucht einen --> 2D OutputPin Anhand des Namens
 * @param name
 * @return
 * @throws ModulException
 */

public OutputPin2D getOutputPin2D(pinName name) throws ModulException{
	OutputPin ret=getOutputPin(name);
	if (!(ret instanceof OutputPin2D)) throw new ModulException("getInputPin2D: pin ist kein 2D-Pin " + "at Modul Type " + Typ + ",ID " + getId());
	return (OutputPin2D) ret;
}


/**
 * sucht einen --> 2D InputPin Anhand des Namens
 * @param name
 * @return
 * @throws ModulException
 */

public InputPin2D getInputPin2D(pinName name) throws ModulException{
	InputPin ret=getInputPin(name);
	if (!(ret instanceof InputPin2D)) throw new ModulException("getInputPin2D: pin ist kein 2D-Pin at Modul Type " + Typ + ",ID " + getId());
	return (InputPin2D) ret;
}

/**
 * gibt InputPin anhand des Namens zurück
 * @param name
 * @return
 * @throws ModulException
 */
public InputPin getInputPin(pinName name) throws ModulException{
	
	//alle Pins durchgehen
	for (InputPin pin:inputPins){
		//Name gleich-> zurueckgeben
		if (pin.getName()==name){
			return pin;
		}
	}
	//	Name nicht gefunden: Exception
	
	throw new ModulException("getInputPin: Pin not found: " + name + " at Modul Type " + Typ + ",ID " + getId());

}

/**
 * gibt Pin anhand des Namens zurück
 * @param name
 * @return
 * @throws ModulException
 */

public Pin getPin(pinName name) throws ModulException{
	
	//zuerst mit outputpin versuchen
	try{
		return getOutputPin(name);
	}
	
	//nicht gefunden? einfach ignorieren...
	catch (ModulException e){
		
	}
	
	//und mit den inputpins versuchen
	
	//wirft automatisch die gewünschte Exception wenn Pin nicht existiert
	return getInputPin(name);
	
	
	
}
	
/** 
 * Modul anhalten
 */
public void stopRunning(){
	isRunning=false;
}
	
/**
 * führt die Berechnungsarbeit durch. wird von run() inj jedem Threadzyklus gerufen
 * @throws ModulException
 */
protected abstract void processData() throws ModulException;


/**
 * den Modulthread starten
 */

public void startThread(){
	isRunning=true;
	thisThread=new Thread(this);
	thisThread.start();
}


/**
 * gibt den passenden Eintrag zum Modul zurück, um es in der XML-Datei zu speichern
 * @return
 */

public Element getSaveEntry(){
	
	Element modul=new Element("modul");

	modul.setAttribute("id",id+"");
	modul.setAttribute("type",Typ.toString());
	
	if (graphXPos!=0 || graphYPos!=0){
		modul.setAttribute("graphXPos",graphXPos+"");
		modul.setAttribute("graphYPos",graphYPos+"");
	}
	
	//falls es zusaetzlichen savestring gibt: alle reinschreiben
	additionalSaveAttribute(modul);

	
	//savestrings der Pins schreiben
	for (InputPin pin:inputPins){
		pin.logEntry(modul);
	}

	return modul;
}

/**
 * für die Unterklassen: falls zusätzliche Zeilen in die Speicherdatei hinzugefügt werden müssen, z.B. Konstruktorargumente
 * @return
 */
protected abstract void additionalSaveAttribute(Element e);



public static void setIdcounter(int idcounter) {
	Module.idcounter = idcounter;
}



public InputPin[] getInputPins() {
	return inputPins;
}

private boolean newDataArrived(){
	if (inputPins.length==0) return true;
	for (InputPin i:inputPins){
		if (i.isNewData()) {
			return true;
		}
	}
	
	return false;
}



public String getTyp() {
	if (Typ!=null)return Typ;
	//Typ wurde nicht gesetzt->unknown
	return "Unknown";
}



public OutputPin[] getOutputPins() {
	return outputPins;
}

/**
 * gibt an, ob eines der Folgemodule bereits die Daten am Ausgang abgeholt hat
 * @return
 */
public boolean dataPushed(){
	for (OutputPin p:getOutputPins()){
		if (!p.isDataReceived()){
			return false;
		}
	}
	return true;
}

/**
 * entfernt alle Verbindungen zu anderen Pins; wird vom JGraphTool gebraucht (damit mittlerweile geloeschte verbindungen nicht bestehen bleiben
 */

public void removeAllConnections(){
	for (InputPin i:inputPins){
		i.connectTo(null);
	}
}

}
