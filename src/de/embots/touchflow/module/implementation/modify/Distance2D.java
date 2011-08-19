package de.embots.touchflow.module.implementation.modify;

import de.embots.touchflow.module.core.ModifyModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.InputPin2D;
import de.embots.touchflow.module.pin.OutputPin;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;


public class Distance2D extends ModifyModule {

	@Override
	protected void processData() throws ModulException {
		
		double x1= getInputPin2D(PinName.P1).getData();
		double x2=getInputPin2D(PinName.P2).getData();
		double y1=getInputPin2D(PinName.P1).getData2();
		double y2=getInputPin2D(PinName.P2).getData2();
		
		//euklidischer Abstand
		double dist= Math.sqrt(Math.pow((x1-x2),2)+Math.pow((y1-y2),2));
		
		getOutputPin(PinName.DISTANCE).writeData(dist);

	}
	@Override
	public String getDescription() {
		return "<html>Computes the distance between <b>P1</b> and <b>P2</b></html>";
	}
	public Distance2D() throws ModulException{
		inputPins=new InputPin[2];
		
		inputPins[0]=new InputPin2D(PinName.P1,this);
		inputPins[1]=new InputPin2D(PinName.P2,this);
		
		outputPins=new OutputPin[1];
		
		outputPins[0]=new OutputPin(PinName.DISTANCE,this);
		

		
		addPortMapEntry(0, PinName.P1);
		addPortMapEntry(3,PinName.P2);
		addPortMapEntry(7, PinName.DISTANCE);
	}



	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getModuleName() {
		
		return "Distance 2D";
	}
	
}
