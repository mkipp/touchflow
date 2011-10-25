package de.embots.touchflow.module.implementation.modify;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.module.core.ModifyModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.InputPin2D;
import de.embots.touchflow.module.pin.OutputPin;
import de.embots.touchflow.module.pin.OutputPin2D;

public class LastTap extends ModifyModule {
	double firstX, firstY;
	boolean wasZero=true;
	
	@Override
	public String getModuleName() {
		// TODO Auto-generated method stub
		return "LastTap";
	}

	@Override
	protected void processData() throws ModulException {
		double inX=getInputPin2D(PinName.IN).getData();
		double inY=getInputPin2D(PinName.IN).getData2();

		if (inX==0 && inY==0) {
			wasZero=true;
			return; //finger not placed-> nothing to do
		}
		
		if (wasZero){
			firstX=inX;
			firstY=inY;
		}
		
		getOutputPin(PinName.OUT).writeData(inX-firstX);
		getOutputPin2D(PinName.OUT).writeData2(inY-firstY);
		
	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "outputs the relative Vector since the finger was put on the screen";
	}
	public LastTap(){
		inputPins=new InputPin[1];
		inputPins[0]=new InputPin2D(PinName.IN, this);
		
		outputPins=new OutputPin[1];
		outputPins[0]=new OutputPin2D(PinName.OUT,this);
	}
}
