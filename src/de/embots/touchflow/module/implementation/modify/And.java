package de.embots.touchflow.module.implementation.modify;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.module.core.ModifyModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.OutputPin;

public class And extends ModifyModule {

	@Override
	public String getModuleName() {
		// TODO Auto-generated method stub
		return "And";
	}

	@Override
	protected void processData() throws ModulException {
		if (getInputPin(PinName.IN1).getData()==1 && getInputPin(PinName.IN2).getData()==1){
			getOutputPin(PinName.OUT).writeData(1);
		}
		else{
			getOutputPin(PinName.OUT).writeData(0);
		}

	}

	@Override
	public String getDescription() {
		return "<html>Returns a logical AND between the two input values.<br><b>OUT</b> is 1 iff <b>IN1</b> and <b>IN2</b> are 1, 0 otherwise.</html>";
	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub

	}
	
	public And(){
		inputPins=new InputPin[2];
		inputPins[0]=new InputPin(PinName.IN1,this);
		inputPins[1]=new InputPin(PinName.IN2,this);
		
		outputPins=new OutputPin[1];
		outputPins[0]=new OutputPin(PinName.OUT, this);
	}

}
