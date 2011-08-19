package de.embots.touchflow.module.pin;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import de.embots.touchflow.module.core.Module;
import de.embots.touchflow.module.core.PinName;


public abstract class Pin {
	protected double data; //die Daten an sich
	
	double lastDrawData; //Daten die beim letzten Zeichnen vorlagen
	
	//Lock um den nebenläufigen Zugriff zu koordinieren
	protected Lock dataLock=new ReentrantLock();
	
	protected double lastData; //Daten des letzten Zyklus
	
	//Name des Pins
	protected PinName name;
	
	//defaultwert
	protected double defaultData;
	
	
	//das Modul zu dem der Pin gehört
	protected Module parentModul;
	
	public Module getParentModul() {
		return parentModul;
	}

	public void setParentModul(Module parentModul) {
		this.parentModul = parentModul;
	}

	public void setDefaultData(double defaultData) {
		this.defaultData = defaultData;
	}


	public boolean isZero(){
		dataLock.lock();
		boolean ret;
		ret=(data==0);
		dataLock.unlock();
		return ret;
	}
	
	public double getData(){
		return data;
	}
	
	/**
	 * schreibt die Daten in den Pin, inclusive Synchronisierung
	 * @param data
	 */
	public void writeData(double data){
		//sperren, damit nicht nebenläufig gelesen wird
		dataLock.lock();
		this.data=data;
		
		//danach wieder freigeben
		dataLock.unlock();
	}

	public PinName getName() {
		return name;
	}

	public Pin(PinName name, Module parentModul) {
		super();
		this.name = name;
		this.parentModul=parentModul;
	}
	public boolean isNewDrawData() {
		if (data==lastDrawData) return false;
		lastDrawData=data;
		return true;
	}
	
	public boolean is2DPin(){
		return (this instanceof InputPin2D || this instanceof OutputPin2D);
	}
}
