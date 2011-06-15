package de.embots.touchflow.gui.serializer;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.gui.TouchFlow;
import de.embots.touchflow.gui.components.GraphModul;
import de.embots.touchflow.gui.components.PinPort;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;

import de.embots.touchflow.module.Globals;
import de.embots.touchflow.module.core.Module;
import de.embots.touchflow.module.core.ModuleGraph;
import de.embots.touchflow.module.core.pinName;
import de.embots.touchflow.module.pin.InputPin;
import de.embots.touchflow.module.pin.OutputPin;

import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.GraphConstants;


public class GraphSerializer {
	private static ArrayList<GraphModul> gmodule;
	private static Module[] module;
	private static ArrayList<DefaultEdge> edges;
	
	
	public static void loadGraph(ModuleGraph graph, TouchFlow jgraph) throws ModulException{
		HashMap<Module,GraphModul> tmp=new HashMap<Module,GraphModul>();
		
		//1.run: Knoten aufbauen
		for (Module m:graph.getAllModules()){
			tmp.put(m,jgraph.createNode(m, m.getGraphXPos(), m.getGraphYPos()));
		}
		
		//2.run: kanten machen
		for (Module m:graph.getAllModules()){
			for (int i=0; i< m.getInputPins().length;i++){
				if (m.getInputPins()[i].getConnectedPin()!=null){
					//establish connection
					Module src=m.getInputPins()[i].getConnectedPin().getParentModul();
					pinName srcName=m.getInputPins()[i].getConnectedPin().getName();
					
					jgraph.createEdge(tmp.get(src), tmp.get(src).getPinID(src, srcName), tmp.get(m), i);
				}
			}
		}
		
		jgraph.repaint();
	}
	public static Module[] getModules(ArrayList<GraphModul> gmods,ArrayList<DefaultEdge> e){
		gmodule=gmods;
		edges=e;
		if (Globals.isDebug) System.out.println("Serializing graph. Mods:" + gmods.size() + " - edges:" + edges.size());
		module=new Module[gmods.size()];
		
		for (int i=0;i<gmodule.size();i++){
			module[i]=gmodule.get(i).getCorrespondingModule();
			module[i].removeAllConnections();
			Rectangle2D pos=GraphConstants.getBounds(gmodule.get(i).getAttributes());
			module[i].setGraphXPos((int) pos.getX());
			module[i].setGraphYPos((int) pos.getY());
		}
		
		for (DefaultEdge edge:edges){
			makeConnection(edge);
		}

		return module;
	}

	private static void makeConnection(DefaultEdge edge)  {

		
		PinPort srcPort=(PinPort) edge.getSource();
		PinPort tarPort=(PinPort) edge.getTarget();
		pinName quelle=srcPort.getPin().getName();
		pinName ziel=tarPort.getPin().getName();
		
		GraphModul srcMod=(GraphModul) srcPort.getParent();
		GraphModul destMod=(GraphModul) tarPort.getParent();
		try{ 
			
			if (srcMod.getPin(quelle) instanceof InputPin){
				OutputPin anderer=destMod.getCorrespondingModule().getOutputPin(ziel);
				srcMod.getCorrespondingModule().getInputPin(quelle).connectTo(anderer);
			}
			else{
				OutputPin anderer=srcMod.getCorrespondingModule().getOutputPin(quelle);
				destMod.getCorrespondingModule().getInputPin(ziel).connectTo(anderer);
	
			}
		
		}	
		
		catch (Exception e){
			e.printStackTrace();
		}
		
	}
}
