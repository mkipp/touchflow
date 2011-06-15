package module.pin;

import module.core.Module;
import module.core.pinName;

public class OutputPin extends Pin{

	protected boolean dataReceived;
	
	public boolean isDataReceived() {
		return dataReceived;
	}

	@Override
	public void writeData(double data) {
		super.writeData(data);
		
		dataLock.lock();
			dataReceived=false;
		dataLock.unlock();
	}

	public OutputPin(pinName name, Module parentModul) {
		super(name, parentModul);
	}


	
}
