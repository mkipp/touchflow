package module.core;

import module.pin.OutputPin;

public abstract class OutputModule extends Module {

	protected OutputModule(){
		showInInspector=true;
		outputPins=new OutputPin[0]; // no InputPins
	}
	

}
