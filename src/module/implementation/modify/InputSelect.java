package module.implementation.modify;

import module.core.ModifyModule;
import module.core.pinName;
import module.pin.InputPin;
import module.pin.OutputPin;

import org.jdom.Element;

import exceptions.ModulException;


public class InputSelect extends ModifyModule {

	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub

	}

	public InputSelect(){
		inputPins=new InputPin[3];
		inputPins[0]=new InputPin(pinName.DATA0, this);
		inputPins[1]=new InputPin(pinName.DATA1, this);
		inputPins[2]=new InputPin(pinName.SELECT, this);
		
		outputPins=new OutputPin[1];
		
		outputPins[0]=new OutputPin(pinName.OUT, this);

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
		
		if (selData==0){
			getOutputPin(pinName.OUT).writeData(inData);
		}
		else{
			getOutputPin(pinName.OUT).writeData(inData2);
			if (selData!=1){
				System.err.println("WARNING: Mux: Sel Pin has no boolean data! Was:" + selData + " - Module ID:" + this.getId());
			}
		}

	}

	@Override
	public String getModuleName() {
		
		return "Input Select";
	}

}
