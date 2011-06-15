package module.implementation.input;

import exceptions.ModulException;
import exceptions.ModulFactoryException;
import gui.touchflow.components.OptionPane.Attribute;
import gui.touchflow.components.OptionPane.NumberAttribute;
import gui.touchflow.components.OptionPane.OptionPane;
import module.core.InputModule;
import module.core.pinName;
import module.pin.OutputPin;
import module.pin.OutputPin2D;

import org.jdom.Element;


public class Const2D extends InputModule {

	private double Konstante;
	private double Konstante2;
	
	public double getKonstante() {
		return Konstante;
	}
	
	@Override
	public void openOptions() {
		NumberAttribute kxarg=new NumberAttribute("Konstante X");
		kxarg.setContent(Konstante);
		NumberAttribute kyarg=new NumberAttribute("Konstante Y");
		kyarg.setContent(Konstante2);
		
		
		OptionPane.showOptionPane(new Attribute[]{kxarg,kyarg},this);
	}

	@Override
	public void reinit(Attribute[] args) {
		Konstante=(Double) args[0].getContent();
		Konstante2=(Double) args[1].getContent();
	}

	@Override
	public void init(String params) throws ModulException {
		String[] paramsa;
		paramsa=params.split(" ");
		if (paramsa.length!=2) throw new ModulFactoryException("ConstPoint: split ergab mehr oder weniger als 2 Konstruktorargumente");
		
		try{
			Konstante=Double.parseDouble(paramsa[0]);
			Konstante2=Double.parseDouble(paramsa[1]);
		}
		catch(Exception nf){
			throw new ModulFactoryException("ConstPoint: Konstruktorparam fehlt oder einer der Konstruktorparams kein int");
		}
	}

	@Override
	protected void processData() throws ModulException {
		//einfach den Wert auf den Output schreiben
		getOutputPin(pinName.CONST).writeData(Konstante);
		getOutputPin2D(pinName.CONST).writeData2(Konstante2);
	}
	
	public Const2D() throws ModulException{
		this(100,100);
	}
	public Const2D(double Konstante, double Konstante2) throws ModulException{
		this.Konstante=Konstante;
		this.Konstante2=Konstante2;
		outputPins=new OutputPin[1];
		outputPins[0]=new OutputPin2D(pinName.CONST,this);

		addPortMapEntry(4, pinName.CONST);
	}
	
	
	@Override
	protected void additionalSaveAttribute(Element e) {
		e.setAttribute("Constructor",Konstante + " " + Konstante2);
		
	}
	@Override
	public String getDescription() {
		return "Outputs 2 fixed constants (Double2D) as a pair";
	}

	@Override
	public String getModuleName() {
		
		return "Const2D";
	}

}
