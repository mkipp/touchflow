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

public class LinearMap extends ModifyModule {

	double gain=1,offset;
	
	
	public LinearMap(){
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
		return "LinearMap";
	}

	@Override
	protected void processData() throws ModulException {
		double overallGain=getInputPin(PinName.OVERALL_GAIN).getData();
		
		double out=(getInputPin(PinName.IN).getData() * gain + offset)*overallGain;
		
		getOutputPin(PinName.OUT).writeData(out);

	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		e.setAttribute("Constructor",gain+" " + offset);

	}

	@Override
	public void init(String params) throws ModulException {
		String[] paramslinearmap;
		paramslinearmap=params.split(" ");
		if (paramslinearmap.length!=2) throw new ModulFactoryException("LinearMap: split ergab mehr oder weniger als 2 Konstruktorargumente");
		
		try{
			gain=Double.parseDouble(paramslinearmap[0]);
			offset=Double.parseDouble(paramslinearmap[1]);
		}
		catch(Exception nf){
			throw new ModulFactoryException("BandFilter: Konstruktorparam fehlt oder einer der Konstruktorparams kein int");
		}
	}

	@Override
	public void reinit(Attribute[] args) {
		double d=(Double) args[0].getContent();
		gain= d;
		d=(Double) args[1].getContent();
		offset= d;
	}

	@Override
	public void openOptions() {
		NumberAttribute gainarg=new NumberAttribute("Gain");
		gainarg.setContent(gain);
		NumberAttribute offsetarg=new NumberAttribute("Offset");
		offsetarg.setContent(offset);
		
		
		OptionPane.showOptionPane(new Attribute[]{gainarg,offsetarg},this);
	}

}
