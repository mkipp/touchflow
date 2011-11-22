package de.embots.touchflow.module.implementation.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.module.core.InputModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.implementation.input.MMouse.MouseBuffer;
import de.embots.touchflow.module.implementation.input.MMouse.MouseState;
import de.embots.touchflow.module.pin.OutputPin;
import de.embots.touchflow.module.pin.OutputPin2D;

public class MultiMouse  extends InputModule{

	static MouseBuffer server;
	int id=1;

	
	@Override
	protected void processData() throws ModulException {
		OutputPin2D out=getOutputPin2D(PinName.POSITION);
		MouseState state=server.getMouseState(id);
		if (state!=null){
			out.writeData(state.getXpos());
			out.writeData2(state.getYpos());
		}
	}

	@Override
	public String getModuleName() {
		// TODO Auto-generated method stub
		return "MultiMouse";
	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		

	}
	
	public MultiMouse(){
		outputPins=new OutputPin[1];
		outputPins[0]=new OutputPin2D(PinName.POSITION,this);
		if (server==null){
			server=new MouseBuffer();
			
		}
	}


	
}
