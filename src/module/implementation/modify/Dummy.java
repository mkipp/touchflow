package module.implementation.modify;

import module.core.ModifyModule;
import module.core.pinName;
import module.pin.InputPin;
import module.pin.OutputPin;

import org.jdom.Element;

import exceptions.ModulException;


public class Dummy extends ModifyModule {

	@Override
	protected void processData() throws ModulException {
		getOutputPin(pinName.OUT).writeData(getInputPin(pinName.IN).getData());

	}
	
	public Dummy(){
		inputPins=new InputPin[1];
		inputPins[0]=new InputPin(pinName.IN,this);
		outputPins=new OutputPin[1];
		outputPins[0]=new OutputPin(pinName.OUT,this);
		

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
