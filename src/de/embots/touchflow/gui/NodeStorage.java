package de.embots.touchflow.gui;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.gui.inspector.InspectorView;
import de.embots.touchflow.gui.components.GraphModul;
import de.embots.touchflow.gui.components.PinPort;

import java.util.ArrayList;

import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.OutputPin;

import org.jgraph.graph.DefaultEdge;


public class NodeStorage {
	ArrayList<GraphModul> module=new ArrayList<GraphModul>();
	ArrayList<DefaultEdge> kanten=new ArrayList<DefaultEdge>();

	public boolean addModul(GraphModul e) {
		//TODO: In Modulgraph einh�ngen
		if (InspectorView.isRunning()) {
			InspectorView.addModule(e.getCorrespondingModule());
			e.getCorrespondingModule().startThread();
		}
		
		return module.add(e);
	}

	public boolean removeModul(GraphModul arg0) {
		if (InspectorView.isRunning()) {
			InspectorView.removeModule(arg0.getCorrespondingModule());
			arg0.getCorrespondingModule().stopRunning();
		}
		return module.remove(arg0);
	}
	public void removeCells(Object[] mods){
		for (Object o:mods){
			if (o instanceof GraphModul) removeModul((GraphModul) o);
			if (o instanceof DefaultEdge) removeKante((DefaultEdge) o);
		}
	}
	public void addKante(DefaultEdge e){
		kanten.add(e);
		PinPort target= (PinPort) e.getTarget();
		PinPort src= (PinPort) e.getSource();
		try {
			GraphModul corrModule=(GraphModul) target.getParent();
			InputPin tarPin=(InputPin) (corrModule).getPin(target.getPin().getName());
			GraphModul corrModule2=(GraphModul) src.getParent();
			OutputPin srcPin=(OutputPin) (corrModule2).getPin(src.getPin().getName());

			tarPin.connectTo(srcPin);
		} catch (ModulException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	public ArrayList<GraphModul> getKnoten(){
		return module;
	}
	
	public ArrayList<DefaultEdge> getKanten(){
		return kanten;
	}
	
	public void clear(){
		module.clear();
		kanten.clear();
	}
	public void removeKante(DefaultEdge e){
		PinPort target= (PinPort) e.getTarget();
		GraphModul corrModule=null;
		
		try {
			corrModule=(GraphModul) target.getParent();
		}
		catch (NullPointerException ne){
			//Modul bereits entfernt, in das Kante eingetragen war -> ueberspringen
			kanten.remove(e);
			return;
		}
		try{
			InputPin tarPin=(InputPin) (corrModule).getPin(target.getPin().getName());
			tarPin.connectTo(null);
		} catch (ModulException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		kanten.remove(e);
		
	}
}
