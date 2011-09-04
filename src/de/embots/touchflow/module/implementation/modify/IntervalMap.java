package de.embots.touchflow.module.implementation.modify;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.exceptions.ModulFactoryException;
import de.embots.touchflow.gui.components.optionpane.Attribute;
import de.embots.touchflow.gui.components.optionpane.NumberAttribute;
import de.embots.touchflow.gui.components.optionpane.OptionPane;
import de.embots.touchflow.module.core.ModifyModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.OutputPin;

public class IntervalMap extends ModifyModule {

	double inLBound=0, inUBound=1,outLBound=0, outUBound=1;
	
	@Override
	public void init(String params) throws ModulException {
		String[] paramsmod;
		paramsmod=params.split(" ");
		if (paramsmod.length!=4) throw new ModulFactoryException("Map: split ergab mehr oder weniger als 4 Konstruktorargumente");
		
		try{
			inLBound=Double.parseDouble(paramsmod[0]);
			inUBound=Double.parseDouble(paramsmod[1]);
			outLBound=Double.parseDouble(paramsmod[2]);
			outUBound=Double.parseDouble(paramsmod[3]);
		}
		catch(Exception nf){
			throw new ModulFactoryException("Map: Konstruktorparam fehlt oder einer der Konstruktorparams kein int");
		}
	}

	@Override
	public void reinit(Attribute[] args) {
		double d=(Double) args[0].getContent();
		inLBound= d;
		d=(Double) args[1].getContent();
		inUBound= d;
		d=(Double) args[2].getContent();
		outLBound= d;
		d=(Double) args[3].getContent();
		outUBound= d;
	}

	@Override
	public void openOptions() {
		NumberAttribute distarg=new NumberAttribute("Input - Lower bound");
		distarg.setContent(inLBound);
		NumberAttribute uparg=new NumberAttribute("Input - Upper bound");
		uparg.setContent(inUBound);
		NumberAttribute outlarg=new NumberAttribute("Output - Lower bound");
		outlarg.setContent(outLBound);
		NumberAttribute outuarg=new NumberAttribute("Output - Upper bound");
		outuarg.setContent(outUBound);
		
		
		OptionPane.showOptionPane(new Attribute[]{distarg,uparg, outlarg, outuarg},this);
		
		
	}

	@Override
	public String getModuleName() {
		// TODO Auto-generated method stub
		return "IntervalMap";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "<html>Maps one interval to another</html>";
	}

	@Override
	protected void processData() throws ModulException {
		double bandBreiteIn, bandBreiteOut, BBvh;
		bandBreiteIn=inUBound-inLBound;
		bandBreiteOut=outUBound-outLBound;
		BBvh=bandBreiteOut/bandBreiteIn;
		
		double in=getInputPin(PinName.IN).getData();
		
		//calculate abs value
		double abs=in-inLBound;
		
		//scale value
		abs=abs*BBvh;
		
		getOutputPin(PinName.OUT).writeData(abs+outLBound);
		
	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		e.setAttribute("Constructor",inLBound+" " + inUBound + " " + outLBound + " " + outUBound);

	}
	
	public IntervalMap(){
		inputPins=new InputPin[1];
		inputPins[0]=new InputPin(PinName.IN,this);
		
		outputPins=new OutputPin[1];
		outputPins[0]=new OutputPin(PinName.OUT,this);
		
	}

}
