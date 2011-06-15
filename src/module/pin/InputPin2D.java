package module.pin;

import module.core.Module;
import module.core.pinName;
import exceptions.PinException;


public class InputPin2D extends InputPin{
	
	@Override
	public boolean isZero() {
		// TODO Auto-generated method stub
		return super.isZero() && (data2==0);
	}
	private double data2;
	private double lastData2;
	private double lastDrawData2;
	
	public void setDefaultData2(double defaultData2) {
		this.defaultData2 = defaultData2;
	}
	private double defaultData2;
	
	public double getData2() {
		return data2;
	}


	
	public InputPin2D(pinName name, Module parentModul) {
		super(name, parentModul);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void pullData() throws PinException {
		// TODO Auto-generated method stub
		super.pullData();
		
		//bei nicht verbundenem Pin nichts machen
		
		if (connectedPin==null) {
			data2=defaultData2;
			lastData2=data2;
			return;
		}
		
		lastData2=data2;
		if (!(connectedPin instanceof OutputPin2D))throw new PinException("2D Pin connected to 1D-Pin (" + name + "(2D) to " + connectedPin.name + "(1D)");
		
		//sperren, schreiben,entsperren um mnebenläufiges lesen/schreiben zu verhindern
		connectedPin.dataLock.lock();
		
		OutputPin2D cpin=(OutputPin2D) connectedPin;
		data2=cpin.data2;
		connectedPin.dataLock.unlock();
		
	}
	public boolean isNewData(){
		if (data2==lastData2 && data==lastData) return false;
		return true;
	}
	
	public boolean isNewDrawData() {
		if (data==lastDrawData && data2==lastDrawData2) return false;
		lastDrawData=data;
		lastDrawData2=data2;
		return true;
	}
}
