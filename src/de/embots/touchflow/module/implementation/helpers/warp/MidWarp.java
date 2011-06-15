package de.embots.touchflow.module.implementation.helpers.warp;

import de.embots.touchflow.module.implementation.modify.Warp;

public class MidWarp implements WarpFunction {

	//Typ4: arctan (n*x) je gr��er n, umso intensiver 0<n<00
	
	double n;
	double scalecorrection; //scalet die function,
	
	double outlbound, outubound;
	
	public MidWarp(){
		setIntensity(1);
		
	}
	@Override
	public double applyFunction(double in) {
		// TODO Auto-generated method stub
		double unnormalized=internalApply(in); //*scalecorrection;
		//System.out.println(unnormalized + ";" + Warp.translateInterval(unnormalized, outlbound, outubound, 0, 1) );
		return MidWarp.translateInterval(unnormalized, outlbound, outubound, 0, 1);
	}
	private double internalApply(double in) {
		return (Math.atan(n*(in-0.5))+0.5);
	}

	@Override
	public double getIntensity() {
		// TODO Auto-generated method stub
		return n;
	}

	@Override
	public int returnModeNumber() {
		// TODO Auto-generated method stub
		return Warp.Mode_Mid;
	}

	@Override
	public void setIntensity(double intensity) {
		n=intensity;
		scalecorrection=1; //reset scale corr.
		scalecorrection=1.0/applyFunction(1.0); //normalize to 1.0
		
		outlbound=internalApply(0.0);
		outubound=internalApply(1.0);
		
	}
	public static double translateInterval(double in, double srcLBound, double srcUBound, double destLBound, double destUBound){
		double srcbandBreite=srcUBound - srcLBound;
		double destbandBreite=destUBound - destLBound;
		
		//Prozentualen anteil am Quellband
		in=(in-srcLBound)/srcbandBreite;
		
		in=(in * destbandBreite) + destLBound;
		
		return in;
	}
}
