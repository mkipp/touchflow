package de.embots.touchflow.module.implementation.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.exceptions.ModulFactoryException;
import de.embots.touchflow.gui.components.optionpane.Attribute;
import de.embots.touchflow.gui.components.optionpane.NumberAttribute;
import de.embots.touchflow.gui.components.optionpane.OptionPane;
import de.embots.touchflow.gui.components.optionpane.StringAttribute;
import de.embots.touchflow.module.core.InputModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.implementation.input.MMouse.MouseBuffer;
import de.embots.touchflow.module.implementation.input.MMouse.MouseState;
import de.embots.touchflow.module.pin.OutputPin;
import de.embots.touchflow.module.pin.OutputPin2D;
import de.embots.touchflow.util.RAClass;

public class MultiMouse  extends InputModule{

	static MouseBuffer server;
	int id=0;

	
	@Override
	protected void processData() throws ModulException {
		OutputPin2D out=getOutputPin2D(PinName.POSITION);
		OutputPin lButton=getOutputPin(PinName.LButton);
		OutputPin rButton=getOutputPin(PinName.RButton);
		
		MouseState state=server.getMouseState(id);
		if (state!=null){
			out.writeData(state.getXpos());
			out.writeData2(state.getYpos());
			getOutputPin(PinName.SCROLL).writeData(state.getScrollpos());
			
			rButton.writeData(RAClass.BooleanToInt(state.isRMouseDown()));
			lButton.writeData(RAClass.BooleanToInt(state.isLMouseDown()));
		}
	}

	@Override
	public String getModuleName() {
		// TODO Auto-generated method stub
		return "MultiMouse";
	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		
		e.setAttribute("Constructor",id+"");
	}
	
	public MultiMouse(){
		outputPins=new OutputPin[4];
		outputPins[0]=new OutputPin2D(PinName.POSITION,this);
		outputPins[1]=new OutputPin(PinName.SCROLL, this);
		outputPins[2]=new OutputPin(PinName.LButton, this);
		outputPins[3]=new OutputPin(PinName.RButton, this);
		
		if (server==null){
			server=new MouseBuffer();
		}
		
	}

	@Override
	public void init(String params) throws ModulException {
		
		try{
			id=Integer.parseInt(params);
		}
		catch(Exception nf){
			throw new ModulFactoryException("MultiMouse:Konstruktorparam fehlt oder ist kein int");
		}

	}
	
	@Override
	public void openOptions() {
		
		NumberAttribute optid=new NumberAttribute("id");
		optid.setContent(this.id);
		
		OptionPane.showOptionPane(new Attribute[]{optid},this);

	}
	
	@Override
	public void reinit(Attribute[] args) {
		double d=(Double)args[0].getContent();
		id =(int) d;
	}
	
}
