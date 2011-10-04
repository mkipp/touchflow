package de.embots.touchflow.module.implementation.modify;

import javax.vecmath.Vector3d;

import de.embots.touchflow.module.core.ModifyModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.InputPin2D;
import de.embots.touchflow.module.pin.InputPin3D;
import de.embots.touchflow.module.pin.OutputPin;
import de.embots.touchflow.module.pin.OutputPin2D;
import de.embots.touchflow.module.pin.OutputPin3D;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;


public class Multiply3D extends ModifyModule {

	public Multiply3D() {
		inputPins=new InputPin[2];
		inputPins[0]=new InputPin3D(PinName.IN,this);
		inputPins[1]=new InputPin(PinName.SCALE,this);
		inputPins[1].setDefaultData(1); //default scale: 1
		
		outputPins=new OutputPin[1];
		outputPins[0]=new OutputPin3D(PinName.OUT,this);

	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub

	}
	@Override
	public String getDescription() {
		return "<html>Computes the 3D-multiplication of <b>IN</b> by <b>SCALE</b></html>";
	}
	@Override
	protected void processData() throws ModulException {
		InputPin3D in=getInputPin3D(PinName.IN);
		OutputPin3D out=getOutputPin3D(PinName.OUT);
		

		
		double scaler=getInputPin(PinName.SCALE).getData();
		
		Vector3d inVector=in.getVector();
		
		inVector.scale(scaler);

		out.writeDataVector(inVector);
		
	}

	@Override
	public String getModuleName() {
		
		return "Multiply 3D";
	}

}
