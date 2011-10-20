package de.embots.touchflow.module.implementation.modify;

import javax.vecmath.Vector3d;

import de.embots.touchflow.module.core.ModifyModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.InputPin2D;
import de.embots.touchflow.module.pin.InputPin3D;
import de.embots.touchflow.module.pin.OutputPin;
import de.embots.touchflow.module.pin.OutputPin2D;
import de.embots.touchflow.module.pin.OutputPin3D;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;


public class InputSelect3D extends ModifyModule {

	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub

	}

	public InputSelect3D(){
		inputPins=new InputPin[3];
		inputPins[0]=new InputPin3D(PinName.DATA0, this);
		inputPins[1]=new InputPin3D(PinName.DATA1, this);
		inputPins[2]=new InputPin(PinName.SELECT, this);
		
		outputPins=new OutputPin[1];
		
		outputPins[0]=new OutputPin3D(PinName.OUT, this);

	}
	@Override
	public String getDescription() {
		return "<html>Multiplexes <b>DATA1 and DATA2</b> to the output channel <br><b>OUT</b> using <b>SELECT</b> as a selector.\n3D-Version.</html>";
	}
	@Override
	protected void processData() throws ModulException {
		double selData= getInputPin(PinName.SELECT).getData();
		Vector3d inData=getInputPin3D(PinName.DATA0).getVector();
		Vector3d inData2=getInputPin3D(PinName.DATA1).getVector();

		if (selData==0){
			getOutputPin3D(PinName.OUT).writeDataVector(inData);
		}
		else{
			getOutputPin3D(PinName.OUT).writeDataVector(inData2);
			if (selData!=1){
				System.err.println("WARNING: Mux: Sel Pin has no boolean data! Was:" + selData + " - Module ID:" + this.getId());
			}
		}

	}

	@Override
	public String getModuleName() {
		
		return "Input Select 3D";
	}

}
