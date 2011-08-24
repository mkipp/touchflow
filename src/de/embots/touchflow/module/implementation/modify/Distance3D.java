package de.embots.touchflow.module.implementation.modify;

import de.embots.touchflow.module.core.ModifyModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.InputPin2D;
import de.embots.touchflow.module.pin.InputPin3D;
import de.embots.touchflow.module.pin.OutputPin;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;


public class Distance3D extends ModifyModule {

	@Override
	protected void processData() throws ModulException {
		
		double x1= getInputPin3D(PinName.P1).getData();
		double x2=getInputPin3D(PinName.P2).getData();
		double y1=getInputPin3D(PinName.P1).getData2();
		double y2=getInputPin3D(PinName.P2).getData2();
		double z1=getInputPin3D(PinName.P1).getData3();
		double z2=getInputPin3D(PinName.P2).getData3();
		
		//euklidischer Abstand
		double dist= Math.sqrt(Math.pow((x1-x2),2)+Math.pow((y1-y2),2)+Math.pow((z1-z2),2));
		
		getOutputPin(PinName.DISTANCE).writeData(dist);

	}
	@Override
	public String getDescription() {
		return "<html>Computes the distance between <b>P1</b> and <b>P2</b></html>";
	}
	public Distance3D() throws ModulException{
		inputPins=new InputPin[2];
		
		inputPins[0]=new InputPin3D(PinName.P1,this);
		inputPins[1]=new InputPin3D(PinName.P2,this);
		
		outputPins=new OutputPin[1];
		
		outputPins[0]=new OutputPin(PinName.DISTANCE,this);
		
	}



	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getModuleName() {
		
		return "Distance 3D";
	}
	
}
