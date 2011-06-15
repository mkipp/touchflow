package de.embots.touchflow.module.implementation.modify;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.exceptions.ModulFactoryException;
import de.embots.touchflow.gui.components.optionpane.Attribute;
import de.embots.touchflow.gui.components.optionpane.ChooseAttribute;
import de.embots.touchflow.gui.components.optionpane.NumberAttribute;
import de.embots.touchflow.gui.components.optionpane.OptionPane;
import de.embots.touchflow.module.core.ModifyModule;
import de.embots.touchflow.module.core.pinName;
import de.embots.touchflow.module.implementation.helpers.warp.BeginEndWarp;
import de.embots.touchflow.module.implementation.helpers.warp.BeginWarp;
import de.embots.touchflow.module.implementation.helpers.warp.EndWarp;
import de.embots.touchflow.module.implementation.helpers.warp.MidWarp;
import de.embots.touchflow.module.implementation.helpers.warp.WarpException;
import de.embots.touchflow.module.implementation.helpers.warp.WarpFunction;

public class Warp extends BandFilter {

	public static final int Mode_Begin=0, Mode_End=1, Mode_Mid=2, Mode_BeginEnd=3; 
	

	private WarpFunction function;
	
	private WarpFunction modeToFunction(int mode) throws WarpException{
		switch(mode){
		case Mode_Begin:
			return new BeginWarp();
		case Mode_End:
			return new EndWarp();
		case Mode_BeginEnd:
			return new BeginEndWarp();
		case Mode_Mid:
			return new MidWarp();
		default:
			throw new WarpException("Unsupported mode number");
		}
	}

	@Override
	public String getModuleName() {
		return "Warp";
	}

	@Override
	protected void processData() throws ModulException {
		// TODO Auto-generated method stub
		super.processData();
		
		double preWarp=getOutputPin(pinName.OUT).getData();
		
		double normiert=translateInterval(preWarp, this.lowerBound, this.upperBound, 0, 1);
		normiert=function.applyFunction(normiert);
		normiert=translateInterval(normiert, 0,1,this.lowerBound, this.upperBound);
		getOutputPin(pinName.OUT).writeData(normiert);
	}
	
	private void setFunction(WarpFunction function) throws WarpException{
		testFunction(function);
		this.function=function;
	}
	private void setTuningParameter(double n) throws WarpException{
		function.setIntensity(n);
		testFunction(function);
	}
	private void testFunction(WarpFunction function2) throws WarpException {
		if (function2.applyFunction(0)!= 0) throw new WarpException("f(0)=" + function2.applyFunction(0) + " - should be 0");
		if (function2.applyFunction(1)!= 1) throw new WarpException("f(1)=" + function2.applyFunction(1) + " - should be 1");
	}

	public static double translateInterval(double in, double srcLBound, double srcUBound, double destLBound, double destUBound){
		double srcbandBreite=srcUBound - srcLBound;
		double destbandBreite=destUBound - destLBound;
		
		//Prozentualen anteil am Quellband
		in=(in-srcLBound)/srcbandBreite;
		in=(in * destbandBreite) + destLBound;
		
		return in;
	}
	
	
	@Override
	public void init(String params) throws ModulException {
		String[] paramsbandfilt;
		paramsbandfilt=params.split(" ");
		if (paramsbandfilt.length!=4) throw new ModulFactoryException("Warp: split ergab mehr oder weniger als 4 Konstruktorargumente");
		
		try{
			lowerBound=Double.parseDouble(paramsbandfilt[0]);
			upperBound=Double.parseDouble(paramsbandfilt[1]);
			function=modeToFunction(Integer.parseInt(paramsbandfilt[2]));
			double n=Double.parseDouble(paramsbandfilt[3]);
			function.setIntensity(n);
		}
		catch(Exception nf){
			throw new ModulFactoryException("BandFilter: Konstruktorparam fehlt oder ist ungültig:" + nf);
		}
	}
	@Override
	protected void additionalSaveAttribute(Element e) {
		e.setAttribute("Constructor",lowerBound+" " + upperBound + " " + function.returnModeNumber() + " " + function.getIntensity());
	}
	
	@Override
	public void openOptions() {
		NumberAttribute distarg=new NumberAttribute("Lower bound");
		distarg.setContent(lowerBound);
		NumberAttribute uparg=new NumberAttribute("Upper bound");
		uparg.setContent(upperBound);
		ChooseAttribute mode=new ChooseAttribute("Intensity max. at...", new String[]{"Begin", "End", "Middle", "Begin and End"} );
		mode.setContent((Integer)function.returnModeNumber());
		NumberAttribute intensity=new NumberAttribute("Intensity");
		intensity.setContent(function.getIntensity());
		
		OptionPane.showOptionPane(new Attribute[]{distarg,uparg,mode,intensity},this);

	}

	public Warp() {
		super();
		try {
			function=modeToFunction(Mode_Begin);
		} catch (WarpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void reinit(Attribute[] args) {
		double d=(Double) args[0].getContent();
		lowerBound= d;
		d=(Double) args[1].getContent();
		upperBound= d;
		
		d=(Integer) args[2].getContent();
		try {
			function= modeToFunction((int) d);
		} catch (WarpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		d=(Double) args[3].getContent();
		function.setIntensity( d);
	}
	@Override
	public String getDescription() {
		return "<html>Applies a Warp function to <b>IN</b></html>";
	}
}
