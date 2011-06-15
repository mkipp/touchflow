package testapp;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JComponent;

import module.Globals;


public class Viereck extends JComponent {
	private Point[] punkte=new Point[4];
	
	private int angle;
	private int size=100;
	private Point rotPoint;
	private boolean marked=false;
	private int radius; //entfernung der ecken zum rotationspunkt
	
	public void mark(){
		marked=true;
	}
	public void unmark(){
		marked=false;
	}
	public Viereck() {
		super();
		radius=(int) Math.sqrt(Math.pow(size/2, 2)*2);
		rotPoint=new Point(radius, radius);
		calcPoints();
	}
	public int getAngle() {
		return angle;
	}
	public int getVSize() {
		return size;
	}
	public void setAngle(int angle){
		//if (angle >= 90||angle <0) angle=angle%90;
		this.angle=angle%360;
		calcPoints();
	}
	public void addAngle(int angle){
		setAngle(this.angle+angle);
	}
	public void setSize(int size){
		if (size<Globals.minSize) size=Globals.minSize;
		this.size=size;
		
		radius=(int) Math.sqrt(Math.pow(size/2, 2)*2);
		rotPoint=new Point(radius, radius);
		calcPoints();
	}
	public void calcPoints() {
		int hVectorX, hVectorY,vVectorX, vVectorY;
		
		double aufpunktX,aufpunktY;
		
		aufpunktX=rotPoint.getX()+Math.sin((angle-45) * (Math.PI/180))*radius;
		aufpunktY=rotPoint.getY()-Math.cos((angle-45) * (Math.PI/180))*radius;
			punkte[0]=new Point((int) aufpunktX,(int) aufpunktY);
		
		
		hVectorX=(int) (Math.cos(angle*(Math.PI/180))*size);
		hVectorY=(int) (Math.sin(angle*(Math.PI/180)) * size);
		vVectorX=(int) (Math.cos((angle+90)*(Math.PI/180))*size);
		vVectorY=(int) (Math.sin((angle+90)*(Math.PI/180)) * size);
		punkte[1]=new Point(punkte[0].x+hVectorX,punkte[0].y+hVectorY);
		punkte[2]=new Point(punkte[1].x+vVectorX,punkte[1].y+vVectorY);
		punkte[3]=new Point(punkte[0].x+vVectorX,punkte[0].y+vVectorY);
		setBounds(getBounds().x,getBounds().y,radius*2,radius*2);
	}
	public boolean isMarked() {
		return marked;
	}
	@Override
	protected void paintComponent(Graphics arg0) {
		
		super.paintComponent(arg0);


		if (!marked) arg0.setColor(Color.black);
		else arg0.setColor(Color.red);
		drawThickLine(arg0,punkte[0].x, punkte[0].y, punkte[1].x, punkte[1].y,3);
		drawThickLine(arg0,punkte[0].x, punkte[0].y, punkte[3].x, punkte[3].y,3);
		drawThickLine(arg0,punkte[1].x, punkte[1].y, punkte[2].x, punkte[2].y,3);
		drawThickLine(arg0,punkte[2].x, punkte[2].y, punkte[3].x, punkte[3].y,3);
	}
	
	public void drawThickLine(
			  Graphics g, int x1, int y1, int x2, int y2, int thickness) {
			  // The thick line is in fact a filled polygon

			  int dX = x2 - x1;
			  int dY = y2 - y1;
			  // line length
			  double lineLength = Math.sqrt(dX * dX + dY * dY);

			  double scale = (double)(thickness) / (2 * lineLength);

			  // The x,y increments from an endpoint needed to create a rectangle...
			  double ddx = -scale * (double)dY;
			  double ddy = scale * (double)dX;
			  ddx += (ddx > 0) ? 0.5 : -0.5;
			  ddy += (ddy > 0) ? 0.5 : -0.5;
			  int dx = (int)ddx;
			  int dy = (int)ddy;

			  // Now we can compute the corner points...
			  int xPoints[] = new int[4];
			  int yPoints[] = new int[4];

			  xPoints[0] = x1 + dx; yPoints[0] = y1 + dy;
			  xPoints[1] = x1 - dx; yPoints[1] = y1 - dy;
			  xPoints[2] = x2 - dx; yPoints[2] = y2 - dy;
			  xPoints[3] = x2 + dx; yPoints[3] = y2 + dy;

			  g.fillPolygon(xPoints, yPoints, 4);
			  }

}
