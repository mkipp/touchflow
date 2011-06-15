package module.implementation.modify;

import module.core.ModifyModule;
import module.core.pinName;
import module.pin.InputPin;
import module.pin.OutputPin;

import org.jdom.Element;

import exceptions.ModulException;
import exceptions.ModulFactoryException;
import gui.touchflow.components.OptionPane.Attribute;
import gui.touchflow.components.OptionPane.NumberAttribute;
import gui.touchflow.components.OptionPane.OptionPane;

public class BandFilter extends ModifyModule {
	double upperBound, lowerBound;
	
	public BandFilter(){
		this(0,100);
	}
	public BandFilter(double lowerBound, double upperBound) {
		super();
		this.upperBound = upperBound;
		this.lowerBound = lowerBound;
		

		
		inputPins=new InputPin[1];
		
		inputPins[0]=new InputPin(pinName.IN, this);
		
		outputPins=new OutputPin[1];
		
		outputPins[0]=new OutputPin(pinName.OUT,this);
		
	}

	double lastData;
	
	@Override
	public String getDescription() {
		return "<html>Filters <b>in</b> so that the <b>out</b> value <br>is always in a specific interval</html>";
	}
	
	@Override
	public void init(String params) throws ModulException {
		String[] paramsbandfilt;
		paramsbandfilt=params.split(" ");
		if (paramsbandfilt.length!=2) throw new ModulFactoryException("BandFilter: split ergab mehr oder weniger als 2 Konstruktorargumente");
		
		try{
			lowerBound=Double.parseDouble(paramsbandfilt[0]);
			upperBound=Double.parseDouble(paramsbandfilt[1]);
		}
		catch(Exception nf){
			throw new ModulFactoryException("BandFilter: Konstruktorparam fehlt oder einer der Konstruktorparams kein int");
		}
	}
	@Override
	protected void additionalSaveAttribute(Element e) {
		e.setAttribute("Constructor",lowerBound+" " + upperBound);
	}

	@Override
	protected void processData() throws ModulException {
		double newIn=getInputPin(pinName.IN).getData();
		
		if (newIn >= lowerBound && newIn<=upperBound){
			lastData=newIn;
		}
		
		getOutputPin(pinName.OUT).writeData(lastData);
	}

	@Override
	public void openOptions() {
		NumberAttribute distarg=new NumberAttribute("Lower bound");
		distarg.setContent(lowerBound);
		NumberAttribute uparg=new NumberAttribute("Upper bound");
		uparg.setContent(upperBound);
		
		
		OptionPane.showOptionPane(new Attribute[]{distarg,uparg},this);

	}

	@Override
	public void reinit(Attribute[] args) {
		double d=(Double) args[0].getContent();
		lowerBound= d;
		d=(Double) args[1].getContent();
		upperBound= d;
	}
	@Override
	public String getModuleName() {
		
		return "Band-Filter";
	}

}
