package de.embots.touchflow.module.implementation.helpers.warp;

import de.embots.touchflow.module.implementation.modify.Warp;

public class BeginEndWarp implements WarpFunction {

	// Typ3: (((x*2)-1)^n)*0.5+0.5, n ungerade, int, >=0
	
	int n;
	@Override
	public double applyFunction(double in) {
		double exp=2*n+1;
		double base= ((in*2)-1);
		base=Math.pow(base, exp);
		
		base=base *0.5+0.5;
		return base;
	}

	@Override
	public double getIntensity() {
		// TODO Auto-generated method stub
		return n;
	}

	@Override
	public int returnModeNumber() {
		// TODO Auto-generated method stub
		return Warp.Mode_BeginEnd;
	}

	@Override
	public void setIntensity(double intensity) {
		if (intensity>=0){
			n=(int) (intensity) ;
		}

	}

}
