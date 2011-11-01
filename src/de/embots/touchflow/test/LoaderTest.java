package de.embots.touchflow.test;


import javax.vecmath.Vector3d;

import junit.framework.Assert;
import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.module.Settings;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.implementation.input.KinectServer;
import de.embots.touchflow.module.implementation.modify.CircleProjection;
import de.embots.touchflow.module.pin.OutputPin;
import de.embots.touchflow.module.pin.OutputPin3D;

import org.junit.Before;
import org.junit.Test;

public class LoaderTest {

	Settings s;
	
	@Before
	public void setUp() throws Exception {
		
	}

	@Test public void CircleProj() throws ModulException{
		CircleProjection proj=new CircleProjection();
		
		Vector3d v1=new Vector3d(new Vector3d(-0.07999999999999999, 0.235, 1.22));
		Vector3d v2=new Vector3d(new Vector3d(0.21, 0.0049999999999999906, 0.0));
		
		OutputPin3D p1=new OutputPin3D(PinName.P1, proj);
		p1.writeDataVector(v1);
		
		OutputPin3D p2=new OutputPin3D(PinName.P2, proj);
		p2.writeDataVector(v2);
		
		OutputPin reset=new OutputPin3D(PinName.RESET, proj);
		reset.writeData(1);
		
		proj.getInputPin3D(PinName.P1).connectTo(p1);
		proj.getInputPin3D(PinName.P2).connectTo(p2);
		proj.getInputPin(PinName.RESET).connectTo(reset);
		
		proj.runOnce();
		
		p1.writeDataVector(v1);
		
		p2.writeDataVector(v2);
		
		reset.writeData(0);
		
		proj.runOnce();
		
		Vector3d out1=proj.getOutputPin3D(PinName.OUT1).getVector();
		Vector3d out2=proj.getOutputPin3D(PinName.OUT2).getVector();
		
		Assert.assertEquals(out1, v1);
		Assert.assertEquals(out2, v2);
		
		p1.writeDataVector(new Vector3d(10,10,0));
		
		p2.writeDataVector(new Vector3d(-10,-10,0));
		
		reset.writeData(0);
		
		proj.runOnce();
		
		out1=proj.getOutputPin3D(PinName.OUT1).getVector();
		out2=proj.getOutputPin3D(PinName.OUT2).getVector();
		
		Assert.assertEquals(out1, new Vector3d(1,1,0));
		Assert.assertEquals(out2, new Vector3d(0,0,0));
	}
}
