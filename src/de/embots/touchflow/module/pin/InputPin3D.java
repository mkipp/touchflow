package de.embots.touchflow.module.pin;

import de.embots.touchflow.module.core.Module;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.exceptions.PinException;


public class InputPin3D extends InputPin2D{
	
	@Override
	public boolean isZero() {
		// TODO Auto-generated method stub
		return super.isZero() && (data3==0);
	}
	private double data3;
	private double lastData3;
	private double lastDrawData3;
	
	public void setDefaultData3(double defaultData3) {
		this.defaultData3 = defaultData3;
	}
	private double defaultData3;
	
	public double getData3() {
		return data3;
	}


	
	public InputPin3D(PinName name, Module parentModul) {
		super(name, parentModul);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void pullData() throws PinException {
		// TODO Auto-generated method stub
		super.pullData();
		
		//bei nicht verbundenem Pin nichts machen
		
		if (connectedPin==null) {
			data3=defaultData3;
			lastData3=data3;
			return;
		}
		
		lastData3=data3;
		if (!(connectedPin instanceof OutputPin3D))throw new PinException("3D Pin connected to non-3D-Pin (" + name + "(3D) to " + connectedPin.name);
		
		//sperren, schreiben,entsperren um mnebenläufiges lesen/schreiben zu verhindern
		connectedPin.dataLock.lock();
		
		OutputPin3D cpin=(OutputPin3D) connectedPin;
		data3=cpin.data3;
		connectedPin.dataLock.unlock();
		
	}
	public boolean isNewData(){
		if (data3==lastData3 && data2==lastData2 && data==lastData) return false;
		return true;
	}
	
	public boolean isNewDrawData() {
		if (data==lastDrawData && data2==lastDrawData2 && data3==lastDrawData3) return false;
		lastDrawData=data;
		lastDrawData2=data2;
		lastDrawData3=data3;
		return true;
	}
}
