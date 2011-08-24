package de.embots.touchflow.module.implementation.input;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.exceptions.ModulFactoryException;
import de.embots.touchflow.gui.components.optionpane.Attribute;
import de.embots.touchflow.gui.components.optionpane.NumberAttribute;
import de.embots.touchflow.gui.components.optionpane.OptionPane;
import de.embots.touchflow.module.core.InputModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.OutputPin;
import de.embots.touchflow.module.pin.OutputPin2D;
import de.embots.touchflow.module.pin.OutputPin3D;

import org.jdom.Element;


public class Const3D extends InputModule {

	private double Konstante;
	private double Konstante2;
	private double Konstante3;
	
	public double getKonstante() {
		return Konstante;
	}
	
	@Override
	public void openOptions() {
		NumberAttribute kxarg=new NumberAttribute("Konstante X");
		kxarg.setContent(Konstante);
		NumberAttribute kyarg=new NumberAttribute("Konstante Y");
		kyarg.setContent(Konstante2);
		
		NumberAttribute kzarg=new NumberAttribute("Konstante Z");
		kzarg.setContent(Konstante3);
		
		OptionPane.showOptionPane(new Attribute[]{kxarg,kyarg,kzarg},this);
	}

	@Override
	public void reinit(Attribute[] args) {
		Konstante=(Double) args[0].getContent();
		Konstante2=(Double) args[1].getContent();
		Konstante3=(Double) args[2].getContent();
	}

	@Override
	public void init(String params) throws ModulException {
		String[] paramsa;
		paramsa=params.split(" ");
		if (paramsa.length!=3) throw new ModulFactoryException("ConstPoint3D: split ergab mehr oder weniger als 3 Konstruktorargumente");
		
		try{
			Konstante=Double.parseDouble(paramsa[0]);
			Konstante2=Double.parseDouble(paramsa[1]);
			Konstante3=Double.parseDouble(paramsa[2]);
		}
		catch(Exception nf){
			throw new ModulFactoryException("ConstPoint: Konstruktorparam fehlt oder einer der Konstruktorparams kein int");
		}
	}

	@Override
	protected void processData() throws ModulException {
		//einfach den Wert auf den Output schreiben
		getOutputPin(PinName.CONST).writeData(Konstante);
		getOutputPin2D(PinName.CONST).writeData2(Konstante2);
		getOutputPin3D(PinName.CONST).writeData3(Konstante3);
	}
	
	public Const3D() throws ModulException{
		this(100,200,300);
	}
	public Const3D(double Konstante, double Konstante2, double Konstante3) throws ModulException{
		this.Konstante=Konstante;
		this.Konstante2=Konstante2;
		this.Konstante3=Konstante3;
		
		outputPins=new OutputPin[1];
		outputPins[0]=new OutputPin3D(PinName.CONST,this);

		addPortMapEntry(4, PinName.CONST);
	}
	
	
	@Override
	protected void additionalSaveAttribute(Element e) {
		e.setAttribute("Constructor",Konstante + " " + Konstante2+ " " + Konstante3);
		
	}
	@Override
	public String getDescription() {
		return "Outputs 2 fixed constants (Double3D) as a pair";
	}

	@Override
	public String getModuleName() {
		
		return "Const3D";
	}

}
