package de.embots.touchflow.module.implementation.modify;

import java.awt.Point;

import de.embots.touchflow.module.Globals;
import de.embots.touchflow.module.core.ModifyModule;
import de.embots.touchflow.module.core.pinName;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.InputPin2D;
import de.embots.touchflow.module.pin.OutputPin;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;


public class Angle2D extends ModifyModule {

	//negative Winkelwerte angeben, oder komplementen winkel benutzen
	
	@Override
	public String getDescription() {
		return "<html>Computes angle between points <b>P1</b> and <b>P2</b>,<br>given the reference points <b>REF_P1</b> and <b>REF_P2</b>.</html>";
	}





	private boolean useNegativeAngle=true;
	
	@Override
	protected void processData() throws ModulException {
		
		//angle is zero when not two fingers used
		/*if (getInputPin2D(pinName.P2).isZero()||getInputPin2D(pinName.P1).isZero()) {
			getOutputPin(pinName.ANGLE).writeData(0);
			return;
		}*/
		
		//auessere Punkte
		int p2x=(int) getInputPin2D(pinName.P2).getData();
		int rp2x=(int) getInputPin2D(pinName.REF_P2).getData();
		int p2y=(int) getInputPin2D(pinName.P2).getData2();
		int rp2y=(int) getInputPin2D(pinName.REF_P2).getData2();
		
		//innere Punkte, die gemerget werden
		int p1x=(int) getInputPin2D(pinName.P1).getData();
		int rp1x=(int) getInputPin2D(pinName.REF_P1).getData();
		int p1y=(int) getInputPin2D(pinName.P1).getData2();
		int rp1y=(int) getInputPin2D(pinName.REF_P1).getData2();
		
		
			
		// innere Punkte mergen
		
		int diffx=p1x-rp1x;
		int diffy=p1y-rp1y;
		
		p2x=p2x-diffx;
		p2y=p2y-diffy;
		
		//angle berechnen
		
		int midx=rp1x;
		int midy=rp1y; 		
		
		//euklidischer Abstand
		int distgegen=(int) Math.sqrt(Math.pow((rp2x-midx),2)+Math.pow((rp2y-midy),2));
		int distan=(int) Math.sqrt(Math.pow((p2x-midx),2)+Math.pow((p2y-midy),2));
		int c=(int) Math.sqrt(Math.pow((p2x-rp2x),2)+Math.pow((p2y-rp2y),2));
		
	
		double winkel= (Math.acos((((Math.pow(distan, 2) + Math.pow(distgegen, 2) - Math.pow(c, 2))/(2*distan*distgegen)))));

		
		
		if (isLeftOf(new Point(rp2x,rp2y), new Point(midx,midy), new Point(p2x,p2y))){
			
			if (useNegativeAngle){
				winkel=-winkel;
				
			}
			else{
				
				//Winkel gegen uhrzeigersinn
				winkel=Math.PI*2 - winkel;
			}
		}
		
		
		//if (useNegativeAngle) winkel=-winkel;
		
		if (!Globals.useAngleRadiant){
			winkel *=(180/Math.PI);
			
		}
		
		
		getOutputPin(pinName.ANGLE).writeData(winkel);

		
	}

	
	public Angle2D() throws ModulException {
		inputPins=new InputPin[4];
		inputPins[0]=new InputPin2D(pinName.REF_P1,this);
		inputPins[1]=new InputPin2D(pinName.REF_P2,this);
		inputPins[2]=new InputPin2D(pinName.P1,this);
		inputPins[3]=new InputPin2D(pinName.P2,this);

		//Default-Referenzkoordinaten: Bildschirmmitte - Vektor nach oben		
		
		((InputPin2D)inputPins[1]).setDefaultData2(-1000);
		

		
		outputPins=new OutputPin[1];
		
		outputPins[0]=new OutputPin(pinName.ANGLE,this);

		
		
		//Portmap
		
		addPortMapEntry(0, pinName.REF_P1);
		addPortMapEntry(1, pinName.REF_P2);
		addPortMapEntry(2, pinName.P1);
		addPortMapEntry(3, pinName.P2);
		addPortMapEntry(4, pinName.ANGLE);
	}
	
	private boolean isLeftOf(Point G1, Point G2, Point check){
		if ((G2.x-G1.x)*(check.y-G1.y)-(check.x-G1.x)*(G2.y-G1.y)>0){
			return true;
		}
		return false;
	}





	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String getModuleName() {
		
		return "Angle 2D";
	}
}
