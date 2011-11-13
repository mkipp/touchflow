package de.embots.touchflow.module.implementation.modify;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.module.core.ModifyModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.OutputPin;

public class Not extends ModifyModule {

	@Override
	public String getModuleName() {
		// TODO Auto-generated method stub
		return "Not";
	}

	@Override
	protected void processData() throws ModulException {
		double in=getInputPin(PinName.IN).getData();
		OutputPin out=getOutputPin(PinName.OUT);
		
		
		if (in==1){
			out.writeData(0);
		}
		else{
			out.writeData(1);
		}

	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub

	}
	public Not(){
		inputPins=new InputPin[1];
		inputPins[0]=new InputPin(PinName.IN, this);
		
		outputPins=new OutputPin[1];
		outputPins[0]= new OutputPin(PinName.OUT, this);
	
	}
}
