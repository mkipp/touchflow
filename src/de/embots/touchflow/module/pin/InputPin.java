package de.embots.touchflow.module.pin;

import de.embots.touchflow.module.core.Module;
import de.embots.touchflow.module.core.pinName;

import org.jdom.Element;

import de.embots.touchflow.exceptions.PinException;


public class InputPin extends Pin {
	
	protected double lastData; //Daten des letzten Zyklus
	
	public InputPin(pinName name, Module parentModul) {
		super(name, parentModul);
	}

	OutputPin connectedPin;
	//angeschlossener Pin
	
	/**
	 * Daten vom angeschlossenen Pin ziehen
	 * @throws PinException 
	 */
	public void pullData() throws PinException{
		lastData=data;
		//Pin nicht verbunden: kommt 0 raus
		if (connectedPin==null){
			data=defaultData;
			lastData=data;
			return;
		}
		
		//sperren, schreiben,entsperren um mnebenläufiges lesen/schreiben zu verhindern
		connectedPin.dataLock.lock();
		connectedPin.dataReceived=true;
		data=connectedPin.data;
		connectedPin.dataLock.unlock();
	}
	
	/**
	 * mit einem OutputPin verbinden
	 * 
	 * @param pin
	 */
	public void connectTo(OutputPin pin){
		connectedPin=pin;
	}
	
	public OutputPin getConnectedPin(){
		return connectedPin;
	}
	
	/**
	* gibt die entsprechende Zeile fürs Schreiben in eine XML-Datei zurück
	 * @param modul 
	 * @return
	 */
	
	public void logEntry(Element modul){
		if (connectedPin!=null) {
			modul.setAttribute(name.toString() + "ModulID",connectedPin.parentModul.getId()+"");
			modul.setAttribute(name.toString() + "PinName",connectedPin.getName().toString());
		}
	}
	
	public boolean isNewData(){
		if (data==lastData) return false;
		return true;
	}
}
