package module.implementation.helpers.warp;

public interface WarpFunction {
	public double applyFunction(double in);
	public void setIntensity(double intensity);
	public double getIntensity();
	public int returnModeNumber();
}
