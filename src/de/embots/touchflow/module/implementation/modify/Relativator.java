package de.embots.touchflow.module.implementation.modify;

import de.embots.touchflow.module.core.ModifyModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.OutputPin;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;


public class Relativator extends ModifyModule {
	double lastData;

	public Relativator() throws ModulException {
		inputPins=new InputPin[1];
		inputPins[0]=new InputPin(PinName.IN, this);
		outputPins=new OutputPin[1];
		outputPins[0]=new OutputPin(PinName.OUT, this);

		addPortMapEntry(0, PinName.IN);
		addPortMapEntry(4, PinName.OUT);
	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processData() throws ModulException {
		double actual=getInputPin(PinName.IN).getData();
		getOutputPin(PinName.OUT).writeData(lastData-actual);
		lastData=actual;
	}

	@Override
	public String getDescription() {
		return "<html>Computes the difference of <b>IN</b> to the last frame</html>";
	}

	@Override
	public String getModuleName() {
		
		return "Relativator";
	}
}
