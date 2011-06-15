package de.embots.touchflow.module.core;

import de.embots.touchflow.module.pin.OutputPin;

public abstract class OutputModule extends Module {

	protected OutputModule(){
		showInInspector=true;
		outputPins=new OutputPin[0]; // no InputPins
	}
	

}
