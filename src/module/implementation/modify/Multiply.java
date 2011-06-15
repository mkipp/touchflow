package module.implementation.modify;

import module.core.ModifyModule;
import module.core.pinName;
import module.pin.InputPin;
import module.pin.OutputPin;

import org.jdom.Element;

import exceptions.ModulException;


public class Multiply extends ModifyModule {

	public Multiply() {
		inputPins=new InputPin[2];
		inputPins[0]=new InputPin(pinName.IN,this);
		inputPins[1]=new InputPin(pinName.SCALE,this);
		inputPins[1].setDefaultData(1); //default scale: 1
		
		outputPins=new OutputPin[1];
		outputPins[0]=new OutputPin(pinName.OUT,this);

	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getDescription() {
		return "<html>Computes the multiplication of <b>IN</b> by <b>SCALE</b></html>";
	}
	
	@Override
	protected void processData() throws ModulException {
		double inData=getInputPin(pinName.IN).getData();
		double scaler=getInputPin(pinName.SCALE).getData();
		inData *=scaler;

		getOutputPin(pinName.OUT).writeData(inData);
	}

	@Override
	public String getModuleName() {
		
		return "Multiply";
	}

}
