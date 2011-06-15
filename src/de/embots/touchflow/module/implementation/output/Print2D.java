package de.embots.touchflow.module.implementation.output;

import de.embots.touchflow.module.core.OutputModule;
import de.embots.touchflow.module.core.pinName;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.InputPin2D;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;


public class Print2D extends OutputModule {
	
	public boolean printOnlyDifferent=true;
	private double oldVal;

	@Override
	protected void processData() throws ModulException {
		double newVal=getInputPin2D(pinName.IN).getData();
		double newVal2=getInputPin2D(pinName.IN).getData2();

		
		
		if (!printOnlyDifferent || oldVal != newVal) System.out.println(newVal + ";" + newVal2);

		oldVal=newVal;
		
	}
	
	public Print2D(){
		inputPins=new InputPin[1];
		inputPins[0]=new InputPin2D(pinName.IN,this);

	}


	@Override
	public String getDescription() {
		return "<html>Outputs the values of <b>IN</b> on stdout, using ';' as a seperator</html>";
	}
	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getModuleName() {
		
		return "Print 2D";
	}

}
