package de.embots.touchflow.module.implementation.modify;

import javax.vecmath.Vector3d;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.exceptions.ModulFactoryException;
import de.embots.touchflow.gui.components.optionpane.Attribute;
import de.embots.touchflow.gui.components.optionpane.NumberAttribute;
import de.embots.touchflow.gui.components.optionpane.OptionPane;
import de.embots.touchflow.module.core.ModifyModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.InputPin3D;
import de.embots.touchflow.module.pin.OutputPin;

public class IsStable extends ModifyModule {

	double stableTime=30;
	double maximumDifference=50;
	
	Vector3d lastStablePoint=new Vector3d(0,0,0);
	long lastStableTime=0;
	
	@Override
	public String getModuleName() {
		// TODO Auto-generated method stub
		return "IsStable";
	}

	@Override
	protected void processData() throws ModulException {
		if (lastStableTime==0){
			lastStableTime=System.currentTimeMillis();
		}
		Vector3d currentPoint=new Vector3d();
		
		InputPin3D curPos=getInputPin3D(PinName.IN);
		
		currentPoint.x=curPos.getData();
		currentPoint.y=curPos.getData2();
		currentPoint.z=curPos.getData3();
		
		Vector3d temp=new Vector3d(currentPoint);
		temp.sub(lastStablePoint);
		
		if (temp.length()>=maximumDifference){
			lastStablePoint=currentPoint;
			lastStableTime=System.currentTimeMillis();
			
		}
		else{
			
		}
		
		if (System.currentTimeMillis()-lastStableTime>=stableTime){
			getOutputPin(PinName.OUT).writeData(1);
		}
		else{
			getOutputPin(PinName.OUT).writeData(0);
		}
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "<html>Checks whether the point data in <b>IN</b> remains constant over time<br>E.g. the point does not move more than <i>max. Difference</i> within the last <i>Frames</i> number of frames.</html>";
	}

	@Override
	public void init(String params) throws ModulException {
		String[] paramsbandfilt;
		paramsbandfilt=params.split(" ");
		if (paramsbandfilt.length!=2) throw new ModulFactoryException("IsStable: split ergab mehr oder weniger als 2 Konstruktorargumente");
		
		try{
			maximumDifference=Double.parseDouble(paramsbandfilt[0]);
			stableTime=Double.parseDouble(paramsbandfilt[1]);
		}
		catch(Exception nf){
			throw new ModulFactoryException("BandFilter: Konstruktorparam fehlt oder einer der Konstruktorparams kein int");
		}
	}

	@Override
	public void reinit(Attribute[] args) {
		double d=(Double) args[0].getContent();
		maximumDifference= d;
		d=(Double) args[1].getContent();
		stableTime= d;
	}

	@Override
	public void openOptions() {
		NumberAttribute distarg=new NumberAttribute("max. Difference");
		distarg.setContent(maximumDifference);
		NumberAttribute uparg=new NumberAttribute("time (ms)");
		uparg.setContent(stableTime);
		
		
		OptionPane.showOptionPane(new Attribute[]{distarg,uparg},this);
	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		e.setAttribute("Constructor",maximumDifference+" " + stableTime);
	}
	
	public IsStable(){
		inputPins=new InputPin[1];
		inputPins[0]=new InputPin3D(PinName.IN, this);
		
		outputPins=new OutputPin[1];
		outputPins[0]=new OutputPin(PinName.OUT, this);
		
		//has to update even when no new data is there
		forceAlwaysUpdate=true;
	}

}
