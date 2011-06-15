package module.implementation.output;

import java.util.ArrayList;

import org.jdom.Element;

import exceptions.ModulException;
import module.core.OutputModule;

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
