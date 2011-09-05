package de.embots.touchflow.module.implementation.modify;

import javax.vecmath.Vector3d;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.module.core.ModifyModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.InputPin3D;
import de.embots.touchflow.module.pin.OutputPin;
import de.embots.touchflow.module.pin.OutputPin3D;

public class CircleProjection extends ModifyModule {

	private Vector3d refMiddle=new Vector3d(0,0,0); //center of reference-Circle
	private Vector3d refRadius=new Vector3d(1,0,0); //vector to points on circle surface
	
	@Override
	public String getModuleName() {
		// TODO Auto-generated method stub
		return "CircleProjection";
	}

	@Override
	protected void processData() throws ModulException {
		Vector3d p1=getInputPin3D(PinName.P1).getVector();
		Vector3d p2=getInputPin3D(PinName.P2).getVector();
		double reset=getInputPin(PinName.RESET).getData();
		
		if (reset==1){
			//calculate middle of both ref-vectors
			p2.sub(p1);
			p2.scale(0.5);
			refRadius=p2;
			p1.add(refRadius);
			refMiddle=p1;

		}
		else{

			//calculate actual radius vector
			p2.sub(p1);
			p2.scale(0.5);
			
			//how much do we need to scale the vector to normalize it to the ref-vector?
			double scalefactor;
			
			scalefactor=refRadius.length()/p2.length();
			
			p2.scale(scalefactor);
			
			System.err.println("scalef:" + scalefactor);
			
			Vector3d outP1, outP2;
			
			//1st outpoint: add current radius-vector to old middle
			outP2=new Vector3d(refMiddle);
			outP2.add(p2);
			
			//2nd outpoint: add current radius-vector (in other direction) to old middle
			p2.scale(-1);
			
			outP1=new Vector3d(refMiddle);
			outP1.add(p2);
			
			getOutputPin3D(PinName.OUT1).writeDataVector(outP1);
			getOutputPin3D(PinName.OUT2).writeDataVector(outP2);
		}
		
	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub

	}
	
	public CircleProjection(){
		inputPins=new InputPin[3];
		inputPins[0]=new InputPin3D(PinName.P1,this);
		inputPins[1]=new InputPin3D(PinName.P2,this);
		inputPins[2]=new InputPin3D(PinName.RESET,this);
		
		outputPins=new OutputPin[2];
		
		outputPins[0]=new OutputPin3D(PinName.OUT1,this);
		outputPins[1]=new OutputPin3D(PinName.OUT2,this);
		
	}

}
