package de.embots.touchflow.util;

import java.awt.Point;

/**
 * implementiert einen mathematischen 2D-Vektor
 * @author cyre
 *
 */
public class MathVector {
private double x1,x2;


public double getX1() {
	return x1;
}

public double getX2() {
	return x2;
}

public MathVector(double x1, double x2) {
	super();
	this.x1 = x1;
	this.x2 = x2;
}

/**
 * Copy constructor
 * @param v
 */

public MathVector(MathVector v){
	this.x1=v.getX1();
	this.x2=v.getX2();
}

/**
 * create "NullVector", e.g. set components to 0.
 */
public MathVector(){
	
}
/**
 * gibt laenge(Betrag) des Vektors aus
 * @return
 */
public double length(){
	Point arg=new Point();
	arg.setLocation(x1,x2);
	Point p0=new Point();
	p0.setLocation(0,0);
	return AdvaMath.getDist(p0, arg);
}
public void drehe(double angle){
	double length=length();
	x1=Math.sin(angle*(Math.PI/180.0))*length;
	x2=Math.cos(angle*(Math.PI/180.0))*length;
}
public void add(MathVector other){
	x1+=other.getX1();
	x2+=other.getX2();
}
public Point toPoint(){
	Point ret=new Point();
	
	ret.setLocation(x1, x2);
	return ret;
}

}
