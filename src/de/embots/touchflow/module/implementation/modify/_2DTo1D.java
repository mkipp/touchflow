package de.embots.touchflow.module.implementation.modify;

import de.embots.touchflow.module.core.ModifyModule;
import de.embots.touchflow.module.core.pinName;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.InputPin2D;
import de.embots.touchflow.module.pin.OutputPin;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;


public class _2DTo1D extends ModifyModule {

	public _2DTo1D() {
		inputPins=new InputPin[1];
		inputPins[0]=new InputPin2D(pinName.IN, this);
		
		outputPins=new OutputPin[2];
		outputPins[0]=new OutputPin(pinName.X, this);
		outputPins[1]=new OutputPin(pinName.Y, this);
		

		
	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processData() throws ModulException {
		getOutputPin(pinName.X).writeData(getInputPin2D(pinName.IN).getData());
		getOutputPin(pinName.Y).writeData(getInputPin2D(pinName.IN).getData2());

	}
	@Override
	public String getDescription() {
		return "<html>Splits a Double2D port <b>IN</b> <br>into two Double ports <b>X and Y</b></html>";
	}

	@Override
	public String getModuleName() {
		
		return "2D-to-1D";
	}
}
