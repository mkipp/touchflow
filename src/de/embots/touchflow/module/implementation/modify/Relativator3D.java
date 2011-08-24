package de.embots.touchflow.module.implementation.modify;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.module.core.ModifyModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.InputPin3D;
import de.embots.touchflow.module.pin.OutputPin;
import de.embots.touchflow.module.pin.OutputPin3D;
import de.embots.touchflow.util.KinectPoint;

public class Relativator3D extends ModifyModule {
	
	KinectPoint referencePoint=new KinectPoint();
	
	@Override
	public String getDescription() {
		return "<html>Outputs a difference vector to the last saved state.<br><br>Use <b>RESET</b> to set point of reference</html>";
	}
	
	@Override
	public String getModuleName() {
		// TODO Auto-generated method stub
		return "Relativator3D";
	}

	@Override
	protected void processData() throws ModulException {
		double x=getInputPin(PinName.IN).getData();
		double y=getInputPin2D(PinName.IN).getData2();
		double z=getInputPin3D(PinName.IN).getData3();
		
		double store=getInputPin(PinName.RESET).getData();
		
		if (store==1){
			referencePoint.x=x;
			referencePoint.y=y;
			referencePoint.z=z;
		}
		
		getOutputPin3D(PinName.OUT).writeData(x-referencePoint.x);
		getOutputPin3D(PinName.OUT).writeData2(y-referencePoint.y);
		getOutputPin3D(PinName.OUT).writeData3(z-referencePoint.z);
		
		
	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub

	}

	public Relativator3D() {
		super();
		inputPins=new InputPin[2];
		
		inputPins[0]=new InputPin3D(PinName.IN, this);
		inputPins[1]=new InputPin(PinName.RESET, this);
		
		outputPins=new OutputPin[1];
		
		outputPins[0]=new OutputPin3D(PinName.OUT, this);
	}



	
}
