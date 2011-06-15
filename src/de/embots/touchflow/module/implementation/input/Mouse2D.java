package de.embots.touchflow.module.implementation.input;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.gui.inspector.InspectorComponent;

import java.awt.MouseInfo;
import java.awt.Point;

import de.embots.touchflow.module.core.InputModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.OutputPin;
import de.embots.touchflow.module.pin.OutputPin2D;

import org.jdom.Element;



public class Mouse2D extends InputModule{

	InspectorComponent attachedComponent;
	
	public void attachComponent(InspectorComponent component){
		attachedComponent=component;
	}
		
	@Override
	protected void processData() throws ModulException {
		Point pos=MouseInfo.getPointerInfo().getLocation();
		
		getOutputPin2D(PinName.POSITION).writeData(pos.x);
		getOutputPin2D(PinName.POSITION).writeData2(pos.y);
		
		if (attachedComponent!= null){
			getOutputPin(PinName.SCROLL).writeData(attachedComponent.getScrollNumber());
		}
	}

	public Mouse2D() throws ModulException{
		super();
		
		outputPins=new OutputPin[2];
		outputPins[0]=new OutputPin2D(PinName.POSITION,this);
		outputPins[1]=new OutputPin(PinName.SCROLL,this);
		
		addPortMapEntry(4, PinName.POSITION);
		addPortMapEntry(7, PinName.SCROLL);

	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getDescription() {
		return "<html>Outputs the mouse position (<b>POSITION</b>)<br>and scroll wheel orientation (<b>SCROLL</b>)</html>";
	}

	@Override
	public String getModuleName() {
		
		return "Mouse 2D";
	}

}
