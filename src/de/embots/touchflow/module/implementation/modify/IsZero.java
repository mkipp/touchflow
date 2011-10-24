package de.embots.touchflow.module.implementation.modify;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.module.core.ModifyModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.InputPin2D;
import de.embots.touchflow.module.pin.OutputPin;

public class IsZero extends ModifyModule {

	@Override
	public String getModuleName() {
		// TODO Auto-generated method stub
		return "isZero";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "<html>Returns whether the 2D-Input at <b>IN</b> is zero or not</html>";
	}

	@Override
	protected void processData() throws ModulException {
		InputPin2D pinIn=getInputPin2D(PinName.IN);
		if (pinIn.getData()==0 && pinIn.getData2()==0){
			getOutputPin(PinName.OUT).writeData(1);
		}
		else{
			getOutputPin(PinName.OUT).writeData(0);
		}

	}

	@Override
	protected void additionalSaveAttribute(Element e) {

	}
	
	public IsZero(){
		inputPins=new InputPin[1];
		inputPins[0]=new InputPin2D(PinName.IN, this);
		
		outputPins=new OutputPin[1];
		outputPins[0]=new OutputPin(PinName.OUT, this);
		
		
	}

}
