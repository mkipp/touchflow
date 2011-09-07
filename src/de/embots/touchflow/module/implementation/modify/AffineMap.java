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

public class AffineMap extends ModifyModule {

	double slope=1,intercept;
	
	
	public AffineMap(){
		inputPins=new InputPin[2];
		inputPins[0]=new InputPin(PinName.IN, this);
		inputPins[1]=new InputPin(PinName.OVERALL_GAIN, this);
		inputPins[1].setDefaultData(1);
		
		outputPins=new OutputPin[1];
		outputPins[0]=new OutputPin(PinName.OUT, this);
		
	}
	
	@Override
	public String getModuleName() {
		// TODO Auto-generated method stub
		return "AffineMap";
	}

	@Override
	protected void processData() throws ModulException {
		double overallGain=getInputPin(PinName.OVERALL_GAIN).getData();
		
		double out=(getInputPin(PinName.IN).getData() * slope + intercept)*overallGain;
		
		getOutputPin(PinName.OUT).writeData(out);

	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		e.setAttribute("Constructor",slope+" " + intercept);

	}

	@Override
	public void init(String params) throws ModulException {
		String[] paramsAffineMap;
		paramsAffineMap=params.split(" ");
		if (paramsAffineMap.length!=2) throw new ModulFactoryException("AffineMap: split ergab mehr oder weniger als 2 Konstruktorargumente");
		
		try{
			slope=Double.parseDouble(paramsAffineMap[0]);
			intercept=Double.parseDouble(paramsAffineMap[1]);
		}
		catch(Exception nf){
			throw new ModulFactoryException("BandFilter: Konstruktorparam fehlt oder einer der Konstruktorparams kein int");
		}
	}

	@Override
	public void reinit(Attribute[] args) {
		double d=(Double) args[0].getContent();
		slope= d;
		d=(Double) args[1].getContent();
		intercept= d;
	}

	@Override
	public void openOptions() {
		NumberAttribute gainarg=new NumberAttribute("Slope");
		gainarg.setContent(slope);
		NumberAttribute offsetarg=new NumberAttribute("Intercept");
		offsetarg.setContent(intercept);
		
		
		OptionPane.showOptionPane(new Attribute[]{gainarg,offsetarg},this);
	}

}
