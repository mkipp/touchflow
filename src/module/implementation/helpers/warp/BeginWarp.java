package module.implementation.helpers.warp;

import module.Globals;
import module.implementation.modify.Warp;

public class BeginWarp implements WarpFunction {

	private double n=1;
	@Override
	public double applyFunction(double in) {
		// TODO Auto-generated method stub
		if (in <0 || in > 1){
			if (Globals.isDebug) {
				System.err.println("Warning: Warp function called with illegal input:" + in);
			}
		}

		return Math.pow(in, 1.0/n);
	}

	@Override
	public void setIntensity(double intensity) {
		if (intensity>=1) n=intensity;
		
	}

	@Override
	public double getIntensity() {
		// TODO Auto-generated method stub
		return n;
	}

	@Override
	public int returnModeNumber() {
		// TODO Auto-generated method stub
		return Warp.Mode_Begin;
	}

}
