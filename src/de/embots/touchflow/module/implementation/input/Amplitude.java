package de.embots.touchflow.module.implementation.input;

import de.embots.touchflow.module.Globals;
import de.embots.touchflow.module.core.InputModule;
import de.embots.touchflow.module.core.pinName;
import de.embots.touchflow.module.pin.OutputPin;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;

public class Amplitude extends InputModule {

	public Amplitude(){
		outputPins=new OutputPin[1];
		outputPins[0]=new OutputPin(pinName.OUT, this);
		PDCommunicator.init( this, Globals.PDAmplitudePort);
	}
	@Override
	public String getDescription() {
		return "<html>Outputs the voice amplitude<br><br><i>Note:</i><br>Needs PureData with corresponding bridge node running!</html>";
	}
	@Override
	protected void processData() throws ModulException {
		getOutputPin(pinName.OUT).writeData(PDCommunicator.getLastNum(Globals.PDAmplitudePort));
	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void stopRunning() {
		// TODO Auto-generated method stub
		super.stopRunning();
		PDCommunicator.stopListening(this, Globals.PDAmplitudePort);
	}
	@Override
	public String getModuleName() {
		// TODO Auto-generated method stub
		return "Amplitude";
	}

}
