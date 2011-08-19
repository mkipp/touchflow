package de.embots.touchflow.module.pin;

import de.embots.touchflow.module.core.Module;
import de.embots.touchflow.module.core.PinName;

public class OutputPin2D extends OutputPin {
	@Override
	public boolean isZero() {
		// TODO Auto-generated method stub
		return super.isZero() && (data2==0);
	}

	public double data2;
	private double lastDrawData2;
	
	public OutputPin2D(PinName name, Module parentModul) {
		super(name, parentModul);
		// TODO Auto-generated constructor stub
	}

	public void writeData2(double wert){
		
		dataLock.lock();
		dataReceived=false;
		data2=wert;
		dataLock.unlock();
	}

	public double getData2() {
		return data2;
	}
	
	public boolean isNewDrawData() {
		if (data==lastDrawData && data2==lastDrawData2) return false;
		lastDrawData=data;
		lastDrawData2=data2;
		return true;
	}
}
