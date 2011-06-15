package module.implementation.modify;

import java.awt.Point;

import util.MathVector;

public class MapPosSquareCalculation extends MapPosCalculation {

	protected int numOfPoints;
	protected boolean hasCenter;
	protected MathVector center;
	protected double breite;

	
	public MapPosSquareCalculation(int numOfPoints, boolean hasCenter,
			MathVector center, double breite) {
		super();
		this.numOfPoints = numOfPoints;
		this.hasCenter = hasCenter;
		this.center = center;
		this.breite = breite;
		
		calcPoints();
	}

	private void calcPoints() {
		if (hasCenter){
			pointPositions=new Point[numOfPoints+1];
		}
		else{
			pointPositions=new Point[numOfPoints];
		}
		
		if (numOfPoints==4){
			pointPositions[0]=new Point((int)(center.getX1()-breite/2),(int)(center.getX2()-breite/2));
			pointPositions[1]=new Point((int)(center.getX1()+breite/2),(int)(center.getX2()-breite/2));
			pointPositions[2]=new Point((int)(center.getX1()+breite/2),(int)(center.getX2()+breite/2));
			pointPositions[3]=new Point((int)(center.getX1()-breite/2),(int)(center.getX2()+breite/2));
			if (hasCenter) pointPositions[4]=new Point((int)(center.getX1()),(int)(center.getX2()));
		}
		
		if (numOfPoints==8){
			pointPositions[0]=new Point((int)(center.getX1()-breite/2),(int)(center.getX2()-breite/2));
			pointPositions[1]=new Point((int)(center.getX1()),(int)(center.getX2()-breite/2));
			pointPositions[2]=new Point((int)(center.getX1()+breite/2),(int)(center.getX2()-breite/2));
			pointPositions[3]=new Point((int)(center.getX1()+breite/2),(int)(center.getX2()));
			pointPositions[4]=new Point((int)(center.getX1()+breite/2),(int)(center.getX2()+breite/2));
			pointPositions[5]=new Point((int)(center.getX1()),(int)(center.getX2()+breite/2));
			pointPositions[6]=new Point((int)(center.getX1()-breite/2),(int)(center.getX2()+breite/2));
			pointPositions[7]=new Point((int)(center.getX1()-breite/2),(int)(center.getX2()));
			if (hasCenter) pointPositions[8]=new Point((int)(center.getX1()),(int)(center.getX2()));
		}
		
	}

	@Override
	public double[] getDistances(Point p) {
		double[] dists=getDists(p);
		
		double[] twobest=new double[dists.length];
		
		System.arraycopy(dists, 0, twobest, 0, dists.length);
		
		eradicateBads(twobest, 2);
		
		if (twobest[twobest.length-1]==0&&hasCenter){
			//Mittelpunkt nicht bei den zwei besten -> hinzufügen und fertig
			twobest[twobest.length-1]=dists[twobest.length-1];
		}
		else{
			//mittelpunkt ist bei den zwei besten-> 3 beste ausgeben
			System.arraycopy(dists, 0, twobest, 0, dists.length);
			
			eradicateBads(twobest, 3);
		}
		
		
		
		double gesamtdist=0;
		for (int i=0; i< twobest.length;i++){
			gesamtdist+=twobest[i];
		}
		
		double gesamtout=0;
		//write data to pins
		for (int i=0; i< twobest.length;i++){
			if (twobest[i]!=0){
				twobest[i]=(gesamtdist/twobest[i]);
				gesamtout +=twobest[i];
			}
			
		}
		for (int i=0; i< twobest.length;i++){
			if (twobest[i]!=0){
				twobest[i]=(twobest[i]/gesamtout);		
			}
			
		}
		
		
		return twobest;
		
	}

	@Override
	public boolean isInside(Point p) {
		return (p.getX()>center.getX1()-(breite/2) && p.getX()<center.getX1()+(breite/2) && p.getY()>center.getX2()-(breite/2) && p.getY()<center.getX2()+(breite/2));
	}

}
