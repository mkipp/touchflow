package de.embots.touchflow.module.implementation.modify;

import java.awt.Point;

import de.embots.touchflow.module.core.ModifyModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.InputPin2D;
import de.embots.touchflow.module.pin.OutputPin;

import org.jdom.Element;

import de.embots.touchflow.util.MathVector;
import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.exceptions.ModulFactoryException;
import de.embots.touchflow.gui.components.optionpane.Attribute;
import de.embots.touchflow.gui.components.optionpane.DisplayAttribute;
import de.embots.touchflow.gui.components.optionpane.NumberAttribute;
import de.embots.touchflow.gui.components.optionpane.OptionPane;

public class MapPosSquare2D extends ModifyModule{

	MathVector center;
	double breite;
	int numOfPoints;
	boolean hasCenter;
	MapPosCalculation calculator;

	public MapPosSquare2D() throws ModulException{
		this(new MathVector(500, 500), 500, 4 ,true);
	}

	public MapPosSquare2D(MathVector center, double breite, int numOfPoints,
			boolean hasCenter) throws ModulException {
		super();
		this.center = center;
		this.breite = breite;
		this.numOfPoints = numOfPoints;
		this.hasCenter = hasCenter;
		if (numOfPoints!=4 && numOfPoints!=8) throw new ModulException("MapPosSquare: numOfPoints must be 4 or 8");
		
		calculator=new MapPosSquareCalculation(numOfPoints, hasCenter, center, breite);
		
		
		//Standart-Initialisierung
		
		inputPins=new InputPin[1];
		
		inputPins[0]=new InputPin2D(PinName.POSITION, this);
		
			
		
		outputPins=new OutputPin[calculator.getNumOfPoints()];
		

		for (int i=0; i<outputPins.length;i++){
			outputPins[i]=new OutputPin(PinName.values()[i+1],this); //+1 damit POS (inputPin) nicht doppelt vergeben wird
		}
		
	}

	@Override
	public void init(String params) throws ModulException {
		String[] para;
		double posx,posy;
		int centerint;
		
		para=params.split(" ");
		if (para.length!=5) throw new ModulFactoryException("MapPosSquare: split ergab mehr oder weniger als 5 Konstruktorargumente");
		
		try{
			posx=Double.parseDouble(para[0]);
			posy=Double.parseDouble(para[1]);
			breite=Double.parseDouble(para[2]);
			numOfPoints=Integer.parseInt(para[3]);
			centerint=Integer.parseInt(para[4]);
			
		}
		catch(Exception nf){
			throw new ModulFactoryException("MapPosSquare: Konstruktorparam fehlt oder einer der Konstruktorparams kein int");
		}

		center=new MathVector(posx, posy);
		if (centerint==0) hasCenter=false;
		else hasCenter=true;
		calculator=new MapPosSquareCalculation(numOfPoints, hasCenter, center, breite);
		//redraw output-Pins
		
		outputPins=new OutputPin[calculator.getNumOfPoints()];
		

		for (int i=0; i<outputPins.length;i++){
			outputPins[i]=new OutputPin(PinName.values()[i+1],this); //+1 damit POS (inputPin) nicht doppelt vergeben wird
		}
	}

	@Override
	public void openOptions() {
		DisplayAttribute numarg=new DisplayAttribute("Anzahl Peripheriepunkte: " + numOfPoints);
		
		
		//BooleanAttribute hascenterarg=new BooleanAttribute("Mittelpunkt vorhanden?");
		DisplayAttribute hascenterarg=new DisplayAttribute("Mittelpunkt vorhanden: " + hasCenter);

		
		NumberAttribute centerxarg=new NumberAttribute("Mittelpunkt X");
		centerxarg.setContent(center.getX1());
		NumberAttribute centeryarg=new NumberAttribute("MittelPunkt Y");
		centeryarg.setContent(center.getX2());
		NumberAttribute radiusarg=new NumberAttribute("Breite");
		radiusarg.setContent(breite);
		
		OptionPane.showOptionPane(new Attribute[]{numarg,hascenterarg,centerxarg,centeryarg, radiusarg},this);
	}

	@Override
	public void reinit(Attribute[] args) {
		
		
		double centerx=(Double)args[2].getContent();
		
		double centery=(Double) args[3].getContent();
		
		center=new MathVector(centerx, centery);
		
		breite=(Double) args[4].getContent();
		
		
		
		calculator=new MapPosSquareCalculation(numOfPoints, hasCenter, center, breite);
		
	}

	@Override
	protected void processData() throws ModulException {
		
		InputPin2D act=getInputPin2D(PinName.POSITION);
		
		Point p=new Point((int)act.getData(),(int)act.getData2());
		
		//don't calculate when outside
		if (!isInside(p)){
			for (int i=0; i< getOutputPins().length;i++){
				getOutputPins()[i].writeData(0);
			}
			return;
		}
		
		
		// Berechnungsteil---------
		

		double[] dists=calculator.getDistances(p);
		
		for (int i=0; i< dists.length;i++){
				getOutputPins()[i].writeData(dists[i]);
		}
		
		
	}
	
	@Override
	public String getDescription() {
		return "<html>Computes the percentual amount of the <br>keypoints on a square morphmap, given <br><b>POSITION</b> as the position on the map</html>";
	}


	public Point[] getPointPositions() {
		return calculator.getPointPositions();
	}

	public boolean isInside (Point p){
		return calculator.isInside(p);
	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		//MathVector center,
		//double radius,int numOfPoints,boolean hasCenter
		int centerint=0;
		if (hasCenter) centerint=1;
		e.setAttribute("Constructor",center.getX1()+" " + center.getX2() + " " + breite + " " + numOfPoints + " " + centerint);
		
	}

	@Override
	public String getModuleName() {
		
		return "MapPos Square 2D";
	}

}
