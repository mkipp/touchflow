package de.embots.touchflow.module.implementation.output;

import de.embots.touchflow.module.core.OutputModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.InputPin;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;


public class StatPrint extends OutputModule {
	
	public boolean printData=false;
	
	private double oldVal;
	private int ms;
	private int moves;
	private long lastMove;
	@Override
	protected void processData() throws ModulException {
		double newVal=getPin(PinName.IN).getData();
		
		if (lastMove==0) {
			lastMove=System.currentTimeMillis();
			return;
		}
		
		
		if (oldVal != newVal) {
			ms += System.currentTimeMillis()-lastMove;
		
			moves++;
			if (printData) System.out.println("Val:" + newVal + " - avg new data every " + ms / moves + "ms" + " - last: " + (System.currentTimeMillis()-lastMove) + "ms");
			else System.out.println("avg new data every " + ms / moves + "ms" + " - last: " + (System.currentTimeMillis()-lastMove) + "ms");
			lastMove=System.currentTimeMillis();
		}
		oldVal=newVal;
		
	}
	
	public StatPrint(){
		inputPins=new InputPin[1];
		inputPins[0]=new InputPin(PinName.IN,this);

	}

	@Override
	public String getDescription() {
		return "<html>Outputs <b>in</b> and additional performance info on stdout</html>";
	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getModuleName() {
		return "StatPrint";
	}

}
