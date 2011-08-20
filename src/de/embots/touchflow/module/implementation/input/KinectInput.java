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

public class KinectInput extends InputModule {

	
	public KinectInput(){
		outputPins=new OutputPin3D[2];
		
		outputPins[0]=new OutputPin3D(PinName.LEFT_HAND, this);
		outputPins[1]=new OutputPin3D(PinName.RIGHT_HAND, this);
		
		
	}
	
	@Override
	protected void processData() throws ModulException {
		OutputPin3D leftHand=getOutputPin3D(PinName.LEFT_HAND);
		OutputPin3D rightHand=getOutputPin3D(PinName.RIGHT_HAND);
		
		leftHand.writeData(KinectServer.getLeftHandPos().x);
		leftHand.writeData2(KinectServer.getLeftHandPos().y);
		leftHand.writeData3(KinectServer.getLeftHandPos().z);
		
		rightHand.writeData(KinectServer.getRightHandPos().x);
		rightHand.writeData2(KinectServer.getRightHandPos().y);
		rightHand.writeData3(KinectServer.getRightHandPos().z);
	}

	@Override
	public String getModuleName() {
		return "KinectInput";
	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub

	}

}
