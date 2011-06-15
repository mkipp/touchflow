package module.implementation.modify;

import exceptions.ModulException;
import exceptions.ModulFactoryException;
import gui.touchflow.components.OptionPane.Attribute;
import gui.touchflow.components.OptionPane.DisplayAttribute;
import gui.touchflow.components.OptionPane.NumberAttribute;
import gui.touchflow.components.OptionPane.OptionPane;

import java.awt.Point;

import module.core.ModifyModule;
import module.core.pinName;
import module.pin.InputPin;
import module.pin.InputPin2D;
import module.pin.OutputPin;

import org.jdom.Element;

import util.MathVector;

public class MapPosCircle2D extends ModifyModule{
protected int numOfPoints;
protected boolean hasCenter;
protected MathVector center;
protected double radius;

protected MapPosCalculation calculator;


@Override
public void openOptions() {
	DisplayAttribute numarg=new DisplayAttribute("Anzahl Peripheriepunkte: " + numOfPoints);
	
	
	//BooleanAttribute hascenterarg=new BooleanAttribute("Mittelpunkt vorhanden?");
	DisplayAttribute hascenterarg=new DisplayAttribute("Mittelpunkt vorhanden: " + hasCenter);
	
	NumberAttribute centerxarg=new NumberAttribute("Mittelpunkt X");
	centerxarg.setContent(center.getX1());
	NumberAttribute centeryarg=new NumberAttribute("MittelPunkt Y");
	centeryarg.setContent(center.getX2());
	NumberAttribute radiusarg=new NumberAttribute("Radius");
	radiusarg.setContent(radius);
	
	OptionPane.showOptionPane(new Attribute[]{numarg,hascenterarg,centerxarg,centeryarg, radiusarg},this);
}

@Override
public void reinit(Attribute[] args) {
	
	
	double centerx=(Double)args[2].getContent();
	double centery=(Double) args[3].getContent();
	
	center=new MathVector(centerx, centery);
	
	radius=(Double) args[4].getContent();
	
	
	
	this.calculator=new MapPosCircleCalculation(numOfPoints,hasCenter,center, radius);
	
	
}

@Override
public void init(String params) throws ModulException {
	String[] para;
	double posx,posy;
	int centerint;
	
	para=params.split(" ");
	if (para.length!=5) throw new ModulFactoryException("MapPosCircle: split ergab mehr oder weniger als 5 Konstruktorargumente");
	
	try{
		posx=Double.parseDouble(para[0]);
		posy=Double.parseDouble(para[1]);
		radius=Double.parseDouble(para[2]);
		numOfPoints=Integer.parseInt(para[3]);
		centerint=Integer.parseInt(para[4]);
		
	}
	catch(Exception nf){
		throw new ModulFactoryException("MapPosCircle: Konstruktorparam fehlt oder einer der Konstruktorparams kein int");
	}
	center=new MathVector(posx, posy);
	if (centerint==0) hasCenter=false;
	else hasCenter=true;
	//redraw output-Pins
	this.calculator=new MapPosCircleCalculation(numOfPoints,hasCenter,center, radius);
	
	outputPins=new OutputPin[calculator.getNumOfPoints()];
	

	for (int i=0; i<outputPins.length;i++){
		outputPins[i]=new OutputPin(pinName.values()[i+1],this); //+1 damit POS (inputPin) nicht doppelt vergeben wird
	}
}

public MapPosCircle2D() throws ModulException{
	this(new MathVector(500, 500), 500, 4 ,true);
}

public MapPosCircle2D(  MathVector center,
		double radius,int numOfPoints,boolean hasCenter) throws ModulException {
	super();
	this.numOfPoints = numOfPoints;
	this.hasCenter = hasCenter;
	this.center = center;
	this.radius = radius;
	
	if (numOfPoints<2) throw new ModulException("MapPosCircle: numOfPoints must be greater than 1");
	
	this.calculator=new MapPosCircleCalculation(numOfPoints,hasCenter,center, radius);
	

	//Standart-Initialisierung
	
	inputPins=new InputPin[1];
	
	inputPins[0]=new InputPin2D(pinName.POSITION, this);
	

		
	
	outputPins=new OutputPin[calculator.getNumOfPoints()];
	

	for (int i=0; i<outputPins.length;i++){
		outputPins[i]=new OutputPin(pinName.values()[i+1],this); //+1 damit POS (inputPin) nicht doppelt vergeben wird
	}
}

@Override
public String getDescription() {
	return "<html>Computes the percentual amount of the <br>keypoints on a circlic morphmap, given <br><b>POSITION</b> as the position on the map</html>";
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
	e.setAttribute("Constructor",center.getX1()+" " + center.getX2() + " " + radius + " " + numOfPoints + " " + centerint);
	
}

@Override
protected void processData() throws ModulException {

	
	InputPin2D act=getInputPin2D(pinName.POSITION);
	
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
public String getModuleName() {
	
	return "MapPos Circle 2D";
}


}
