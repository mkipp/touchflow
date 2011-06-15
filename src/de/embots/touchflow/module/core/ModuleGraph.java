package de.embots.touchflow.module.core;

import java.util.ArrayList;

import de.embots.touchflow.exceptions.ModulException;


public class ModuleGraph {

	private ArrayList<Module> allModules=new ArrayList<Module>();
	private ArrayList<Module> endModules=new ArrayList<Module>();
	
	
	public ArrayList<Module> getAllModules() {
		return allModules;
	}
	public ArrayList<Module> getEndModules() {
		return endModules;
	}
	
	/**
	 * fügt ein Modul in die Liste ein
	 * @param modul
	 * @param isEndModul
	 */
	public void addModul(Module modul, boolean isEndModul){
		allModules.add(modul);
		if (isEndModul) endModules.add(modul);
	}
	
	/**
	 * Gibt Modul anhand der id zurück
	 * @param id
	 * @return
	 * @throws ModulException
	 */
	public Module findModul(int id) throws ModulException{
		
		//alle module durchgehen 
		for (Module m:allModules){
			//id passt->zurückgeben
			if (m.getId()==id) return m;
		}
		throw new ModulException("Module id not found: " + id);
	}
}
