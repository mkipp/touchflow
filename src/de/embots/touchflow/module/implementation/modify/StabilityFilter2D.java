package de.embots.touchflow.module.implementation.modify;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.exceptions.ModulFactoryException;
import de.embots.touchflow.gui.components.optionpane.Attribute;
import de.embots.touchflow.gui.components.optionpane.NumberAttribute;
import de.embots.touchflow.gui.components.optionpane.OptionPane;
import de.embots.touchflow.module.core.ModifyModule;
import de.embots.touchflow.module.core.pinName;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.InputPin2D;
import de.embots.touchflow.module.pin.OutputPin;
import de.embots.touchflow.module.pin.OutputPin2D;

import org.jdom.Element;


public class StabilityFilter2D extends ModifyModule {

	
	private int threshold=4; //wieviele Zyklen muss der wert �hnlich sein, um durchgeleitet zu werden
	private double maxDist=100; //welche maximale Distanz gilt als "aehnlich"?
	private int curCycle; // wie oft war dieser gefilterte Wert schon aehnlich (wenn er threshold erreicht, wird er durchgeleitet)?
	double oldX=-1,oldY=-1;
	
	@Override
	public void openOptions() {
		NumberAttribute distarg=new NumberAttribute("max. Distance");
		distarg.setContent(maxDist);
		NumberAttribute thrarg=new NumberAttribute("Threshold");
		thrarg.setContent(threshold);
		
		
		OptionPane.showOptionPane(new Attribute[]{distarg,thrarg},this);
	}

	@Override
	public void reinit(Attribute[] args) {
		double d=(Double) args[1].getContent();
		threshold=(int) d;
		
		maxDist=(Double) args[0].getContent();
	}

	@Override
	public void init(String params) throws ModulFactoryException {
		
		String[] params2;
		params2=params.split(" ");
		if (params2.length!=2) throw new ModulFactoryException("Filter: split ergab mehr oder weniger als 2 Konstruktorargumente");
		
		try{
			maxDist=Double.parseDouble(params2[0]);
			threshold=Integer.parseInt(params2[1]);
		}
		catch(Exception nf){
			throw new ModulFactoryException("Filter: Konstruktorparam fehlt oder einer der Konstruktorparams kein int");
		}
	}

	public StabilityFilter2D(){
		this(100,4);
	}
	public StabilityFilter2D(double maxDist, int Threshold){
		//has to update even when no new data is there
		forceAlwaysUpdate=true;
		
		threshold=Threshold;
		this.maxDist=maxDist;
		inputPins=new InputPin[1];
		inputPins[0]=new InputPin2D(pinName.IN, this);
		
		outputPins=new OutputPin[1];
		outputPins[0]=new OutputPin2D(pinName.OUT, this);

	}
	
	@Override
	public String getDescription() {
		return "<html>Forwards <b>IN</b> to <b>OUT</b> only when IN stays <br>nearly constant for a certain period of time</html>";
	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		e.setAttribute("Constructor",maxDist+" " + threshold);
	}

	@Override
	protected void processData() throws ModulException {
		
		//init
		if (oldX==-1){
			oldX=getInputPin2D(pinName.IN).getData();
			oldY=getInputPin2D(pinName.IN).getData2();
			
			getOutputPin2D(pinName.OUT).writeData(oldX);
			getOutputPin2D(pinName.OUT).writeData2(oldY);
			return;
		}
		
		double curX=getInputPin2D(pinName.IN).getData();
		double curY=getInputPin2D(pinName.IN).getData2();
		
		double dist=Math.sqrt((curX-oldX)*(curX-oldX) + (curY-oldY)*(curY-oldY));
		
		if (dist<maxDist){ //guter Punkt, weiterleiten
			oldX=curX;
			oldY=curY;
			curCycle=0;

		}
		
		else{//schlechter Punkt, erstmal warten
			curCycle++;
			//maximale anzahl zyklen erreicht-> durchleiten
			if (curCycle>=threshold){
				oldX=curX;
				oldY=curY;
				curCycle=0;
			}
			else{
				//nicht ueber threshold->nichts machen
				return;
			}
		}
		
		getOutputPin2D(pinName.OUT).writeData(oldX);
		getOutputPin2D(pinName.OUT).writeData2(oldY);

	}

	@Override
	public String getModuleName() {
		
		return "Stability-Filter 2D";
	}

}
