package de.embots.touchflow.module.implementation.output;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.module.core.OutputModule;
import de.embots.touchflow.module.core.PinName;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.InputPin2D;

public class MouseRobot extends OutputModule{

	double lastClickStat=0;
	Robot robot;
	
	@Override
	public String getModuleName() {
		// TODO Auto-generated method stub
		return "MouseRobot";
	}

	@Override
	protected void processData() throws ModulException {
		InputPin2D pos=getInputPin2D(PinName.POSITION);
		double click=getInputPin(PinName.CLICK).getData();
		
		if (pos.getData()!=0||pos.getData2()!=0){
			robot.mouseMove((int)pos.getData(), (int)pos.getData2());
		}
		
		if (click!=lastClickStat){
			//just pressed/released mouse
			if (click==1){
				//now pressed, not pressed before
				robot.mousePress(InputEvent.BUTTON1_MASK);
				lastClickStat=1;
			}
			else{
				robot.mouseRelease(InputEvent.BUTTON1_MASK);
				lastClickStat=0;
			}
		}
		
	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		// TODO Auto-generated method stub
		
	}
	
	public MouseRobot(){
		inputPins=new InputPin[2];
		inputPins[0]=new InputPin2D(PinName.POSITION, this);
		inputPins[1]=new InputPin(PinName.CLICK, this);
		
		try {
			robot=new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
