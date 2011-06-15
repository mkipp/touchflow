package module.implementation.helpers.warp;

import module.Globals;
import module.implementation.modify.Warp;

public class EndWarp implements WarpFunction {
	double n;
	
	@Override
	public double applyFunction(double in) {
		if (in <0 || in > 1){
			if (Globals.isDebug) {
				System.err.println("Warning: Warp function called with illegal input:" + in);
			}
		}

		return Math.pow(in, n);
	}

	@Override
	public void setIntensity(double intensity) {
		// TODO Auto-generated method stub
		if(intensity >=1){
			n=(intensity);
		}
	}

	@Override
	public double getIntensity() {
		// TODO Auto-generated method stub
		return n;
	}

	@Override
	public int returnModeNumber() {
		// TODO Auto-generated method stub
		return Warp.Mode_End;
	}

}
