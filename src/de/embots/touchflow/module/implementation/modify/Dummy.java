package de.embots.touchflow.module.implementation.modify;

import de.embots.touchflow.module.core.ModifyModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.OutputPin;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;


public class Dummy extends ModifyModule {

	@Override
	protected void processData() throws ModulException {
		getOutputPin(PinName.OUT).writeData(getInputPin(PinName.IN).getData());

	}
	
	public Dummy(){
		inputPins=new InputPin[1];
		inputPins[0]=new InputPin(PinName.IN,this);
		outputPins=new OutputPin[1];
		outputPins[0]=new OutputPin(PinName.OUT,this);
		

	}



	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getModuleName() {
		
		return "Dummy";
	}

}
