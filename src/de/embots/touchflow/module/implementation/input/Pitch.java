package de.embots.touchflow.module.implementation.input;

import de.embots.touchflow.module.Globals;
import de.embots.touchflow.module.core.InputModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.OutputPin;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;

public class Pitch extends InputModule {

	public Pitch(){
		outputPins=new OutputPin[1];
		outputPins[0]=new OutputPin(PinName.OUT, this);

		PDCommunicator.init( this, Globals.PDPitchPort);
	}
	public String getDescription() {
		return "<html>Outputs the voice pitch<br><br><i>Note:</i><br>Needs PureData with corresponding bridge node running!</html>";
	}
	@Override
	protected void processData() throws ModulException {
		getOutputPin(PinName.OUT).writeData(PDCommunicator.getLastNum(Globals.PDPitchPort));
	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void stopRunning() {
		// TODO Auto-generated method stub
		super.stopRunning();
		PDCommunicator.stopListening(this, Globals.PDPitchPort);
	}
	@Override
	public String getModuleName() {
		
		return "Pitch";
	}

}
