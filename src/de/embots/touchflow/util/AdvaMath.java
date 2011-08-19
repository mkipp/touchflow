package de.embots.touchflow.util;

import java.awt.Point;

public class AdvaMath {
public static double getDist(Point p1, Point p2){
	double xdiff=Math.pow((p2.getX()-p1.getX()),2);
	double ydiff=Math.pow((p2.getY()-p1.getY()),2);
	return Math.sqrt(xdiff+ydiff);
}



}
