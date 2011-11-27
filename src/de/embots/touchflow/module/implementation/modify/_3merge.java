package de.embots.touchflow.module.implementation.modify;

import javax.vecmath.Vector3d;

import de.embots.touchflow.module.core.ModifyModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.InputPin2D;
import de.embots.touchflow.module.pin.OutputPin;
import de.embots.touchflow.module.pin.OutputPin3D;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;


public class _3merge extends ModifyModule {

	public _3merge() {
		inputPins=new InputPin[2];
		inputPins[0]=new InputPin2D(PinName.IN, this);
		inputPins[1]=new InputPin(PinName.IN2, this);
		
		outputPins=new OutputPin[1];
		outputPins[0]=new OutputPin3D(PinName.OUT, this);
				
	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processData() throws ModulException {
		InputPin2D in1=getInputPin2D(PinName.IN);
		OutputPin3D out=getOutputPin3D(PinName.OUT);
        Vector3d outv=new Vector3d(in1.getData(), in1.getData2(), getInputPin(PinName.IN2).getData());		
 
        out.writeDataVector(outv);
	}
	@Override
	public String getDescription() {
		return "<html>Merges a Double2D port <b>IN</b> and a Double port <b>IN2</b> <br>into one Double3D port <b>OUT</b></html>";
	}

	@Override
	public String getModuleName() {
		
		return "2D+1D-to-3D";
	}
}
