package de.embots.touchflow.unittests;

import junit.framework.Assert;
import de.embots.touchflow.module.implementation.modify.Warp;

import org.junit.Test;


public class WarpTest {

	@Test 
	public void testTranslate(){
		Warp warp=new Warp();
		
		double out=warp.translateInterval(5, 0, 10, 0, 1);
		
		Assert.assertEquals(0.5, out, 0.01);
	}
	
	@Test 
	public void testTranslate2(){
		Warp warp=new Warp();
		
		double out=warp.translateInterval(0, -1, 1, 0, 1);
		
		Assert.assertEquals(0.5, out, 0.01);
	}
	@Test 
	public void testTranslate3(){
		Warp warp=new Warp();
		
		double out=warp.translateInterval(0, -1, 1, -2, -1);
		
		Assert.assertEquals(-1.5, out, 0.01);
	}
}
