package de.embots.touchflow.controller;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.gui.inspector.InspectorView;

import java.util.Observer;

import de.embots.touchflow.module.Globals;
import de.embots.touchflow.module.core.Module;
import de.embots.touchflow.module.core.ModuleGraph;


public class ModulesController {
	private ModuleGraph graph;
	
	/**
	 * startet das gesamte Modulsystem
	 */
	public void startSystem(){

		//erst nicht-Endmodule starten, dann die endmodule sonst kommen evtl. null-values raus
		for (Module m:graph.getAllModules()){
			if (!graph.getEndModules().contains(m)){
				m.startThread();
			}
		}

		for (Module m:graph.getEndModules()){
			m.startThread();
		}
		
		if (Globals.showInspectorView) {
			Module[] arr=new Module[graph.getAllModules().size()];
			for (int i=0; i< arr.length;i++){
				arr[i]=graph.getAllModules().get(i);
			}
			InspectorView.startInspectors(arr);
		}
	}
	
	public ModulesController(ModuleGraph graph) {
		super();
		this.graph = graph;
	}

	/**
	 * haengt einen Observer an Modul an
	 * @param o
	 * @param ModulID
	 * @throws ModulException 
	 */
	public void attachObserver(Observer o, int ModulID) throws ModulException{
		graph.findModul(ModulID).addObserver(o);
	}
	public Module findModul(int id) throws ModulException {
		return graph.findModul(id);
	}

	/**
	 * stoppt das Modulsystem
	 */
	
	public void stopAll(){
		//erst Endmodule stoppen, dann die nicht-endmodule sonst kommen evtl. null-values raus

		for (Module m:graph.getEndModules()){
			m.stopRunning();
		}
		
		for (Module m:graph.getAllModules()){
			if (!graph.getEndModules().contains(m)){
				m.stopRunning();
			}
		}

	
	}
}
