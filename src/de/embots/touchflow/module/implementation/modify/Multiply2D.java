package de.embots.touchflow.module.implementation.modify;

import de.embots.touchflow.module.core.ModifyModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.InputPin2D;
import de.embots.touchflow.module.pin.OutputPin;
import de.embots.touchflow.module.pin.OutputPin2D;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;


public class Multiply2D extends ModifyModule {

	public Multiply2D() {
		inputPins=new InputPin[2];
		inputPins[0]=new InputPin2D(PinName.IN,this);
		inputPins[1]=new InputPin(PinName.SCALE,this);
		inputPins[1].setDefaultData(1); //default scale: 1
		
		outputPins=new OutputPin[1];
		outputPins[0]=new OutputPin2D(PinName.OUT,this);

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
		InputPin2D in=getInputPin2D(PinName.IN);
		OutputPin2D out=getOutputPin2D(PinName.OUT);
		
		double inData=in.getData();
		double inData2=in.getData2();
		
		double scaler=getInputPin(PinName.SCALE).getData();
		inData *=scaler;
		inData2 *=scaler;

		out.writeData(inData);
		out.writeData2(inData2);
		
	}

	@Override
	public String getModuleName() {
		
		return "Multiply 2D";
	}

}
