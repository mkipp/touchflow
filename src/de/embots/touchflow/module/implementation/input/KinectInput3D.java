package de.embots.touchflow.module.implementation.input;

import java.io.InputStream;
import java.net.Socket;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.module.core.InputModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.OutputPin;
import de.embots.touchflow.module.pin.OutputPin3D;
import de.embots.touchflow.util.KinectPoint;

public class KinectInput3D extends InputModule {

	
	public KinectInput3D(){
		outputPins=new OutputPin3D[19];
		
		for (int i=0;i<outputPins.length;i++){
			outputPins[i]=new OutputPin3D(PinName.values()[PinName.HIP_CENTER.ordinal()+i],this);
		}
				
		
	}
	
	@Override
	public String getDescription() {
		return "<html>Input from a Kinect device.<br><br>Needs <i>KinectSender</i> to be started!</html>";
	}
	
	@Override
	protected void processData() throws ModulException {
		for (int i=0; i<outputPins.length;i++){
			((OutputPin3D)outputPins[i]).writeDataVector(KinectServer.getPosition(i));
		}
	}

	@Override
	public String getModuleName() {
		return "KinectInput3D";
	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub

	}

}
