package module.implementation.modify;

import java.awt.Point;

import module.Globals;
import module.core.ModifyModule;
import module.core.pinName;
import module.pin.InputPin;
import module.pin.InputPin2D;
import module.pin.OutputPin;

import org.jdom.Element;

import exceptions.ModulException;


public class AngleVector extends ModifyModule {

	//negative Winkelwerte angeben, oder komplementen winkel benutzen
	
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
//refp1 und p1 werden übereinander gelegt, damit isLeftOf vergleich möglich ist
//ab hier NIE wieder refp1 oder p1 verwenden, sondern mid (gemergter punkt)

		
		int diffx=p1x-rp1x;
		int diffy=p1y-rp1y;
		
		//p2 wird mit p1 mitverschoben
		
		p2x=p2x-diffx;
		p2y=p2y-diffy;
		
		//angle berechnen
		
		int midx=rp1x;
		int midy=rp1y; 		
		
		//vektoren berechnen
		int px=p2x-midx;
		int py=p2y-midy;
		
		int rpx=rp2x-midx;
		int rpy=rp2y-midy;
		
		
		//euklidischer Abstand
		double betragrp=Math.sqrt(Math.pow((rp2x-midx),2)+Math.pow((rp2y-midy),2));
		double betragp=Math.sqrt(Math.pow((p2x-midx),2)+Math.pow((p2y-midy),2));
		
		double pMalRP=px*rpx + py*rpy;
	
	
		double winkel= (Math.acos(pMalRP/(betragrp*betragp)));

	
		
		if (!isLeftOf(new Point(midx,midy), new Point(rp2x,rp2y),new Point(p2x,p2y))){
			System.out.println("left");
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

	
	public AngleVector() throws ModulException {
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
		
		return "Angle Vector";
	}
}
