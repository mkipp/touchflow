package de.embots.touchflow.module.implementation.input;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.module.core.InputModule;
import de.embots.touchflow.util.KinectPoint;

public class KinectInput extends InputModule {

	KinectPoint leftHandPos=new KinectPoint();
	KinectPoint rightHandPos=new KinectPoint();
	
	@Override
	protected void processData() throws ModulException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getModuleName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub

	}

}
