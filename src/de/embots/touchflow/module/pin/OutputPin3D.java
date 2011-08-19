package de.embots.touchflow.module.pin;

import de.embots.touchflow.module.core.Module;
import de.embots.touchflow.module.core.PinName;

public class OutputPin3D extends OutputPin2D {
	@Override
	public boolean isZero() {
		// TODO Auto-generated method stub
		return super.isZero() && (data3==0);
	}

	public double data3;
	private double lastDrawData3;
	
	public OutputPin3D(PinName name, Module parentModul) {
		super(name, parentModul);
		// TODO Auto-generated constructor stub
	}

	public void writeData3(double wert){
		
		dataLock.lock();
		dataReceived=false;
		data3=wert;
		dataLock.unlock();
	}

	public double getData3() {
		return data3;
	}
	
	public boolean isNewDrawData() {
		if (data==lastDrawData && data2==lastDrawData2 && data3==lastDrawData3) return false;
		lastDrawData=data;
		lastDrawData2=data2;
		lastDrawData3=data3;
		return true;
	}
}
