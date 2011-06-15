package de.embots.touchflow.module.implementation.modify;

import java.awt.Point;

import de.embots.touchflow.util.AdvaMath;
import de.embots.touchflow.util.MathVector;

public class MapPosCircleCalculation extends MapPosCalculation {
	protected int numOfPoints;
	protected boolean hasCenter;
	protected MathVector center;
	protected double radius;

	
	
	public MapPosCircleCalculation(int numOfPoints, boolean hasCenter,
			MathVector center, double radius) {
		super();
		this.numOfPoints = numOfPoints;
		this.hasCenter = hasCenter;
		this.center = center;
		this.radius = radius;
		calcPointPos();
	}
	
	public boolean isInside (Point p){
		double dist=AdvaMath.getDist(p, center.toPoint());
		if (dist<=radius) return true;
		else return false;
	}
	

	private void calcPointPos(){
		
		if (hasCenter){
			pointPositions=new Point[numOfPoints+1];
			
		}
		else{
			pointPositions=new Point[numOfPoints];	
		}
		
		double winkelabstand=-360.0/numOfPoints;
		
		for (int i=0; i<numOfPoints;i++){
			pointPositions[i]=addPoint(i*winkelabstand+180);
		}
		
		if (hasCenter){
			pointPositions[numOfPoints]=center.toPoint();
		}
	}
	
	private Point addPoint(double d){
		
		MathVector v=new MathVector(0, radius);
		v.drehe(d);
		v.add(center);
		return v.toPoint();
	}
	
	//calculate dists&eliminate all excepting the 3 best
	public double[] getDistances(Point p){
		double[] dists=getDists(p);
		eradicateBads(dists,3);
		
		double gesamtdist=0;
		for (int i=0; i< dists.length;i++){
			gesamtdist+=dists[i];
		}
		
		double gesamtout=0;
		//write data to pins
		for (int i=0; i< dists.length;i++){
			if (dists[i]!=0){
				dists[i]=(gesamtdist/dists[i]);
				gesamtout +=dists[i];
			}
			
		}
		for (int i=0; i< dists.length;i++){
			if (dists[i]!=0){
				dists[i]=(dists[i]/gesamtout);		
			}
			
		}
		return dists;
	}
	
	

}
