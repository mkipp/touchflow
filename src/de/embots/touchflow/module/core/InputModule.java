package de.embots.touchflow.module.core;

import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.exceptions.ModulException;

/**
 * Modul, das ein Eingabegerät repräsentiert
 * @author cyre
 *
 */
public abstract class InputModule extends Module {

	protected abstract void processData() throws ModulException;

	protected InputModule(){
		showInInspector=true;
		inputPins=new InputPin[0]; // no InputPins
	}
}
