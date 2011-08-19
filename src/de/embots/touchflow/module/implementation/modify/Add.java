package de.embots.touchflow.module.implementation.modify;

import de.embots.touchflow.module.core.ModifyModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.OutputPin;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;

public class Add extends ModifyModule {

	public Add(){
		inputPins=new InputPin[2];
		inputPins[0]=new InputPin(PinName.IN, this);
		inputPins[1]=new InputPin(PinName.AMOUNT, this);
		
		outputPins=new OutputPin[1];
		outputPins[0]=new OutputPin(PinName.OUT,this);

		
	}
	
	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processData() throws ModulException {
		double in,sh;
		
		in=getInputPin(PinName.IN).getData();
		sh=getInputPin(PinName.AMOUNT).getData();

		getOutputPin(PinName.OUT).writeData(in + sh);
		
	}
	
	@Override
	public String getDescription() {
		return "<html>Computes the sum of <b>IN</b> by <b>AMOUNT</b></html>";
	}

	@Override
	public String getModuleName() {
		
		return "Add";
	}

}
