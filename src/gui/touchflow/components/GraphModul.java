package gui.touchflow.components;

import module.core.Module;
import module.core.OutputModule;
import module.core.pinName;
import module.factory.LibraryManager;
import module.pin.Pin;

import org.jgraph.graph.DefaultGraphCell;

import exceptions.ModulException;



public class GraphModul extends DefaultGraphCell {
	public int getPinID(Module parent,pinName name){
		
		int inPinLength=parent.getInputPins().length;
		for (int i=0; i< inPinLength;i++){
			if ( parent.getInputPins()[i].getName()==name){
				return i;
			}
		}
		for (int i=0; i< parent.getOutputPins().length;i++){
			if ( parent.getOutputPins()[i].getName()==name){
				return inPinLength+i;
			}
		}
		return 0;
		
	}
	public pinName getPortMapEntry(int portNum) {
		return correspondingModule.getPortMapEntry(portNum);
	}
	Module correspondingModule;
	public GraphModul(String typ) throws ModulException{
		super(typ);
			correspondingModule=LibraryManager.manager.createModulForGraph(typ);
	}
	public GraphModul(Module modul){
		super(modul.getModuleName());

			correspondingModule=modul;
	}
	public Pin getPin(pinName name) throws ModulException {
		return correspondingModule.getPin(name);
	}
	public boolean isPortExisting(int portNum){
		return correspondingModule.getPortMapEntry(portNum)!=null;
	}
	public Module getCorrespondingModule() {
		return correspondingModule;
	}
}
