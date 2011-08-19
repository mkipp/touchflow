package de.embots.touchflow.gui.components;

import de.embots.touchflow.gui.inspector.StringOP;
import de.embots.touchflow.TouchFlow;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import de.embots.touchflow.module.Globals;
import de.embots.touchflow.module.implementation.modify.MapPosCircle2D;
import de.embots.touchflow.module.implementation.modify.MapPosSquare2D;
import de.embots.touchflow.module.pin.InputPin2D;
import de.embots.touchflow.module.pin.OutputPin;
import de.embots.touchflow.module.pin.OutputPin2D;
import de.embots.touchflow.module.pin.Pin;

import org.jgraph.JGraph;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.PortView;


public class MyJGraph extends JGraph{
	
	

	public static PortView markedPort;
	public static PortView secondMarkedPort;
	
	
	@Override
	public String getToolTipText(MouseEvent e) {
	    if(e != null) {
	      // Fetch Cell under Mousepointer
	      Object c = getFirstCellForLocation(e.getX(), e.getY());
	      if (c != null && c instanceof GraphModul){
	    	  GraphModul m=(GraphModul) c;
	    	  return m.getCorrespondingModule().getDescription();
	      }
	        
	    }
	    return null;
	  }

	
	
	@Override
	protected void paintComponent(Graphics arg0) {
		// TODO Auto-generated method stub
		super.paintComponent(arg0);
		
		/*
		arg0.setFont(new Font("Arial", 0, Globals.PinBeschriftungLetterSize));
		
		
		for (GraphModul m:JGraphTool.getNodes()){
			InputPin[] iPins=m.getCorrespondingModule().getInputPins();
			double abstand= GraphConstants.PERMILLE/(double)iPins.length;
	     
			for (int i=0; i< iPins.length;i++){
				String text=iPins[i].getName().toString();
				m.
				double ycor=(i+0.5)*abstand;
				ycor-=StringOP.getStringHeight(text, arg0)/2;
				double xcor=Globals.PinBeschriftungBorder;
				arg0.drawString(text,(int) xcor,(int) ycor);
			}
		}*/
		
		drawMarkedPort(arg0, markedPort);
		drawMarkedPort(arg0, secondMarkedPort);
		
		if (TouchFlow.getFirstPortView()!=null){
			Point2D firstport=TouchFlow.getFirstPortView().getLocation();
			Pin pin=TouchFlow.getFirstPort().getPin();
			arg0.setColor(Globals.ConnectionMarkerColor);
			if (pin instanceof OutputPin)arg0.fillOval((int)firstport.getX()-9, (int)firstport.getY()-4, Globals.ConnectionMarkerSize, Globals.ConnectionMarkerSize);
			else arg0.fillOval((int)firstport.getX()+1, (int)firstport.getY()-4, Globals.ConnectionMarkerSize, Globals.ConnectionMarkerSize);
		}
	}

	private void drawMarkedPort(Graphics arg0, PortView markedPort) {
		if (markedPort!=null){
			
			double xcor=markedPort.getLocation().getX();
			double ycor=markedPort.getLocation().getY();
			
			Pin pin=((PinPort)markedPort.getCell()).getPin();
			arg0.setFont(new Font("Arial", 1, Globals.PinBeschriftungLetterSize));
			String text=pin.getName().toString();
			
			if (pin instanceof InputPin2D||pin instanceof OutputPin2D){
				text=text+" : Double2D";
			}
			else{
				text=text+" : Double";
			}
			
			//spezialfall morphmap: name aendern
			if (pin instanceof OutputPin){
				if (pin.getParentModul() instanceof MapPosCircle2D || pin.getParentModul() instanceof MapPosSquare2D){
					text="TARGET" + (pin.getName().ordinal());
				}
			}
			int width=(int)StringOP.getStringWidth(text, arg0) +4;
			ycor -=StringOP.getStringHeight(text, arg0)/2;
			xcor -=width+Globals.PinBeschriftungBorder;
			
			
			xcor=xcor * TouchFlow.getScaleRate();
			ycor=ycor * TouchFlow.getScaleRate();
			
			arg0.setColor(new Color(0,0,0));
			arg0.fill3DRect((int)xcor,(int) ycor-2, width, (int)StringOP.getStringHeight(text, arg0) +2, true);
			if (pin.is2DPin()){
				arg0.setColor(new Color(255,50,50));
			}
			else{
				arg0.setColor(new Color(150,150,255));
			}
			arg0.drawString(text,(int)xcor+2, (int)(ycor+2+StringOP.getStringHeight(text, arg0)/2));
		}
	}

	public MyJGraph() {
		super();
		
		// TODO Auto-generated constructor stub
	}

	public MyJGraph(GraphLayoutCache arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public MyJGraph(GraphModel arg0, BasicMarqueeHandler arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public MyJGraph(GraphModel arg0, GraphLayoutCache arg1,
			BasicMarqueeHandler arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	public MyJGraph(GraphModel arg0, GraphLayoutCache arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public MyJGraph(GraphModel arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}



}
