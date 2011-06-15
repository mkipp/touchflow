package de.embots.touchflow.unittests;

import java.awt.Point;

import de.embots.touchflow.module.Globals;
import de.embots.touchflow.module.core.pinName;
import de.embots.touchflow.module.implementation.input.Const2D;
import de.embots.touchflow.module.implementation.modify.MapPosCircle2D;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.embots.touchflow.util.AdvaMath;
import de.embots.touchflow.util.MathVector;
import de.embots.touchflow.exceptions.ModulException;




public class MapPosTest {

MapPosCircle2D pos;
MathVector center;
	
@Before public void setUp(){
	center=new MathVector(0, 0);
	
}

@Test public void testCirclePos4ohneCenter() throws ModulException{
	pos=new MapPosCircle2D(center, 1, 4, false);
	
	Point[] points=pos.getPointPositions();
	Point[] erwartet=new Point[]{
		new Point(0, 1),
		new Point(1, 0),
		new Point(0, -1),
		new Point(-1, 0),
	};
	Assert.assertArrayEquals(erwartet, points);
	
	
}
@Test public void testCirclePos4MitCenter() throws ModulException{
	pos=new MapPosCircle2D(center, 1, 4, true);
	
	Point[] points=pos.getPointPositions();
	Point[] erwartet=new Point[]{
		new Point(0, 1),
		new Point(1, 0),
		new Point(0, -1),
		new Point(-1, 0),
		new Point(0,0)
	};
	Assert.assertArrayEquals(erwartet, points);
	
	
}
@Test public void testCirclePos8MitCenter() throws ModulException{
	pos=new MapPosCircle2D(center, 1000, 8, true);
	
	Point[] points=pos.getPointPositions();
	Point[] erwartet=new Point[]{
		new Point(0, 1000),
		new Point(707,707),
		new Point(1000, 0),
		new Point(707,-707),
		new Point(0, -1000),
		new Point(-707,-707),
		new Point(-1000, 0),
		new Point (-707,707),
		new Point(0,0)
	};
	Assert.assertArrayEquals(erwartet, points);
	
	
}
@Test public void testVector(){
	Point p1=new Point();
	p1.setLocation(0,0);
	Point p2=new Point();
	p2.setLocation(0,1);
	
	
	Assert.assertEquals(1, AdvaMath.getDist(p1, p2),0.01);
	MathVector v=new MathVector(0,1);
	Assert.assertEquals(1,v.length(),0.01);
	v.drehe(45);
	Assert.assertEquals(0.70, v.getX1(),0.01);
	Assert.assertEquals(0.70, v.getX2(),0.01);
}

@Test public void testInAction() throws ModulException{
	Const2D act=new Const2D(10, 10);
	MathVector center=new MathVector(0, 0);
	MapPosCircle2D mappos=new MapPosCircle2D(center, 100, 4, true);
	
	mappos.getInputPin2D(pinName.POSITION).connectTo(act.getOutputPin2D(pinName.CONST));
	
	//execute one cycle
	Globals.onlyCalculateIfNewData=false;
	act.runOnce();
	mappos.runOnce();
	
	double[] result=new double[]{
			//90.553, 90.553,0,0,14.142
			0.4637, 0.4637,0,0,0.0724
	};
	
	Assert.assertEquals(5,mappos.getOutputPins().length );
	for (int i=0; i< result.length;i++){
		Assert.assertEquals(result[i], mappos.getOutputPins()[i].getData(),0.1);
	}
	
}

@Test public void testInActioncomplex() throws ModulException{
	Const2D act=new Const2D(50, 20);
	MathVector center=new MathVector(100, 10);
	MapPosCircle2D mappos=new MapPosCircle2D(center, 100, 8, true);
	
	mappos.getInputPin2D(pinName.POSITION).connectTo(act.getOutputPin2D(pinName.CONST));
	
	//execute one cycle
	Globals.onlyCalculateIfNewData=false;
	act.runOnce();
	mappos.runOnce();
	
	double[] result=new double[]{
			//0,0,0,0,0,0,50.99,64.13,50.99
			0,0,0,0,0,0,0.274,0.345,0.274
	};
	
	Assert.assertEquals(9, mappos.getPointPositions().length);
	for (int i=0; i< result.length;i++){
		Assert.assertEquals(result[i], mappos.getOutputPins()[i].getData(),0.1);
	}
	
}
/*
@Test public void testSquarePos4MitCenter() throws ModulException{
	pos=new MapPosSquare(center, 2, 8, true);
	
	Point[] points=pos.getPointPositions();
	Point[] erwartet=new Point[]{
		new Point(0, 1),
		new Point(1, 1),
		new Point(1, 0),
		new Point(1,-1),
		new Point(0, -1),
		new Point(-1, -1),
		new Point(-1, 0),
		new Point(-1, 1),
		new Point(0, 0),
	};
	Assert.assertArrayEquals(erwartet, points);
	
	
}

@Test public void testSquareInAction() throws ModulException{
	ConstPoint act=new ConstPoint(85, 80);
	MathVector center=new MathVector(0, 0);
	MapPosCircle mappos=new MapPosSquare(center, 200, 8, true);
	
	mappos.getInputPin2D(pinName.POSITION).connectTo(act.getOutputPin2D(pinName.CONST));
	
	//execute one cycle
	Globals.onlyCalculateIfNewData=false;
	act.runOnce();
	mappos.runOnce();
	
	double[] result=new double[]{
			//90.553, 90.553,0,0,14.142
			0,0.112,0.365,0,0,0,0,0,0.523 
	};
	
	Assert.assertEquals(result.length,mappos.getOutputPins().length );
	for (int i=0; i< result.length;i++){
		System.out.println(mappos.getOutputPins()[i].getData());
		//Assert.assertEquals("falsch: " + i, result[i], mappos.getOutputPins()[i].getData(),0.1);
	}
	
}*/

}
