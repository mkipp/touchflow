package module.implementation.output;

public interface ModuleObservable {

	void attachObserver(ModuleObserver observer);
	void removeObserver(ModuleObserver observer);
	
}
