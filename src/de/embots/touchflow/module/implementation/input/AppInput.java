package de.embots.touchflow.module.implementation.input;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.module.core.InputModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.OutputPin;
import de.embots.touchflow.module.pin.OutputPin3D;

public class AppInput extends InputModule {

	double[] data=new double[4];
	
	@Override
	protected void processData() throws ModulException {
		for (int i=0; i< data.length;i++){
			outputPins[i].writeData(data[i]);
		}
	}

	public void setData(int port, double data){
		this.data[port]=data;
	}
	
	@Override
	public String getModuleName() {
		// TODO Auto-generated method stub
		return "AppInput";
	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getDescription() {
		return "<html>Allows controlling application to send data into TouchFlow</html>";
	}
	
	public AppInput(){
		outputPins=new OutputPin[4];
		
		outputPins[0]=new OutputPin(PinName.DATA0, this);
		outputPins[1]=new OutputPin(PinName.DATA1, this);
		outputPins[2]=new OutputPin(PinName.DATA2, this);
		outputPins[3]=new OutputPin(PinName.DATA3, this);
		
	}
}
