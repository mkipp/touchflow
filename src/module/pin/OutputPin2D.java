package module.pin;

import module.core.Module;
import module.core.pinName;

public class OutputPin2D extends OutputPin {
	@Override
	public boolean isZero() {
		// TODO Auto-generated method stub
		return super.isZero() && (data2==0);
	}

	public double data2;
	private double lastDrawData2;
	
	public OutputPin2D(pinName name, Module parentModul) {
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
