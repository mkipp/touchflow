package de.embots.touchflow.module.implementation.input;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.gui.inspector.InspectorComponent;

import java.awt.MouseInfo;
import java.awt.Point;

import de.embots.touchflow.module.core.InputModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.OutputPin;
import de.embots.touchflow.module.pin.OutputPin2D;
import de.embots.touchflow.module.pin.OutputPin3D;

import org.jdom.Element;



public class KinectSimulator3D extends InputModule{

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
			getOutputPin3D(PinName.POSITION).writeData3(attachedComponent.getScrollNumber()*10);
		}
	}

	public KinectSimulator3D() throws ModulException{
		super();
		
		outputPins=new OutputPin[1];
		outputPins[0]=new OutputPin3D(PinName.POSITION,this);


	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getDescription() {
		return "<html>Interprets the mouse position and scroll state as a 3D point (<b>POSITION</b>)<br><br>Useful for simulating a Kinect hand position.</html>";
	}

	@Override
	public String getModuleName() {
		
		return "KinectSimulator3D";
	}

}
