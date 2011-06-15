package module.implementation.input;

import module.Globals;
import module.core.InputModule;
import module.core.pinName;
import module.pin.OutputPin;

import org.jdom.Element;

import exceptions.ModulException;

public class Pitch extends InputModule {

	public Pitch(){
		outputPins=new OutputPin[1];
		outputPins[0]=new OutputPin(pinName.OUT, this);

		PDCommunicator.init( this, Globals.PDPitchPort);
	}
	public String getDescription() {
		return "<html>Outputs the voice pitch<br><br><i>Note:</i><br>Needs PureData with corresponding bridge node running!</html>";
	}
	@Override
	protected void processData() throws ModulException {
		getOutputPin(pinName.OUT).writeData(PDCommunicator.getLastNum(Globals.PDPitchPort));
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
