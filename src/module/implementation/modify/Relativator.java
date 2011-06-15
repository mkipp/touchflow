package module.implementation.modify;

import module.core.ModifyModule;
import module.core.pinName;
import module.pin.InputPin;
import module.pin.OutputPin;

import org.jdom.Element;

import exceptions.ModulException;


public class Relativator extends ModifyModule {
	double lastData;

	public Relativator() throws ModulException {
		inputPins=new InputPin[1];
		inputPins[0]=new InputPin(pinName.IN, this);
		outputPins=new OutputPin[1];
		outputPins[0]=new OutputPin(pinName.OUT, this);

		addPortMapEntry(0, pinName.IN);
		addPortMapEntry(4, pinName.OUT);
	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processData() throws ModulException {
		double actual=getInputPin(pinName.IN).getData();
		getOutputPin(pinName.OUT).writeData(lastData-actual);
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
