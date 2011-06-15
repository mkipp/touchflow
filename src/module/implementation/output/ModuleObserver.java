package module.implementation.output;

import module.core.Module;

public interface ModuleObserver {
	void moduleUpdate(Module sender);
}
