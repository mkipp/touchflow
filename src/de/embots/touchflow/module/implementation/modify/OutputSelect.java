package de.embots.touchflow.module.implementation.modify;

import de.embots.touchflow.module.core.ModifyModule;
import de.embots.touchflow.module.core.pinName;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.OutputPin;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;


public class OutputSelect extends ModifyModule {

	public OutputSelect() throws ModulException {
		inputPins=new InputPin[2];
		inputPins[0]=new InputPin(pinName.DATA,this);
		inputPins[1]=new InputPin(pinName.SELECT,this);
		
		outputPins=new OutputPin[2];
		
		outputPins[0]=new OutputPin (pinName.OUT1,this);
		outputPins[1]=new OutputPin (pinName.OUT2,this);

		addPortMapEntry(0, pinName.DATA);
		addPortMapEntry(3, pinName.SELECT);
		addPortMapEntry(4, pinName.OUT1);
		addPortMapEntry(7, pinName.OUT2);
	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub

	}
	@Override
	public String getDescription() {
		return "<html>Demultiplexes <b>DATA</b> to the output channels <br><b>OUT1 and OUT2</b> using <b>SELECT</b> as a selector</html>";
	}
	@Override
	protected void processData() throws ModulException {
		double selData= getInputPin(pinName.SELECT).getData();
		double inData=getInputPin(pinName.DATA).getData();

		if (selData==0){
			getOutputPin(pinName.OUT1).writeData(inData);
		}
		else{
			getOutputPin(pinName.OUT2).writeData(inData);
			if (selData!=1){
				System.err.println("WARNING: Demux: Sel Pin has no boolean data! Was:" + selData + " - Module ID:" + this.getId());
			}
		}
	}

	@Override
	public String getModuleName() {
		
		return "Output Select";
	}

}
