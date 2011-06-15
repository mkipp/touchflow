package gui.touchflow.components;

import module.pin.InputPin;
import module.pin.OutputPin;
import module.pin.Pin;

import org.jgraph.graph.DefaultPort;


public class PinPort extends DefaultPort {
private Pin pin;

public PinPort(Pin pin) {
	super();
	this.pin = pin;
}

public Pin getPin() {
	return pin;
}
public boolean isCompatibleWith(PinPort p2){
	Pin anderer=p2.getPin();
	
	if (pin.is2DPin()!= anderer.is2DPin() ) return false;
	if (pin instanceof InputPin && anderer instanceof InputPin) return false;
	if (pin instanceof OutputPin && anderer instanceof OutputPin) return false;
	return true;
}

}
