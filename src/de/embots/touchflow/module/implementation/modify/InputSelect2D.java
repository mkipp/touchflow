package de.embots.touchflow.module.implementation.modify;

import de.embots.touchflow.module.core.ModifyModule;
import de.embots.touchflow.module.core.pinName;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.InputPin2D;
import de.embots.touchflow.module.pin.OutputPin;
import de.embots.touchflow.module.pin.OutputPin2D;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;


public class InputSelect2D extends ModifyModule {

	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub

	}

	public InputSelect2D(){
		inputPins=new InputPin[3];
		inputPins[0]=new InputPin2D(pinName.DATA0, this);
		inputPins[1]=new InputPin2D(pinName.DATA1, this);
		inputPins[2]=new InputPin(pinName.SELECT, this);
		
		outputPins=new OutputPin[1];
		
		outputPins[0]=new OutputPin2D(pinName.OUT, this);

	}
	@Override
	public String getDescription() {
		return "<html>Multiplexes <b>DATA1 and DATA2</b> to the output channel <br><b>OUT</b> using <b>SELECT</b> as a selector</html>";
	}
	@Override
	protected void processData() throws ModulException {
		double selData= getInputPin(pinName.SELECT).getData();
		double inData=getInputPin(pinName.DATA0).getData();
		double inData2=getInputPin(pinName.DATA1).getData();
		double inData12=getInputPin2D(pinName.DATA0).getData2();
		double inData22=getInputPin2D(pinName.DATA1).getData2();
		
		if (selData==0){
			getOutputPin(pinName.OUT).writeData(inData);
			getOutputPin2D(pinName.OUT).writeData2(inData12);
		}
		else{
			getOutputPin(pinName.OUT).writeData(inData2);
			getOutputPin2D(pinName.OUT).writeData2(inData22);
			if (selData!=1){
				System.err.println("WARNING: Mux: Sel Pin has no boolean data! Was:" + selData + " - Module ID:" + this.getId());
			}
		}

	}

	@Override
	public String getModuleName() {
		
		return "Input Select 2D";
	}

}
