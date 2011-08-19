package de.embots.touchflow.module.implementation.output;

import java.util.ArrayList;

import org.jdom.Element;

import de.embots.touchflow.exceptions.ModulException;
import de.embots.touchflow.module.core.OutputModule;

public class ObservableOutput extends OutputModule implements ModuleObservable {
	ArrayList<ModuleObserver> observers=new ArrayList<ModuleObserver>();
	
	@Override
	public void attachObserver(ModuleObserver observer) {
		observers.add(observer);

	}

	@Override
	public void removeObserver(ModuleObserver observer) {
		observers.remove(observer);

	}

	@Override
	protected void processData() throws ModulException {
		for (ModuleObserver o:observers){
			o.moduleUpdate(this);
		}
		
 
	}

	@Override
	protected void additionalSaveAttribute(Element e) {
		

	}

	@Override
	public String getModuleName() {
		
		return "Observable out";
	}

}
