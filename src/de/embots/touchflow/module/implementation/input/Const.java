package de.embots.touchflow.module.implementation.input;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.exceptions.ModulFactoryException;
import de.embots.touchflow.gui.components.optionpane.Attribute;
import de.embots.touchflow.gui.components.optionpane.OptionPane;
import de.embots.touchflow.gui.components.optionpane.SliderAttribute;
import de.embots.touchflow.module.core.InputModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.OutputPin;

import org.jdom.Element;


public class Const extends InputModule {

	@Override
	public void reinit(Attribute[] args) {
		Konstante=(Double) args[0].getContent();
	}
	
	@Override
	public void openOptions() {
		SliderAttribute karg=new SliderAttribute("Konstante",1,100);
		karg.setContent(Konstante);
		
		OptionPane.showOptionPane(new Attribute[]{karg},this);
	}
	private double Konstante;

	@Override
	protected void processData() throws ModulException {
		//einfach den Wert auf den Output schreiben
		getOutputPin(PinName.CONST).writeData(Konstante);

	}
	@Override
	public void init(String params) throws ModulException {
		try{
			Konstante=Double.parseDouble(params);
		}
		catch(Exception nf){
			throw new ModulFactoryException("Const:Konstruktorparam fehlt oder ist kein int");
		}
	}
	public Const() throws ModulException{
		this(2);

	}
	public Const(double Konstante) throws ModulException{
		this.Konstante=Konstante;
		outputPins=new OutputPin[1];
		outputPins[0]=new OutputPin(PinName.CONST,this);
		addPortMapEntry(4, PinName.CONST);
	}
	
	
	@Override
	protected void additionalSaveAttribute(Element e) {
		e.setAttribute("Constructor",Konstante+"");
		
	}
	@Override
	public String getDescription() {
		return "Outputs one fixed constant (double)";
	}
	@Override
	public String getModuleName() {
	
		return "Const";
	}

}
