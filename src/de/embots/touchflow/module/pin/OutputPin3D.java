package de.embots.touchflow.module.pin;

import javax.vecmath.Vector3d;

import de.embots.touchflow.module.core.Module;
import de.embots.touchflow.module.core.PinName;

public class OutputPin3D extends OutputPin2D {
	@Override
	public boolean isZero() {
		// TODO Auto-generated method stub
		return super.isZero() && (data3==0);
	}

	public double data3;
	private double lastDrawData3;
	
	public OutputPin3D(PinName name, Module parentModul) {
		super(name, parentModul);
		// TODO Auto-generated constructor stub
	}

	public void writeData3(double wert){
		
		dataLock.lock();
		dataReceived=false;
		data3=wert;
		dataLock.unlock();
	}

	public void writeDataVector(Vector3d wert){
		
		dataLock.lock();
		dataReceived=false;
		data=wert.x;
		data2=wert.y;
		data3=wert.z;
		dataLock.unlock();
	}
	
	public double getData3() {
		return data3;
	}
	
	public Vector3d getVector(){
		Vector3d ret=new Vector3d();
		
		ret.x=getData();
		ret.y=getData2();
		ret.z=getData3();
		
		return ret;
		
	}
	
	public boolean isNewDrawData() {
		if (data==lastDrawData && data2==lastDrawData2 && data3==lastDrawData3) return false;
		lastDrawData=data;
		lastDrawData2=data2;
		lastDrawData3=data3;
		return true;
	}
}
